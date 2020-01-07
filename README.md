# Activiti工作流-Demo

**项⽬框架:**
+ Activiti+SpringMVC+Spring+Mybatis 

**数据存储:** 
+ Mysql8.0.3

**实现功能:** 
+ 1.部署信息的管理
+ 2.流程定义的管理(默认采用zip方式)
+ 3.流程图的查看
+ 4.请假业务的管理
+ 5.个人任务的的管理
+ 6.任务办理的管理(通过流程图定位当前流程位置)


**需要修改的地方:** 
+ [db.properties](src/main/resources/db.properties)
+ *数据库名称:*
+ *用户名:*
+ *密码*


````properties
    jdbc.driver=com.mysql.cj.jdbc.Driver
    jdbc.url=jdbc:mysql://localhost:3306/activiti_demo?characterEncoding=utf8&nullCatalogMeansCurrent=true
    jdbc.username=****
    jdbc.password=****
````
**创建数据库**
+ 执行 [resources](/src/main/resources)下的.sql文件

**程序运行后查看地址**
+ 首页: http://localhost:8080/login

**预览**
![](/src/main/webapp/images/预览1.png)
![](/src/main/webapp/images/预览2.png)
![](/src/main/webapp/images/预览3.png)
![](/src/main/webapp/images/预览4.png)
![](/src/main/webapp/images/预览5.png)
![](/src/main/webapp/images/预览6.png)




