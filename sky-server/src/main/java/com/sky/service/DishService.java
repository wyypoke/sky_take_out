package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;

import java.util.List;

public interface DishService {
    public Result saveWithFlavor(DishDTO dishDTO);

    public PageResult page(DishPageQueryDTO dto);

    public void delete(Long id);

    public void deleteBatch(List<Long> idList);
}
