package com.yf.document.modules.sys.config.controller;

import com.yf.boot.base.api.annon.DataProtect;
import com.yf.boot.base.api.api.ApiRest;
import com.yf.boot.base.api.api.controller.BaseController;
import com.yf.document.modules.sys.config.dto.CfgBaseDTO;
import com.yf.document.modules.sys.config.entity.CfgBase;
import com.yf.document.modules.sys.config.service.CfgBaseService;
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
@RequestMapping("/api/sys/config")
public class BaseConfigController extends BaseController {

    @Autowired
    private CfgBaseService baseService;

    /**
    * 添加或修改
    * @param reqDTO
    * @return
    */

    @DataProtect(clazz = CfgBase.class, update = true)
    @ApiOperation(value = "保存基础配置")
    @RequestMapping(value = "/save", method = { RequestMethod.POST})
    public ApiRest save(@RequestBody CfgBaseDTO reqDTO) {
        return super.failure("重要演示数据无法修改和删除");
        //baseService.save(reqDTO);
        //return super.success();
    }

    /**
    * 查找详情
    * @return
    */
    @ApiOperation(value = "简略详情")
    @RequestMapping(value = "/detail", method = { RequestMethod.POST})
    public ApiRest<CfgBaseDTO> detail() {
        CfgBaseDTO dto = baseService.findSimple();
        return super.success(dto);
    }
}
