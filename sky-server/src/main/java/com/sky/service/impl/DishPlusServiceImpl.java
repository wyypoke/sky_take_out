package com.sky.service.impl;

import com.sky.constant.StatusConstant;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.repository.DishFlavorRepo;
import com.sky.repository.DishRepo;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
@Service
@Slf4j
public class DishPlusServiceImpl implements DishService {
    @Autowired
    private DishRepo dishRepo;

    @Autowired
    private DishFlavorRepo dishFlavorRepo;

    @Override

    public Result saveWithFlavor(DishDTO dishDTO) {
        return dishRepo.saveWithFlavor(dishDTO);
    }

    @Override
    public PageResult page(DishPageQueryDTO dto) {
        return dishRepo.pageQuery(dto);
    }

    @Transactional
    @Override
    public void delete(Long id) {
        dishRepo.deleteById(id);
    }

    @Override
    public void deleteBatch(List<Long> idList) {
        for(Long id : idList) {
            delete(id);
        }
    }

    @Override
    @Transactional
    public Result update(DishDTO dishDTO) {
        log.info("更新菜品：{}", dishDTO);
        dishRepo.update(dishDTO);
        return Result.success();
    }

    @Override
    public DishVO getById(Long id) {
        Dish dish = dishRepo.getById(id);
        List<DishFlavor> flavors = dishFlavorRepo.selectByDishId(id);
        DishVO dishVO = DishVO
                .builder()
                .build();
        BeanUtils.copyProperties(dish, dishVO);
        dishVO.setFlavors(flavors);
        return dishVO;
    }

    /**
     * @param categoryId
     * @return
     */
    @Override
    public List<Dish> selectEnableDishByCategoryId(Long categoryId) {
        Dish dish = Dish.builder()
                .categoryId(categoryId)
                .status(StatusConstant.ENABLE)
                .build();
        return dishRepo.select(dish);
    }

    /**
     * 条件查询菜品和口味
     * @param dish
     * @return
     */
    @Cacheable(cacheNames = "DishPlusServiceImpl.listWithFlavor",
            key = "#dish.categoryId + '_' + #dish.status")
    public List<DishVO> listWithFlavor(Dish dish) {
        List<Dish> dishList = dishRepo.select(dish);

        List<DishVO> dishVOList = new ArrayList<>();

        for (Dish d : dishList) {
            DishVO dishVO = new DishVO();
            BeanUtils.copyProperties(d,dishVO);

            //根据菜品id查询对应的口味
            List<DishFlavor> flavors = dishFlavorRepo.selectByDishId(d.getId());

            dishVO.setFlavors(flavors);
            dishVOList.add(dishVO);
        }

        return dishVOList;
    }
}
