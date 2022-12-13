package com.yf.document.modules.sys.user.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yf.boot.base.api.api.dto.PagingReqDTO;
import com.yf.boot.base.api.utils.BeanMapper;
import com.yf.document.modules.sys.user.dto.SysUserBindDTO;
import com.yf.document.modules.sys.user.entity.SysUserBind;
import com.yf.document.modules.sys.user.mapper.SysUserBindMapper;
import com.yf.document.modules.sys.user.service.SysUserBindService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* <p>
* 登录绑定业务实现类
* </p>
*
* @author 聪明笨狗
* @since 2021-08-02 14:49
*/
@Service
public class SysUserBindServiceImpl extends ServiceImpl<SysUserBindMapper, SysUserBind> implements SysUserBindService {

    @Override
    public IPage<SysUserBindDTO> paging(PagingReqDTO<SysUserBindDTO> reqDTO) {

        //查询条件
        QueryWrapper<SysUserBind> wrapper = new QueryWrapper<>();

        // 请求参数
        SysUserBindDTO params = reqDTO.getParams();

        //获得数据
        IPage<SysUserBind> page = this.page(reqDTO.toPage(), wrapper);
        //转换结果
        IPage<SysUserBindDTO> pageData = JSON.parseObject(JSON.toJSONString(page), new TypeReference<Page<SysUserBindDTO>>(){});
        return pageData;
    }


    @Override
    public void save(SysUserBindDTO reqDTO){
        //复制参数
        SysUserBind entity = new SysUserBind();
        BeanMapper.copy(reqDTO, entity);
        this.saveOrUpdate(entity);
    }

    @Override
    public void delete(List<String> ids){
        //批量删除
        this.removeByIds(ids);
    }

    @Override
    public SysUserBindDTO detail(String id){
        SysUserBind entity = this.getById(id);
        SysUserBindDTO dto = new SysUserBindDTO();
        BeanMapper.copy(entity, dto);
        return dto;
    }

    @Override
    public List<SysUserBindDTO> list(SysUserBindDTO reqDTO){

        //分页查询并转换
        QueryWrapper<SysUserBind> wrapper = new QueryWrapper<>();

        //转换并返回
        List<SysUserBind> list = this.list(wrapper);

        //转换数据
        List<SysUserBindDTO> dtoList = BeanMapper.mapList(list, SysUserBindDTO.class);

        return dtoList;
    }

    @Override
    public String findBind(String loginType, String openId) {
        //分页查询并转换
        QueryWrapper<SysUserBind> wrapper = new QueryWrapper<>();
        wrapper.lambda()
                .eq(SysUserBind::getLoginType, loginType)
                .eq(SysUserBind::getOpenId, openId);

        SysUserBind bind = this.getOne(wrapper, false);

        if(bind!=null){
            return bind.getUserId();
        }

        return null;
    }
}
