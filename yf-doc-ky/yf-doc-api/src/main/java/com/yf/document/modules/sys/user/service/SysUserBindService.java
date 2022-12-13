package com.yf.document.modules.sys.user.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yf.boot.base.api.api.dto.PagingReqDTO;
import com.yf.document.modules.sys.user.dto.SysUserBindDTO;
import com.yf.document.modules.sys.user.entity.SysUserBind;

import java.util.List;

/**
* <p>
* 登录绑定业务接口类
* </p>
*
* @author 聪明笨狗
* @since 2021-08-02 14:49
*/
public interface SysUserBindService extends IService<SysUserBind> {

    /**
    * 分页查询数据
    * @param reqDTO
    * @return
    */
    IPage<SysUserBindDTO> paging(PagingReqDTO<SysUserBindDTO> reqDTO);

    /**
    * 添加或保存
    * @param reqDTO
    * @return
    */
    void save(SysUserBindDTO reqDTO);

    /**
    * 批量删除
    * @param ids
    * @return
    */
    void delete(List<String> ids);

    /**
    * 查找详情
    * @param id
    * @return
    */
    SysUserBindDTO detail(String id);

    /**
    * 查找列表
    * @param reqDTO
    * @return
    */
    List<SysUserBindDTO> list(SysUserBindDTO reqDTO);

    /**
     * 根据绑定信息查找用户ID
     * @param loginType
     * @param openId
     * @return
     */
    String findBind(String loginType, String openId);
}
