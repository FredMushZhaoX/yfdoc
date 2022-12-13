package com.yf.document.modules.news.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yf.boot.base.api.api.dto.PagingReqDTO;
import com.yf.boot.base.api.utils.BeanMapper;
import com.yf.boot.base.api.utils.StringUtils;
import com.yf.document.modules.news.dto.TNewsDTO;
import com.yf.document.modules.news.entity.TNews;
import com.yf.document.modules.news.mapper.TNewsMapper;
import com.yf.document.modules.news.service.TNewsService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

import java.util.List;

/**
* <p>
* 新闻列表业务实现类
* </p>
*
* @author Panjp
* @since 2021-09-22 14:51
*/
@Service
public class TNewsServiceImpl extends ServiceImpl<TNewsMapper, TNews> implements TNewsService {

    @Override
    public IPage<TNewsDTO> paging(PagingReqDTO<TNewsDTO> reqDTO) {

        //查询条件
        QueryWrapper<TNews> wrapper = new QueryWrapper<>();

        // 请求参数
        TNewsDTO params = reqDTO.getParams();
        if(params!=null){
            if(params.getTitle()!=null){
                wrapper.lambda().like(TNews::getTitle, params.getTitle());
            }
            if(!StringUtils.isBlank(params.getNewsStatus())){
                wrapper.lambda().eq(TNews::getNewsStatus, params.getNewsStatus());
            }

        }

        List<OrderItem> lsOrder = new ArrayList<>();
        OrderItem item = new OrderItem();
        item.setAsc(false);
        item.setColumn(" create_time ");
        lsOrder.add(item);
        reqDTO.setOrders(lsOrder);
        //获得数据
        IPage<TNews> page = this.page(reqDTO.toPage(), wrapper);
        //转换结果
        IPage<TNewsDTO> pageData = JSON.parseObject(JSON.toJSONString(page), new TypeReference<Page<TNewsDTO>>(){});
        return pageData;
    }


    @Override
    public void save(TNewsDTO reqDTO){
        //复制参数
        TNews entity = new TNews();
        BeanMapper.copy(reqDTO, entity);
        this.saveOrUpdate(entity);
    }

    @Override
    public void delete(List<String> ids){
        //批量删除
        this.removeByIds(ids);
    }

    @Override
    public TNewsDTO detail(String id){
        TNews entity = this.getById(id);
        TNewsDTO dto = new TNewsDTO();
        BeanMapper.copy(entity, dto);
        return dto;
    }

    @Override
    public List<TNewsDTO> list(TNewsDTO reqDTO){

        //分页查询并转换
        QueryWrapper<TNews> wrapper = new QueryWrapper<>();

        //转换并返回
        List<TNews> list = this.list(wrapper);

        //转换数据
        List<TNewsDTO> dtoList = BeanMapper.mapList(list, TNewsDTO.class);

        return dtoList;
    }
}
