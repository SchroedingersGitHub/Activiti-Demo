package com.max.controller;

import com.max.domain.Employee;
import com.max.domain.LeaveBill;
import com.max.domain.WorkflowBean;
import com.max.service.LeaveBillService;
import com.max.service.WorkflowService;
import jdk.nashorn.internal.objects.NativeNumber;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.Task;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.MalformedInputException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/workflow")
public class WorkflowController {


    @Autowired
    private WorkflowService workflowService;

    @Autowired
    private LeaveBillService leaveBillService;

    /**
     * 部署管理首页 （查询）
     *
     * @return
     */
    @RequestMapping("/deploy")
    public String deploy(Model model) {
        // 1.部署信息管理列表 （ACT_RE_DEPLOYMENT）
        List<Deployment> deployments = workflowService.findDeploymentList();
        // 2.流程定义信息列表 （ACT_RE_PROCDEF）
        List<ProcessDefinition> processDefinitions = workflowService.findProcessDefinitionList();
        model.addAttribute("deployments", deployments);
        model.addAttribute("processDefinitions", processDefinitions);
        return "deployManage";
    }


    /**
     * 部署流程的定义
     */
    @RequestMapping("/newDeploy")
    public String newDeploy(@RequestParam("file") CommonsMultipartFile file, String name) {

        try {
            if (file.getSize() > 0 || !StringUtils.isEmpty(name)) {
                InputStream inputStream = file.getInputStream();
                workflowService.saveNewDeploy(inputStream, name);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "redirect:/workflow/deploy";
    }

    /**
     * 删除流程定义 (级联)
     *
     * @param workflowBean
     * @return
     */
    @RequestMapping("/delDeployment")
    public String delDeployment(WorkflowBean workflowBean) {
        workflowService.deleteDeploymentByDeploymentId(workflowBean.getDeploymentId());

        return "redirect:/workflow/deploy";
    }

    /**
     * 查看流程图
     *
     * @param workflowBean
     * @param rp
     * @return
     */
    @RequestMapping("/viewImage")
    @ResponseBody
    public String viewImage(WorkflowBean workflowBean, HttpServletResponse rp) {

        // 通过部署对象ID 和 资源图片名称获取 图片输入流
        InputStream inputStream = workflowService.findImageInputStream(workflowBean.getDeploymentId(), workflowBean.getImageName());
        OutputStream outputStream = null;
        try {
            outputStream = rp.getOutputStream();
            int count = 0;
            byte[] buffer = new byte[1024 * 8];
            while ((count = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, count);
                outputStream.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return "ok";
    }

    /**###############################################################流程实例#########################################################################*/


    /**
     * 启动流程实例
     * 1.更新状态  2.启动流程  3.使流程实例关联业务
     * @return
     */
    @RequestMapping("/startProcess")
    public String startProcess(Long id, HttpSession session){
        Employee employee = (Employee) session.getAttribute("user"); // 从Session中获取当前任务办理人
        workflowService.saveStartProcess(id,employee);
        return "redirect:/workflow/listTask";
    }

    /**
     * 获取个人任务 by Name
     */
    @RequestMapping("/listTask")
    public String listTask(HttpSession session, Model model){
        Employee employee = (Employee) session.getAttribute("user");
        List<Task> tasks = workflowService.findTaskListByName(employee.getName());
        model.addAttribute("tasks",tasks);

        return "taskManage";
    }

    /**
     * 打开任务表单
     * 获取当前任务节点的FormKey
     * 值为当前节点跳转的Url
     * @param id 任务ID
     * @return
     */
    @RequestMapping("/viewTaskForm")
    public String viewTaskForm(String id){
        String url = workflowService.findTaskFormKeyByTaskId(id); // activiti:formKey="workflow/editor"
        url = url+"?taskId="+id; // 向下传递参数
        return "redirect:/"+url;
    }

    /**
     * 准备表单数据
     * @param taskId
     * @return
     */
    @RequestMapping("/editor")
    public String editor(String taskId,Model model){

        model.addAttribute("taskId",taskId); //传递任务ID 用来完成任务

        // 1.使用任务Id查询请假单信息
        LeaveBill leaveBill = workflowService.findLeaveBillByTaskId(taskId);
        model.addAttribute("leaveBill",leaveBill);

        // 2.使用任务ID查询ProcessDefinitionEntity对象(.bpmn文件对象) 从而获取连线名称，并放置List<String>集合中
        List<String> outcomes = workflowService.findOutcomeListByTaskID(taskId);
        model.addAttribute("outcomes",outcomes);

        // 3.查询所有历史审核人信息，帮助当前人完成审核 返回List<Comment>
        List<Comment> comments = workflowService.findCommentListByTaskID(taskId);
        model.addAttribute("comments",comments);

        return "viewTaskForm";
    }

    /**
     * 完成任务
     * @param workflowBean
     * @param session
     * @return
     */
    @RequestMapping("/submitTask")
    public String submitTask(WorkflowBean workflowBean,HttpSession session){
        Employee employee = (Employee) session.getAttribute("user"); //传递当前是谁审批的
        workflowService.saveSubmitTask(workflowBean,employee.getName());

        return "redirect:/workflow/listTask";

    }


    /**
     * 通过请假单ID查询历史批注信息
     * @param id 请假单ID
     * @return
     */
    @RequestMapping("/viewHisComment")
    public String viewHisComment(Long id,Model model){
        // i: 请假单数据回显
        LeaveBill leaveBill = leaveBillService.findLeaveBillByprimaryKey(id);
        model.addAttribute("leaveBill",leaveBill);

        // ii: 历史批注信息的回显  通过请假单ID
        List<Comment> comments = workflowService.findCommentListByLeaveBillID(id);
        model.addAttribute("comments",comments);

        return "viewHisComment";
    }


    /**
     * 查看当前流程图
     * @param taskId
     * @return
     */
    @RequestMapping("/viewCurrentImage")
    public String viewCurrentImage(String taskId,Model model){
        // i: 通过任务Id查询任务对象 -> 通过任务对象查询流程定义Id -> 通过流程定义Id查询流程定义对象
        ProcessDefinition processDefinition = workflowService.findProcessDefinitionByTaskId(taskId);
        model.addAttribute("deploymentId",processDefinition.getDeploymentId());
        model.addAttribute("ImageName",processDefinition.getDiagramResourceName());

        // ii:查看当前活动节点,获取坐标:x,y,width,height
        Map<String,Object> map = workflowService.findCoordingByTaskId(taskId);
        model.addAttribute("map",map);

        return "viewImage";
    }



}
