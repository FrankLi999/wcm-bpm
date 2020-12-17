package com.bpwizard.bpm.demo.config;

import static org.camunda.bpm.engine.variable.Variables.createVariables;
import static org.camunda.bpm.engine.variable.Variables.fileValue;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Calendar;

import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.authorization.Groups;
import org.camunda.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.camunda.bpm.engine.impl.util.ClockUtil;
import org.camunda.bpm.engine.impl.util.IoUtil;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.engine.repository.ProcessDefinitionQuery;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.camunda.bpm.spring.boot.starter.event.PreUndeployEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.ResourceLoader;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.bpwizard.spring.boot.commons.service.domain.Role;
import com.bpwizard.spring.boot.commons.service.domain.RoleService;
import com.bpwizard.spring.boot.commons.service.domain.User;
import com.bpwizard.spring.boot.commons.service.domain.UserService;

@Component
@Order(20)
public class InvoiceDemoStartup implements ApplicationListener<ApplicationReadyEvent> {

	private static final Logger logger = LoggerFactory.getLogger(InvoiceDemoStartup.class);

	@Autowired
	ProcessEngine processEngine;

	@Autowired
	protected RoleService<Role, Long> roleService;

	@Autowired
	protected UserService<User<Long>, Long> userService;

	@Autowired
	protected PasswordEncoder passwordEncoder;

	@Autowired
	ResourceLoader resourceLoader;

	@EventListener
	public void onPreUndeploy(PreUndeployEvent event) {
		System.out.println("Will undeploy " + event);
	}

	/**
	 * This event is executed as late as conceivably possible to indicate that the
	 * application is ready to service requests.
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, timeout = 60)
	public void onApplicationEvent(final ApplicationReadyEvent event) {
		logger.debug("Entry");
		boolean created = createUsers(processEngine);
		if (!created) {
			return;
		}
		try {
			// enable metric reporting
			ProcessEngineConfigurationImpl processEngineConfiguration = (ProcessEngineConfigurationImpl) processEngine
					.getProcessEngineConfiguration();
			processEngineConfiguration.setDbMetricsReporterActivate(true);
			processEngineConfiguration.getDbMetricsReporter().setReporterId("REPORTER");

			startProcessInstances(processEngine, "invoice", 1);
			startProcessInstances(processEngine, "invoice", null);

			// for (int i = 0; i < 1000; i++) {
			// startProcessInstances(processEngine, "invoice", null);
			// }

			// disable reporting
			processEngineConfiguration.setDbMetricsReporterActivate(false);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		logger.debug("Exit");
	}

	// public void createDeployment(String processArchiveName, DeploymentBuilder
	// deploymentBuilder) throws IOException {
//				public void createDeployment() throws IOException {
//					ProcessEngine processEngine = BpmPlatform.getProcessEngineService().getProcessEngine("default");
	//
//					// Hack: deploy the first version of the invoice process once before the process
//					// application
//					// is deployed the first time
//					if (processEngine != null) {
	//
//						RepositoryService repositoryService = processEngine.getRepositoryService();
	//
//						if (!isProcessDeployed(repositoryService, "invoice")) {
//							// repositoryService.createDeployment(this.getReference())
//							repositoryService.createDeployment()
//									.addInputStream("invoice.v1.bpmn",
//											resourceLoader.getResource("classpath:/bpmn/invoice.v1.bpmn").getInputStream())
//									.addInputStream("invoiceBusinessDecisions.dmn",
//											resourceLoader.getResource("classpath:/dmn/invoiceBusinessDecisions.dmn")
//													.getInputStream())
//									.addInputStream("reviewInvoice.bpmn",
//											resourceLoader.getResource("classpath:/reviewInvoice.bpmn").getInputStream())
//									.deploy();
//						}
//					}
//				}

	protected boolean isProcessDeployed(RepositoryService repositoryService, String key) {
		return repositoryService.createProcessDefinitionQuery().processDefinitionKey("invoice").count() > 0;
	}

	private void startProcessInstances(ProcessEngine processEngine, String processDefinitionKey, Integer version)
			throws IOException {

		ProcessEngineConfigurationImpl processEngineConfiguration = (ProcessEngineConfigurationImpl) processEngine
				.getProcessEngineConfiguration();
		ProcessDefinitionQuery processDefinitionQuery = processEngine.getRepositoryService()
				.createProcessDefinitionQuery().processDefinitionKey(processDefinitionKey);

		if (version != null) {
			processDefinitionQuery.processDefinitionVersion(version);
		} else {
			processDefinitionQuery.latestVersion();
		}

		ProcessDefinition processDefinition = processDefinitionQuery.singleResult();

//					InputStream invoiceInputStream = InvoiceProcessApplication.class.getClassLoader()
//							.getResourceAsStream("invoice.pdf");

		InputStream invoiceInputStream = this.resourceLoader.getResource("classpath:/files/invoice.pdf")
				.getInputStream();
		long numberOfRunningProcessInstances = processEngine.getRuntimeService().createProcessInstanceQuery()
				.processDefinitionId(processDefinition.getId()).count();

		if (numberOfRunningProcessInstances == 0) { // start three process instances

			logger.info("Start 3 instances of " + processDefinition.getName() + ", version "
					+ processDefinition.getVersion());
			// process instance 1
			processEngine.getRuntimeService().startProcessInstanceById(processDefinition.getId(), createVariables()
					.putValue("creditor", "Great Pizza for Everyone Inc.").putValue("amount", 30.00d)
					.putValue("invoiceCategory", "Travel Expenses").putValue("invoiceNumber", "GPFE-23232323")
					.putValue("invoiceDocument",
							fileValue("invoice.pdf").file(invoiceInputStream).mimeType("application/pdf").create()));

			IoUtil.closeSilently(invoiceInputStream);
			// invoiceInputStream =
			// InvoiceProcessApplication.class.getClassLoader().getResourceAsStream("invoice.pdf");
			invoiceInputStream = this.resourceLoader.getResource("classpath:/files/invoice.pdf").getInputStream();
			processEngineConfiguration.getDbMetricsReporter().reportNow();

			// process instance 2
			try {
				Calendar calendar = Calendar.getInstance();
				calendar.add(Calendar.DAY_OF_MONTH, -14);
				ClockUtil.setCurrentTime(calendar.getTime());

				ProcessInstance pi = processEngine.getRuntimeService().startProcessInstanceById(
						processDefinition.getId(),
						createVariables().putValue("creditor", "Bobby's Office Supplies").putValue("amount", 900.00d)
								.putValue("invoiceCategory", "Misc").putValue("invoiceNumber", "BOS-43934")
								.putValue("invoiceDocument", fileValue("invoice.pdf").file(invoiceInputStream)
										.mimeType("application/pdf").create()));

				processEngineConfiguration.getDbMetricsReporter().reportNow();
				calendar.add(Calendar.DAY_OF_MONTH, 14);
				ClockUtil.setCurrentTime(calendar.getTime());

				processEngine.getIdentityService().setAuthentication("demo@example.com",
						Arrays.asList(Groups.CAMUNDA_ADMIN));
				Task task = processEngine.getTaskService().createTaskQuery().processInstanceId(pi.getId())
						.singleResult();
				processEngine.getTaskService().claim(task.getId(), "demo@example.com");
				processEngine.getTaskService().complete(task.getId(), createVariables().putValue("approved", true));
			} finally {
				processEngineConfiguration.getDbMetricsReporter().reportNow();
				ClockUtil.reset();
				processEngine.getIdentityService().clearAuthentication();
			}

			IoUtil.closeSilently(invoiceInputStream);
			// invoiceInputStream =
			// InvoiceProcessApplication.class.getClassLoader().getResourceAsStream("invoice.pdf");
			invoiceInputStream = this.resourceLoader.getResource("classpath:/files/invoice.pdf").getInputStream();

			// process instance 3
			try {
				Calendar calendar = Calendar.getInstance();
				calendar.add(Calendar.DAY_OF_MONTH, -5);
				ClockUtil.setCurrentTime(calendar.getTime());

				ProcessInstance pi = processEngine.getRuntimeService().startProcessInstanceById(
						processDefinition.getId(),
						createVariables().putValue("creditor", "Papa Steve's all you can eat")
								.putValue("amount", 10.99d).putValue("invoiceCategory", "Travel Expenses")
								.putValue("invoiceNumber", "PSACE-5342")
								.putValue("invoiceDocument", fileValue("invoice.pdf").file(invoiceInputStream)
										.mimeType("application/pdf").create()));

				processEngineConfiguration.getDbMetricsReporter().reportNow();
				calendar.add(Calendar.DAY_OF_MONTH, 5);
				ClockUtil.setCurrentTime(calendar.getTime());

//					processEngine.getIdentityService().setAuthenticatedUserId("mary@example.com");
//					Task task = processEngine.getTaskService().createTaskQuery().processInstanceId(pi.getId())
//							.singleResult();
//					processEngine.getTaskService().createComment(null, pi.getId(),
//							"I cannot approve this invoice: the amount is missing.\n\n Could you please provide the amount?");
//					processEngine.getTaskService().complete(task.getId(), createVariables().putValue("approved", false));

				processEngine.getIdentityService().setAuthentication("mary@example.com",
						Arrays.asList(Groups.CAMUNDA_ADMIN));
				Task task = processEngine.getTaskService().createTaskQuery().processInstanceId(pi.getId())
						.singleResult();
				processEngine.getTaskService().claim(task.getId(), "mary@example.com");
				processEngine.getTaskService().createComment(null, pi.getId(),
						"I cannot approve this invoice: the amount is missing.\n\n Could you please provide the amount?");
				processEngine.getTaskService().complete(task.getId(), createVariables().putValue("approved", false));
			} finally {
				processEngineConfiguration.getDbMetricsReporter().reportNow();
				ClockUtil.reset();
				processEngine.getIdentityService().clearAuthentication();
			}
		} else {
			logger.info(
					"No new instances of " + processDefinition.getName() + " version " + processDefinition.getVersion()
							+ " started, there are " + numberOfRunningProcessInstances + " instances running");
		}
	}

	private boolean createUsers(ProcessEngine processEngine) {
		// create demo users
		return new DemoDataGenerator(roleService, userService, passwordEncoder).createUsers(processEngine);
	}
}