package org.throwable;

import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * @author zjc
 * @version 2016/12/8 0:40
 * @description
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring-config.xml",
        "classpath:spring-activiti.xml", "classpath:dispatcher-servlet.xml"})
public class ActivitiTestTest {

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private HistoryService historyService;

    @Test
    public void TestHelloWorld() throws Exception {

        //部署流程定义文件
        repositoryService.createDeployment()
                .addClasspathResource("org/throwable/leave/sayhelloleave.bpmn").deploy();

        //验证已部署的流程定义
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().singleResult();

        //这个processDefinition.getKey() 值 和 bpmn里面的id一致
        assertEquals("leavesayhello", processDefinition.getKey());

        //启动流程并且返回流程实例
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("leavesayhello");
        assertNotNull(processInstance);

        System.out.println("流程实例id = " + processInstance.getId() + "; 流程定义id = " + processInstance.getProcessDefinitionId());

    }

    @Test
    public void SayHelloToLeave() throws Exception {
        //发布流程定义
        DeploymentBuilder deploymentBuilder = repositoryService.createDeployment();
        deploymentBuilder.addClasspathResource("org/throwable/leave/SayHelloToLeave.bpmn").deploy();

        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .processDefinitionKey("SayHelloToLeave").latestVersion().singleResult();
        assertEquals("SayHelloToLeave", processDefinition.getKey());
        Map<String, Object> variables = new HashMap<>();
        variables.put("applyUser", "ppDoger");
        variables.put("days", 3);
        //开始流程实例,并且绑定值,值的域是整个流程实例的
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("SayHelloToLeave", variables);
        assertNotNull(processInstance);
        System.out.println("流程实例id = " + processInstance.getId() + "; 流程定义id = " + processInstance.getProcessDefinitionId());

        //根据候选人的组别获取任务(userTask)
        Task deptLeaderTask = taskService.createTaskQuery().taskCandidateGroup("deptLeader").singleResult();
        assertNotNull(deptLeaderTask);
        assertEquals("领导审批", deptLeaderTask.getName());

        //Claim responsibility for a task: the given user is made assignee for the task
        //设置任务代理人
        taskService.claim(deptLeaderTask.getId(), "leaderUser");
        variables = new HashMap<>();
        variables.put("approved", true);

        //完成任务,传入参数
        taskService.complete(deptLeaderTask.getId(),variables);

        deptLeaderTask = taskService.createTaskQuery().taskCandidateGroup("deptLeader").singleResult();
        assertNull(deptLeaderTask);

        //查询已完成的任务的历史个数
        long count = historyService.createHistoricProcessInstanceQuery().finished().count();
        System.out.println("完成任务个数:" + count);
    }
}