package com.yf.document.modules.sys.config.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yf.document.modules.sys.config.dto.CfgUploadLocalDTO;
import com.yf.document.modules.sys.config.entity.CfgUploadLocal;

/**
* <p>
* 本地文件上传配置业务类
* </p>
*
* @author 聪明笨狗
* @since 2021-02-05 11:16
*/
public interface CfgUploadLocalService extends IService<CfgUploadLocal> {

    /**
     * 保存配置
     * @param reqDTO
     */
    void save(CfgUploadLocalDTO reqDTO);

    /**
     * 查找配置
     * @return
     */
    CfgUploadLocalDTO find();
}
