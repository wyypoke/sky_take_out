package com.sky.controller.admin;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.Employee;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/dish")
@Api(tags = "菜品相关接口")
@Slf4j
public class DishController {
    @Autowired
    @Qualifier("dishPlusServiceImpl")
    private DishService dishService;
    @PostMapping
    @ApiOperation("新增菜品")
    Result save(@RequestBody DishDTO dishDTO) {
        dishService.saveWithFlavor(dishDTO);
        return Result.success();
    }
    @GetMapping("/page")
    @ApiOperation("分页查询")
    Result<PageResult> page(DishPageQueryDTO dto) {
        log.info("菜品分页查询");
        PageResult pageResult = dishService.page(dto);
        return Result.success(pageResult);
    }

    @DeleteMapping
    @ApiOperation("删除菜品")
    Result delete(@RequestParam List<Long> ids) {
        log.info("批量删除菜品");
        dishService.deleteBatch(ids);
        return Result.success();
    }

    @PutMapping()
    @ApiOperation("修改菜品")
    Result update(@RequestBody DishDTO dishDTO) {
        dishService.update(dishDTO);

        return Result.success();
    }

    @GetMapping("/{id}")
    @ApiOperation("根据id查询菜品")
    public Result<DishVO> getById(@PathVariable Long id) {
        DishVO dishVO = dishService.getById(id);
        log.info("查询菜品：{}", dishVO);
        return Result.success(dishVO);
    }

    /**
     * 根据分类id查询菜品
     * @param categoryId
     * @return
     */
    @GetMapping("/list")
    @ApiOperation("根据分类id查询菜品")
    public Result<List<Dish>> selectByCategoryId(Long categoryId){
        List<Dish> list = dishService.selectEnableDishByCategoryId(categoryId);
        return Result.success(list);
    }
}
