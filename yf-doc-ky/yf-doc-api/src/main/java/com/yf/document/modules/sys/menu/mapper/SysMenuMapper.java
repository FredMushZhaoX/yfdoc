package com.yf.document.modules.sys.menu.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yf.document.modules.sys.menu.entity.SysMenu;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* <p>
* 系统菜单Mapper
* </p>
*
* @author 聪明笨狗
* @since 2021-03-02 13:09
*/
public interface SysMenuMapper extends BaseMapper<SysMenu> {

    /**
     * 查找用户菜单表
     * @param userId
     * @return
     */
    List<String> findUserMenu(@Param("userId") String userId);

    /**
     * 查找全部菜单
     * @return
     */
    List<SysMenu> findAllMenu();


    /**
     * 查找用户全部的权限表
     * @param userId
     * @return
     */
    List<String> findPermissions(@Param("userId") String userId);


}
