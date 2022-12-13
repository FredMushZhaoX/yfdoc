package com.yf.document.modules.sys.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yf.document.modules.sys.user.dto.request.SysUserQueryReqDTO;
import com.yf.document.modules.sys.user.dto.response.UserExportDTO;
import com.yf.document.modules.sys.user.dto.response.UserListRespDTO;
import com.yf.document.modules.sys.user.entity.SysUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* <p>
* 管理用户Mapper
* </p>
*
* @author 聪明笨狗
* @since 2020-04-13 16:57
*/
public interface SysUserMapper extends BaseMapper<SysUser> {

    /**
     * 查找数据用于导出
     * @param reqDTO
     * @return
     */
    List<UserExportDTO> listForExport(@Param("query") SysUserQueryReqDTO reqDTO);

    /**
     * 查找用户分页
     * @param page
     * @param reqDTO
     * @return
     */
    IPage<UserListRespDTO> paging(Page page, @Param("query") SysUserQueryReqDTO reqDTO);
}
