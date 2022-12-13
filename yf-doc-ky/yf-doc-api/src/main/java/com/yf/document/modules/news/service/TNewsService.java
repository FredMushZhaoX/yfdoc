package com.yf.document.modules.news.service;

import com.yf.document.modules.news.entity.TNews;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yf.boot.base.api.api.dto.PagingReqDTO;
import com.yf.document.modules.news.dto.TNewsDTO;

import java.util.List;

/**
* <p>
* 新闻列表业务接口类
* </p>
*
* @author Panjp
* @since 2021-09-22 14:51
*/
public interface TNewsService extends IService<TNews> {

    /**
    * 分页查询数据
    * @param reqDTO
    * @return
    */
    IPage<TNewsDTO> paging(PagingReqDTO<TNewsDTO> reqDTO);

    /**
    * 添加或保存
    * @param reqDTO
    * @return
    */
    void save(TNewsDTO reqDTO);

    /**
    * 批量删除
    * @param ids
    * @return
    */
    void delete(List<String> ids);

    /**
    * 查找详情
    * @param id
    * @return
    */
    TNewsDTO detail(String id);

    /**
    * 查找列表
    * @param reqDTO
    * @return
    */
    List<TNewsDTO> list(TNewsDTO reqDTO);
}
