package com.yf.document.modules.sys.user.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

import java.util.Date;

/**
* <p>
* 角色实体类
* </p>
*
* @author 聪明笨狗
* @since 2020-04-13 16:57
*/
@Data
@TableName("sys_role")
public class SysRole extends Model<SysRole> {

    private static final long serialVersionUID = 1L;

    /**
    * 角色ID
    */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 角色名称
     */
    @TableField("role_name")
    private String roleName;

    /**
     * 1学员2管理员
     */
    @TableField("role_type")
    private Integer roleType;

    /**
     * 数据权限
     */
    @TableField("data_scope")
    private Integer dataScope;

    /**
     * 备注信息
     */
    private String remark;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField("update_time")
    private Date updateTime;

    /**
     * 创建人
     */
    @TableField("create_by")
    private String createBy;

    /**
     * 修改人
     */
    @TableField("update_by")
    private String updateBy;

    /**
     * 数据标识
     */
    @TableField("data_flag")
    private Integer dataFlag;

}
