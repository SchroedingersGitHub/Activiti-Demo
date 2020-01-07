package com.max.controller;

import com.max.domain.Employee;
import com.max.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class LoginController {

    @Autowired
    private EmployeeService employeeService;

    /**
     * 登录
     * @param name
     * @param session
     * @return
     */
    @RequestMapping("register")
    public String register(String name, HttpSession session){
        Employee employee = employeeService.getOneByName(name);
        session.setAttribute("user",employee);
        return "index";
    }

    /**
     * 退出
     * @param session
     * @return
     */
    @RequestMapping("logout")
    public String logout(HttpSession session){
        session.removeAttribute("user");
        return "redirect:login";
    }

    /**
     * 进入登录界面
     * @return
     */
    @RequestMapping(value = {"/login","/"})
    public String login(Model model){
        List<Employee> employees = employeeService.getAllEmployee();
        model.addAttribute(employees);
        return "login";
    }

}
