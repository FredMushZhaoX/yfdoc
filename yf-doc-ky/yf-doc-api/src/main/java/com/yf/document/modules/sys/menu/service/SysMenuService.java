package com.yf.document.modules.sys.menu.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yf.boot.base.api.api.dto.PagingReqDTO;
import com.yf.document.modules.sys.menu.dto.SysMenuDTO;
import com.yf.document.modules.sys.menu.dto.response.RouteRespDTO;
import com.yf.document.modules.sys.menu.entity.SysMenu;

import java.util.List;

/**
* <p>
* 系统菜单业务类
* </p>
*
* @author 聪明笨狗
* @since 2021-03-02 13:09
*/
public interface SysMenuService extends IService<SysMenu> {

    /**
    * 分页查询数据
    * @param reqDTO
    * @return
    */
    IPage<SysMenuDTO> paging(PagingReqDTO<SysMenuDTO> reqDTO);

    /**
     * 列出全部菜单
     * @param userId 
     * @return
     */
    List<RouteRespDTO> listRoutes(String userId);


    /**
     * 查找用户权限表
     * @param userId
     * @return
     */
    List<String> listPermission(String userId);

    /**
     * 列出全部菜单
     * @return
     */
    List<RouteRespDTO> listTree();

    /**
     * 排序
     * @param id
     * @param sort
     */
    void sort(String id, Integer sort);

    /**
     * 保存菜单
     * @param reqDTO
     */
    void save(SysMenuDTO reqDTO);

    /**
     * 删除菜单
     * @param ids
     */
    void delete(List<String> ids);

    /**
     * 查找用户全部的权限表
     * @param userId
     * @return
     */
    List<String> findPermissions(String userId);
}


