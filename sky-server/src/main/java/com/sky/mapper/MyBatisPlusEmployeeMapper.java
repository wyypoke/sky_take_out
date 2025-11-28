package com.sky.mapper;

import com.sky.entity.Employee;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MyBatisPlusEmployeeMapper extends BaseMapper<Employee>{

}
