package com.max.service;

import com.max.domain.Employee;

import java.util.List;

public interface EmployeeService {

    List<Employee> getAllEmployee();

    Employee getOneByName(String name);
}
