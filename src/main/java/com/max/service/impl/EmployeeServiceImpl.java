package com.max.service.impl;

import com.max.domain.Employee;
import com.max.mapper.EmployeeMapper;
import com.max.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;

    @Override
    public List<Employee> getAllEmployee() {
        return employeeMapper.selectAll();
    }

    @Override
    public Employee getOneByName(String name) {

        return employeeMapper.selectOneByName(name);
    }
}
