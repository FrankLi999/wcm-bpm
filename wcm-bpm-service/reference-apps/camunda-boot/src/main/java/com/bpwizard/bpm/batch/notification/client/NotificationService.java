package com.bpwizard.bpm.batch.notification.client;

import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.camunda.bpm.engine.impl.interceptor.CommandExecutor;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.databind.ObjectMapper;

//@Service
public class NotificationService {

	private ObjectMapper mapper = new ObjectMapper();
//	@Autowired
//	private ProcessEngineConfigurationImpl config;

	@Autowired
	private ProcessEngine engine;

	
//	public NotificationService(final ObjectMapper mapper, final ProcessEngineConfigurationImpl config) {
//		this.mapper = mapper;
//		this.config = config;
//	}

	public void sendNotificationInaAJob(String recipient, String subject, String body) {
		final CommandExecutor executor = ((ProcessEngineConfigurationImpl)engine.getProcessEngineConfiguration())
				.getCommandExecutorTxRequired();
		executor.execute(new SendNotificationCommand(mapper, recipient, subject, body));
	}
}
