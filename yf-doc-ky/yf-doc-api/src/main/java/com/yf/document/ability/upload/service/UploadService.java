package com.yf.document.ability.upload.service;

import com.yf.document.ability.upload.dto.UploadReqDTO;
import com.yf.document.ability.upload.dto.UploadRespDTO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 阿里云OSS业务类
 * @author bool 
 * @date 2019-07-12 16:45
 */
public interface UploadService {

    /**
     * 文件上传
     * @param reqDTO
     * @return
     */
    UploadRespDTO upload(UploadReqDTO reqDTO);

    /**
     * 下载文件
     * @param request
     * @param response
     */
    void download(HttpServletRequest request, HttpServletResponse response);

}
