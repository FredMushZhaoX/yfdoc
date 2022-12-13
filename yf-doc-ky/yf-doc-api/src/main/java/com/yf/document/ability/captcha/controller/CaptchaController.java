package com.yf.document.ability.captcha.controller;

import com.wf.captcha.SpecCaptcha;
import com.yf.boot.base.api.api.controller.BaseController;
import com.yf.document.ability.captcha.service.CaptchaService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;


/**
 * <p>
 * 图形验证码生成
 * </p>
 *
 * @author 聪明笨狗
 *
 * @since 2019-04-16 10:14
 */
@Api(tags = {"验证码生成类"})
@RestController
@RequestMapping("/api/common/captcha")
public class CaptchaController extends BaseController {

    @Autowired
    private CaptchaService captchaService;


    @RequestMapping(value="/gen", method = RequestMethod.GET)
    @ApiOperation(value = "生成图形验证码")
    public void captcha(HttpServletResponse response, @RequestParam("key") String key) throws Exception {

        // 设置请求头为输出图片类型
        response.setContentType("image/gif");
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);

        // 算术类型
        SpecCaptcha captcha = new SpecCaptcha(130, 48);
        // 几位数运算，默认是两位
        captcha.setLen(4);

        // 存入REDIS
        captchaService.saveCaptcha(key, captcha.text().toLowerCase());

        // 输出图片流
        captcha.out(response.getOutputStream());
    }

}
