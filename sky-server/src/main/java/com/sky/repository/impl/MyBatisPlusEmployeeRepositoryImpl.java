package com.sky.repository.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.mapper.MyBatisPlusEmployeeMapper;
import com.sky.repository.EmployeeRepository;
import com.sky.result.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MyBatisPlusEmployeeRepositoryImpl implements EmployeeRepository {
    @Autowired
    private MyBatisPlusEmployeeMapper employeeMapper;
    @Override
    public Employee getByUsername(String username) {
        LambdaQueryWrapper<Employee> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Employee::getUsername, username);
        return employeeMapper.selectOne(wrapper);
    }

    @Override
    public void insert(Employee employee) {
        employeeMapper.insert(employee);
    }

    @Override
    public PageResult pageQuery(EmployeePageQueryDTO employeePageQueryDTO) {
        IPage<Employee> page = new Page<>(employeePageQueryDTO.getPage(), employeePageQueryDTO.getPageSize());
        LambdaQueryWrapper<Employee> wrapper = new LambdaQueryWrapper<>();
        String name = employeePageQueryDTO.getName();
        if (name != null && !name.isEmpty()) {
            wrapper.like(Employee::getName, name);
        }
        IPage<Employee> resultPage = employeeMapper.selectPage(page, wrapper);
        long total = resultPage.getTotal();
        List<Employee> records = resultPage.getRecords();
        return new PageResult(total, records);
    }

    @Override
    public void update(Employee employee) {
        employeeMapper.updateById(employee);
    }

    @Override
    public Employee getById(Long id) {
        return employeeMapper.selectById(id);
    }
}
