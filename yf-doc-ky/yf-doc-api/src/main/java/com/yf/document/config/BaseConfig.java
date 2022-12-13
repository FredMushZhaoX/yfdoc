package com.yf.document.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;


/**
 * 全局静态配置
 * @author bool
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "ycloud")
public class BaseConfig {

    /**
     * 踢出登录
     */
    private Boolean loginTick;

    /**
     * 微信登录回调地址
     */
    private String loginRedirect;

    /**
     * PC端登录同步地址
     */
    private String loginSyncPc;

    /**
     * 手机端登录同步地址
     */
    private String loginSyncH5;



}
