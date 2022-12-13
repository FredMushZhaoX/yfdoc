package com.yf.document.modules.news.dto;

import com.yf.boot.base.api.annon.Dict;
import com.yf.boot.base.api.utils.StringUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.Date;

import java.io.Serializable;

/**
* <p>
* 新闻列表数据传输类
* </p>
*
* @author Panjp
* @since 2021-09-22 14:51
*/
@Data
@ApiModel(value="新闻列表", description="新闻列表")
public class TNewsDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    
    
    @ApiModelProperty(value = "ID", required=true)
    private String id;
    
    @ApiModelProperty(value = "创建时间", required=true)
    private Date createTime;
    
    @ApiModelProperty(value = "更新时间", required=true)
    private Date updateTime;
    
    @ApiModelProperty(value = "创建人", required=true)
    private String createBy;
    
    @ApiModelProperty(value = "修改人", required=true)
    private String updateBy;
    
    @ApiModelProperty(value = "标题", required=true)
    private String title;
    
    @ApiModelProperty(value = "缩略图", required=true)
    private String imgUrl;
    @Dict(dicCode = "news_type")
    @ApiModelProperty(value = "新闻类别", required=true)
    private String newsType;
    @Dict(dicCode = "news_status")
    @ApiModelProperty(value = "新闻状态", required=true)
    private String newsStatus;
    
    @ApiModelProperty(value = "新闻内容", required=true)
    private String text;
    
    @ApiModelProperty(value = "点击数", required=true)
    private Long viewCount;
    
    @ApiModelProperty(value = "数据标识", required=true)
    private Integer dataFlag;
    @ApiModelProperty(value = "新闻消息", required=true)
    private String summary;


    public String getSummary(){
        if(null != this.text ){
            String s = StringUtils.delHtmlTags(this.text);
            s = s.replaceAll("&nbsp;"," ");
            if(s.length()>150){
                return s.substring(0,150);
            }else{
                return s;
            }
        }else{
            return "";
        }
    }
    
}
