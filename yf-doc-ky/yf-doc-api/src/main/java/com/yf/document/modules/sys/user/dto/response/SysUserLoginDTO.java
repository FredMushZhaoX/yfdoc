package com.yf.document.modules.sys.user.dto.response;

import com.yf.boot.base.api.annon.Dict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
* <p>
* 管理用户请求类
* </p>
*
* @author 聪明笨狗
* @since 2020-04-13 16:57
*/
@Data
@ApiModel(value="管理用户登录响应类", description="管理用户登录响应类")
public class SysUserLoginDTO implements Serializable {

    private static final long serialVersionUID = 1L;


    @ApiModelProperty(value = "ID", required=true)
    private String id;

    @ApiModelProperty(value = "用户名", required=true)
    private String userName;

    @ApiModelProperty(value = "头像", required=true)
    private String avatar;

    @ApiModelProperty(value = "真实姓名", required=true)
    private String realName;

    @ApiModelProperty(value = "用户积分", required=true)
    private Integer points;

    @ApiModelProperty(value = "数据权限", required=true)
    private Integer dataScope;

    @Dict(dictTable = "sys_depart", dicCode = "dept_code", dicText = "dept_name")
    @ApiModelProperty(value = "部门编码", required=true)
    private String deptCode;

    @ApiModelProperty(value = "角色类型", required=true)
    private Integer roleType;

    @ApiModelProperty(value = "状态", required=true)
    private Integer state;

    @ApiModelProperty(value = "角色列表", required=true)
    private List<String> roles;

    @ApiModelProperty(value = "权限列表", required=true)
    private List<String> permissions;

    @ApiModelProperty(value = "登录令牌", required=true)
    private String token;
}
