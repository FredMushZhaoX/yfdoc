package com.yf.document.modules.sys.log.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yf.document.modules.sys.log.entity.SysLogLearn;

/**
* <p>
* 学习记录业务类
* </p>
*
* @author 聪明笨狗
* @since 2021-06-04 09:53
*/
public interface SysLogLearnService extends IService<SysLogLearn> {

    /**
     * 保存学习记录
     * @param userId
     * @param courseId
     * @param fileId
     * @param learnMin
     */
    void merge(String userId, String courseId, String fileId, Integer learnMin);
}
