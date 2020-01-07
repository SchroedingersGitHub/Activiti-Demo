package com.max.service;

import com.max.domain.Employee;
import com.max.domain.LeaveBill;
import com.max.domain.WorkflowBean;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.Task;

import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

public interface WorkflowService {

    void saveNewDeploy(InputStream inputStream, String name);

    List<Deployment> findDeploymentList();

    List<ProcessDefinition> findProcessDefinitionList();

    InputStream findImageInputStream(String deploymentId, String imageName);

    void deleteDeploymentByDeploymentId(String deploymentId);

    void saveStartProcess(Long id, Employee employee);

    List<Task> findTaskListByName(String name);

    String findTaskFormKeyByTaskId(String id);

    LeaveBill findLeaveBillByTaskId(String taskId);

    List<String> findOutcomeListByTaskID(String taskId);

    void saveSubmitTask(WorkflowBean workflowBean,String name);

    List<Comment> findCommentListByTaskID(String taskId);

    List<Comment> findCommentListByLeaveBillID(Long id);

    ProcessDefinition findProcessDefinitionByTaskId(String taskId);

    Map<String, Object> findCoordingByTaskId(String taskId);
}
