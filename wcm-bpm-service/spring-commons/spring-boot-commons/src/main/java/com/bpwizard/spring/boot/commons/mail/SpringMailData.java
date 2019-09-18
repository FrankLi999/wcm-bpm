package com.bpwizard.spring.boot.commons.mail;

import lombok.Getter;
import lombok.Setter;

/**
 * Data needed for sending a mail.
 * Override this if you need more data to be sent.
 */
@Getter @Setter
public class SpringMailData {
	
	private String to;
	private String subject;
	private String body;

	public static SpringMailData of(String to, String subject, String body) {
		
		SpringMailData data = new SpringMailData();
		
		data.to = to;
		data.subject = subject;
		data.body = body;

		return data;
	}
}
