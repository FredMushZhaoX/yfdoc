package com.yf.document.modules.sys.user.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yf.boot.base.api.annon.Dict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
* <p>
* 管理用户请求类
* </p>
*
* @author 聪明笨狗
* @since 2020-04-13 16:57
*/
@Data
@ApiModel(value="管理用户", description="管理用户")
public class SysUserDTO implements Serializable {

    private static final long serialVersionUID = 1L;


    @ApiModelProperty(value = "ID", required=true)
    private String id;

    @ApiModelProperty(value = "用户名", required=true)
    private String userName;

    @ApiModelProperty(value = "头像", required=true)
    private String avatar;

    @ApiModelProperty(value = "真实姓名", required=true)
    private String realName;

    @ApiModelProperty(value = "密码", required=true)
    private String password;

    @ApiModelProperty(value = "密码盐", required=true)
    private String salt;

    @Dict(dictTable = "sys_depart", dicCode = "dept_code", dicText = "dept_name")
    @ApiModelProperty(value = "部门编码", required=true)
    private String deptCode;

    @ApiModelProperty(value = "状态", required=true)
    private Integer state;

    @ApiModelProperty(value = "积分", required=true)
    private Integer points;

    @ApiModelProperty(value = "身份证号码")
    private String idCard;

    @ApiModelProperty(value = "手机号")
    private String mobile;

    @ApiModelProperty(value = "邮箱")
    private String email;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间", required=true)
    private Date createTime;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新时间", required=true)
    private Date updateTime;

    @ApiModelProperty(value = "创建人", required=true)
    private String createBy;

    @ApiModelProperty(value = "修改人", required=true)
    private String updateBy;

    @ApiModelProperty(value = "数据标识", required=true)
    private Integer dataFlag;
}
