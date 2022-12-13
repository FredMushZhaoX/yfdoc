package com.yf.document.modules.sys.notice.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yf.boot.base.api.annon.DataProtect;
import com.yf.boot.base.api.api.ApiRest;
import com.yf.boot.base.api.api.controller.BaseController;
import com.yf.boot.base.api.api.dto.BaseIdReqDTO;
import com.yf.boot.base.api.api.dto.BaseIdRespDTO;
import com.yf.boot.base.api.api.dto.BaseIdsReqDTO;
import com.yf.boot.base.api.api.dto.BaseStateReqDTO;
import com.yf.boot.base.api.api.dto.PagingReqDTO;
import com.yf.boot.base.api.utils.BeanMapper;
import com.yf.document.modules.sys.notice.dto.SysNoticeDTO;
import com.yf.document.modules.sys.notice.entity.SysNotice;
import com.yf.document.modules.sys.notice.service.SysNoticeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
* <p>
* 系统公告控制器
* </p>
*
* @author 聪明笨狗
* @since 2020-10-16 12:01
*/
@Api(tags={"系统公告"})
@RestController
@RequestMapping("/api/sys/notice")
public class SysNoticeController extends BaseController {

    @Autowired
    private SysNoticeService baseService;

    /**
    * 添加或修改
    * @param reqDTO
    * @return
    */
    @RequiresPermissions(value = {"sys:notice:add", "sys:notice:update"}, logical = Logical.OR)
    @DataProtect(clazz = SysNotice.class, update = true)
    @ApiOperation(value = "添加或修改")
    @RequestMapping(value = "/save", method = { RequestMethod.POST})
    public ApiRest<BaseIdRespDTO> save(@RequestBody SysNoticeDTO reqDTO) {
        //复制参数
        SysNotice entity = new SysNotice();
        BeanMapper.copy(reqDTO, entity);
        baseService.saveOrUpdate(entity);
        return super.success(new BaseIdRespDTO(entity.getId()));
    }

    /**
    * 批量删除
    * @param reqDTO
    * @return
    */
    @RequiresPermissions(value = {"sys:notice:delete"})
    @DataProtect(clazz = SysNotice.class, delete = true)
    @ApiOperation(value = "批量删除")
    @RequestMapping(value = "/delete", method = { RequestMethod.POST})
    public ApiRest edit(@RequestBody BaseIdsReqDTO reqDTO) {
        //根据ID删除
        baseService.removeByIds(reqDTO.getIds());
        return super.success();
    }

    /**
    * 查找详情
    * @param reqDTO
    * @return
    */
    @ApiOperation(value = "查找详情")
    @RequestMapping(value = "/detail", method = { RequestMethod.POST})
    public ApiRest<SysNoticeDTO> find(@RequestBody BaseIdReqDTO reqDTO) {
        SysNotice entity = baseService.getById(reqDTO.getId());
        SysNoticeDTO dto = new SysNoticeDTO();
        BeanUtils.copyProperties(entity, dto);
        return super.success(dto);
    }


    /**
     * 修改状态
     * @param reqDTO
     * @return
     */
    @ApiOperation(value = "修改状态")
    @RequestMapping(value = "/state", method = { RequestMethod.POST})
    public ApiRest state(@RequestBody BaseStateReqDTO reqDTO) {

        // 条件
        QueryWrapper<SysNotice> wrapper = new QueryWrapper<>();
        wrapper.lambda()
                .in(SysNotice::getId, reqDTO.getIds());

        SysNotice record = new SysNotice();
        record.setState(reqDTO.getState());
        baseService.update(record, wrapper);

        return super.success();
    }

    /**
    * 分页查找
    * @param reqDTO
    * @return
    */
    @ApiOperation(value = "分页查找")
    @RequestMapping(value = "/paging", method = { RequestMethod.POST})
    public ApiRest<IPage<SysNoticeDTO>> paging(@RequestBody PagingReqDTO<SysNoticeDTO> reqDTO) {

        //分页查询并转换
        IPage<SysNoticeDTO> page = baseService.paging(reqDTO);

        return super.success(page);
    }
}
