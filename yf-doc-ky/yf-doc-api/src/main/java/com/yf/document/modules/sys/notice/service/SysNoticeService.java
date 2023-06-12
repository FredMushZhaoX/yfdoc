package com.yf.document.modules.sys.notice.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yf.boot.base.api.api.dto.PagingReqDTO;
import com.yf.document.modules.sys.notice.dto.SysNoticeDTO;
import com.yf.document.modules.sys.notice.entity.SysNotice;
/**
* <p>
* 系统公告业务类
* </p>
*
* @author 聪明笨狗
* @since 2020-10-16 12:01
*/
public interface SysNoticeService extends IService<SysNotice> {

    /**
    * 分页查询数据
    * @param reqDTO
    * @return
    */
    IPage<SysNoticeDTO> paging(PagingReqDTO<SysNoticeDTO> reqDTO);
}
