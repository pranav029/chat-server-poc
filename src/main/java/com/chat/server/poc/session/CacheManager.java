package com.chat.server.poc.session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class CacheManager {
    private final RedisTemplate<String, String> redisTemplate;

    @Autowired
    public CacheManager(@Qualifier("pocRedisTemplate") RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public String getValue(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public void setValue(String key, String value) {
        redisTemplate.opsForValue().set(key, value);
    }

    public void removeKey(String key) {
        redisTemplate.delete(key);
    }

    public boolean keyExists(String userId) {
        return redisTemplate.hasKey(userId);
    }
}
