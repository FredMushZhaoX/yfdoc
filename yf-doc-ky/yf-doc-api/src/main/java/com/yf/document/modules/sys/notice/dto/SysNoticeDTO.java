package com.yf.document.modules.sys.notice.dto;

import com.yf.boot.base.api.annon.Dict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.Date;

import java.io.Serializable;

/**
* <p>
* 系统公告数据传输类
* </p>
*
* @author 聪明笨狗
* @since 2020-10-16 12:01
*/
@Data
@ApiModel(value="系统公告", description="系统公告")
public class SysNoticeDTO implements Serializable {

    private static final long serialVersionUID = 1L;


    @ApiModelProperty(value = "ID", required=true)
    private String id;

    @ApiModelProperty(value = "公告标题")
    private String title;

    @ApiModelProperty(value = "公告内容")
    private String content;

    @ApiModelProperty(value = "状态")
    private Integer state;

    @ApiModelProperty(value = "创建时间", required=true)
    private Date createTime;

    @ApiModelProperty(value = "更新时间", required=true)
    private Date updateTime;

    @Dict(dictTable = "sys_user", dicText = "real_name", dicCode = "id")
    @ApiModelProperty(value = "创建人", required=true)
    private String createBy;

    @ApiModelProperty(value = "修改人", required=true)
    private String updateBy;

    @ApiModelProperty(value = "数据标识", required=true)
    private Integer dataFlag;

}
