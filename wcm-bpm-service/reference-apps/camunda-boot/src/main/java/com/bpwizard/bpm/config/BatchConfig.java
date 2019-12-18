package com.bpwizard.bpm.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.bpwizard.bpm.batch.notification.client.NotificationService;

// @EnableScheduling
// @Configuration
public class BatchConfig {
//	@Autowired
//	@Lazy
	private NotificationService notificationService;

	//@Scheduled(initialDelay = 5000, fixedDelay = 5000)
	public void send() {
		notificationService.sendNotificationInaAJob("kermit@muppetshow.biz", "Miss Piggy is lost!", "Find her");
	}
}
