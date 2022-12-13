package com.yf.document.modules.sys.notice.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yf.boot.base.api.api.dto.PagingReqDTO;
import com.yf.boot.base.api.utils.StringUtils;
import com.yf.document.modules.sys.notice.dto.SysNoticeDTO;
import com.yf.document.modules.sys.notice.entity.SysNotice;
import com.yf.document.modules.sys.notice.mapper.SysNoticeMapper;
import com.yf.document.modules.sys.notice.service.SysNoticeService;
import org.springframework.stereotype.Service;

/**
* <p>
* 系统公告业务实现类
* </p>
*
* @author 聪明笨狗
* @since 2020-10-16 12:01
*/
@Service
public class SysNoticeServiceImpl extends ServiceImpl<SysNoticeMapper, SysNotice> implements SysNoticeService {

    @Override
    public IPage<SysNoticeDTO> paging(PagingReqDTO<SysNoticeDTO> reqDTO) {

        //查询条件
        QueryWrapper<SysNotice> wrapper = new QueryWrapper<>();

        // 请求参数
        SysNoticeDTO params = reqDTO.getParams();

        if(params!=null){
            if(!StringUtils.isBlank(params.getTitle())){
                wrapper.lambda().like(SysNotice::getTitle, params.getTitle());
            }

            if(params.getState()!=null){
                wrapper.lambda().eq(SysNotice::getState, params.getState());
            }
        }

        wrapper.lambda().orderByDesc(SysNotice::getCreateTime);

        //获得数据
        IPage<SysNotice> page = this.page(reqDTO.toPage(), wrapper);
        //转换结果
        IPage<SysNoticeDTO> pageData = JSON.parseObject(JSON.toJSONString(page), new TypeReference<Page<SysNoticeDTO>>(){});
        return pageData;
     }
}
