package com.shiro.cloudshirosso.utils;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.Set;

/**
 * redis工具类,分别对对象,集合进行存取
 */
@Component
public class RedisTool {

    private static RedisTemplate<String, Object> redisTemplate;

    @PostConstruct
    public void init() {
        redisTemplate = (RedisTemplate<String, Object>) ApplicationContextUtils.getBean("customRedisTemplate");
    }


    /**
     * 添加key-value
     *
     * @param key   键
     * @param value 值
     */
    public static void setKey(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
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
