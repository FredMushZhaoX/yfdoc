package com.yf.document.modules.sys.dict.controller;

import com.yf.boot.base.api.annon.DataProtect;
import com.yf.boot.base.api.api.ApiRest;
import com.yf.boot.base.api.api.controller.BaseController;
import com.yf.boot.base.api.api.dto.BaseIdRespDTO;
import com.yf.boot.base.api.api.dto.BaseIdsReqDTO;
import com.yf.document.modules.sys.dict.dto.SysDicValueDTO;
import com.yf.document.modules.sys.dict.dto.ext.DicValueTreeDTO;
import com.yf.document.modules.sys.dict.entity.SysDicValue;
import com.yf.document.modules.sys.dict.service.SysDicValueService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
* <p>
* 分类字典控制器
* </p>
*
* @author 聪明笨狗
* @since 2020-12-01 14:00
*/
@Api(tags={"分类字典"})
@RestController
@RequestMapping("/api/sys/dic/value")
public class SysDicValueController extends BaseController {

    @Autowired
    private SysDicValueService baseService;

    /**
    * 添加或修改
    * @param reqDTO
    * @return
    */
    @RequiresPermissions(value = {"sys:dict:catalog:sub", "sys:dict:value:sub"}, logical = Logical.OR)
    @DataProtect(clazz = SysDicValue.class, update = true)
    @ApiOperation(value = "添加或修改")
    @RequestMapping(value = "/save", method = { RequestMethod.POST})
    public ApiRest<BaseIdRespDTO> save(@RequestBody SysDicValueDTO reqDTO) {
        baseService.save(reqDTO);
        return super.success();
    }

    /**
    * 批量删除
    * @param reqDTO
    * @return
    */
    @RequiresPermissions(value = {"sys:dict:catalog:sub", "sys:dict:value:sub"}, logical = Logical.OR)
    @DataProtect(clazz = SysDicValue.class, delete = true)
    @ApiOperation(value = "批量删除")
    @RequestMapping(value = "/delete", method = { RequestMethod.POST})
    public ApiRest edit(@RequestBody BaseIdsReqDTO reqDTO) {
        //根据ID删除
        baseService.removeByIds(reqDTO.getIds());
        return super.success();
    }

    /**
     * 分类树列表
     * @return
     */
    @ApiOperation(value = "分类树列表")
    @RequestMapping(value = "/tree", method = { RequestMethod.POST})
    public ApiRest<List<DicValueTreeDTO>> tree(@RequestBody SysDicValue reqDTO) {
        List<DicValueTreeDTO> dtoList = baseService.findTree(reqDTO.getDicCode());
        return super.success(dtoList);
    }

}
