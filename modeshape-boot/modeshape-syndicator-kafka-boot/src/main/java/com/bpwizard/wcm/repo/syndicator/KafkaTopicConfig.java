package com.bpwizard.wcm.repo.syndicator;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {
	@Value("${bpw.wcm.kafka.topic.wcm-event:wcm-event}")
	private String wcmEventTopic;

	@Bean
	NewTopic wcmEventTopic() {
		return TopicBuilder.name(wcmEventTopic).build();
	}
}
