package com.yf.document.modules.sys.config.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
* <p>
* 通用配置请求类
* </p>
*
* @author 聪明笨狗
* @since 2020-04-17 09:12
*/
@Data
@ApiModel(value="通用配置", description="通用配置")
public class CfgBaseDTO implements Serializable {

    private static final long serialVersionUID = 1L;


    @ApiModelProperty(value = "ID", required=true)
    private String id;

    @ApiModelProperty(value = "系统名称")
    private String siteName;

    @ApiModelProperty(value = "前端LOGO")
    private String frontLogo;

    @ApiModelProperty(value = "后台LOGO")
    private String backLogo;

    @ApiModelProperty(value = "版权信息")
    private String copyRight;

    @ApiModelProperty(value = "系统临时目录")
    private String tmpDir;

    @ApiModelProperty(value = "存储方案1本地2阿里云OSS", required=true)
    private Integer uploadType;

    @ApiModelProperty(value = "1阿里云", required=true)
    private Integer liveType;

    @ApiModelProperty(value = "手机端二维码")
    private String h5Code;

    @ApiModelProperty(value = "小程序二维码")
    private String mpCode;

    @ApiModelProperty(value = "创建时间", required=true)
    private Date createTime;

    @ApiModelProperty(value = "更新时间", required=true)
    private Date updateTime;

    @ApiModelProperty(value = "创建人", required=true)
    private String createBy;

    @ApiModelProperty(value = "修改人", required=true)
    private String updateBy;

    @ApiModelProperty(value = "数据标识", required=true)
    private Integer dataFlag;
    
}
