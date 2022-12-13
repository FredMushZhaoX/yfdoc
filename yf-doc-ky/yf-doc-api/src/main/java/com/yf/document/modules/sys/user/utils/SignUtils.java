package com.yf.document.modules.sys.user.utils;

import com.yf.boot.base.api.utils.Md5Util;


/**
 * MD5签名验证工具
 * @author bool
 */
public class SignUtils {

    /**
     * 约定的加密密钥
     */
    public static final String SECRET = "JoQEJeD7grF8JdyVaOCvPDNL8arsyvfL";

    /**
     * 校验token是否正确
     * @param userName
     * @param timestamp
     * @param sign
     * @return
     */
    public static boolean checkToken(String userName, Long timestamp, String sign){

        // 拼字符
        String str = String.format("%s-%s-%s", userName, timestamp, SECRET);

        // 进行第二次MD5
        String md5 = Md5Util.md5(str);

        return md5.equals(sign);
    }

    /**
     * 生成Token
     * @param userName
     * @param timestamp
     * @return
     */
    public static String generateToken(String userName, Long timestamp){

        // 拼接字符串
        String str = String.format("%s-%s-%s", userName, timestamp, SECRET);
        System.out.println(str);

        // 直接MD5
        String sign = Md5Util.md5(str);
        System.out.println(sign);

        return sign;
    }

    public static void main(String [] args) {

        String userName = "zaoliu12";
        String realName = "赵六子";
        String role = "sa";
        // 获得当前timestamp
        Long timestamp = System.currentTimeMillis() / 1000;
        String token = generateToken(userName, timestamp);
        System.out.println(String.format("加密的token为：%s", token));
        System.out.println(String.format("https://t1.jeegen.com/api/sys/user/sync-login?userName=%s" +
                "&realName=%s&timestamp=%s&departs=神马科技&role=%s&sign=%s", userName, realName, timestamp, role, token));

        // 验证
        boolean result = checkToken(userName, timestamp, token);
        System.out.println(String.format("验证结果：%s", result));

    }
}
