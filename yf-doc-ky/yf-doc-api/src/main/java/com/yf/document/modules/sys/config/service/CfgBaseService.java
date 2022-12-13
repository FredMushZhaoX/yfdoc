package com.yf.document.modules.sys.config.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yf.document.modules.sys.config.dto.CfgBaseDTO;
import com.yf.document.modules.sys.config.entity.CfgBase;

/**
* <p>
* 通用配置业务类
* </p>
*
* @author 聪明笨狗
* @since 2020-04-17 09:12
*/
public interface CfgBaseService extends IService<CfgBase> {

    /**
     * 查找公开配置信息
     * @return
     */
    CfgBaseDTO findSimple();

    /**
     * 保存配置
     * @param reqDTO
     */
    void save(CfgBaseDTO reqDTO);

}
