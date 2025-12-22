package com.demo.controller;

import com.demo.common.pojo.dto.login.LoginDTO;
import com.demo.common.pojo.vo.login.LoginCaptchaVO;
import com.demo.service.login.LoginService;
import com.dev.holder.pojo.vo.RespVO;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/login")
public class LoginController {

    @Resource
    private LoginService loginService;

    @GetMapping("captcha")
    public RespVO<LoginCaptchaVO> captcha() {
        return RespVO.success(loginService.generateCaptcha());
    }

    @PostMapping("")
    public RespVO<String> login(@RequestBody @Validated LoginDTO dto) {
        loginService.doLogin(dto);
        return RespVO.success("");
    }

}
