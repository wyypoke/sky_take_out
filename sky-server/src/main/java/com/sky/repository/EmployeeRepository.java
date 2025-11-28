package com.sky.repository;

import com.github.pagehelper.Page;
import com.sky.annotation.CreateInfo;
import com.sky.annotation.UpdateInfo;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.result.PageResult;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface EmployeeRepository {
    /**
     * 根据用户名查询员工
     * @param username
     * @return
     */
    Employee getByUsername(String username);

    /**
     * 插入员工
     * @param employee
     */
    void insert(Employee employee);

    /**
     * 员工分页查询
     * @param employeePageQueryDTO
     * @return
     */
    PageResult pageQuery(EmployeePageQueryDTO employeePageQueryDTO);

    /**
     * 修改员工
     * @param employee
     */
    @UpdateInfo
    void update(Employee employee);

    /**
     * 根据id查找员工
     * @param id
     * @return
     */
    Employee getById(Long id);
}
