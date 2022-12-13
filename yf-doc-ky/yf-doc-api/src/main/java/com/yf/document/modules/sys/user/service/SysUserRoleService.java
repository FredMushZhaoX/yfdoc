package com.yf.document.modules.sys.user.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yf.boot.base.api.api.dto.PagingReqDTO;
import com.yf.document.modules.sys.user.dto.SysUserRoleDTO;
import com.yf.document.modules.sys.user.dto.request.UserRoleReqDTO;
import com.yf.document.modules.sys.user.entity.SysRole;
import com.yf.document.modules.sys.user.entity.SysUserRole;

import java.util.List;

/**
* <p>
* 用户角色业务类
* </p>
*
* @author 聪明笨狗
* @since 2020-04-13 16:57
*/
public interface SysUserRoleService extends IService<SysUserRole> {

    /**
    * 分页查询数据
    * @param reqDTO
    * @return
    */
    IPage<SysUserRoleDTO> paging(PagingReqDTO<SysUserRoleDTO> reqDTO);

    /**
     * 查找用户角色列表
     * @param userId
     * @return
     */
    List<SysRole> listRoles(String userId);

    /**
     * 查找角色ID列表
     * @param userId
     * @return
     */
    List<String> listRoleIds(String userId);

    /**
     * 保存全部角色
     * @param userId
     * @param ids
     * @return
     */
    void saveRoles(String userId, List<String> ids);


    /**
     * 查找用户的权限标签
     * @param userId
     * @return
     */
    List<String> findUserPermission(String userId);

    /**
     * 批量操作用户角色
     * @param reqDTO
     */
    void batchRole(UserRoleReqDTO reqDTO);
}
