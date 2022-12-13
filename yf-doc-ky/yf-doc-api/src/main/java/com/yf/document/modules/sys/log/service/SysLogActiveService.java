package com.yf.document.modules.sys.log.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yf.document.modules.sys.log.entity.SysLogActive;

/**
* <p>
* 用户活跃记录业务类
* </p>
*
* @author 聪明笨狗
* @since 2021-06-04 08:10
*/
public interface SysLogActiveService extends IService<SysLogActive> {

    /**
     * 保存用户活跃度
     * @param userId
     */
    void merge(String userId);
}
