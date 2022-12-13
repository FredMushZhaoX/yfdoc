package com.yf.document.modules.doc.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.Date;

/**
* <p>
* 文档信息表实体类
* </p>
*
* @author Panjp
* @since 2021-08-23 20:30
*/
@Data
@TableName("t_document_info")
public class TDocumentInfo extends Model<TDocumentInfo> {

    private static final long serialVersionUID = 1L;
    
    /**
    * ID
    */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;
    
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
    * 文档标题
    */
    private String title;
    
    /**
    * 附件类型
    */
    @TableField("file_type")
    private String fileType;
    
    /**
    * 文档类别
    */
    @TableField("doc_type")
    private String docType;
    
    /**
    * 文件状态：1草稿，2：待审核，3：审核通过，4：审核不通过
    */

    private String status;
    
    /**
    * 附件路径
    */
    @TableField("file_url")
    private String fileUrl;
    
    /**
    * 预览路径
    */
    @TableField("view_url")
    private String viewUrl;
    
    /**
    * 审核不通过原因
    */
    @TableField("failure_reason")
    private String failureReason;
    
    /**
    * 下载权限
    */
    @TableField("download_power")
    private String downloadPower;
    
    /**
    * 查看权限
    */
    @TableField("view_power")
    private String viewPower;
    
    /**
    * 查看次数
    */
    @TableField("view_count")
    private Long viewCount;
    
    /**
    * 下载次数
    */
    @TableField("download_count")
    private Long downloadCount;
    
    /**
    * 备注
    */
    private String remarks;

    /**
     * 关键字
     */
    @TableField("key_word")
    private String keyWord;

    /**
     * 资料来源
     */
    @TableField("doc_from")
    private String docFrom;
    /**
     * 缩略图
     */
    private String cover;
}
