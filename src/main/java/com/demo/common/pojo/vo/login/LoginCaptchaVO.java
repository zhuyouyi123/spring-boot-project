package com.demo.common.pojo.vo.login;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class LoginCaptchaVO {

    private String captcha;

    private String uuid;

    private String captchaImage;

}
