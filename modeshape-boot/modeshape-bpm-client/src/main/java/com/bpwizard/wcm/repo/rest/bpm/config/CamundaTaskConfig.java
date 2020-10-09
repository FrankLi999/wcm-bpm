package com.bpwizard.wcm.repo.rest.bpm.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.bpwizard.wcm.repo.rest.bpm.service.ContentReviewTaskEndListener;
import com.bpwizard.wcm.repo.rest.bpm.service.ContentReviewTaskStartListener;
import com.bpwizard.wcm.repo.rest.bpm.service.PublishContentItemDelegate;

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
