package com.bpwizard.bpm.batch;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;
import java.util.stream.Collectors;
import org.camunda.bpm.engine.impl.cfg.ProcessEnginePlugin;
import org.camunda.bpm.extension.batch.CustomBatchBuilder;
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

@Configuration
@EnableProcessApplication
public class CamundaConfig {
	private static final Logger logger = LoggerFactory.getLogger(CamundaConfig.class);
	@Autowired
	private PrintStringBatchJobHandler printStringBatchJobHandler;
	
	@Bean
	public PrintStringBatchJobHandler simpleCustomBatchJobHandler() {
		return new PrintStringBatchJobHandler();
	}

	@Bean
	public ProcessEnginePlugin customBatchHandlerPlugin(PrintStringBatchJobHandler printStringBatchJobHandler) {
		return new CustomBatchHandlerPlugin(Collections.singletonList(printStringBatchJobHandler));
	}



	@EventListener
	public void afterEngineStarted(PostDeployEvent event) {
		logger.info("Create new Batch");
		final List<String> simpleStringList = IntStream.range(0, 200)
				.mapToObj(i -> "SomeRandomBatchData_" + UUID.randomUUID()).collect(Collectors.toList());

		CustomBatchBuilder.of(simpleStringList).configuration(event.getProcessEngine().getProcessEngineConfiguration())
				.jobHandler(printStringBatchJobHandler).create();
	}
}
