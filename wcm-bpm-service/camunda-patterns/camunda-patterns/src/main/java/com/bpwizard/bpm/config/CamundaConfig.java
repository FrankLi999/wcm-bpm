package com.bpwizard.bpm.config;

import org.camunda.bpm.spring.boot.starter.annotation.EnableProcessApplication;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableProcessApplication
public class CamundaConfig {

//	@Autowired
//	private RuntimeService runtimeService;
//
//	@Autowired
//	ProcessEngine processEngine;
	
//	@EventListener
//	private void processPostDeploy(PostDeployEvent event) {
//		// ProcessInstance pi = 
//		runtimeService.startProcessInstanceByKey("business_rule");
//	}
//	
//	@EventListener
//	private void startCaseInstance(PostDeployEvent event) {
//		CaseService caseService = processEngine.getCaseService();
//		caseService.createCaseInstanceByKey("loan_application",
//				Variables.createVariables().putValue("applicationSufficient", Variables.booleanValue(null))
//						.putValue("rating", Variables.integerValue(null)));
//
//	}

//	@SuppressWarnings("unused")
//	@EventListener
//	private void evaluateDecisionTable(PostDeployEvent event) {
//		DecisionService decisionService = processEngine.getDecisionService();
//		VariableMap variables = Variables.createVariables().putValue("season", "Spring").putValue("guestCount", 10)
//				.putValue("guestsWithChildren", false);
//
//		DmnDecisionTableResult dishDecisionResult = decisionService.evaluateDecisionTableByKey("dish", variables);
//		String desiredDish = dishDecisionResult.getSingleEntry();
//
//		DmnDecisionTableResult beveragesDecisionResult = decisionService.evaluateDecisionTableByKey("beverages",
//				variables);
//		List<Object> beverages = beveragesDecisionResult.collectEntries("beverages");
//	}
	
//	@Bean
//  public FilterRegistrationBean statelessUserAuthenticationFilter(){
//      FilterRegistrationBean filterRegistration = new FilterRegistrationBean();
//      filterRegistration.setFilter(new StatelessUserAuthenticationFilter(processEngine));
//      filterRegistration.setOrder(102); // make sure the filter is registered after the Spring Security Filter Chain
//      filterRegistration.addUrlPatterns("/camunda/api/engine/*");
//      return filterRegistration;
//  }
}
