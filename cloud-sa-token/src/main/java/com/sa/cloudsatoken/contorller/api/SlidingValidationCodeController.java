package com.sa.cloudsatoken.contorller.api;

import cloud.tianai.captcha.annotation.Captcha;
import cloud.tianai.captcha.request.CaptchaRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 前端滑动验证码
 *
 * @author zhangtingting
 */
@Controller
@Slf4j
@RequestMapping("/sliding/validation")
public class SlidingValidationCodeController {

    @Captcha
    @RequestMapping("/code")
    @ResponseBody
    public String validationCode(CaptchaRequest<String> request) {
        log.info("参数消息:{}",request.getForm());
        return "验证成功";
    }
}
