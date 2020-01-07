package com.max.service.impl;

import com.max.domain.Employee;
import com.max.domain.LeaveBill;
import com.max.domain.WorkflowBean;
import com.max.mapper.LeaveBillMapper;
import com.max.service.WorkflowService;
import org.activiti.engine.*;
import org.activiti.engine.form.TaskFormData;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.impl.identity.Authentication;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.Task;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipInputStream;

@Service
@Transactional
public class WorkflowServiceImpl implements WorkflowService {

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private HistoryService historyService;

    @Autowired
    private FormService formService;

    @Autowired
    private LeaveBillMapper leaveBillMapper;


    /**
     * 部署流程的定义
     *
     * @param inputStream
     * @param name
     */
    @Override
    public void saveNewDeploy(InputStream inputStream, String name) {
        // 将File类型转换成ZipInputStream
        ZipInputStream zipInputStream = new ZipInputStream(inputStream);
        repositoryService.createDeployment()  // 创建部署对象
                .name(name)
                .addZipInputStream(zipInputStream)
                .deploy();
    }

    @Override
    public List<Deployment> findDeploymentList() {
        return repositoryService.createDeploymentQuery() //创建部署对象查询
                .orderByDeploymenTime().asc()
                .list();
    }

    @Override
    public List<ProcessDefinition> findProcessDefinitionList() {
        return repositoryService.createProcessDefinitionQuery() // 创建流程定义查询
                .orderByProcessDefinitionVersion().asc()
                .list();
    }

    @Override
    public InputStream findImageInputStream(String deploymentId, String imageName) {
        return repositoryService.getResourceAsStream(deploymentId, imageName);
    }

    @Override
    public void deleteDeploymentByDeploymentId(String deploymentId) {
        repositoryService.deleteDeployment(deploymentId, true); // 级联
    }

    @Override
    public void saveStartProcess(Long id, Employee employee) {
        // 1.通过ID查询请假单的信息
        LeaveBill leaveBill = leaveBillMapper.selectByPrimaryKey(id);
        // 2.更改请假单的状态 从0 -> 1 (初始录入 -> 审核中)
        leaveBill.setState(1);
        leaveBillMapper.updateByPrimaryKey(leaveBill);
        // 3.使用当前对象获取流程定义的Key （对象名称就是流程定义的Key）
        String key = leaveBill.getClass().getSimpleName();
        // 4.从Session中获取当前任务的办理人 (由Controller层传递过来)
        Map<String, Object> variables = new HashMap<>();
        variables.put("inputUser", employee.getName());  //使用流程变量设置下个任务办理人
        // 5.让流程实例关联业务
        //  1.使用流程变量设置字符串(格式:LeaveBill.id形式) 启动时候通过 流程变量 绑定业务
        String objId = key + "." + id;
        variables.put("objId", objId);
        //  2.使用  ACT_RU_EXECUTION  #正在执行的执行对象表中 BUSINESS_KEY_字段关联业务 startProcessInstanceByKey(String processDefinitionKey, String businessKey, Map<String, Object> variables);
        runtimeService.startProcessInstanceByKey(key, objId, variables);
    }

    @Override
    public List<Task> findTaskListByName(String name) {
        return taskService.createTaskQuery()
                .taskAssignee(name)
                .orderByTaskCreateTime().asc()
                .list();
    }

    @Override
    public String findTaskFormKeyByTaskId(String id) {
        TaskFormData taskFormData = formService.getTaskFormData(id);
        return taskFormData.getFormKey();
    }

    @Override
    public LeaveBill findLeaveBillByTaskId(String taskId) {
        // 1. 通过任务ID查询查询任务对象
        Task task = taskService.createTaskQuery()
                .taskId(taskId)
                .singleResult();
        // 2. 通过任务对象获取流程实例ID
        String processInstanceId = task.getProcessInstanceId();

        // 3.通过流程实例ID查询正在执行对象表，返回流程实例对象
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
                .processInstanceId(processInstanceId)
                .singleResult();

        // 4.通过流程实例对象获取BUSINESS_KEY_
        String businessKey = processInstance.getBusinessKey();

        // 5.获取BUSINESS_KEY_主键 查询请假单对象
        String id = "";
        if (StringUtils.isNotBlank(businessKey)) {
            id = businessKey.split("\\.")[1];
        }

        // 查询请假单对象
        return leaveBillMapper.selectByPrimaryKey(Long.parseLong(id));

    }

    @Override
    public List<String> findOutcomeListByTaskID(String taskId) {

        List<String> list = new ArrayList<>();

        //1.使用任务ID查询任务对象
        Task task = taskService.createTaskQuery()
                .taskId(taskId)
                .singleResult();

        //2. 获取流程定义Id
        String processDefinitionId = task.getProcessDefinitionId();

        //3.查询ProcessDefinitionEntity对象 强转一下 ProcessDefinitionEntity extends ProcessDefinitionImpl
        ProcessDefinitionEntity processDefinitionEntity = (ProcessDefinitionEntity) repositoryService.getProcessDefinition(processDefinitionId);

        //4.获取当前的活动节点
        String processInstanceId = task.getProcessInstanceId(); // 通过任务对象获取流程实例ID

        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()   // 通过流程实例ID查询正在执行对象表，返回流程实例对象
                .processInstanceId(processInstanceId)
                .singleResult();
        String activityId = processInstance.getActivityId(); // 获取活动Id

        ActivityImpl activity = processDefinitionEntity.findActivity(activityId);

        //5.获取当前活动节点的连线名称
        List<PvmTransition> pvmList = activity.getOutgoingTransitions();
        for (PvmTransition pvm : pvmList) {
            String name = (String) pvm.getProperty("name");
            if (StringUtils.isNotBlank(name)) {
                list.add(name);
            } else {
                list.add("默认提交");
            }

        }

        return list;
    }

    @Override
    public void saveSubmitTask(WorkflowBean workflowBean, String name) {  // name：从session中获取的
        // 任务ID
        String taskId = workflowBean.getTaskId();
        // 获取连线名称
        String outcome = workflowBean.getOutcome();
        // 批注
        String comment = workflowBean.getComment();
        // 请假单ID
        Long id = workflowBean.getId();


        // 1.在完成之前，添加一个批注信息，向ACT_HI_COMMENT表添加审批意见信息
        Task task = taskService.createTaskQuery()//  使用任务ID查询任务对象
                .taskId(taskId)
                .singleResult();
        String processInstanceId = task.getProcessInstanceId(); //获取任务实例ID
        /**
         * 由于 底层 String userId = Authentication.getAuthenticatedUserId();
         * 需要从session获取当前登录人，做为该任务的办理人（审核人） 若未指定则为Null
         * 对应ACT_HI_COMMENT表中USER_ID_字段
         * */

        Authentication.setAuthenticatedUserId(name); // 记录审批人
        taskService.addComment(taskId, processInstanceId, comment);

        // 2.如果连线名称为'默认提交'就不需要设置流程变量 否则需设置
        Map<String, Object> variables = new HashMap<>();

        if (outcome != null && !"默认提交".equals(outcome)) {
            variables.put("outcome", outcome);
        }
        // 3.使用任务ID完成当前任务 同时设置流程变量
        taskService.complete(taskId, variables);

        // 4.指定下个任务办理人 -> 通过类监听 com.max.listener.ManagerTaskHandler

        // 5.判断流程是否结束 (true:设置请假单状态 1 -> 2 审核中 审核完成)
        ProcessInstance pi = runtimeService.createProcessInstanceQuery()
                .processInstanceId(processInstanceId)
                .singleResult();
        // 流程是否结束
        if (pi == null) {
            LeaveBill leaveBill = leaveBillMapper.selectByPrimaryKey(id);
            leaveBill.setState(2); // 更新状态
            leaveBillMapper.updateByPrimaryKey(leaveBill);

        }


    }

    /**
     * 获取历史批注信息 By任务ID
     *
     * @param taskId
     * @return
     */
    @Override
    public List<Comment> findCommentListByTaskID(String taskId) {

        List<Comment> comments = new ArrayList<>();

        // 通过当前任务ID获取流程实例Id
        Task task = taskService.createTaskQuery()
                .taskId(taskId)
                .singleResult();
        String processInstanceId = task.getProcessInstanceId();

        /** // 通过流程实例ID查询历史任务   // <1>
         List<HistoricTaskInstance> list = historyService.createHistoricTaskInstanceQuery() //历史任务表中查询
         .processInstanceId(processInstanceId)
         .list();
         // 遍历历史任务 获取每个历史任务Id
         for (HistoricTaskInstance historicTaskInstance : list) {
         String id = historicTaskInstance.getId(); // 任务ID
         List<Comment> commentList = taskService.getTaskComments(id);//通过任务ID查询批注信息 List
         comments.addAll(commentList); // 添加

         }*/

        comments = taskService.getProcessInstanceComments(processInstanceId); // <2>直接通过流程实例ID去查询


        return comments;
    }

    /**
     * 请假单ID 查询批注
     *
     * @param id
     * @return
     */
    @Override
    public List<Comment> findCommentListByLeaveBillID(Long id) {
        // 查询请假单对象
        LeaveBill leaveBill = leaveBillMapper.selectByPrimaryKey(id);
        // 获取对象名称
        String simpleName = leaveBill.getClass().getSimpleName();
        // 拼接流程表中的字段
        String objId = simpleName + "." + leaveBill.getId();

        /**
         * i:使用历史的流程实例查询 返回历史的流程实例对象 获取流程实例Id
         */
//        HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery()
//                .processInstanceBusinessKey(objId)
//                .singleResult();
//        String processInstanceId = historicProcessInstance.getId();  // 流程实例Id


        /**
         * ii:使用历史流程变量查询 返回历史流程变量对象  获取流程实例ID
         */
        HistoricVariableInstance historicVariableInstance = historyService.createHistoricVariableInstanceQuery()
                .variableValueEquals("objId", objId) // 使用流程变量名称 和 流程变量的值
                .singleResult();
        String processInstanceId = historicVariableInstance.getProcessInstanceId(); // 流程实例ID

        return taskService.getProcessInstanceComments(processInstanceId);
    }

    @Override
    public ProcessDefinition findProcessDefinitionByTaskId(String taskId) {
        Task task = taskService.createTaskQuery() // 查询任务对象
                .taskId(taskId)
                .singleResult();
        String processDefinitionId = task.getProcessDefinitionId();

        return repositoryService.createProcessDefinitionQuery()
                .processDefinitionId(processDefinitionId)
                .singleResult(); // 查询流程定义对象
    }

    @Override
    public Map<String, Object> findCoordingByTaskId(String taskId) {

        Map<String, Object> map = new HashMap<>();

        Task task = taskService.createTaskQuery()
                .taskId(taskId)
                .singleResult();
        // 获取流程定义ID
        String processDefinitionId = task.getProcessDefinitionId();

        // 获取流程定义实体对象 (bpmn对象)
        ProcessDefinitionEntity processDefinitionEntity = (ProcessDefinitionEntity) repositoryService.getProcessDefinition(processDefinitionId);


        String processInstanceId = task.getProcessInstanceId(); // 流程实例ID
        //获取活动对象
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
                .processInstanceId(processInstanceId)
                .singleResult();
        String activityId = processInstance.getActivityId(); //获取活动ID


        // 获取坐标
        ActivityImpl activity = processDefinitionEntity.findActivity(activityId);
        map.put("x", activity.getX());
        map.put("y", activity.getY());
        map.put("width", activity.getWidth());
        map.put("height", activity.getHeight());
        return map;
    }

}
