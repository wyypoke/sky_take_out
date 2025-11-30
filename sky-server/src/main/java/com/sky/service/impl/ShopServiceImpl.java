package com.sky.service.impl;

import com.sky.service.ShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class ShopServiceImpl implements ShopService {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    static String key = "SHOP_STATUS";
    /**
     * @param status
     */
    @Override
    public void startOrStop(Integer status) {

        redisTemplate.opsForValue().set(key, status);
    }
}
