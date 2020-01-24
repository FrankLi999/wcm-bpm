package com.bpwizard.spring.boot.commons.mail;

/**
 * The mail sender interface for sending mail
 */
public interface MailSender<MailData> {

	void send(MailData mail);
}