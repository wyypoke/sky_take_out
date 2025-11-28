package com.sky.repository;

import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.vo.DishVO;

public interface DishRepo {
    PageResult pageQuery(DishPageQueryDTO dto);
    /**
     * 根据分类id查询菜品数量
     * @param categoryId
     * @return
     */
    Integer countByCategoryId(Long categoryId);

    void insert(Dish dish);

    Dish getById(Long id);

    int deleteById(Long id);

    void update(Dish dish);
}
