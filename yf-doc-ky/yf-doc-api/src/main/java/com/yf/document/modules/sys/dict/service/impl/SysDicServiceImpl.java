package com.yf.document.modules.sys.dict.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yf.boot.base.api.api.dto.PagingReqDTO;
import com.yf.boot.base.api.exception.ServiceException;
import com.yf.boot.base.api.utils.BeanMapper;
import com.yf.boot.base.api.utils.StringUtils;
import com.yf.document.modules.sys.dict.dto.SysDicDTO;
import com.yf.document.modules.sys.dict.entity.SysDic;
import com.yf.document.modules.sys.dict.mapper.SysDicMapper;
import com.yf.document.modules.sys.dict.service.SysDicService;
import org.springframework.stereotype.Service;

/**
* <p>
* 分类字典业务实现类
* </p>
*
* @author 聪明笨狗
* @since 2020-12-01 14:00
*/
@Service
public class SysDicServiceImpl extends ServiceImpl<SysDicMapper, SysDic> implements SysDicService {

    @Override
    public IPage<SysDicDTO> paging(PagingReqDTO<SysDicDTO> reqDTO) {



        //查询条件
        QueryWrapper<SysDic> wrapper = new QueryWrapper<>();

        // 请求参数
        SysDicDTO params = reqDTO.getParams();

        if(params!=null){

            if(!StringUtils.isBlank(params.getTitle())){

                wrapper.lambda().and(obj -> obj.like(SysDic::getCode, params.getTitle())
                        .or()
                        .like(SysDic::getTitle, params.getTitle()));
            }

            if(params.getType()!=null){
                wrapper.lambda()
                        .eq(SysDic::getType, params.getType());
            }
        }


        //获得数据
        IPage<SysDic> page = this.page(reqDTO.toPage(), wrapper);
        //转换结果
        IPage<SysDicDTO> pageData = JSON.parseObject(JSON.toJSONString(page), new TypeReference<Page<SysDicDTO>>(){});
        return pageData;
     }

    @Override
    public void save(SysDicDTO reqDTO) {

        QueryWrapper<SysDic> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(SysDic::getCode, reqDTO.getCode());

        if(!StringUtils.isBlank(reqDTO.getId())){
            wrapper.lambda().ne(SysDic::getId, reqDTO.getId());
        }

        int count = this.count(wrapper);

        if(count > 0){
            throw new ServiceException("分类编码不可以重复！");
        }

        //复制参数
        SysDic entity = new SysDic();
        BeanMapper.copy(reqDTO, entity);
        this.saveOrUpdate(entity);
    }
}
