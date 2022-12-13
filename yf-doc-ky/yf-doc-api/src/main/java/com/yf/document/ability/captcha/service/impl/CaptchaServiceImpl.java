package com.yf.document.ability.captcha.service.impl;


import com.yf.document.ability.captcha.service.CaptchaService;
import com.yf.document.ability.redis.service.RedisService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 验证码业务类
 * @author bool
 * @date 2020-02-21 10:05
 */
@Service
public class CaptchaServiceImpl implements CaptchaService {

    @Autowired
    private RedisService redisService;

    /**
     * 验证码缓存前缀
     */
    private static final String CAPTCHA_PREFIX = "sys:captcha:";

    @Override
    public void saveCaptcha(String key, String value) {
        redisService.set(appendKey(key), value, 300L);
    }

    @Override
    public boolean checkCaptcha(String key, String input) {

//        // 测试性能用，记得删除
//        if("616".equals(input)){
//            return true;
//        }

        String value = redisService.getString(appendKey(key));
        return StringUtils.isNotBlank(value) && value.equalsIgnoreCase(input);
    }

    /**
     * 组合KEY
     * @param key
     * @return
     */
    private String appendKey(String key){
        return new StringBuffer(CAPTCHA_PREFIX).append(key).toString();
    }
}
