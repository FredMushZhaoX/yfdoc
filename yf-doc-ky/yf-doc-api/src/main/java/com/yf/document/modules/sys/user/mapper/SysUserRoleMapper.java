package com.yf.document.modules.sys.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yf.document.modules.sys.user.entity.SysRole;
import com.yf.document.modules.sys.user.entity.SysUserRole;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* <p>
* 用户角色Mapper
* </p>
*
* @author 聪明笨狗
* @since 2020-04-13 16:57
*/
public interface SysUserRoleMapper extends BaseMapper<SysUserRole> {

    /**
     * 查找用户的角色列表
     * @param userId
     * @return
     */
    List<SysRole> listByUser(@Param("userId") String userId);

    /**
     * 查找用户的权限标签
     * @param userId
     * @return
     */
    List<String> findUserPermission(@Param("userId") String userId);
}
