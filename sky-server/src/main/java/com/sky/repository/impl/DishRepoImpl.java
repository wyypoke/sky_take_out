package com.sky.repository.impl;

import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.repository.DishRepo;
import com.sky.result.PageResult;

public class DishRepoImpl implements DishRepo {
    @Override
    public PageResult pageQuery(DishPageQueryDTO dto) {
        return null;
    }

    @Override
    public Integer countByCategoryId(Long categoryId) {
        return 0;
    }

    @Override
    public void insert(Dish dish) {

    }

    @Override
    public Dish getById(Long id) {
        return null;
    }

    @Override
    public int deleteById(Long id) {
        return 0;
    }

    @Override
    public void update(Dish dish) {

    }
}
