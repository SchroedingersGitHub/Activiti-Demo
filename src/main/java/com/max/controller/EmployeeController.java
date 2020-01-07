package com.max.controller;

import com.max.domain.Employee;
import com.max.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @RequestMapping("getAllEmployee")
    @ResponseBody
    public List<Employee> getAllEmployee() {

        return employeeService.getAllEmployee();

    }

}
