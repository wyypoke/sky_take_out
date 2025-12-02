package com.sky.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.sky.constant.WeChatConstant;
import com.sky.entity.SetmealDish;
import com.sky.entity.User;
import com.sky.json.JacksonObjectMapper;
import com.sky.mapper.UserMapper;
import com.sky.properties.WeChatProperties;
import com.sky.service.UserService;
import com.sky.utils.HttpClientUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;

@Slf4j
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    WeChatProperties weChatProperties;
    @Autowired
    JacksonObjectMapper jacksonObjectMapper;
    @Autowired
    UserMapper userMapper;
    /**
     * @param code
     * @return
     */
    @Override
    public User login(String code) {
        HashMap<String, String> paramMap = new HashMap<>();
        paramMap.put(WeChatConstant.APP_ID, weChatProperties.getAppid());
        // log.info(weChatProperties.getSecret());
        paramMap.put(WeChatConstant.SECRET, weChatProperties.getSecret());
        paramMap.put(WeChatConstant.JS_CODE, code);
        paramMap.put(WeChatConstant.GRANT_TYPE, WeChatConstant.AUTHORIZATION_CODE);
        String json = HttpClientUtil.doGet(WeChatConstant.JS_TO_SESSION_URL, paramMap);
        JSONObject responseJson = JSONObject.parseObject(json);
        String openid = responseJson.getString("openid");

        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getOpenid, openid);
        User user = userMapper.selectOne(wrapper);
        if(user == null) {
            user = User.builder()
                    .openid(openid)
                    .createTime(LocalDateTime.now())
                    .build();
            userMapper.insert(user);
            log.info("新用户：{}", user);
        }
        return user;
    }
}
