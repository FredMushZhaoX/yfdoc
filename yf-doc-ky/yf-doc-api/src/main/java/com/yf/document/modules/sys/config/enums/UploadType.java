package com.yf.document.modules.sys.config.enums;

/**
 * 文件存储方案
 * @author bool
 */
public interface UploadType {

    /**
     * 本地存储
     */
    Integer LOCAL = 1;

    /**
     * 阿里云OSS
     */
    Integer ALIYUN_OSS = 2;

    /**
     * 七牛云
     */
    Integer QI_NIU = 3;
}
