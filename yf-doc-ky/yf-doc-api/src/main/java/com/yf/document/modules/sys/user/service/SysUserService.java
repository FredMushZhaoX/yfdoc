package com.yf.document.modules.sys.user.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yf.boot.base.api.api.dto.PagingReqDTO;
import com.yf.document.modules.sys.user.dto.SysUserDTO;
import com.yf.document.modules.sys.user.dto.request.MobileLoginReqDTO;
import com.yf.document.modules.sys.user.dto.request.MobileRegReqDTO;
import com.yf.document.modules.sys.user.dto.request.ResetPassReqDTO;
import com.yf.document.modules.sys.user.dto.request.SysUserLoginReqDTO;
import com.yf.document.modules.sys.user.dto.request.SysUserPassReqDTO;
import com.yf.document.modules.sys.user.dto.request.SysUserQueryReqDTO;
import com.yf.document.modules.sys.user.dto.request.SysUserSaveReqDTO;
import com.yf.document.modules.sys.user.dto.request.UserDeptReqDTO;
import com.yf.document.modules.sys.user.dto.response.SysUserLoginDTO;
import com.yf.document.modules.sys.user.dto.response.UserExportDTO;
import com.yf.document.modules.sys.user.dto.response.UserListRespDTO;
import com.yf.document.modules.sys.user.entity.SysUser;

import java.util.List;

/**
* <p>
* 管理用户业务类
* </p>
*
* @author 聪明笨狗
* @since 2020-04-13 16:57
*/
public interface SysUserService extends IService<SysUser> {

    /**
    * 分页查询数据
    * @param reqDTO
    * @return
    */
    IPage<UserListRespDTO> paging(PagingReqDTO<SysUserQueryReqDTO> reqDTO);

    /**
     * 用户登录请求类
     * @param reqDTO
     * @return
     */
    SysUserLoginDTO login(SysUserLoginReqDTO reqDTO);

    /**
     * 手机登录请求类
     * @param reqDTO
     * @return
     */
    SysUserLoginDTO mobileLogin(MobileLoginReqDTO reqDTO);


    /**
     * 获取管理会话
     * @param token
     * @return
     */
    SysUserLoginDTO token(String token);

    /**
     * 退出登录
     * @param token
     */
    void logout(String token);

    /**
     * 修改用户资料
     * @param reqDTO
     */
    void update(SysUserDTO reqDTO);

    /**
     * 修改密码
     * @param reqDTO
     */
    void pass(SysUserPassReqDTO reqDTO);
    /**
     * 保存添加系统用户
     * @param reqDTO
     */
    void save(SysUserSaveReqDTO reqDTO);

    /**
     * 手机号码注册
     * @param reqDTO
     * @return
     */
    SysUserLoginDTO reg(MobileRegReqDTO reqDTO);

    /**
     * 导入用户
     * @param reqDTO
     */
    void importExcel(List<UserExportDTO> reqDTO);

    /**
     * 查找数据用于导出
     * @param reqDTO
     * @return
     */
    List<UserExportDTO> listForExport(SysUserQueryReqDTO reqDTO);

    /**
     * 用户详情
     * @param id
     * @return
     */
    SysUserSaveReqDTO detail(String id);

    /**
     * 根据部门加载用户ID列表
     * @param codes
     * @return
     */
    List<String> listByDept(List<String> codes);

    /**
     * 批量操作用户机构
     * @param reqDTO
     */
    void batchDept(UserDeptReqDTO reqDTO);

    /**
     * 重置密码
     * @param reqDTO
     */
    void resetPass(ResetPassReqDTO reqDTO);

    /**
     * 三方登录
     * @param loginType
     * @param openId
     * @param nickName
     * @param avatar
     * @return
     */
    SysUserLoginDTO loginByThird(String loginType, String openId, String nickName, String avatar);

    /**
     * 用户同步登录接口
     * @param userName
     * @param realName
     * @param roles
     * @param timestamp
     * @param departs
     * @param sign
     * @return
     */
    SysUserLoginDTO syncLogin(String userName, String realName, String roles, Long timestamp, String departs, String sign);
}
