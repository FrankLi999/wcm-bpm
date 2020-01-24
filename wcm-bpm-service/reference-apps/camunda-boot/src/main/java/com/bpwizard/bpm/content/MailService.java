package com.bpwizard.bpm.content;

import java.io.IOException;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

// @Service
public class MailService {
//	
//	@Autowired
	private JavaMailSender javaMailSender;

	// @Autowired
	private SimpleMailMessage templateSimpleMessage;
	public void sendEmail(String subject, String[] recipient, String message) {

        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(recipient);

        msg.setSubject("Testing from Spring Boot");
        msg.setSubject(subject);
        String text = String.format(templateSimpleMessage.getText(), message);  
        msg.setText(text);

        javaMailSender.send(msg);

    }

    public void sendEmailWithAttachment(String subject, String[] recipient, String message, String attachment) throws MessagingException, IOException {

        MimeMessage msg = javaMailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(msg, true);
        helper.setTo(recipient);
        helper.setSubject(subject);
        String text = String.format(templateSimpleMessage.getText(), message); 
        helper.setText(text, true);
         helper.addAttachment("my_photo.png", new ClassPathResource("android.png"));
        helper.addAttachment(attachment, new ClassPathResource(attachment));
        javaMailSender.send(msg);
    }
    
    
}
