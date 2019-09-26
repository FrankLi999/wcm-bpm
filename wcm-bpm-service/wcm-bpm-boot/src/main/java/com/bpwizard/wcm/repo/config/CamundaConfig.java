package com.bpwizard.wcm.repo.config;

import java.util.List;

import org.camunda.bpm.dmn.engine.DmnDecisionTableResult;
import org.camunda.bpm.engine.CaseService;
import org.camunda.bpm.engine.DecisionService;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.variable.VariableMap;
import org.camunda.bpm.engine.variable.Variables;
import org.camunda.bpm.spring.boot.starter.event.PostDeployEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

@Configuration
public class CamundaConfig {
	 @Autowired
	 private RuntimeService runtimeService;

	@Autowired
	ProcessEngine processEngine;
	
	@EventListener
	private void processPostDeployLoadApproval(PostDeployEvent event) {
		runtimeService.startProcessInstanceByKey("loanApproval");
	}

	@EventListener
	private void startCaseInstance(PostDeployEvent event) {
		CaseService caseService = processEngine.getCaseService();
		caseService.createCaseInstanceByKey("loan_application",
				Variables.createVariables().putValue("applicationSufficient", Variables.booleanValue(null))
						.putValue("rating", Variables.integerValue(null)));

	}

	@EventListener
	private void evaluateDecisionTable(PostDeployEvent event) {
		DecisionService decisionService = processEngine.getDecisionService();
		VariableMap variables = Variables.createVariables().putValue("season", "Spring").putValue("guestCount", 10)
				.putValue("guestsWithChildren", false);

		DmnDecisionTableResult dishDecisionResult = decisionService.evaluateDecisionTableByKey("dish", variables);
		String desiredDish = dishDecisionResult.getSingleEntry();

		DmnDecisionTableResult beveragesDecisionResult = decisionService.evaluateDecisionTableByKey("beverages",
				variables);
		List<Object> beverages = beveragesDecisionResult.collectEntries("beverages");
	}
}
