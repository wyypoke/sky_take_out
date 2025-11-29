package com.sky.repository;


import com.sky.entity.DishFlavor;

import java.util.List;

public interface DishFlavorRepo {
    void saveWithDishId(List<DishFlavor> dishFlavorList, Long dishId);
    void deleteByDishId(Long dishId);
    List<DishFlavor> selectByDishId(Long id);
}
