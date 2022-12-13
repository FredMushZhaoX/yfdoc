package com.yf.document.modules.sys.config.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

/**
* <p>
* 本地文件上传配置实体类
* </p>
*
* @author 聪明笨狗
* @since 2021-02-05 11:16
*/
@Data
@TableName("el_cfg_upload_local")
public class CfgUploadLocal extends Model<CfgUploadLocal> {

    private static final long serialVersionUID = 1L;

    /**
    * ID
    */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    /**
    * 本地目录地址
    */
    @TableField("local_dir")
    private String localDir;

    /**
    * 访问路径
    */
    private String url;

}
