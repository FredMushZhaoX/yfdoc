package com.yf.document.modules.sys.user.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author bool
 */
@Data
@ApiModel(value="用户部门批量操作请求类", description="用户部门批量操作请求类")
public class UserDeptReqDTO implements Serializable {

    @ApiModelProperty(value = "用户列表", required=true)
    private List<String> userIds;

    @ApiModelProperty(value = "部门编码", required=true)
    private String deptCode;
}
