package com.sky.config;

import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DebugConfig {

    @Autowired
    private RedisProperties redisProperties;

    public void printRedisPassword() {
        System.out.println("DEBUG: 实际加载的 Redis 密码是: [" + redisProperties.getPassword() + "]");
    }
}