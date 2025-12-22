package com.demo.common.utils;

import cn.hutool.core.util.HexUtil;
import cn.hutool.crypto.SmUtil;
import cn.hutool.crypto.asymmetric.SM2;
import cn.hutool.crypto.symmetric.SM4;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.buf.HexUtils;
import org.bouncycastle.jce.interfaces.ECPrivateKey;
import org.bouncycastle.jce.interfaces.ECPublicKey;
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

    private static final String SM2_PRIVATE_KEY = "3ca1ac61987cfaa61115666b499e1875ec3f21f60887ca059953aa6e124a6668";
    private static final String SM2_PUBLIC_KEY = "0484971c0df3aad0f6335942603f5b45abd604a11b97bc4873c67bebb7d88a8ec1fe15d8a5a6618e5608277b42cf6526a94e6263622d57010d55157de1c92c7f6f";

    // SM4对称加密的密钥（需16字节，实际项目中建议从配置文件读取，不要硬编码）
    private static final String SM4_KEY = "1234567890123456";
    // 盐值（增强SM3摘要安全性，实际项目中建议每个用户生成独立盐值）
    private static final String SALT = "springboot_sm_salt";

    /**
     * SM2公钥加密（前端用）
     *
     * @param content 待加密内容（密码）
     * @return 加密后Base64字符串
     */
    public static String sm2Encrypt(String content) {
        SM2 sm2 = new SM2(null, SM2_PUBLIC_KEY);
        byte[] cipherBytes = sm2.encrypt(Cryptos.utf8encode(content));
        return HexUtils.toHexString(cipherBytes);
    }

    /**
     * SM2私钥解密（后端用）
     *
     * @param encryptContent 加密后的Base64字符串
     * @return 解密后的原文
     */
    public static String sm2Decrypt(String encryptContent) {
        SM2 sm2Decrypt = new SM2(SM2_PRIVATE_KEY, null);
        byte[] plainBytes = sm2Decrypt.decrypt(HexUtil.decodeHex(encryptContent));
        return Cryptos.utf8decode(plainBytes);
    }

    /**
     * 对密码进行SM3摘要（加盐）
     *
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
     *
     * @param content 要加密的内容
     * @return 加密后的16进制字符串
     */
    public static String sm4Encrypt(String content) {
        SM4 sm4 = SmUtil.sm4(SM4_KEY.getBytes(StandardCharsets.UTF_8));
        return sm4.encryptHex(content);
    }

    /**
     * SM4对称解密（用于后端解密前端传输的加密密码）
     *
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
     *
     * @param inputPassword     用户输入的密码
     * @param storedSm3Password 数据库中存储的SM3摘要密码
     * @return 是否匹配
     */
    public static boolean verifyPassword(String inputPassword, String storedSm3Password) {
        String inputSm3 = sm3Encrypt(inputPassword);
        return inputSm3.equals(storedSm3Password);
    }

    /**
     * 生成SM2密钥对（PKCS8私钥 + X.509公钥，Base64编码）
     * 兼容所有Hutool 5.x版本
     */
    public static void generateSm2KeyPair() {
        SM2 sm2Root = new SM2();
        ECPublicKey publicKeyRoot = (ECPublicKey) sm2Root.getPublicKey();
        byte[] encoded = publicKeyRoot.getQ().getEncoded(false);
        String publicKeyEncoded = HexUtils.toHexString(encoded);
        System.out.println(publicKeyEncoded);

        ECPrivateKey ecPrivateKey = (ECPrivateKey) sm2Root.getPrivateKey();
        byte[] byteArray = ecPrivateKey.getD().toByteArray();
        String privateEncoded = HexUtils.toHexString(byteArray);
        System.out.println(privateEncoded);

    }

    // 测试主方法
    public static void main(String[] args) {
        // 第一步：运行生成密钥
        generateSm2KeyPair();

        // 第二步：将生成的密钥复制到SM2_PRIVATE_KEY和SM2_PUBLIC_KEY，再测试加解密
        /*
        String plain = "123456";
        String encrypt = sm2Encrypt(plain);
        String decrypt = sm2Decrypt(encrypt);
        System.out.println("原文：" + plain);
        System.out.println("加密后：" + encrypt);
        System.out.println("解密后：" + decrypt);
        */
    }
}