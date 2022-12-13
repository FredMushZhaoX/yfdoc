package com.yf.document.modules.sys.user.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yf.boot.base.api.annon.DataProtect;
import com.yf.boot.base.api.api.ApiRest;
import com.yf.boot.base.api.api.controller.BaseController;
import com.yf.boot.base.api.api.dto.BaseIdReqDTO;
import com.yf.boot.base.api.api.dto.BaseIdRespDTO;
import com.yf.boot.base.api.api.dto.BaseIdsReqDTO;
import com.yf.boot.base.api.api.dto.PagingReqDTO;
import com.yf.boot.base.api.utils.BeanMapper;
import com.yf.document.modules.sys.user.dto.SysRoleDTO;
import com.yf.document.modules.sys.user.dto.request.SysRoleMenuReqDTO;
import com.yf.document.modules.sys.user.entity.SysRole;
import com.yf.document.modules.sys.user.service.SysRoleMenuService;
import com.yf.document.modules.sys.user.service.SysRoleService;
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
 * 管理用户控制器
 * </p>
 *
 * @author 聪明笨狗
 * @since 2020-04-13 16:57
 */
@Api(tags = {"管理用户"})
@RestController
@RequestMapping("/api/sys/role")
public class SysRoleController extends BaseController {

    @Autowired
    private SysRoleService baseService;

    @Autowired
    private SysRoleMenuService sysRoleMenuService;


    /**
     * 添加或修改
     * @param reqDTO
     * @return
     */
    @RequiresPermissions(value = {"sys:role:add", "sys:role:update"}, logical = Logical.OR)
    @DataProtect(clazz = SysRole.class, update = true)
    @ApiOperation(value = "添加或修改")
    @RequestMapping(value = "/save", method = { RequestMethod.POST})
    public ApiRest<BaseIdRespDTO> save(@RequestBody SysRoleDTO reqDTO) {
        //复制参数
        SysRole entity = new SysRole();
        BeanMapper.copy(reqDTO, entity);
        baseService.saveOrUpdate(entity);
        return super.success(new BaseIdRespDTO(entity.getId()));
    }

    /**
     * 批量删除
     * @param reqDTO
     * @return
     */
    @RequiresPermissions(value = {"sys:menu:delete"})
    @DataProtect(clazz = SysRole.class, delete = true)
    @ApiOperation(value = "批量删除")
    @RequestMapping(value = "/delete", method = { RequestMethod.POST})
    public ApiRest delete(@RequestBody BaseIdsReqDTO reqDTO) {
        //根据ID删除
        baseService.removeByIds(reqDTO.getIds());
        return super.success();
    }


    /**
     * 查找详情
     * @param reqDTO
     * @return
     */

    @ApiOperation(value = "查找详情")
    @RequestMapping(value = "/detail", method = { RequestMethod.POST})
    public ApiRest<SysRoleDTO> find(@RequestBody BaseIdReqDTO reqDTO) {
        SysRole entity = baseService.getById(reqDTO.getId());
        SysRoleDTO dto = new SysRoleDTO();
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
    public ApiRest<IPage<SysRoleDTO>> paging(@RequestBody PagingReqDTO<SysRoleDTO> reqDTO) {

        //分页查询并转换
        IPage<SysRoleDTO> page = baseService.paging(reqDTO);
        return super.success(page);
    }

    /**
     * 查找列表，每次最多返回200条数据
     * @return
     */

    @ApiOperation(value = "查找列表")
    @RequestMapping(value = "/list", method = { RequestMethod.POST})
    public ApiRest<List<SysRoleDTO>> list() {

        //分页查询并转换
        QueryWrapper<SysRole> wrapper = new QueryWrapper<>();

        //转换并返回
        List<SysRole> list = baseService.list(wrapper);

        //转换数据
        List<SysRoleDTO> dtoList = BeanMapper.mapList(list, SysRoleDTO.class);

        return super.success(dtoList);
    }


    /**
     * 查找角色菜单授权
     * @return
     */
    @RequiresPermissions(value = {"sys:role:grant"})
    @ApiOperation(value = "查找角色菜单授权")
    @RequestMapping(value = "/list-menus", method = { RequestMethod.POST})
    public ApiRest<List<String>> listMenus(@RequestBody BaseIdReqDTO reqDTO) {
        //分页查询并转换
        List<String> ids = sysRoleMenuService.findRoleMenus(reqDTO.getId());
        return super.success(ids);
    }


    /**
     * 保存角色菜单授权
     * @return
     */
    @RequiresPermissions(value = {"sys:role:grant"})
    @ApiOperation(value = "保存角色菜单授权")
    @RequestMapping(value = "/save-menus", method = { RequestMethod.POST})
    public ApiRest saveMenus(@RequestBody SysRoleMenuReqDTO reqDTO) {

        // 保存授权
        sysRoleMenuService.saveRoleIds(reqDTO.getRoleId(), reqDTO.getMenuIds());
        return super.success();
    }


}
