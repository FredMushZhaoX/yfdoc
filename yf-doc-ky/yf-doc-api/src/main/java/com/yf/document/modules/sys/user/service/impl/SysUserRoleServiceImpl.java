package com.yf.document.modules.sys.user.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yf.boot.base.api.api.dto.PagingReqDTO;
import com.yf.boot.base.api.exception.ServiceException;
import com.yf.document.modules.sys.user.dto.SysUserRoleDTO;
import com.yf.document.modules.sys.user.dto.request.UserRoleReqDTO;
import com.yf.document.modules.sys.user.entity.SysRole;
import com.yf.document.modules.sys.user.entity.SysUserRole;
import com.yf.document.modules.sys.user.mapper.SysUserRoleMapper;
import com.yf.document.modules.sys.user.service.SysRoleService;
import com.yf.document.modules.sys.user.service.SysUserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
* <p>
* 语言设置 服务实现类
* </p>
*
* @author 聪明笨狗
* @since 2020-04-13 16:57
*/
@Service
public class SysUserRoleServiceImpl extends ServiceImpl<SysUserRoleMapper, SysUserRole> implements SysUserRoleService {

    @Autowired
    private SysRoleService sysRoleService;

    @Override
    public IPage<SysUserRoleDTO> paging(PagingReqDTO<SysUserRoleDTO> reqDTO) {

        //创建分页对象
        IPage<SysUserRole> query = reqDTO.toPage();

        //查询条件
        QueryWrapper<SysUserRole> wrapper = new QueryWrapper<>();

        //获得数据
        IPage<SysUserRole> page = this.page(query, wrapper);
        //转换结果
        IPage<SysUserRoleDTO> pageData = JSON.parseObject(JSON.toJSONString(page), new TypeReference<Page<SysUserRoleDTO>>(){});
        return pageData;
     }

    @Override
    public List<SysRole> listRoles(String userId) {
        return baseMapper.listByUser(userId);
    }

    @Override
    public List<String> listRoleIds(String userId) {
        // 角色是要
        List<SysRole> roles = this.listRoles(userId);
        List<String> ids = new ArrayList<>();
        for(SysRole role: roles){
            // 角色ID
            ids.add(role.getId());
        }
        return ids;
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveRoles(String userId, List<String> ids) {


        this.checkRoles(ids);

        // 删除全部角色
        QueryWrapper<SysUserRole> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(SysUserRole::getUserId, userId);
        this.remove(wrapper);

        if(!CollectionUtils.isEmpty(ids)){

            List<SysUserRole> list = new ArrayList<>();

            for(String item: ids){
                SysUserRole role = new SysUserRole();
                role.setRoleId(item);
                role.setUserId(userId);
                list.add(role);
            }

            this.saveBatch(list);
        }
    }

    @Override
    public List<String> findUserPermission(String userId) {
        return baseMapper.findUserPermission(userId);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void batchRole(UserRoleReqDTO reqDTO) {


        // 先清理
        for(String userId: reqDTO.getUserIds()){
            for(String roleId: reqDTO.getRoleIds()){
                QueryWrapper<SysUserRole> wrapper = new QueryWrapper<>();
                wrapper.lambda()
                        .eq(SysUserRole::getUserId, userId)
                        .eq(SysUserRole::getRoleId, roleId);
                this.remove(wrapper);
            }
        }


        // 添加角色
        if(reqDTO.getFlag().equals(1)){
            List<SysUserRole> list = new ArrayList<>();
            // 再增加
            for(String userId: reqDTO.getUserIds()){
                for(String roleId: reqDTO.getRoleIds()){
                    SysUserRole item = new SysUserRole();
                    item.setUserId(userId);
                    item.setRoleId(roleId);
                    list.add(item);
                }
            }
            this.saveBatch(list);
        }

    }


    /**
     * 校验角色
     * @param ids
     */
    private void checkRoles(List<String> ids){

        QueryWrapper<SysRole> wrapper = new QueryWrapper<>();
        wrapper.lambda().in(SysRole::getId, ids);
        int count = sysRoleService.count(wrapper);

        if(count !=ids.size()){
            throw new ServiceException("角色ID不存在，请先添加后再继续！");
        }
    }
}
