package com.bpwizard.bpm;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;

import javax.annotation.PostConstruct;

import org.camunda.bpm.dmn.engine.DmnDecisionRuleResult;
import org.camunda.bpm.dmn.engine.DmnDecisionTableResult;
import org.camunda.bpm.engine.DecisionService;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.test.Deployment;
import org.camunda.bpm.engine.test.ProcessEngineRule;
import org.camunda.bpm.engine.variable.VariableMap;
import org.camunda.bpm.engine.variable.Variables;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.bpwizard.bpm.helper.BaseProcessTestCase;

public class DmnTest extends BaseProcessTestCase {

	private static final String DECISION_KEY = "orderDecision";

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
	@Deployment(resources = "dmn/Example.dmn")
	public void startTestProcess() throws InterruptedException {
		DecisionService decisionService = processEngineRule.getDecisionService();
		// Set input variables
		VariableMap variables = Variables.createVariables().putValue("status", "silver").putValue("sum", 9000);

		// Evaluate decision with id 'orderDecision' from file 'Example.dmn'
		DmnDecisionTableResult results = decisionService.evaluateDecisionTableByKey(DECISION_KEY, variables);

		// Check that one rule has matched
		assertThat(results).hasSize(1);
		
		DmnDecisionRuleResult result = results.getSingleResult();
	    assertThat(result)
	      .containsOnly(
	        entry("result", "notok"),
	        entry("reason", "you took too much man, you took too much!")
	      );

	    // Change input variables
	    variables.putValue("status", "gold");

	    // Evaluate decision again
	    results = decisionService.evaluateDecisionTableByKey(DECISION_KEY, variables);

	    // Check new result
	    assertThat(results).hasSize(1);

	    result = results.getSingleResult();
	    assertThat(result)
	      .containsOnly(
	        entry("result", "ok"),
	        entry("reason", "you get anything you want")
	      );

	}
}