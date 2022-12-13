package com.yf.document.modules.news.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import java.util.Date;

/**
* <p>
* 新闻列表实体类
* </p>
*
* @author Panjp
* @since 2021-09-22 14:51
*/
@Data
@TableName("t_news")
public class TNews extends Model<TNews> {

    private static final long serialVersionUID = 1L;
    
    /**
    * ID
    */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;
    
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
    * 标题
    */
    private String title;
    
    /**
    * 缩略图
    */
    @TableField("img_url")
    private String imgUrl;
    
    /**
    * 新闻类别
    */
    @TableField("news_type")
    private String newsType;
    
    /**
    * 新闻状态
    */
    @TableField("news_status")
    private String newsStatus;
    
    /**
    * 新闻内容
    */
    private String text;
    
    /**
    * 点击数
    */
    @TableField("view_count")
    private Long viewCount;
    
    /**
    * 数据标识
    */
    @TableField("data_flag")
    private Integer dataFlag;
    
}
