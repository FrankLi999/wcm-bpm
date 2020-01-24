package com.bpwizard.bpm.content.conf;

import org.camunda.bpm.engine.impl.history.handler.HistoryEventHandler;
import org.camunda.bpm.extension.process_test_coverage.listeners.FlowNodeHistoryEventHandler;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.TestPropertySource;

@ComponentScan(basePackages = { "com.bpwizard.bpm.content" })
@TestConfiguration
@TestPropertySource(locations = { "classpath:test.properties" })
public class TestApplication {

	@Bean
	public HistoryEventHandler customHistoryEventHandler() {
		return new FlowNodeHistoryEventHandler();
	}
}
