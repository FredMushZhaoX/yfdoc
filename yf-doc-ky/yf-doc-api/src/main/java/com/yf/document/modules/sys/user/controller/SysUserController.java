package com.yf.document.modules.sys.user.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yf.boot.base.api.annon.DataProtect;
import com.yf.boot.base.api.api.ApiRest;
import com.yf.boot.base.api.api.controller.BaseController;
import com.yf.boot.base.api.api.dto.*;
import com.yf.boot.base.api.exception.ServiceException;
import com.yf.boot.base.api.utils.excel.ExportExcel;
import com.yf.boot.base.api.utils.excel.ImportExcel;
import com.yf.boot.base.utils.ResourceUtil;
import com.yf.document.modules.sys.user.dto.SysUserDTO;
import com.yf.document.modules.sys.user.dto.request.*;
import com.yf.document.modules.sys.user.dto.response.SysUserLoginDTO;
import com.yf.document.modules.sys.user.dto.response.UserExportDTO;
import com.yf.document.modules.sys.user.dto.response.UserListRespDTO;
import com.yf.document.modules.sys.user.entity.SysUser;
import com.yf.document.modules.sys.user.service.SysUserRoleService;
import com.yf.document.modules.sys.user.service.SysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
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
@RequestMapping("/api/sys/user")
public class SysUserController extends BaseController {

    @Autowired
    private SysUserService baseService;

    @Autowired
    private SysUserRoleService sysUserRoleService;

    /**
     * 用户详情
     * @return
     */
    @ApiOperation(value = "用户详情")
    @RequestMapping(value = "/detail", method = {RequestMethod.POST})
    public ApiRest<SysUserSaveReqDTO> detail(@RequestBody BaseIdReqDTO reqDTO) {
        SysUserSaveReqDTO respDTO = baseService.detail(reqDTO.getId());
        return super.success(respDTO);
    }

    /**
     * 用户登录
     * @return
     */
    @ApiOperation(value = "账号密码登录")
    @RequestMapping(value = "/login", method = {RequestMethod.POST})
    public ApiRest<SysUserLoginDTO> login(@RequestBody SysUserLoginReqDTO reqDTO) {
        SysUserLoginDTO respDTO = baseService.login(reqDTO);
        return super.success(respDTO);
    }

    /**
     * 用户登录
     * @return
     */
    @ApiOperation(value = "手机号登录")
    @RequestMapping(value = "/mobile-login", method = {RequestMethod.POST})
    public ApiRest<SysUserLoginDTO> mobileLogin(@RequestBody MobileLoginReqDTO reqDTO) {
        SysUserLoginDTO respDTO = baseService.mobileLogin(reqDTO);
        return super.success(respDTO);
    }

    /**
     * 用户登录
     * @return
     */
    @ApiOperation(value = "退出登录")
    @RequestMapping(value = "/logout", method = {RequestMethod.POST})
    public ApiRest logout(HttpServletRequest request) {
         String token = request.getHeader("token");
         baseService.logout(token);
        return super.success();
    }

    /**
     * 获取会话
     * @return
     */
    @ApiOperation(value = "获取会话")
    @RequestMapping(value = "/info", method = {RequestMethod.POST})
    public ApiRest info(@RequestBody BaseTokenReqDTO reqDTO) {
        SysUserLoginDTO respDTO = baseService.token(reqDTO.getToken());
        return success(respDTO);
    }

    /**
     * 修改用户资料
     * @return
     */
    @DataProtect(clazz = SysUser.class, update = true, currUsr = true)
    @ApiOperation(value = "修改用户资料")
    @RequestMapping(value = "/update", method = {RequestMethod.POST})
    public ApiRest update(@RequestBody SysUserDTO reqDTO) {
        baseService.update(reqDTO);
        return success();
    }

    /**
     * 修改密码
     * @return
     */
    @DataProtect(clazz = SysUser.class, update = true, currUsr = true)
    @ApiOperation(value = "修改密码")
    @RequestMapping(value = "/update-pass", method = {RequestMethod.POST})
    public ApiRest updatePass(@RequestBody SysUserPassReqDTO reqDTO) {
        baseService.pass(reqDTO);
        return success();
    }


    /**
     * 保存或修改系统用户
     * @return
     */
    @RequiresPermissions(value = {"sys:user:add", "sys:user:update"}, logical = Logical.OR)
    @DataProtect(clazz = SysUser.class, update = true)
    @ApiOperation(value = "保存或修改")
    @RequestMapping(value = "/save", method = {RequestMethod.POST})
    public ApiRest save(@RequestBody SysUserSaveReqDTO reqDTO) {
        baseService.save(reqDTO);
        return success();
    }


    /**
     * 批量删除
     * @param reqDTO
     * @return
     */
    @RequiresPermissions(value = {"sys:user:delete"})
    @DataProtect(clazz = SysUser.class, delete = true)
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
    public ApiRest<IPage<UserListRespDTO>> paging(@RequestBody PagingReqDTO<SysUserQueryReqDTO> reqDTO) {

        //分页查询并转换
        IPage<UserListRespDTO> page = baseService.paging(reqDTO);
        return super.success(page);
    }

    /**
     * 修改状态
     * @param reqDTO
     * @return
     */
    @RequiresPermissions(value = {"sys:user:state"})
    @DataProtect(clazz = SysUser.class, update = true, currUsr = true)
    @ApiOperation(value = "修改状态")
    @RequestMapping(value = "/state", method = { RequestMethod.POST})
    public ApiRest state(@RequestBody BaseStateReqDTO reqDTO) {

        // 条件
        QueryWrapper<SysUser> wrapper = new QueryWrapper<>();
        wrapper.lambda()
                .in(SysUser::getId, reqDTO.getIds())
                .ne(SysUser::getUserName, "admin");


        SysUser record = new SysUser();
        record.setState(reqDTO.getState());
        baseService.update(record, wrapper);

        return super.success();
    }

    /**
     * 保存或修改系统用户
     * @return
     */
    @ApiOperation(value = "手机号注册")
    @RequestMapping(value = "/reg", method = {RequestMethod.POST})
    public ApiRest<SysUserLoginDTO> reg(@RequestBody MobileRegReqDTO reqDTO) {
        SysUserLoginDTO respDTO = baseService.reg(reqDTO);
        return success(respDTO);
    }

    /**
     * 批量修改部门
     * @return
     */
    @ApiOperation(value = "批量修改部门")
    @RequestMapping(value = "/batch-dept", method = {RequestMethod.POST})
    public ApiRest batchDept(@RequestBody UserDeptReqDTO reqDTO) {
        baseService.batchDept(reqDTO);
        return success();
    }

    /**
     * 批量修改角色
     * @return
     */
    @ApiOperation(value = "批量修改角色")
    @RequestMapping(value = "/batch-role", method = {RequestMethod.POST})
    public ApiRest batchRole(@RequestBody UserRoleReqDTO reqDTO) {
        sysUserRoleService.batchRole(reqDTO);
        return success();
    }

    /**
     * 重置密码
     * @return
     */
    @ApiOperation(value = "重置密码")
    @RequestMapping(value = "/reset-pass", method = {RequestMethod.POST})
    public ApiRest resetPass(@RequestBody ResetPassReqDTO reqDTO) {
        baseService.resetPass(reqDTO);
        return success();
    }


    /**
     * 导出excel文件
     */
    @RequiresPermissions(value = {"sys:user:export"})
    @ResponseBody
    @RequestMapping(value = "/export")
    public ApiRest exportFile(HttpServletResponse response, @RequestBody SysUserQueryReqDTO reqDTO) {


        List<UserExportDTO> list = baseService.listForExport(reqDTO);

        try {
            new ExportExcel("用户", UserExportDTO.class).setDataList(list).write(response).dispose();
            return super.success();
        } catch (Exception e) {
            return failure(e.getMessage());
        }
    }



    /**
     * 导入Excel
     *
     * @param file
     * @return
     */
    @RequiresPermissions(value = {"sys:user:import"})
    @ResponseBody
    @RequestMapping(value = "import")
    public ApiRest importFile(@RequestParam("file") MultipartFile file) {

        try {

            // 解析导入源数据
            ImportExcel ei = new ImportExcel(file, 1, 0);
            List<UserExportDTO> list = ei.getDataList(UserExportDTO.class);

            // 导入数据条数
            baseService.importExcel(list);

            // 导入成功
            return super.success();

        } catch (ServiceException e) {

            return super.failure(e);

        } catch (Exception e) {
            return super.failure(e);
        }

    }


    /**
     * 下载导入题目数据模板
     */

    @ResponseBody
    @RequestMapping(value = "import/template")
    public ApiRest importFileTemplate(HttpServletResponse response) {
        try {

            // 读出文件流
            ResourceUtil.write(response, "excel/user_template.xlsx");

            return super.success();
        } catch (Exception e) {
            e.printStackTrace();
            return super.failure("导入模板下载失败！失败信息："+e.getMessage());
        }
    }



    /**
     * 用户同步登录接口
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "sync-login", method = {RequestMethod.GET})
    public ApiRest syncLogin(HttpServletResponse response,
                             @RequestParam("userName") String userName,
                             @RequestParam("realName") String realName,
                             @RequestParam("timestamp") Long timestamp,
                             @RequestParam("departs") String departs,
                             @RequestParam("role") String role,
                             @RequestParam("sign") String token) throws IOException {

        SysUserLoginDTO respDTO = baseService.syncLogin(userName, realName, role, timestamp, departs, token);

        response.setCharacterEncoding("utf-8");
        response.setHeader("Content-Type","text/html;charset=utf-8");

        Cookie cookie = new Cookie("token", respDTO.getToken());
        cookie.setPath("/");
        // 90天失效
        cookie.setMaxAge(60 * 60 * 24 * 90);
        response.addCookie(cookie);
        response.setHeader("refresh","3;URL=/");

        PrintWriter writer = response.getWriter();
        // 写出html，客户端设置token
        writer.write("<html>" +
                "<head>" +
                "<title>登录跳转</title>" +
                "</head>" +
                "<body>" +
                "<div>登录成功，正在跳转...</div>" +
                "</body>" +
                "</html>");
        writer.close();
        return null;
    }
}
