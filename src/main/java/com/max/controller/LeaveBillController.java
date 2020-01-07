package com.max.controller;

import com.max.domain.Employee;
import com.max.domain.LeaveBill;
import com.max.service.LeaveBillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/leaveBill")
public class LeaveBillController {


    @Autowired
    private LeaveBillService leaveBillService;

    /**
     * 查询自个的请假申请
     * @return
     */
    @RequestMapping("/home")
    public String home(HttpSession session, Model model){

        Employee user = (Employee) session.getAttribute("user");

        List<LeaveBill> leaveBills = leaveBillService.findLeaveBillListByUserID(user.getId());
        model.addAttribute("leaveBills",leaveBills);

        return "leaveBillManage";
    }

    /**
     * 回显请假单 和 新建请假单
     * @return
     */
    @RequestMapping("/details")
    public String leaveBillDetails(Long id,Model model){
        if(id != null){
            LeaveBill leaveBill = leaveBillService.findLeaveBillByprimaryKey(id);
            model.addAttribute("echoLeaveBill",leaveBill);
        }
        return "leaveBillDetails";
    }


    /**
     * 新增 和 修改请假单
     * @param leaveBill
     * @return
     */
    @RequestMapping("/save")
    public String save(LeaveBill leaveBill){
        if (leaveBill.getId() != null){
            leaveBillService.update(leaveBill);

        }else {
            leaveBillService.save(leaveBill);
        }

        return "redirect:/leaveBill/home";
    }

    @RequestMapping("/delete")
    public String delete(Long id){
        leaveBillService.deleteByPrimaryKey(id);

        return "redirect:/leaveBill/home";
    }


}
