package com.yf.document.modules.sys.config.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yf.boot.base.api.annon.DataProtect;
import com.yf.boot.base.api.api.ApiRest;
import com.yf.boot.base.api.api.controller.BaseController;
import com.yf.document.modules.sys.config.dto.CfgBaseDTO;
import com.yf.document.modules.sys.config.dto.request.UploadConfigReqDTO;
import com.yf.document.modules.sys.config.entity.CfgBase;
import com.yf.document.modules.sys.config.service.CfgBaseService;
import com.yf.document.modules.sys.config.service.CfgUploadLocalService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
* <p>
* 通用配置控制器
* </p>
*
* @author 聪明笨狗
* @since 2020-04-17 09:12
*/
@Api(tags={"通用配置"})
@RestController
@RequestMapping("/api/sys/config/upload")
public class UploadConfigController extends BaseController {

    @Autowired
    private CfgBaseService baseService;

    @Autowired
    private CfgUploadLocalService cfgUploadLocalService;

    /**
     * 添加或修改
     * @param reqDTO
     * @return
     */

    @DataProtect(clazz = CfgBase.class, update = true)
    @ApiOperation(value = "添加或修改")
    @RequestMapping(value = "/save", method = { RequestMethod.POST})
    public ApiRest save(@RequestBody UploadConfigReqDTO reqDTO) {

        // 更新基础
        CfgBase base = new CfgBase();
        base.setUploadType(reqDTO.getUploadType());
        baseService.update(base, new QueryWrapper<>());

        // 本地存储
        if(reqDTO.getLocalUpload()!=null){
            cfgUploadLocalService.save(reqDTO.getLocalUpload());
        }
        return super.success();
    }

    /**
     * 直播配置详情
     * @return
     */

    @ApiOperation(value = "查找详情")
    @RequestMapping(value = "/detail", method = { RequestMethod.POST})
    public ApiRest<UploadConfigReqDTO> find() {

        // 基础信息
        UploadConfigReqDTO respDTO = new UploadConfigReqDTO();

        // 上传详情
        respDTO.setLocalUpload(cfgUploadLocalService.find());
        CfgBaseDTO base = baseService.findSimple();
        respDTO.setUploadType(base.getUploadType());

        return super.success(respDTO);
    }
}
