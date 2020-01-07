package com.max.listener;

import com.max.domain.Employee;
import com.max.mapper.EmployeeMapper;
import lombok.Setter;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.http.HttpSession;

@Component
public class ManagerTaskHandler implements TaskListener {


    @Autowired
    private EmployeeMapper employeeMapper;

    @Autowired
    private HttpSession session;


    @Override
    public void notify(DelegateTask delegateTask) {


//        从session中获取当前用户
        Employee employee = (Employee) session.getAttribute("user");

//         查询当前用户对应的领导
        Employee cascEmployee = employeeMapper.selectOneByName(employee.getName());

//         设置个人任务的办理人
        String name = cascEmployee.getManager().getName();
        delegateTask.setAssignee(name);

    }
}
