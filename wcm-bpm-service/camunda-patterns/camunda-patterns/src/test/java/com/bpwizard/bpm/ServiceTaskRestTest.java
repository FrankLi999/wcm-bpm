package com.bpwizard.bpm;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.task.Task;
import org.camunda.bpm.engine.test.Deployment;
import org.camunda.bpm.engine.test.ProcessEngineRule;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

/**
 * @author Stefan Hentschel.
 */
public class ServiceTaskRestTest {
	@Rule
	public ProcessEngineRule processEngineRule = new ProcessEngineRule();

	@Test
	@Deployment(resources = { "bpmn/http_connector.bpmn" })
	public void shouldPackForHoliday() {
		Map<String, Object> variables = new HashMap<String, Object>();
		variables.put("date", "2014-04-21");

		RuntimeService runtimeService = processEngineRule.getRuntimeService();
		TaskService taskService = processEngineRule.getTaskService();

		runtimeService.startProcessInstanceByKey("http_connector", variables);

		Task task = taskService.createTaskQuery().singleResult();
		Assert.assertNotNull(task);
		assertEquals("Pack for holiday", task.getName());

		boolean isHoliday = Boolean.parseBoolean(taskService.getVariable(task.getId(), "isHoliday").toString());
		Assert.assertTrue(isHoliday);
	}

	@Test
	@Deployment(resources = { "invokeRestService.bpmn" })
	public void shouldPackForWork() {
		Map<String, Object> variables = new HashMap<String, Object>();
		variables.put("date", "2014-01-02");

		RuntimeService runtimeService = processEngineRule.getRuntimeService();
		TaskService taskService = processEngineRule.getTaskService();

		runtimeService.startProcessInstanceByKey("http_connector", variables);

		Task task = taskService.createTaskQuery().singleResult();
		Assert.assertNotNull(task);
		assertEquals("Pack for work", task.getName());

		boolean isHoliday = Boolean.parseBoolean(taskService.getVariable(task.getId(), "isHoliday").toString());
		Assert.assertFalse(isHoliday);
	}

}
