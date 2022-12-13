package com.yf.document.modules.sys.user.dto;

import com.yf.boot.base.api.annon.Dict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
* <p>
* 角色请求类
* </p>
*
* @author 聪明笨狗
* @since 2020-04-13 16:57
*/
@Data
@ApiModel(value="角色", description="角色")
public class SysRoleDTO implements Serializable {

    private static final long serialVersionUID = 1L;


    @ApiModelProperty(value = "角色ID", required=true)
    private String id;

    @ApiModelProperty(value = "角色名称", required=true)
    private String roleName;

    @Dict(dicCode = "role_type")
    @ApiModelProperty(value = "1学员2管理员", required=true)
    private Integer roleType;

    @Dict(dicCode = "data_scope")
    @ApiModelProperty(value = "数据权限", required=true)
    private Integer dataScope;

    @ApiModelProperty(value = "备注信息")
    private String remark;

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
