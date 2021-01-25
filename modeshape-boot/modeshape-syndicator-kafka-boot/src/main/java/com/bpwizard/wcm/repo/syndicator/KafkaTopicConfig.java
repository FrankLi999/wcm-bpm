package com.bpwizard.wcm.repo.syndicator;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {
	@Value("${bpw.wcm.kafka.topic.jcr-wcm-event:jcr-wcm-event}")
	private String jcrWcmEventTopic;

	@Value("${bpw.wcm.kafka.topic.json-wcm-event:json-wcm-event}")
	private String jsonWcmEventTopic;
	
	@Bean
	NewTopic jcrWcmEventTopic() {
		return TopicBuilder.name(jcrWcmEventTopic).build();
	}
	
	@Bean
	NewTopic jsonWcmEventTopic() {
		return TopicBuilder.name(jsonWcmEventTopic).build();
	}

}
