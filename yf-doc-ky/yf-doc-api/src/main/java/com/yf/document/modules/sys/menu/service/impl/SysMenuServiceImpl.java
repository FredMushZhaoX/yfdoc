package com.yf.document.modules.sys.menu.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yf.boot.base.api.api.dto.PagingReqDTO;
import com.yf.boot.base.api.exception.ServiceException;
import com.yf.boot.base.api.utils.BeanMapper;
import com.yf.document.modules.sys.menu.dto.SysMenuDTO;
import com.yf.document.modules.sys.menu.dto.response.RouteRespDTO;
import com.yf.document.modules.sys.menu.entity.SysMenu;
import com.yf.document.modules.sys.menu.mapper.SysMenuMapper;
import com.yf.document.modules.sys.menu.service.SysMenuService;
import com.yf.document.utils.CacheKey;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 系统菜单业务实现类
 * </p>
 *
 * @author 聪明笨狗
 * @since 2021-03-02 13:09
 */
@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements SysMenuService {

    @Override
    public IPage<SysMenuDTO> paging(PagingReqDTO<SysMenuDTO> reqDTO) {

        // 创建分页对象
        Page query = reqDTO.toPage();


        //查询条件
        QueryWrapper<SysMenu> wrapper = new QueryWrapper<>();

        // 请求参数
        SysMenuDTO params = reqDTO.getParams();


        //获得数据
        IPage<SysMenu> page = this.page(query, wrapper);
        //转换结果
        IPage<SysMenuDTO> pageData = JSON.parseObject(JSON.toJSONString(page), new TypeReference<Page<SysMenuDTO>>() {
        });
        return pageData;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void save(SysMenuDTO reqDTO) {

        if (StringUtils.isBlank(reqDTO.getId())) {
            this.fillSort(reqDTO);
        } else {
            reqDTO.setSort(null);
        }

        SysMenu entity = new SysMenu();
        BeanMapper.copy(reqDTO, entity);
        this.saveOrUpdate(entity);
    }

    @Override
    public void delete(List<String> ids) {
        //查询条件
        QueryWrapper<SysMenu> wrapper = new QueryWrapper<>();
        wrapper.lambda().in(SysMenu::getParentId, ids);
        int count = this.count(wrapper);
        if(count > 0){
            throw new ServiceException("请先删除下级菜单！");
        }

        this.removeByIds(ids);
    }

    @Override
    public List<String> findPermissions(String userId) {
        return baseMapper.findPermissions(userId);
    }


    @Cacheable(value = CacheKey.MENU, key = "#userId")
    @Override
    public List<RouteRespDTO> listRoutes(String userId) {

        // 全部菜单列表
        LinkedHashMap<String,SysMenu> map = this.allMap();

        // 构建路由菜单
        List<String> list = baseMapper.findUserMenu(userId);

        // 补全上级
        List<String> ids = new ArrayList<>();
        for(String id: list){
            this.addParent(map.get(id), map, ids);
        }

        // 移除为未授权菜单
        List<String> keys = new ArrayList<>();
        BeanMapper.copy(map.keySet(), keys);

        for(String key: keys){
            if(!ids.contains(key)){
                map.remove(key);
            }
        }

        // 剩下授权的
        List<SysMenu> routes = new ArrayList<>(map.values());

        return this.buildRoutes(routes);
    }

    @Override
    public List<String> listPermission(String userId) {
        return null;
    }

    /**
     * 递归添加上级
     * @param menu
     * @param map
     * @param ids
     */
    private void addParent(SysMenu menu, Map<String, SysMenu> map, List<String> ids){

        // 添加菜单
        if(menu == null){
            return;
        }

        ids.add(menu.getId());

        // 还存在上级
        String parentId = menu.getParentId();
        if(!StringUtils.isBlank(parentId)
                && !"0".equals(parentId)
                && !ids.contains(parentId)){
            this.addParent(map.get(parentId), map, ids);
        }
    }

    @Override
    public List<RouteRespDTO> listTree() {

        // 列出用户菜单
        List<SysMenu> list = baseMapper.findAllMenu();
        return this.buildTree(list);
    }


    /**
     * 将菜单列表展开成包含父子关系的Map
     * @param list
     * @return
     */
    private Map<String, List<RouteRespDTO>> listToMap(List<SysMenu> list){

        // 列出用户菜单
        List<RouteRespDTO> dtoList = BeanMapper.mapList(list, RouteRespDTO.class);

        //子结构的列表
        Map<String, List<RouteRespDTO>> map = new HashMap<>(16);

        for (RouteRespDTO item : dtoList) {

            //如果存在
            if (map.containsKey(item.getParentId())) {
                map.get(item.getParentId()).add(item);
                continue;
            }

            //增加新的结构
            List<RouteRespDTO> a = new ArrayList<>();
            a.add(item);
            map.put(item.getParentId(), a);
        }

        return map;
    }


    /**
     * 构建路由表
     * @param list
     * @return
     */
    private List<RouteRespDTO> buildRoutes(List<SysMenu> list) {

        // 获取Map
        Map<String, List<RouteRespDTO>> map = this.listToMap(list);

        //注意，第0级为顶级的
        List<RouteRespDTO> topList = map.get("0");
        if (!CollectionUtils.isEmpty(topList)) {
            for (RouteRespDTO item : topList) {
                // 展开下级
                List<RouteRespDTO> children = new ArrayList<>();
                this.expandRoutes(map, children, item);
                item.setChildren(children);
            }
        }

        return topList;
    }


    /**
     * 递归去做填充数据
     *
     * @param map
     * @param item
     */
    private void expandRoutes(Map<String, List<RouteRespDTO>> map, List<RouteRespDTO> routes, RouteRespDTO item) {

        //设置子类
        if (map.containsKey(item.getId())) {
            List<RouteRespDTO> children = map.get(item.getId());
            if(!CollectionUtils.isEmpty(children)){

                // 继续平铺展开
                for (RouteRespDTO child: children){
                    if(!StringUtils.isBlank(child.getPath()) && !StringUtils.isBlank(child.getComponent())){
                        routes.add(child);
                    }

                    this.expandRoutes(map, routes, child);
                }
            }
        }
    }



    /**
     * 构建菜单树
     *
     * @param list
     * @return
     */
    private List<RouteRespDTO> buildTree(List<SysMenu> list) {

        // 获取Map
        Map<String, List<RouteRespDTO>> map = this.listToMap(list);

        //注意，第0级为顶级的
        List<RouteRespDTO> topList = map.get("0");
        if (!CollectionUtils.isEmpty(topList)) {
            for (RouteRespDTO item : topList) {
                this.fillChildren(map, item);
            }
        }

        return topList;
    }

    /**
     * 递归去做填充数据
     *
     * @param map
     * @param item
     */
    private void fillChildren(Map<String, List<RouteRespDTO>> map, RouteRespDTO item) {

        //设置子类
        if (map.containsKey(item.getId())) {

            List<RouteRespDTO> children = map.get(item.getId());

            if (!CollectionUtils.isEmpty(children)) {
                for (RouteRespDTO sub : children) {
                    this.fillChildren(map, sub);
                }
            }
            item.setChildren(children);
        }
    }

    /**
     * 填充功能数据
     * @param map
     * @param item
     * @return
     */
    private List<RouteRespDTO> fillFunction(Map<String, List<RouteRespDTO>> map, RouteRespDTO item) {

        //设置子类
        if (map.containsKey(item.getId())) {

            List<RouteRespDTO> children = map.get(item.getId());

            List<RouteRespDTO> functions = new ArrayList<>();

            if (!CollectionUtils.isEmpty(children)) {
                for (RouteRespDTO sub : children) {

                    if(sub.getMenuType().equals(2)){
                        functions.add(sub);
                    }
                }
            }

            return functions;
        }

        return null;
    }


    @Override
    public void sort(String id, Integer sort) {

        SysMenu depart = this.getById(id);
        SysMenu exchange = null;

        QueryWrapper<SysMenu> wrapper = new QueryWrapper<>();
        // 同级排序
        wrapper.lambda().eq(SysMenu::getParentId, depart.getParentId());
        wrapper.last("LIMIT 1");

        // 上升
        if (sort == 0) {
            // 同级排序
            wrapper.lambda()
                    .lt(SysMenu::getSort, depart.getSort())
                    .orderByDesc(SysMenu::getSort);
            exchange = this.getOne(wrapper, false);
        }

        // 下降
        if (sort == 1) {
            // 同级排序
            wrapper.lambda()
                    .gt(SysMenu::getSort, depart.getSort())
                    .orderByAsc(SysMenu::getSort);
            exchange = this.getOne(wrapper, false);
        }


        if (exchange != null) {
            SysMenu a = new SysMenu();
            a.setId(id);
            a.setSort(exchange.getSort());
            SysMenu b = new SysMenu();
            b.setId(exchange.getId());
            b.setSort(depart.getSort());
            this.updateById(a);
            this.updateById(b);
        }
    }


    /**
     * 填充排序
     *
     * @param reqDTO
     */
    private void fillSort(SysMenuDTO reqDTO) {

        QueryWrapper<SysMenu> wrapper = new QueryWrapper<>();

        // 同级排序
        wrapper.lambda()
                .eq(SysMenu::getParentId, reqDTO.getParentId())
                .orderByDesc(SysMenu::getSort);
        wrapper.last("LIMIT 1");
        SysMenu depart = this.getOne(wrapper, false);

        if (depart != null) {
            reqDTO.setSort(depart.getSort() + 1);
        } else {
            reqDTO.setSort(1);
        }
    }

    /**
     * 全部菜单的Map
     * @return
     */
    public LinkedHashMap<String,SysMenu> allMap(){
        List<SysMenu> menus = baseMapper.findAllMenu();
        LinkedHashMap<String,SysMenu> map = new LinkedHashMap<>(16);
        for(SysMenu item: menus){
            map.put(item.getId(), item);
        }
        return map;
    }

}
