package com.sky.repository.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sky.constant.StatusConstant;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.entity.Employee;
import com.sky.entity.SetmealDish;
import com.sky.mapper.DishMapper;
import com.sky.mapper.MyBatisPlusDishFlavorMapper;
import com.sky.mapper.MyBatisPlusDishMapper;
import com.sky.mapper.MyBatisPlusSetmealDishMapper;
import com.sky.repository.DishFlavorRepo;
import com.sky.repository.DishRepo;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.vo.DishVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Slf4j
public class DishRepoImpl implements DishRepo {
    @Autowired
    private MyBatisPlusDishFlavorMapper dishFlavorMapper;
    @Autowired
    private MyBatisPlusSetmealDishMapper setmealDishMapper;
    @Autowired
    private MyBatisPlusDishMapper dishMapper;
    @Autowired
    private DishFlavorRepo dishFlavorRepo;
    @Override
    public PageResult pageQuery(DishPageQueryDTO dto) {
        Page<DishVO> page = new Page<>(dto.getPage(), dto.getPageSize());
        IPage<DishVO> resultPage = dishMapper.pageQuery(page, dto);

        return PageResult.builder()
                .total(resultPage.getTotal())
                .records(resultPage.getRecords())
                .build();
    }

    @Override
    public Integer countByCategoryId(Long categoryId) {
        LambdaQueryWrapper<Dish> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Dish::getCategoryId, categoryId);
        return dishMapper.selectCount(wrapper).intValue();
    }

    @Override
    public void insert(Dish dish) {
        dishMapper.insert(dish);
    }

    @Override
    public Dish getById(Long id) {
        return dishMapper.selectById(id);
    }

    @Override
    public void deleteById(Long id) {
        if(deletable(id)) {
            log.info("可以删除id为{}的菜品", id);
            dishMapper.deleteById(id);
            dishFlavorRepo.deleteByDishId(id);

        } else {
            log.info("不可以删除id为{}的菜品", id);
        }
    }

    @Override
    @Transactional
    public void update(DishDTO dishDTO) {
        Dish dish = Dish.builder().build();
        BeanUtils.copyProperties(dishDTO, dish);
        dishMapper.updateById(dish);
        dishFlavorRepo.deleteByDishId(dish.getId());

        List<DishFlavor> dishFlavorList = dishDTO.getFlavors();
        if(dishFlavorList != null && !dishFlavorList.isEmpty()) {
            dishFlavorRepo.saveWithDishId(dishFlavorList, dish.getId());
        }

    }

    @Override
    public Result saveWithFlavor(DishDTO dishDTO) {
        Dish dish = Dish.builder().build();
        BeanUtils.copyProperties(dishDTO, dish);
        dishMapper.insert(dish);
        List<DishFlavor> dishFlavorList = dishDTO.getFlavors();
        if(dishFlavorList != null && !dishFlavorList.isEmpty()) {
            dishFlavorList.forEach(dishFlavor -> {
                dishFlavor.setDishId(dish.getId());
                dishFlavorMapper.insert(dishFlavor);
            });
        }
        dishDTO.setId(dish.getId());
        return Result.success();
    }

    /**
     * @param dish
     * @return
     */
    @Override
    public List<Dish> select(Dish dish) {
        LambdaQueryWrapper<Dish> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(dish.getCategoryId() != null,
                Dish::getCategoryId,
                dish.getCategoryId());
        wrapper.like(dish.getName() != null && !dish.getName().equals(""),
                Dish::getName,
                dish.getName() != null);
        wrapper.eq(dish.getStatus() != null,
                Dish::getStatus,
                dish.getStatus());
        return dishMapper.selectList(wrapper);
    }

    /**
     * @param id
     * @return
     */
    @Override
    public List<Dish> getBySetmealId(Long id) {
        return dishMapper.getBySetmealId(id);
    }

    private boolean deletable(Long id) {
        Dish dish = dishMapper.selectById(id);
        log.info("判断菜品{}是否可以删除", dish);
        if(dish == null) {
            return true;
        }
        if(dish.getStatus().equals(StatusConstant.ENABLE)) {
            return false;
        }
        LambdaQueryWrapper<SetmealDish> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SetmealDish::getDishId, id);
        if(setmealDishMapper.selectCount(wrapper) > 0) {
            return false;
        }
        return true;
    }

}
