package com.bpwizard.bpm.wcm.config;

import org.springframework.context.annotation.Bean;
import org.springframework.mail.SimpleMailMessage;

public class MailConfig {
	@Bean(name="workflowTMessage")
	public SimpleMailMessage workflowTMessage() {
	    SimpleMailMessage message = new SimpleMailMessage();
	    message.setText(
	      "This is the test email template for your email:\n%s\n");
	    return message;
	}
}
