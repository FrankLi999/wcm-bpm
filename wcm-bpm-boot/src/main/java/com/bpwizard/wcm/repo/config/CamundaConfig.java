package com.bpwizard.wcm.repo.config;

import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.spring.boot.starter.annotation.EnableProcessApplication;
import org.camunda.wcm.repo.bpm.rest.filter.StatelessUserAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.bpwizard.wcm.repo.content.ContentReviewTaskEndListener;
import com.bpwizard.wcm.repo.content.ContentReviewTaskStartListener;
import com.bpwizard.wcm.repo.content.PublishContentItemDelegate;

@Configuration
@EnableProcessApplication("wcm-boot")
@ComponentScan(basePackages={" org.camunda.bpm.engine.rest"})
public class CamundaConfig {
//	 @Autowired
//	 private RuntimeService runtimeService;

	@Autowired
	ProcessEngine processEngine;
	
//	@EventListener
//	private void processPostDeployLoadApproval(PostDeployEvent event) {
//		runtimeService.startProcessInstanceByKey("loanApproval");
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
//
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
	
	@Bean
	public FilterRegistrationBean<StatelessUserAuthenticationFilter> statelessUserAuthenticationFilter(){
		FilterRegistrationBean<StatelessUserAuthenticationFilter> filterRegistration = new FilterRegistrationBean<>();
		filterRegistration.setFilter(new StatelessUserAuthenticationFilter(processEngine));
		filterRegistration.setOrder(102); // make sure the filter is registered after the Spring Security Filter Chain
		filterRegistration.addUrlPatterns("/camunda/api/engine/*");
		filterRegistration.addUrlPatterns("/rest/*");
		filterRegistration.addUrlPatterns("/content/server/*");
		return filterRegistration;
	}
	
	@Bean
	public PublishContentItemDelegate publishContentItemDelegate() {
		return new PublishContentItemDelegate();
	}
	
	@Bean
	public ContentReviewTaskStartListener contentReviewTaskStartListener() {
		return new ContentReviewTaskStartListener();
	}

	@Bean
	public ContentReviewTaskEndListener contentReviewTaskEndListener() {
		return new ContentReviewTaskEndListener();
	}
}
