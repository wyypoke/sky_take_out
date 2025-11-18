package com.sky.service.impl;

import com.fasterxml.jackson.databind.annotation.JsonAppend;
import com.sky.dto.DishDTO;
import com.sky.entity.Dish;
import com.sky.mapper.DishMapper;
import com.sky.result.Result;
import com.sky.service.DishService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service
public class DishServiceImpl implements DishService {
    @Autowired
    private DishMapper dishMapper;
    @Override
    @Transactional
    public Result saveWithFlavor(DishDTO dishDTO) {
        Dish dish = Dish.builder().build();
        BeanUtils.copyProperties(dishDTO, dish);
        dishMapper.insert(dish);
        return null;
    }
}
