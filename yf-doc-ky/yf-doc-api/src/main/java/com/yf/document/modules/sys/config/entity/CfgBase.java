package com.yf.document.modules.sys.config.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

import java.util.Date;

/**
* <p>
* 通用配置实体类
* </p>
*
* @author 聪明笨狗
* @since 2020-04-17 09:12
*/
@Data
@TableName("el_cfg_base")
public class CfgBase extends Model<CfgBase> {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 系统名称
     */
    @TableField("site_name")
    private String siteName;

    /**
     * 前端LOGO
     */
    @TableField("front_logo")
    private String frontLogo;

    /**
     * 后台LOGO
     */
    @TableField("back_logo")
    private String backLogo;

    /**
     * 版权信息
     */
    @TableField("copy_right")
    private String copyRight;

    /**
     * 系统临时目录
     */
    @TableField("tmp_dir")
    private String tmpDir;

    /**
     * 存储方案1本地2阿里云OSS
     */
    @TableField("upload_type")
    private Integer uploadType;

    /**
     * 1阿里云
     */
    @TableField("live_type")
    private Integer liveType;

    /**
     * 手机端二维码
     */
    @TableField("h5_code")
    private String h5Code;

    /**
     * 小程序二维码
     */
    @TableField("mp_code")
    private String mpCode;

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
