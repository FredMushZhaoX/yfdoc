package com.yf.document.modules.sys.depart.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yf.boot.base.api.api.dto.PagingReqDTO;
import com.yf.boot.base.api.exception.ServiceException;
import com.yf.boot.base.api.utils.BeanMapper;
import com.yf.document.modules.sys.depart.dto.SysDepartDTO;
import com.yf.document.modules.sys.depart.dto.response.SysDepartTreeDTO;
import com.yf.document.modules.sys.depart.entity.SysDepart;
import com.yf.document.modules.sys.depart.mapper.SysDepartMapper;
import com.yf.document.modules.sys.depart.service.SysDepartService;
import com.yf.document.modules.user.UserUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
* <p>
* 部门信息业务实现类
* </p>
*
* @author 聪明笨狗
* @since 2020-09-02 17:25
*/
@Service
public class SysDepartServiceImpl extends ServiceImpl<SysDepartMapper, SysDepart> implements SysDepartService {


    /**
     * 0标识为顶级分类
     */
    private static final String ROOT_TAG = "0";


    @Transactional(rollbackFor = Exception.class)
    @Override
    public void save(SysDepartDTO reqDTO) {


        SysDepart entity = new SysDepart();
        BeanMapper.copy(reqDTO, entity);

        if(StringUtils.isBlank(reqDTO.getId())) {
            this.fillCode(entity);
        }else{
            reqDTO.setSort(null);
            reqDTO.setDeptCode(null);
        }

        this.saveOrUpdate(entity);
    }

    @Override
    public IPage<SysDepartTreeDTO> paging(PagingReqDTO<SysDepartDTO> reqDTO) {

        // 请求参数
        SysDepartDTO params = reqDTO.getParams();

        if(params!=null){
            params = new SysDepartDTO();
        }

        params.setParentId("0");

        // 用户部门
        String code = UserUtils.departCode();
        Integer scope = UserUtils.getDataScope();




        //转换结果
        IPage<SysDepartTreeDTO> pageData = baseMapper.paging(reqDTO.toPage(), params);

        return pageData;
     }


    @Override
    public List<SysDepartTreeDTO> findTree(boolean self) {
        Page page = new Page<>();
        page.setCurrent(1);
        page.setSize(1000);

        SysDepartDTO reqDTO = new SysDepartDTO();

        // 只看自己部门数据
        if(self){
            // 用户部门
            String code = UserUtils.departCode();
            Integer scope = UserUtils.getDataScope();

            reqDTO.setParentId("0");
        }else{
            reqDTO.setParentId("0");
        }

        // 查询参数
        IPage<SysDepartTreeDTO> pageData = baseMapper.paging(page, reqDTO);
        return pageData.getRecords();
    }


    @Override
    public void sort(String id, Integer sort) {

        SysDepart depart = this.getById(id);
        SysDepart exchange = null;

        QueryWrapper<SysDepart> wrapper = new QueryWrapper<>();
        // 同级排序
        wrapper.lambda()
                .eq(SysDepart::getParentId, depart.getParentId());
        wrapper.last("LIMIT 1");

        // 上升
        if(sort == 0){
            // 同级排序
            wrapper.lambda()
                    .lt(SysDepart::getSort, depart.getSort())
                    .orderByDesc(SysDepart::getSort);
            exchange = this.getOne(wrapper, false);
        }

        // 下降
        if(sort == 1){
            // 同级排序
            wrapper.lambda()
                    .gt(SysDepart::getSort, depart.getSort())
                    .orderByAsc(SysDepart::getSort);
            exchange = this.getOne(wrapper, false);
        }


        if(exchange!=null) {
            SysDepart a = new SysDepart();
            a.setId(id);
            a.setSort(exchange.getSort());
            SysDepart b = new SysDepart();
            b.setId(exchange.getId());
            b.setSort(depart.getSort());
            this.updateById(a);
            this.updateById(b);
        }
    }

    @Override
    public String syncDepart(String str) {

        String [] arr = str.split(",");

        // 如：云帆互联,产品研发部,技术部
        List<String> subs = new ArrayList<>();


        // 默认0
        String parentId = ROOT_TAG;
        String deptCode = null;

        for(int i=0; i<arr.length; i++){

            // 跳出循环
            if( i>= arr.length){
                break;
            }

            // 自上往下搜索
            subs.add(arr[i]);


            SysDepart depart = this.findLastChild(subs);

            if(depart!=null){
                parentId = depart.getId();
                deptCode = depart.getDeptCode();
            }else{

                // 循环剩下的
                List<String> left = new ArrayList<>();

                // 剩余要添加的子部门
                for(int j=i;j<arr.length;j++){
                    left.add(arr[j]);
                }

                // 创建剩余的子部门
                deptCode = this.createSubs(parentId, left);
                break;
            }
        }

        return deptCode;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delete(List<String> ids) {
        //查询条件
        QueryWrapper<SysDepart> wrapper = new QueryWrapper<>();
        wrapper.lambda().in(SysDepart::getParentId, ids);
        int count = this.count(wrapper);
        if(count > 0){
            throw new ServiceException("请先删除下级部门！");
        }

        this.removeByIds(ids);
    }

    /**
     * 从上往下查找部门子节点
     * @param list
     * @return
     */
    private SysDepart findLastChild(List<String> list){
        String parentId = ROOT_TAG;

        for(int i=0;i<list.size();i++){
            SysDepart depart = this.findDeptByName(parentId, list.get(i));

            // 找不到返回空
            if(depart == null){
                return null;
            }

            parentId = depart.getId();

            // 找到了最底级
            if(i>=list.size()-1){
                return depart;
            }
        }

        return null;
    }

    /**
     * 创建下级并返回部门ID
     * @param parentId
     * @param list
     * @return
     */
    private String createSubs(String parentId, List<String> list){


        String deptCode = null;

        for (String name: list){

            SysDepart entity = new SysDepart();
            entity.setDeptName(name);
            entity.setParentId(parentId);
            this.fillCode(entity);
            this.save(entity);

            parentId = entity.getId();
            deptCode = entity.getDeptCode();
        }

        return deptCode;
    }

    /**
     * 根据名称查找部门
     * @param name
     * @return
     */
    public SysDepart findDeptByName(String parentId, String name) {
        QueryWrapper<SysDepart> wrapper = new QueryWrapper<>();

        // 同级排序
        wrapper.lambda()
                .eq(SysDepart::getDeptName, name)
                .eq(SysDepart::getParentId, parentId);

        return this.getOne(wrapper, false);

    }

    /**
     * 填充部门编号
     * @param depart
     */
    private void fillCode(SysDepart depart){

        // 前缀
        String code = "";

        if(StringUtils.isNotBlank(depart.getParentId())
                && !ROOT_TAG.equals(depart.getParentId())){
            SysDepart parent = this.getById(depart.getParentId());
            code = parent.getDeptCode();
        }

        QueryWrapper<SysDepart> wrapper = new QueryWrapper<>();

        // 同级排序
        wrapper.lambda()
                .eq(SysDepart::getParentId, depart.getParentId())
                .orderByDesc(SysDepart::getSort);
        wrapper.last("LIMIT 1");
        SysDepart max = this.getOne(wrapper, false);

        if(max !=null){
            code += this.formatCode(max.getSort()+1);
            depart.setSort(max.getSort()+1);
        }else{
            code += this.formatCode(1);
            depart.setSort(1);
        }

        depart.setDeptCode(code);
    }

    /**
     * 根式化加0
     * @param sort
     * @return
     */
    private String formatCode(Integer sort){
        if(sort < 10){
            return "A0"+sort;
        }
        return "A"+sort;
    }
}
