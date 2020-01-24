package com.bpwizard.bpm.config;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.camunda.bpm.extension.batch.CustomBatchBuilder;
import org.camunda.bpm.spring.boot.starter.event.PostDeployEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.bpwizard.bpm.assignment.MyAssignmentHandler;
import com.bpwizard.bpm.batch.handler.PrintStringBatchJobHandler;
import com.bpwizard.bpm.batch.notification.client.NotificationService;

@EnableScheduling
@Configuration
public class BatchConfig {
	private static final Logger logger = LoggerFactory.getLogger(BatchConfig.class);
	@Autowired
	@Lazy
	private NotificationService notificationService;

	@Scheduled(initialDelay = 5000, fixedDelay = 600000)
	public void send() {
		notificationService.sendNotificationInaAJob("kermit@muppetshow.biz", "Miss Piggy is lost!", "Find her");
	}
	
	@EventListener
	public void afterEngineStarted(PostDeployEvent event) {
		logger.info("Create new Batch");
		final List<String> simpleStringList = IntStream.range(0, 10)
				.mapToObj(i -> "SomeRandomBatchData_" + UUID.randomUUID()).collect(Collectors.toList());

		CustomBatchBuilder.of(simpleStringList)
			.configuration(event.getProcessEngine().getProcessEngineConfiguration())
			.exclusive(true)
		    .jobHandler(simpleCustomBatchJobHandler())
		    .create();	
		    // .jobHandler(printStringBatchJobHandler).create();
	}
	
	@Bean
	public PrintStringBatchJobHandler simpleCustomBatchJobHandler() {
		return new PrintStringBatchJobHandler();
	}
	
	@Bean
	public MyAssignmentHandler myAssignmentHandler() {
		return new MyAssignmentHandler();
		
	}
}
