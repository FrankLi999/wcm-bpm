package com.bpwizard.spring.boot.commons.mail;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * A mock mail sender for 
 * writing the mails to the log.
 * 
 * @author Sanjay Patel
 */
public class MockMailSender implements MailSender<SpringMailData> {
	
	private static final Logger log = LogManager.getLogger(MockMailSender.class);
	
	public MockMailSender() {
		log.info("Created");
	}

	@Override
	public void send(SpringMailData mail) {
		
		log.info("Sending mail to " + mail.getTo());
		log.info("Subject: " + mail.getSubject());
		log.info("Body: " + mail.getBody());
	}

}
