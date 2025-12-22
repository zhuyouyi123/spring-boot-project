package com.demo.service.impl;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import cn.hutool.core.codec.Base64;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.demo.common.cache.VerificationCodeCache;
import com.demo.common.enums.errors.LoginError;
import com.demo.common.pojo.dataobject.ManagerDO;
import com.demo.common.pojo.dto.login.LoginDTO;
import com.demo.common.pojo.vo.login.LoginCaptchaVO;
import com.demo.common.utils.SmCryptoUtil;
import com.demo.mapper.ManagerMapper;
import com.demo.service.LoginService;
import jakarta.annotation.Resource;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.Objects;
import java.util.UUID;

@Service
public class LoginServiceImpl implements LoginService {

    @Resource
    private ManagerMapper managerMapper;

    @Resource
    private VerificationCodeCache verificationCodeCache;

    @Override
    public void doLogin(LoginDTO dto) {
         checkCode(dto.getCode(), dto.getUuid());
        ManagerDO managerDO = managerMapper.selectOne(new LambdaQueryWrapper<ManagerDO>()
                .eq(ManagerDO::getUsername, dto.getUsername()));

        if (managerDO == null) {
            throw LoginError.USER_NOT_EXIST.build();
        }

        if (!Objects.equals(SmCryptoUtil.sm2Decrypt(managerDO.getPassword()), SmCryptoUtil.sm2Decrypt(dto.getPassword()))) {
            throw LoginError.LOGIN_FAILED.build();
        }

        generateToken();
    }

    /**
     * 生成验证码
     */
    @Override
    public LoginCaptchaVO generateCaptcha() {
        // 定义宽、高、验证码长度、干扰线数量
        LineCaptcha captcha = CaptchaUtil.createLineCaptcha(200, 100, 4, 20);
        // 2. 将验证码图片写入字节数组
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        captcha.write(out); // 写入 PNG 数据

        // 3. 转为 Base64 字符串
        String base64 = Base64.encode(out.toByteArray());

        String uuid = UUID.randomUUID().toString();

        verificationCodeCache.put(uuid, captcha.getCode());

        return LoginCaptchaVO.builder()
                .uuid(uuid)
                .captcha(captcha.getCode())
                .captchaImage((base64))
                .build();
    }

    /**
     * 生成token
     */
    private void generateToken() {
    }


    private void checkCode(String code, String uuid) {
        if (StringUtils.isBlank(code)) {
            throw LoginError.CAPTCHA_ERROR.build();
        }
        if (!verificationCodeCache.validate(uuid, code)) {
            throw LoginError.CAPTCHA_ERROR.build();
        }
    }
}
