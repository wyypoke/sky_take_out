package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.CreateInfo;
import com.sky.annotation.UpdateInfo;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.vo.DishVO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface DishMapper {

    Page<DishVO> pageQuery(DishPageQueryDTO dto);

    /**
     * 根据分类id查询菜品数量
     * @param categoryId
     * @return
     */
    @Select("select count(id) from dish where category_id = #{categoryId}")
    Integer countByCategoryId(Long categoryId);

    @CreateInfo
    @UpdateInfo
    void insert(Dish dish);

    @Select("select * from dish where id = #{id}")
    Dish getById(Long id);

    @Delete("DELETE FROM dish WHERE id = #{id}")
    int deleteById(Long id);
}
