package com.yf.document.ability.upload.service.impl;

import com.yf.boot.base.api.exception.ServiceException;
import com.yf.document.ability.Constant;
import com.yf.document.ability.upload.dto.UploadReqDTO;
import com.yf.document.ability.upload.dto.UploadRespDTO;
import com.yf.document.ability.upload.service.UploadService;
import com.yf.document.ability.upload.utils.OssUtils;
import com.yf.document.modules.sys.config.dto.CfgUploadLocalDTO;
import com.yf.document.modules.sys.config.service.CfgUploadLocalService;
import com.yf.document.utils.LocalFileUtils;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 文件上传业务类
 * @author bool
 * @date 2019-07-30 21:02
 */
@Log4j2
@Service
public class UploadServiceImpl implements UploadService {


    @Autowired
    private CfgUploadLocalService cfgUploadLocalService;


    @Override
    public UploadRespDTO upload(UploadReqDTO reqDTO) {

        // 查找上传配置
        CfgUploadLocalDTO conf = cfgUploadLocalService.find();

        // 文件内容
        MultipartFile file = reqDTO.getFile();

        // 上传文件夹
        String fileDir = conf.getLocalDir();

        // 真实物理地址
        String fullPath;

        try {
            // 新文件
            String filePath = OssUtils.processPath(file);
            // 文件保存地址
            fullPath = fileDir + filePath;
            // 创建文件夹
            OssUtils.checkDir(fullPath);
            // 上传文件
            FileCopyUtils.copy(file.getInputStream(), new FileOutputStream(fullPath));

            return this.generateResult(conf, filePath);

        } catch (IOException e) {
            e.printStackTrace();
            throw new ServiceException("文件上传失败："+e.getMessage());
        }
    }



    @Override
    public void download(HttpServletRequest request, HttpServletResponse response) {


        // 查找上传配置
        CfgUploadLocalDTO conf = cfgUploadLocalService.find();

        // 获取真实的文件路径
        String filePath = this.getRealPath(conf, request.getRequestURI());

        try {

            LocalFileUtils.writeRange(request, response, filePath);
        } catch (IOException e) {
            response.setStatus(404);
            log.error("预览文件失败" + e.getMessage());
        }
    }


    /**
     * 构造返回
     * @param fileName
     * @return
     */
    private UploadRespDTO generateResult(CfgUploadLocalDTO conf, String fileName) {

        //获取加速域名
        String domain = conf.getUrl();

        // 返回结果
        return new UploadRespDTO(domain + fileName);
    }


    /**
     * 获取真实物理文件地址
     * @param uri
     * @return
     */
    public String getRealPath(CfgUploadLocalDTO conf, String uri){

        String regx = Constant.FILE_PREFIX+"(.*)";

        // 查找全部变量
        Pattern pattern = Pattern.compile(regx);
        Matcher m = pattern.matcher(uri);
        if (m.find()) {
            String str = m.group(1);
            return conf.getLocalDir() + str;
        }

        return null;
    }

}
