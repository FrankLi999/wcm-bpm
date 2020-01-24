package com.camunda.demo.springboot.conf;

import static org.mockito.Mockito.mock;

import org.camunda.bpm.engine.impl.history.handler.HistoryEventHandler;
import org.camunda.bpm.extension.process_test_coverage.listeners.FlowNodeHistoryEventHandler;
import org.mockito.Mock;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.TestPropertySource;

@ComponentScan(basePackages = { "com.camunda.demo.springboot" })
@EnableAutoConfiguration(exclude = { RabbitAutoConfiguration.class })
@TestConfiguration
@TestPropertySource(locations = { "classpath:bpm-unit-test.properties" })
public class TestApplication {

	@Mock
	private RabbitTemplate rabbitTemplate = mock(RabbitTemplate.class);

	@Bean
	protected RabbitTemplate rabbitTemplate() {
		return this.rabbitTemplate;
	}

	@Bean
	public HistoryEventHandler customHistoryEventHandler() {
		return new FlowNodeHistoryEventHandler();
	}
}
