package com.bpwizard.bpm.wcm.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.bpwizard.bpm.wcm.service.ContentReviewTaskEndListener;
import com.bpwizard.bpm.wcm.service.ContentReviewTaskStartListener;
import com.bpwizard.bpm.wcm.service.PublishContentItemDelegate;

@Configuration
public class CamundaTaskConfig {
	@Bean
	public PublishContentItemDelegate publishContentItemDelegate() {
		return new PublishContentItemDelegate();
	}
	
	@Bean
	public ContentReviewTaskStartListener contentReviewTaskStartListener() {
		return new ContentReviewTaskStartListener();
	}

	@Bean
	public ContentReviewTaskEndListener contentReviewTaskEndListener() {
		return new ContentReviewTaskEndListener();
	}
}
