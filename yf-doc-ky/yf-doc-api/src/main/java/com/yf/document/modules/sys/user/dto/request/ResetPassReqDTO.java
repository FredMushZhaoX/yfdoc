package com.yf.document.modules.sys.user.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
* <p>
* 密码重置请求类
* </p>
*
* @author 聪明笨狗
* @since 2020-04-13 16:57
*/
@Data
@ApiModel(value="密码重置请求类", description="密码重置请求类")
public class ResetPassReqDTO implements Serializable {


    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "手机号码", required=true)
    private String mobile;

    @ApiModelProperty(value = "短信验证码", required=true)
    private String smsCode;

    @ApiModelProperty(value = "新密码", required=true)
    private String newPassword;

}
