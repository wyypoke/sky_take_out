package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.StatusConstant;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
@Slf4j
@Service
public class DishServiceImpl implements DishService {
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private DishFlavorMapper dishFlavorMapper;
    @Autowired
    private SetmealDishMapper setmealDishMapper;
    @Override
    @Transactional
    public Result saveWithFlavor(DishDTO dishDTO) {
        Dish dish = Dish.builder().build();
        BeanUtils.copyProperties(dishDTO, dish);
        dishMapper.insert(dish);
        List<DishFlavor> dishFlavorList = dishDTO.getFlavors();
        if(dishFlavorList != null && !dishFlavorList.isEmpty()) {
            dishFlavorList.forEach(dishFlavor -> dishFlavor.setDishId(dish.getId()));
            dishFlavorMapper.insertBatch(dishFlavorList);
        }
        dishDTO.setId(dish.getId());
        return Result.success();
    }

    @Override
    public PageResult page(DishPageQueryDTO dto) {
        PageHelper.startPage(dto.getPage(), dto.getPageSize());
        Page<DishVO> page = dishMapper.pageQuery(dto);
        long total = page.getTotal();
        List<DishVO> records = page.getResult();
        return new PageResult(total, records);
    }


    public boolean canDelete(Long id) {
        Dish dish = dishMapper.getById(id);
        log.info("判断菜品{}是否可以删除", dish);
        if(dish == null) {

            return true;
        }
        if(dish.getStatus() == StatusConstant.ENABLE) {
            return false;
        }
        if(setmealDishMapper.countByDishId(id) > 0) {
            return false;
        }
        return true;
    }
    /**
     * 1.检查是否能删除菜品
     *      如果菜品处于起售状态，则不能删除
     *      如果菜品属于某个套餐，则不能删除
     * 2.删除菜品表中的菜品数据
     * 3.删除口味表中对应的口味数据
     */
    @Transactional
    public void delete(Long id) {
        if(canDelete(id)) {
            log.info("可以删除id为{}的菜品", id);
            dishMapper.deleteById(id);
            dishFlavorMapper.deleteByDishId(id);

        } else {
            log.info("不可以删除id为{}的菜品", id);
        }
    }

    /**
     * 批量删除
     *
     */
    public void deleteBatch(List<Long> idList) {
        for(Long id : idList) {
            delete(id);
        }
        return;
    }
    /**
     * 更新菜品
     */
    @Transactional
    public Result update(DishDTO dishDTO) {
        log.info("更新菜品：{}", dishDTO);
        Dish dish = dishMapper.getById(dishDTO.getId());
        dishMapper.deleteById(dish.getId());
        saveWithFlavor(dishDTO);
        dish.setId(dishDTO.getId());
        dishMapper.updateCreate(dish);
        return Result.success();
    }

    @Override
    public DishVO getById(Long id) {
        Dish dish = dishMapper.getById(id);
        DishVO dishVO = DishVO.builder().build();
        BeanUtils.copyProperties(dish, dishVO);
        dishVO.setFlavors(new ArrayList<>());
        return dishVO;
    }

    @Override
    public List<Dish> selectEnableDishByCategoryId(Long categoryId) {
        return List.of();
    }

}
