package com.yf.document.modules.sys.user.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
* <p>
* 手机号码注册请求类
* </p>
*
* @author 聪明笨狗
* @since 2020-04-13 16:57
*/
@Data
@ApiModel(value="手机号码注册请求类", description="手机号码注册请求类")
public class MobileRegReqDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "手机号码", required=true)
    private String mobile;

    @ApiModelProperty(value = "密码", required=true)
    private String password;

    @ApiModelProperty(value = "短信验证码", required=true)
    private String smsCode;

    @ApiModelProperty(value = "姓名", required=true)
    private String realName;

    @ApiModelProperty(value = "部门", required=true)
    private String deptCode;


}
