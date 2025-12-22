package com.demo.common.cache;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * 验证码缓存工具类（基于 Caffeine）
 * 适用于图形验证码、短信验证码等场景
 */
@Component
public class VerificationCodeCache {

    private final Cache<String, String> cache;

    public VerificationCodeCache() {
        // 默认验证码有效期：5 分钟（300 秒）
        this.cache = Caffeine.newBuilder()
                // 最多缓存 1 万个验证码
                .maximumSize(100)
                // 写入后 5 分钟过期
                .expireAfterWrite(300, TimeUnit.SECONDS)
                .build();
    }

    /**
     * 存储验证码
     *
     * @param key  唯一标识（如手机号、邮箱、UUID）
     * @param code 验证码（如 "123456"）
     */
    public void put(String key, String code) {
        if (key == null || code == null) {
            throw new IllegalArgumentException("Key or code cannot be null");
        }
        cache.put(key, code);
    }

    /**
     * 获取验证码（不删除）
     *
     * @param key 唯一标识
     * @return 验证码，若不存在或已过期则返回 null
     */
    public String get(String key) {
        return cache.getIfPresent(key);
    }

    /**
     * 验证验证码是否正确（验证成功后自动删除，防止重放）
     *
     * @param key  唯一标识
     * @param code 用户输入的验证码
     * @return true 表示验证成功
     */
    public boolean validate(String key, String code) {
        if (key == null || code == null) {
            return false;
        }
        String cachedCode = cache.getIfPresent(key);
        if (cachedCode != null && cachedCode.equalsIgnoreCase(code)) {
            // 验证成功，立即删除（一次性使用）
            cache.invalidate(key);
            return true;
        }
        return false;
    }

    /**
     * 手动删除验证码（例如用户重新获取时清除旧验证码）
     *
     * @param key 唯一标识
     */
    public void remove(String key) {
        cache.invalidate(key);
    }

    /**
     * 清空所有验证码（一般用于测试）
     */
    public void clear() {
        cache.invalidateAll();
    }
}