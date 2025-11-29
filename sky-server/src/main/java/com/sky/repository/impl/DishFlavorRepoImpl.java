package com.sky.repository.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.sky.entity.DishFlavor;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.MyBatisPlusDishFlavorMapper;
import com.sky.repository.DishFlavorRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public class DishFlavorRepoImpl implements DishFlavorRepo {
    @Autowired
    private MyBatisPlusDishFlavorMapper dishFlavorMapper;

    @Override
    public void saveWithDishId(List<DishFlavor> dishFlavorList, Long dishId) {
        dishFlavorList.forEach(dishFlavor -> {
            dishFlavor.setDishId(dishId);
            dishFlavorMapper.insert(dishFlavor);
        });
    }

    @Override
    public void deleteByDishId(Long dishId) {
        LambdaQueryWrapper<DishFlavor> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(DishFlavor::getDishId, dishId);
        dishFlavorMapper.delete(wrapper);
    }

    @Override
    public List<DishFlavor> selectByDishId(Long id) {
        LambdaQueryWrapper<DishFlavor> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(DishFlavor::getDishId, id);
        return dishFlavorMapper.selectList(wrapper);
    }
}
