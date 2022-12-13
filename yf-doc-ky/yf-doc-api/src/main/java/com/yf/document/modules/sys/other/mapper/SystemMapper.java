package com.yf.document.modules.sys.other.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 系统零散的Mapper,用于处理一些通用的需求，非模块化的数据查询
 * @author bool
 */
@Mapper
public interface SystemMapper {

    /**
     * 统计系统数据的数量
     * @param table
     * @param ids
     * @return
     */
    int countSysData(@Param("table") String table, @Param("ids") List<String> ids);
}
