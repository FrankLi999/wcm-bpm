package com.bpwizard.bpm.conf;

import org.camunda.bpm.engine.impl.history.handler.HistoryEventHandler;
import org.camunda.bpm.extension.process_test_coverage.listeners.FlowNodeHistoryEventHandler;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.TestPropertySource;

import com.bpwizard.bpm.CamundaProcessApplication;

@ComponentScan(basePackageClasses = { CamundaProcessApplication.class })
@TestConfiguration
@TestPropertySource(locations = { "classpath:test.properties" })
public class CamundaTestApplication {

	@Bean
	public HistoryEventHandler customHistoryEventHandler() {
		return new FlowNodeHistoryEventHandler();
	}
}
