package com.demo.common.utils;

import cn.hutool.crypto.SmUtil;
import cn.hutool.crypto.symmetric.SM4;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.nio.charset.StandardCharsets;
import java.security.Security;
import java.util.Arrays;

/**
 * 国密算法工具类（基于Hutool封装）
 */
@Slf4j
public class SmCryptoUtil {
    // 静态代码块注册BouncyCastle提供者
    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    // SM4对称加密的密钥（需16字节，实际项目中建议从配置文件读取，不要硬编码）
    private static final String SM4_KEY = "1234567890123456";
    // 盐值（增强SM3摘要安全性，实际项目中建议每个用户生成独立盐值）
    private static final String SALT = "springboot_sm_salt";

    /**
     * 对密码进行SM3摘要（加盐）
     * @param password 原始密码
     * @return SM3摘要后的16进制字符串
     */
    public static String sm3Encrypt(String password) {
        // 密码 + 盐值后进行SM3摘要
        String passwordWithSalt = password + SALT;
        return SmUtil.sm3(Arrays.toString(passwordWithSalt.getBytes(StandardCharsets.UTF_8)));
    }

    /**
     * SM4对称加密（用于前端传输密码时加密）
     * @param content 要加密的内容
     * @return 加密后的16进制字符串
     */
    public static String sm4Encrypt(String content) {
        SM4 sm4 = SmUtil.sm4(SM4_KEY.getBytes(StandardCharsets.UTF_8));
        return sm4.encryptHex(content);
    }

    /**
     * SM4对称解密（用于后端解密前端传输的加密密码）
     * @param encryptStr 加密后的16进制字符串
     * @return 解密后的原始内容
     */
    public static String sm4Decrypt(String encryptStr) {
        try {
            SM4 sm4 = SmUtil.sm4(SM4_KEY.getBytes(StandardCharsets.UTF_8));
            return sm4.decryptStr(encryptStr, StandardCharsets.UTF_8);
        } catch (Exception e) {
            log.error("SM4解密失败", e);
            return null;
        }
    }

    /**
     * 验证密码是否匹配
     * @param inputPassword 用户输入的密码
     * @param storedSm3Password 数据库中存储的SM3摘要密码
     * @return 是否匹配
     */
    public static boolean verifyPassword(String inputPassword, String storedSm3Password) {
        String inputSm3 = sm3Encrypt(inputPassword);
        return inputSm3.equals(storedSm3Password);
    }
}