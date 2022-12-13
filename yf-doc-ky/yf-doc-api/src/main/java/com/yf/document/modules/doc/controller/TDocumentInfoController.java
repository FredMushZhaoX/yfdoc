package com.yf.document.modules.doc.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.yf.boot.base.api.api.ApiRest;
import com.yf.boot.base.api.api.controller.BaseController;
import com.yf.boot.base.api.api.dto.BaseIdReqDTO;
import com.yf.boot.base.api.api.dto.BaseIdRespDTO;
import com.yf.boot.base.api.api.dto.BaseIdsReqDTO;
import com.yf.boot.base.api.api.dto.PagingReqDTO;
import com.yf.boot.base.api.utils.BeanMapper;
import com.yf.boot.base.api.utils.file.StringUtils;
import com.yf.document.modules.doc.dto.TDocumentInfoDTO;
import com.yf.document.modules.doc.entity.TDocumentInfo;
import com.yf.document.modules.doc.service.TDocumentInfoService;
import com.yf.document.modules.user.UserUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
* <p>
* 文档信息表控制器
* </p>
*
* @author Panjp
* @since 2021-08-23 20:30
*/
@Api(tags={"文档信息表"})
@RestController
@RequestMapping("/api/doc/doc")
public class TDocumentInfoController extends BaseController {

    @Autowired
    private TDocumentInfoService baseService;

    /**
    * 添加或修改
    * @param reqDTO
    * @return
    */
    @ApiOperation(value = "添加或修改")
    @RequestMapping(value = "/save", method = { RequestMethod.POST})
    public ApiRest save(@RequestBody TDocumentInfoDTO reqDTO) {
        baseService.save(reqDTO);
        return super.success();
    }

    /**
     * 提交审核
     * @param reqDTO
     * @return
     */
    @ApiOperation(value = "提交审核")
    @RequestMapping(value = "/submiReview", method = { RequestMethod.POST})
    public ApiRest submiReview(@RequestBody TDocumentInfoDTO reqDTO) {
        reqDTO = baseService.detail(reqDTO.getId());
        reqDTO.setStatus("2");
        baseService.updateStatus(reqDTO);
        return super.success();
    }
    /**
     * 审核通过
     * @param reqDTO
     * @return
     */
    @ApiOperation(value = "审核通过")
    @RequestMapping(value = "/approved", method = { RequestMethod.POST})
    public ApiRest approved(@RequestBody TDocumentInfoDTO reqDTO) {
        reqDTO = baseService.detail(reqDTO.getId());
        reqDTO.setStatus("3");
        baseService.updateStatus(reqDTO);
        return super.success();
    }

    /**
     * 审核不通过
     * @param reqDTO
     * @return
     */
    @ApiOperation(value = "审核不通过")
    @RequestMapping(value = "/approvedFailed", method = { RequestMethod.POST})
    public ApiRest approvedFailed(@RequestBody TDocumentInfoDTO reqDTO) {
        reqDTO = baseService.detail(reqDTO.getId());
        reqDTO.setStatus("4");
        baseService.updateStatus(reqDTO);
        return super.success();
    }

    /**
    * 批量删除
    * @param reqDTO
    * @return
    */
    @ApiOperation(value = "批量删除")
    @RequestMapping(value = "/delete", method = { RequestMethod.POST})
    public ApiRest delete(@RequestBody BaseIdsReqDTO reqDTO) {
        //根据ID删除
        baseService.delete(reqDTO.getIds());
        return super.success();
    }

    /**
    * 查找详情
    * @param reqDTO
    * @return
    */
    @ApiOperation(value = "查找详情")
    @RequestMapping(value = "/detail", method = { RequestMethod.POST})
    public ApiRest<TDocumentInfoDTO> detail(@RequestBody BaseIdReqDTO reqDTO) {
        TDocumentInfoDTO dto = baseService.detail(reqDTO.getId());
        return super.success(dto);
    }

    /**
    * 分页查找
    * @param reqDTO
    * @return
    */
    @ApiOperation(value = "分页查找")
    @RequestMapping(value = "/paging", method = { RequestMethod.POST})
    public ApiRest<IPage<TDocumentInfoDTO>> paging(@RequestBody PagingReqDTO<TDocumentInfoDTO> reqDTO) {

        //分页查询并转换
        IPage<TDocumentInfoDTO> page = baseService.paging(reqDTO);

        return super.success(page);
    }

    /**
     * 前端分页查找
     * @param reqDTO
     * @return
     */
    @ApiOperation(value = "前端分页查找")
    @RequestMapping(value = "/webpaging", method = { RequestMethod.POST})
    public ApiRest<IPage<TDocumentInfoDTO>> webpaging(@RequestBody PagingReqDTO<TDocumentInfoDTO> reqDTO) {
        TDocumentInfoDTO dto = reqDTO.getParams();
        if(null == dto){
            dto = new TDocumentInfoDTO();
        }
        if(!StringUtils.isBlank(dto.getSortType())){
            List<OrderItem> lsOrder = new ArrayList<>();
            OrderItem item = new OrderItem();
            item.setAsc(dto.isAsc());
            item.setColumn(" a."+dto.getSortType());
            lsOrder.add(item);
            reqDTO.setOrders(lsOrder);
        }
        dto.setStatus("3");
        //分页查询并转换
        IPage<TDocumentInfoDTO> page = baseService.paging(reqDTO);

        return super.success(page);
    }

    /**
     * 我的资料
     * @param reqDTO
     * @return
     */
    @ApiOperation(value = "分页查找")
    @RequestMapping(value = "/myDocPaging", method = { RequestMethod.POST})
    public ApiRest<IPage<TDocumentInfoDTO>> myDocPaging(@RequestBody PagingReqDTO<TDocumentInfoDTO> reqDTO) {
        TDocumentInfoDTO parm = reqDTO.getParams();
        if(null == parm){
            parm = new TDocumentInfoDTO();
        }
        parm.setCreateBy(UserUtils.getUserId());
        //分页查询并转换
        IPage<TDocumentInfoDTO> page = baseService.paging(reqDTO);

        return super.success(page);
    }



    /**
     * 查找列表，每次最多返回200条数据
     * @param reqDTO
     * @return
     */
    @ApiOperation(value = "查找列表")
    @RequestMapping(value = "/list", method = { RequestMethod.POST})
    public ApiRest<List<TDocumentInfoDTO>> list(@RequestBody TDocumentInfoDTO reqDTO) {
        // 查找列表
        List<TDocumentInfoDTO> dtoList = baseService.list(reqDTO);
        return super.success(dtoList);
    }
}
