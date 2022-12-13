package com.yf.document.modules.sys.user.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yf.boot.base.api.api.ApiRest;
import com.yf.boot.base.api.api.controller.BaseController;
import com.yf.boot.base.api.api.dto.BaseIdReqDTO;
import com.yf.boot.base.api.api.dto.BaseIdsReqDTO;
import com.yf.boot.base.api.api.dto.PagingReqDTO;
import com.yf.document.modules.sys.user.dto.SysUserBindDTO;
import com.yf.document.modules.sys.user.service.SysUserBindService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
* <p>
* 登录绑定控制器
* </p>
*
* @author 聪明笨狗
* @since 2021-08-02 14:49
*/
@Api(tags={"登录绑定"})
@RestController
@RequestMapping("/api/v1/user/user-bind")
public class SysUserBindController extends BaseController {

    @Autowired
    private SysUserBindService baseService;

    /**
    * 添加或修改
    * @param reqDTO
    * @return
    */
    @ApiOperation(value = "添加或修改")
    @RequestMapping(value = "/save", method = { RequestMethod.POST})
    public ApiRest save(@RequestBody SysUserBindDTO reqDTO) {
        baseService.save(reqDTO);
        return super.success();
    }

    /**
    * 批量删除
    * @param reqDTO
    * @return
    */
    @ApiOperation(value = "批量删除")
    @RequestMapping(value = "/delete", method = { RequestMethod.POST})
    public ApiRest delete(@RequestBody BaseIdsReqDTO reqDTO) {
        //根据ID删除
        baseService.delete(reqDTO.getIds());
        return super.success();
    }

    /**
    * 查找详情
    * @param reqDTO
    * @return
    */
    @ApiOperation(value = "查找详情")
    @RequestMapping(value = "/detail", method = { RequestMethod.POST})
    public ApiRest<SysUserBindDTO> detail(@RequestBody BaseIdReqDTO reqDTO) {
        SysUserBindDTO dto = baseService.detail(reqDTO.getId());
        return super.success(dto);
    }

    /**
    * 分页查找
    * @param reqDTO
    * @return
    */
    @ApiOperation(value = "分页查找")
    @RequestMapping(value = "/paging", method = { RequestMethod.POST})
    public ApiRest<IPage<SysUserBindDTO>> paging(@RequestBody PagingReqDTO<SysUserBindDTO> reqDTO) {

        //分页查询并转换
        IPage<SysUserBindDTO> page = baseService.paging(reqDTO);

        return super.success(page);
    }

    /**
     * 查找列表，每次最多返回200条数据
     * @param reqDTO
     * @return
     */
    @ApiOperation(value = "查找列表")
    @RequestMapping(value = "/list", method = { RequestMethod.POST})
    public ApiRest<List<SysUserBindDTO>> list(@RequestBody SysUserBindDTO reqDTO) {
        // 查找列表
        List<SysUserBindDTO> dtoList = baseService.list(reqDTO);
        return super.success(dtoList);
    }
}
