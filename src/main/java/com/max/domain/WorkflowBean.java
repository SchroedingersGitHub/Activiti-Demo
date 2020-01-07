package com.max.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.File;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkflowBean {
    private File file;  // 流程定义部署文件
    private String fileName; //流程定义名称

    private Long id;  // 请假单Id
    private String deploymentId; // 部署对象ID
    private String imageName; // 资源文件名称
    private String outcome; // 连线名称
    private String comment; // 批注

    private String taskId; // 任务Id

}
