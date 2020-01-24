package com.bpwizard.bpm.config;


import static org.camunda.bpm.engine.variable.Variables.createVariables;
import static org.camunda.bpm.engine.variable.Variables.fileValue;

import java.io.InputStream;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;

import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.authorization.Groups;
import org.camunda.bpm.engine.impl.cfg.ProcessEnginePlugin;
import org.camunda.bpm.engine.impl.util.ClockUtil;
import org.camunda.bpm.engine.impl.util.IoUtil;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.engine.repository.ProcessDefinitionQuery;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.camunda.bpm.extension.batch.plugin.CustomBatchHandlerPlugin;
import org.camunda.bpm.spring.boot.starter.annotation.EnableProcessApplication;
import org.camunda.bpm.spring.boot.starter.event.PostDeployEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

import com.bpwizard.bpm.batch.handler.PrintStringBatchJobHandler;
import com.bpwizard.bpm.batch.notification.client.NotificationService;
import com.bpwizard.bpm.content.ContentReviewTaskEndListener;
import com.bpwizard.bpm.content.ContentReviewTaskStartListener;
import com.bpwizard.bpm.content.PublishContentItemDelegate;
import com.bpwizard.bpm.content.SaveDraftDelegate;
import com.bpwizard.bpm.content.TestEventDelegate;

@Configuration
@EnableProcessApplication
public class CamundaConfig {
	private static final Logger logger = LoggerFactory.getLogger(CamundaConfig.class);
	@Autowired
	ProcessEngine processEngine;

//	@Autowired
//	private PrintStringBatchJobHandler printStringBatchJobHandler;

	@EventListener
	private void processPostDeploy(PostDeployEvent event) {
		// this.startFirstProcess();
	}

	@Bean
	public ProcessEnginePlugin customBatchHandlerPlugin(PrintStringBatchJobHandler printStringBatchJobHandler) {
		return new CustomBatchHandlerPlugin(Collections.singletonList(printStringBatchJobHandler));
	}
	
	@Bean
	public PublishContentItemDelegate publishContentItemDelegate() {
		return new PublishContentItemDelegate();
	}
	
	@Bean
	public SaveDraftDelegate saveDraftDelegate() {
		return new SaveDraftDelegate();
	}
	
	@Bean
	public TestEventDelegate testEventDelegate() {
		return new TestEventDelegate();
	}
	
	@Bean
	public ContentReviewTaskStartListener contentReviewTaskStartListener() {
		return new ContentReviewTaskStartListener();
	}

	@Bean
	public ContentReviewTaskEndListener contentReviewTaskEndListener() {
		return new ContentReviewTaskEndListener();
	}
	
	public void startFirstProcess() {
		createUsers();
		startProcessInstances("invoice", 1);
		startProcessInstances("invoice", null);
		for (int i = 0; i < 1000; i++) {
			startProcessInstances("invoice", null);
		}
	}

	private void createUsers() {
		// create demo users
		new DemoDataGenerator().createUsers(this.processEngine);
	}

	private void startProcessInstances(String processDefinitionKey, Integer version) {

		ProcessDefinitionQuery processDefinitionQuery = processEngine.getRepositoryService()
				.createProcessDefinitionQuery().processDefinitionKey(processDefinitionKey);

		if (version != null) {
			processDefinitionQuery.processDefinitionVersion(version);
		} else {
			processDefinitionQuery.latestVersion();
		}

		ProcessDefinition processDefinition = processDefinitionQuery.singleResult();

		InputStream invoiceInputStream = Thread.currentThread().getContextClassLoader()
				.getResourceAsStream("files/invoice.pdf");

		// process instance 1
		processEngine.getRuntimeService().startProcessInstanceById(processDefinition.getId(), createVariables()
				.putValue("creditor", "Great Pizza for Everyone Inc.").putValue("amount", 30.00d)
				.putValue("invoiceCategory", "Travel Expenses").putValue("invoiceNumber", "GPFE-23232323")
				.putValue("invoiceDocument",
						fileValue("invoice.pdf").file(invoiceInputStream).mimeType("application/pdf")
						.create()));

		IoUtil.closeSilently(invoiceInputStream);
		invoiceInputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("invoice.pdf");

		// process instance 2
		try {
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.DAY_OF_MONTH, -14);
			ClockUtil.setCurrentTime(calendar.getTime());

			ProcessInstance pi = processEngine.getRuntimeService().startProcessInstanceById(processDefinition.getId(),
					createVariables().putValue("creditor", "Bobby's Office Supplies").putValue("amount", 900.00d)
							.putValue("invoiceCategory", "Misc").putValue("invoiceNumber", "BOS-43934")
							.putValue("invoiceDocument", fileValue("invoice.pdf").file(invoiceInputStream)
									.mimeType("application/pdf").create()));

			calendar.add(Calendar.DAY_OF_MONTH, 14);
			ClockUtil.setCurrentTime(calendar.getTime());

			processEngine.getIdentityService().setAuthentication("demo", Arrays.asList(Groups.CAMUNDA_ADMIN));
			Task task = processEngine.getTaskService().createTaskQuery().processInstanceId(pi.getId()).singleResult();
			processEngine.getTaskService().claim(task.getId(), "demo");
			processEngine.getTaskService().complete(task.getId(), createVariables().putValue("approved", true));
		} finally {
			ClockUtil.reset();
			processEngine.getIdentityService().clearAuthentication();
		}

		IoUtil.closeSilently(invoiceInputStream);
		invoiceInputStream =Thread.currentThread().getContextClassLoader().getResourceAsStream("files/invoice.pdf");

		// process instance 3
		try {
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.DAY_OF_MONTH, -5);
			ClockUtil.setCurrentTime(calendar.getTime());

			ProcessInstance pi = processEngine.getRuntimeService().startProcessInstanceById(processDefinition.getId(),
					createVariables().putValue("creditor", "Papa Steve's all you can eat").putValue("amount", 10.99d)
							.putValue("invoiceCategory", "Travel Expenses").putValue("invoiceNumber", "PSACE-5342")
							.putValue("invoiceDocument", fileValue("invoice.pdf").file(invoiceInputStream)
									.mimeType("application/pdf").create()));

			calendar.add(Calendar.DAY_OF_MONTH, 5);
			ClockUtil.setCurrentTime(calendar.getTime());

			processEngine.getIdentityService().setAuthenticatedUserId("mary");
			Task task = processEngine.getTaskService().createTaskQuery().processInstanceId(pi.getId()).singleResult();
			processEngine.getTaskService().createComment(null, pi.getId(),
					"I cannot approve this invoice: the amount is missing.\n\n Could you please provide the amount?");
			processEngine.getTaskService().complete(task.getId(), createVariables().putValue("approved", false));
		} finally {
			ClockUtil.reset();
			processEngine.getIdentityService().clearAuthentication();
		}

		try {
			invoiceInputStream = Thread.currentThread().getContextClassLoader()
					.getResourceAsStream("files/invoice.pdf");

			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.DAY_OF_MONTH, -5);
			ClockUtil.setCurrentTime(calendar.getTime());

			// ProcessInstance pi = 
			processEngine.getRuntimeService().startProcessInstanceById(processDefinition.getId(),
					createVariables().putValue("creditor", "Food at the dinner").putValue("amount", 55.99d)
							.putValue("invoiceCategory", "Travel Expenses").putValue("invoiceNumber", "PSACE-9876")
							.putValue("invoiceDocument", fileValue("invoice.pdf").file(invoiceInputStream)
									.mimeType("application/pdf").create()));

			calendar.add(Calendar.DAY_OF_MONTH, 5);
			ClockUtil.setCurrentTime(calendar.getTime());

			processEngine.getIdentityService().setAuthenticatedUserId("mary");
		} finally {
			ClockUtil.reset();
			processEngine.getIdentityService().clearAuthentication();
		}
	}
	
	@Bean
	public NotificationService NotificationService() {
		return new NotificationService();
	}
	
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

//	public void createDeployment(String processArchiveName, DeploymentBuilder deploymentBuilder) {
//		ProcessEngine processEngine = BpmPlatform.getProcessEngineService().getProcessEngine("default");
//
//		// Hack: deploy the first version of the invoice process once before the process
//		// application
//		// is deployed the first time
//		if (processEngine != null) {
//
//			RepositoryService repositoryService = processEngine.getRepositoryService();
//
//			if (!isProcessDeployed(repositoryService, "invoice")) {
//				ClassLoader classLoader = getProcessApplicationClassloader();
//
//				repositoryService.createDeployment(this.getReference())
//						.addInputStream("invoice.v1.bpmn", classLoader.getResourceAsStream("invoice.v1.bpmn"))
//						.addInputStream("assign-approver-groups.dmn",
//								classLoader.getResourceAsStream("assign-approver-groups.dmn"))
//						.deploy();
//			}
//		}
//	}
//
//	protected boolean isProcessDeployed(RepositoryService repositoryService, String key) {
//		return repositoryService.createProcessDefinitionQuery().processDefinitionKey("invoice").count() > 0;
//	}
}
