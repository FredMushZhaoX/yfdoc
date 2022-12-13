package com.yf.document.modules.sys.user.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 用户登录请求类
 * @author bool
 */
@Data
@ApiModel(value="用户登录请求类", description="用户登录请求类")
public class MobileLoginReqDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "手机号码", required=true)
    private String mobile;

    @ApiModelProperty(value = "短信验证码", required=true)
    private String smsCode;

}
