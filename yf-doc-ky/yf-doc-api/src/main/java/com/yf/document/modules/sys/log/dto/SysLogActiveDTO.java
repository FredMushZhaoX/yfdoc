package com.yf.document.modules.sys.log.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
* <p>
* 用户活跃记录数据传输类
* </p>
*
* @author 聪明笨狗
* @since 2021-06-04 08:10
*/
@Data
@ApiModel(value="用户活跃记录", description="用户活跃记录")
public class SysLogActiveDTO implements Serializable {

    private static final long serialVersionUID = 1L;


    @ApiModelProperty(value = "ID", required=true)
    private String id;

    @ApiModelProperty(value = "用户ID")
    private String userId;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

}
