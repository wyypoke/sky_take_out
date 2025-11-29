package com.sky.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sky.entity.SetmealDish;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
@Mapper
public interface SetmealDishMapper extends BaseMapper<SetmealDish> {
    @Select("select count(id) from setmeal_dish where dish_id = #{dishId}")
    Integer countByDishId(Long dishId);
}
