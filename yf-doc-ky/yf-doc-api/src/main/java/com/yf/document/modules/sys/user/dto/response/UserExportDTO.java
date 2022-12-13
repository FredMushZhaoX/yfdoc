package com.yf.document.modules.sys.user.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yf.boot.base.api.utils.excel.annotation.ExcelField;
import lombok.Data;

/**
 * 用于导入导出的用户结构
 * @author bool
 */
@Data
public class UserExportDTO {

    private static final long serialVersionUID = 1L;

    /**
     * 题目ID
     */
    @JsonIgnore
    private String id;

    @ExcelField(title="账号", align=2, sort=1)
    private String userName;

    @ExcelField(title="姓名", align=2, sort=2)
    private String realName;

    @ExcelField(title="部门", align=2, sort=3)
    private String deptCode;

    @ExcelField(title="手机", align=2, sort=4)
    private String mobile;

    @ExcelField(title="邮箱", align=2, sort=5)
    private String email;

    @ExcelField(title="身份证号", align=2, sort=6)
    private String idCard;

    @ExcelField(title="积分", align=2, sort=7)
    private Integer points;

    @ExcelField(title="角色", align=2, sort=8)
    private String roleIds;

    @ExcelField(title="密码", align=2, sort=9)
    private String password;
}
