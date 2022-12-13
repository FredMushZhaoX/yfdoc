package com.yf.document.modules.sys.menu.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yf.boot.base.api.annon.DataProtect;
import com.yf.boot.base.api.api.ApiRest;
import com.yf.boot.base.api.api.controller.BaseController;
import com.yf.boot.base.api.api.dto.BaseIdReqDTO;
import com.yf.boot.base.api.api.dto.BaseIdsReqDTO;
import com.yf.boot.base.api.api.dto.PagingReqDTO;
import com.yf.document.modules.sys.depart.dto.request.DepartSortReqDTO;
import com.yf.document.modules.sys.menu.dto.SysMenuDTO;
import com.yf.document.modules.sys.menu.dto.response.RouteRespDTO;
import com.yf.document.modules.sys.menu.entity.SysMenu;
import com.yf.document.modules.sys.menu.service.SysMenuService;
import com.yf.document.modules.user.UserUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
* <p>
* 系统菜单控制器
* </p>
*
* @author 聪明笨狗
* @since 2021-03-02 13:09
*/
@Api(tags={"系统菜单"})
@RestController
@RequestMapping("/api/sys/menu")
public class SysMenuController extends BaseController {

    @Autowired
    private SysMenuService baseService;

    @ApiOperation(value = "查找列表")
    @RequestMapping(value = "/routes", method = { RequestMethod.POST})
    public ApiRest<List<RouteRespDTO>> routes() {
        List<RouteRespDTO> list = baseService.listRoutes(UserUtils.getUserId());
        return super.success(list);
    }

    /**
    * 添加或修改
    * @param reqDTO
    * @return
    */
    @RequiresPermissions(value = {"sys:menu:add", "sys:menu:update"}, logical = Logical.OR)
    @DataProtect(clazz = SysMenu.class, update = true)
    @ApiOperation(value = "添加或修改")
    @RequestMapping(value = "/save", method = { RequestMethod.POST})
    public ApiRest save(@RequestBody SysMenuDTO reqDTO) {
        baseService.save(reqDTO);
        return super.success();
    }

    /**
    * 批量删除
    * @param reqDTO
    * @return
    */
    @RequiresPermissions(value = {"sys:menu:delete"})
    @DataProtect(clazz = SysMenu.class, delete = true)
    @ApiOperation(value = "批量删除")
    @RequestMapping(value = "/delete", method = { RequestMethod.POST})
    public ApiRest edit(@RequestBody BaseIdsReqDTO reqDTO) {
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
    public ApiRest<SysMenuDTO> find(@RequestBody BaseIdReqDTO reqDTO) {
        SysMenu entity = baseService.getById(reqDTO.getId());
        SysMenuDTO dto = new SysMenuDTO();
        BeanUtils.copyProperties(entity, dto);
        return super.success(dto);
    }

    /**
    * 分页查找
    * @param reqDTO
    * @return
    */

    @ApiOperation(value = "分页查找")
    @RequestMapping(value = "/paging", method = { RequestMethod.POST})
    public ApiRest<IPage<SysMenuDTO>> paging(@RequestBody PagingReqDTO<SysMenuDTO> reqDTO) {

        //分页查询并转换
        IPage<SysMenuDTO> page = baseService.paging(reqDTO);

        return super.success(page);
    }

    /**
     * 分页查找树结构
     * @return
     */

    @ApiOperation(value = "树结构列表")
    @RequestMapping(value = "/tree", method = { RequestMethod.POST})
    public ApiRest<List<RouteRespDTO>> tree() {

        //分页查询并转换
        List<RouteRespDTO> list = baseService.listTree();

        return super.success(list);
    }


    /**
     * 分类排序
     * @param reqDTO
     * @return
     */
    @RequiresPermissions(value = {"sys:menu:sort"})
    @DataProtect(clazz = SysMenu.class, update = true)
    @ApiOperation(value = "分类排序")
    @RequestMapping(value = "/sort", method = { RequestMethod.POST})
    public ApiRest sort(@RequestBody DepartSortReqDTO reqDTO) {
        baseService.sort(reqDTO.getId(), reqDTO.getSort());
        return super.success();
    }


}
