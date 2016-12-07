package org.throwable;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

/**
 * @author zjc
 * @version 2016/12/8 0:40
 * @description
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring-config.xml",
		"classpath:spring-activiti.xml","classpath:dispatcher-servlet.xml"})
public class ActivitiTestTest {

	@Autowired
	private RepositoryService repositoryService;

	@Autowired
	private RuntimeService runtimeService;

	@Test
	public void TestHelloWorld()throws Exception{
		//部署流程定义文件
		repositoryService.createDeployment()
				.addClasspathResource("org/throwable/leave/sayhelloleave.bpmn").deploy();

		//验证已部署的流程定义
		ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().singleResult();

		//这个processDefinition.getKey() 值 和 bpmn里面的id一致
		assertEquals("leavesayhello",processDefinition.getKey());

		//启动流程并且返回流程实例
		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("leavesayhello");
		assertNotNull(processInstance);

		System.out.println("流程实例id = " + processInstance.getId() + "; 流程定义id = " + processInstance.getProcessDefinitionId());

	}
}