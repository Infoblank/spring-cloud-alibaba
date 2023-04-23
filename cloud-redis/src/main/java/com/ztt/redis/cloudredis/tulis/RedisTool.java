package com.ztt.redis.cloudredis.tulis;

import jakarta.validation.constraints.NotNull;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Map;
import java.util.Set;

/**
 * redis工具类,分别对对象,集合进行存取
 */
public class RedisTool {

    /**
     * ApplicationContextUtils.setApplicationContext 当中初始化redisTemplate
     */
    public static @NotNull RedisTemplate<String, Object> redisTemplate;


    /**
     * 添加key-value
     *
     * @param key   键
     * @param value 值
     */
    public static void setKey(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    public static void setKey(String key, Object value,long off) {
        redisTemplate.opsForValue().set(key, value,off);
    }

    public static void deleteKey(String key) {
         redisTemplate.delete(key);
    }

    /**
     * 获取指定 key 的值
     *
     * @param key key
     */
    public static Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public static void setHashKey(String key, Map<String, ?> value) {
        redisTemplate.opsForHash().putAll(key, value);
    }

    public static Object getHashValue(String key) {
        return redisTemplate.opsForHash().entries(key);
    }


    public static Long setKeyBySet(String key, Set<String> values) {
        return redisTemplate.opsForSet().add(key, values);
    }

    public static Set<Object> getValuesBySetKey(String key) {
        return redisTemplate.opsForSet().members(key);
    }
}
