package com.demo.service;

import com.demo.common.pojo.dto.login.LoginDTO;
import com.demo.common.pojo.vo.login.LoginCaptchaVO;

public interface LoginService {

    void doLogin(LoginDTO dto);

    /**
     * 生成验证码
     * @return 验证码
     */
    LoginCaptchaVO generateCaptcha();
}
