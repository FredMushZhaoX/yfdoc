package com.yf.document.modules.sys.log.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
* <p>
* 学习记录数据传输类
* </p>
*
* @author 聪明笨狗
* @since 2021-06-04 09:53
*/
@Data
@ApiModel(value="学习记录", description="学习记录")
public class SysLogLearnDTO implements Serializable {

    private static final long serialVersionUID = 1L;


    @ApiModelProperty(value = "ID", required=true)
    private String id;

    @ApiModelProperty(value = "课程ID")
    private String courseId;

    @ApiModelProperty(value = "课件ID")
    private String fileId;

    @ApiModelProperty(value = "学习分钟")
    private Integer learnMin;

    @ApiModelProperty(value = "用户ID")
    private String userId;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

}
