package com.sky.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule; // 核心修正依赖
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

@Configuration
@EnableCaching
public class RedisConfig {

    /**
     * 【新】创建一个支持 LocalDateTime 的 JSON 序列化器
     * 这是解决 Type id handling 错误的**核心**
     * @return GenericJackson2JsonRedisSerializer
     */
    private GenericJackson2JsonRedisSerializer createJacksonSerializer() {
        ObjectMapper objectMapper = new ObjectMapper();

        // 🚀 关键步骤：注册 Java 8 时间模块
        objectMapper.registerModule(new JavaTimeModule());

        return new GenericJackson2JsonRedisSerializer(objectMapper);
    }

    //---------------------------------------------------------

    /**
     * 自定义 Redis 缓存管理器配置
     * @return RedisCacheConfiguration
     */
    @Bean
    public RedisCacheConfiguration redisCacheConfiguration() {
        // 1. 设置 Key 和 Value 的序列化器
        StringRedisSerializer keySerializer = new StringRedisSerializer();

        // 🔴 修正点 1: 使用增强后的序列化器
        GenericJackson2JsonRedisSerializer valueSerializer = createJacksonSerializer();

        return RedisCacheConfiguration.defaultCacheConfig()
                // 2. 配置 Key 的序列化方式
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(keySerializer))
                // 3. 配置 Value 的序列化方式
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(valueSerializer));
    }

    /**
     * 自定义 RedisTemplate 配置
     * @param connectionFactory
     * @return RedisTemplate<String, Object>
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        // 设置 Key 的序列化器
        StringRedisSerializer stringSerializer = new StringRedisSerializer();
        template.setKeySerializer(stringSerializer);
        template.setHashKeySerializer(stringSerializer);

        // 🔴 修正点 2: 使用增强后的序列化器
        GenericJackson2JsonRedisSerializer jsonSerializer = createJacksonSerializer();

        template.setValueSerializer(jsonSerializer);
        template.setHashValueSerializer(jsonSerializer);

        template.afterPropertiesSet();
        return template;
    }
}