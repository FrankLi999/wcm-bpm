package com.bpwizard.spring.boot.commons.mail;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A mock mail sender for 
 * writing the mails to the log.
 */
public class MockMailSender implements MailSender<SpringMailData> {
	
	private static final Logger logger = LoggerFactory.getLogger(MockMailSender.class);
	
	public MockMailSender() {
		logger.info("Created");
	}

	@Override
	public void send(SpringMailData mail) {
		
		logger.info("Sending mail to " + mail.getTo());
		logger.info("Subject: " + mail.getSubject());
		logger.info("Body: " + mail.getBody());
	}

}
