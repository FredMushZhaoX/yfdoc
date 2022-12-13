package com.yf.document.ability.upload.dto;


import com.yf.boot.base.api.api.dto.BaseDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件上传请求类
 * @author 
 * @date 2019-12-26 17:54
 */
@Data
@ApiModel(value="文件上传参数", description="文件上传参数")
public class UploadReqDTO extends BaseDTO {

    @ApiModelProperty(value = "上传文件内容", required=true)
    private MultipartFile file;

}
