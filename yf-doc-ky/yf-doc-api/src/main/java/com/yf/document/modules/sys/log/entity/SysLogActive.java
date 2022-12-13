package com.yf.document.modules.sys.log.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

import java.util.Date;

/**
* <p>
* 用户活跃记录实体类
* </p>
*
* @author 聪明笨狗
* @since 2021-06-04 08:10
*/
@Data
@TableName("sys_log_active")
public class SysLogActive extends Model<SysLogActive> {

    private static final long serialVersionUID = 1L;

    /**
    * ID
    */
    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;

    /**
    * 用户ID
    */
    @TableField("user_id")
    private String userId;

    /**
    * 创建时间
    */
    @TableField("create_time")
    private Date createTime;

}
