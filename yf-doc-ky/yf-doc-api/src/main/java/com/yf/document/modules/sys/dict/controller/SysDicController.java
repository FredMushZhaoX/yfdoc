package com.yf.document.modules.sys.dict.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yf.boot.base.api.annon.DataProtect;
import com.yf.boot.base.api.api.ApiRest;
import com.yf.boot.base.api.api.controller.BaseController;
import com.yf.boot.base.api.api.dto.BaseIdReqDTO;
import com.yf.boot.base.api.api.dto.BaseIdsReqDTO;
import com.yf.boot.base.api.api.dto.PagingReqDTO;
import com.yf.document.modules.sys.dict.dto.SysDicDTO;
import com.yf.document.modules.sys.dict.dto.ext.DicValueTreeDTO;
import com.yf.document.modules.sys.dict.entity.SysDic;
import com.yf.document.modules.sys.dict.service.SysDicService;
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
@RequestMapping("/api/sys/dic")
public class SysDicController extends BaseController {

    @Autowired
    private SysDicService baseService;

    @Autowired
    private SysDicValueService sysDicValueService;

    /**
    * 添加或修改
    * @param reqDTO
    * @return
    */
    @RequiresPermissions(value = {"sys:dict:catalog:add", "sys:dict:catalog:update", "sys:dict:value:add", "sys:dict" +
            ":value:update"}, logical = Logical.OR)
    @DataProtect(clazz = SysDic.class, update = true)
    @ApiOperation(value = "添加或修改")
    @RequestMapping(value = "/save", method = { RequestMethod.POST})
    public ApiRest save(@RequestBody SysDicDTO reqDTO) {
        baseService.save(reqDTO);
        return super.success();
    }

    /**
    * 批量删除
    * @param reqDTO
    * @return
    */
    @RequiresPermissions(value = {"sys:dict:catalog:delete", "sys:dict:value:delete"})
    @DataProtect(clazz = SysDic.class, delete = true)
    @ApiOperation(value = "批量删除")
    @RequestMapping(value = "/delete", method = { RequestMethod.POST})
    public ApiRest edit(@RequestBody BaseIdsReqDTO reqDTO) {
        //根据ID删除
        baseService.removeByIds(reqDTO.getIds());
        return super.success();
    }

    /**
    * 分页查找
    * @param reqDTO
    * @return
    */
    @ApiOperation(value = "分页查找")
    @RequestMapping(value = "/paging", method = { RequestMethod.POST})
    public ApiRest<IPage<SysDicDTO>> paging(@RequestBody PagingReqDTO<SysDicDTO> reqDTO) {
        //分页查询并转换
        IPage<SysDicDTO> page = baseService.paging(reqDTO);
        return super.success(page);
    }

    /**
     * 分类树列表
     * @return
     */
    @ApiOperation(value = "分类树列表")
    @RequestMapping(value = "/sub-tree", method = { RequestMethod.POST})
    public ApiRest<List<DicValueTreeDTO>> tree(@RequestBody BaseIdReqDTO reqDTO) {
        List<DicValueTreeDTO> dtoList = sysDicValueService.findTree(reqDTO.getId());
        return super.success(dtoList);
    }

}
