package com.bpwizard.bpm.content;

import static org.camunda.bpm.engine.test.assertions.bpmn.BpmnAwareTests.assertThat;

import javax.annotation.PostConstruct;

import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.test.Deployment;
import org.camunda.bpm.engine.test.ProcessEngineRule;
import org.junit.ClassRule;
import org.junit.Rule;
//import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.bpwizard.bpm.content.helper.BaseProcessTestCase;

public class ProcessTest extends BaseProcessTestCase {

    private static final String TEST_PROCESS_KEY = "testDemoProcess";

	@Autowired
	public ProcessEngine processEngine;
    
	@Rule
	@ClassRule
	public static ProcessEngineRule processEngineRule;
	
	@PostConstruct
	void initRule() {
		//// With Coverage: processEngineRule = TestCoverageProcessEngineRuleBuilder.create(processEngine).build();
		processEngineRule = new ProcessEngineRule(processEngine);
	}
	
    @Test
    @Deployment(resources = "bpmn/testDemoProcess.bpmn")
    public void startTestProcess() throws InterruptedException {
//	    List<String> groups = new ArrayList<>();
//	    groups.add("wcm-admins1");
//	    groups.add("wcm-authors1");
//	    IdentityService identityService = processEngineRule.getIdentityService();
//	    System.out.println(identityService);
//	    identityService.setAuthentication("demo1", groups);
	    
    	RuntimeService runtimeService = processEngineRule.getRuntimeService();
	    ProcessInstance pi = runtimeService.startProcessInstanceByKey(TEST_PROCESS_KEY);
	    assertThat(pi).isNotNull();
        runtimeService.deleteProcessInstance(pi.getId(), "JUnit test");
//      identityService.clearAuthentication();
    }
}