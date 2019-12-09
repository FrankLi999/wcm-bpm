package com.bpwizard.wcm.repo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.SimpleMailMessage;

@Configuration
public class MailConfig {
	@Bean
	public SimpleMailMessage templateSimpleMessage() {
	    SimpleMailMessage message = new SimpleMailMessage();
	    message.setText(
	      "This is the test email template for your email:\n%s\n");
	    return message;
	}
//	@Bean
//	public JavaMailSender javaMailSender() {
//	    JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
//	    mailSender.setHost("smtp.gmail.com");
//	    mailSender.setPort(587);
//	     
//	    mailSender.setUsername("username@gmail.com");
//	    mailSender.setPassword("password");
//	    
//	    mailSender.setHost("localhost");
//	    mailSender.setPort(2525);
//	     
//	    mailSender.setUsername("username@gmail.com");
//	    mailSender.setPassword("password");
//	    Properties props = mailSender.getJavaMailProperties();
//	    props.put("mail.transport.protocol", "smtp");
//	    props.put("mail.smtp.auth", "true");
//	    props.put("mail.smtp.starttls.enable", "true");
	   
	    
//	    props.put("mail.debug", "true");
//	     
//	    return mailSender;
//	}
}
