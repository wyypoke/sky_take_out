package com.sky.service;

import com.sky.dto.SetmealDTO;
import com.sky.result.PageResult;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.vo.SetmealVO;

public interface SetmealService {
    /**
     * 新增套餐，同时需要保存套餐和菜品的关联关系
     * @param setmealDTO
     */
    void saveWithDish(SetmealDTO setmealDTO);

    PageResult pageQuery(SetmealPageQueryDTO setmealPageQueryDTO);

    SetmealVO getByIdWithDish(Long id);

    void update(SetmealDTO setmealDTO);

    void startOrStop(Integer status, Long id);
}
