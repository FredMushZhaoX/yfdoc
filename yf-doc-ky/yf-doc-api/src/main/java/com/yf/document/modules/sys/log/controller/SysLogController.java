package com.yf.document.modules.sys.log.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yf.boot.base.api.api.ApiRest;
import com.yf.boot.base.api.api.controller.BaseController;
import com.yf.boot.base.api.api.dto.PagingReqDTO;
import com.yf.document.modules.sys.log.dto.SysLogDTO;
import com.yf.document.modules.sys.log.service.SysLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
* <p>
* 系统日志控制器
* </p>
*
* @author 聪明笨狗
* @since 2020-04-28 10:23
*/
@Api(tags={"系统日志"})
@RestController
@RequestMapping("/api/sys/log")
public class SysLogController extends BaseController {

    @Autowired
    private SysLogService baseService;


    /**
    * 分页查找
    * @param reqDTO
    * @return
    */
    @ApiOperation(value = "分页查找")
    @RequestMapping(value = "/paging", method = { RequestMethod.POST})
    public ApiRest<IPage<SysLogDTO>> paging(@RequestBody PagingReqDTO<SysLogDTO> reqDTO) {

        //分页查询并转换
        IPage<SysLogDTO> page = baseService.paging(reqDTO);

        return super.success(page);
    }

}
