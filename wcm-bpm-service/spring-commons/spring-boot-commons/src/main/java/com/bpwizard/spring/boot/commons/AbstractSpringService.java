package com.bpwizard.spring.boot.commons;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.oidc.StandardClaimNames;

import com.bpwizard.spring.boot.commons.SpringProperties.Admin;
import com.bpwizard.spring.boot.commons.domain.SpringUser;
import com.bpwizard.spring.boot.commons.mail.SpringMailData;
import com.bpwizard.spring.boot.commons.mail.MailSender;
import com.bpwizard.spring.boot.commons.security.BlueTokenService;
import com.bpwizard.spring.boot.commons.security.GreenTokenService;
import com.bpwizard.spring.boot.commons.util.SecurityUtils;
import com.bpwizard.spring.boot.commons.util.UserUtils;
import com.bpwizard.spring.boot.commons.exceptions.util.SpringExceptionUtils;

public abstract class AbstractSpringService
	<U extends SpringUser<ID>, ID extends Serializable> {

    private static final Log log = LogFactory.getLog(AbstractSpringService.class);
	protected PasswordEncoder passwordEncoder;
	protected SpringProperties properties;
	protected BlueTokenService blueTokenService;
	protected GreenTokenService greenTokenService;
	protected MailSender mailSender;

    /**
     * This method is called after the application is ready.
     * Needs to be public - otherwise Spring screams.
     * 
     * @param event
     */
    @EventListener
    public void afterApplicationReady(ApplicationReadyEvent event) {
    	
    	log.info("Starting up Spring Lemon ...");
    	onStartup(); // delegate to onStartup()
    	log.info("Spring Lemon started");	
    }

	protected abstract void onStartup();
	
	/**
	 * Creates the initial Admin user.
	 * Override this if needed.
	 */
    protected U createAdminUser() {
		
    	// fetch data about the user to be created
    	Admin initialAdmin = properties.getAdmin();
    	
    	log.info("Creating the first admin user: " + initialAdmin.getUsername());

    	// create the user
    	U user = newUser();
    	user.setEmail(initialAdmin.getUsername());
		user.setPassword(passwordEncoder.encode(
			properties.getAdmin().getPassword()));
		user.getRoleNames().add(UserUtils.Role.ADMIN);
		
		return user;
	}

	protected abstract U newUser();
	
	protected Map<String, Object> buildContext() {
		
		// make the context
		Map<String, Object> sharedProperties = new HashMap<String, Object>(2);
		sharedProperties.put("reCaptchaSiteKey", properties.getRecaptcha().getSitekey());
		sharedProperties.put("shared", properties.getShared());
		
		Map<String, Object> context = new HashMap<>();
		context.put("context", sharedProperties);
		
		return context;		
	}
	
	protected void initUser(U user) {
		
		log.debug("Initializing user: " + user);

		user.setPassword(passwordEncoder.encode(user.getPassword())); // encode the password
		makeUnverified(user); // make the user unverified
	}
	
	/**
	 * Makes a user unverified
	 */
	protected void makeUnverified(U user) {
		
		user.getRoleNames().add(UserUtils.Role.UNVERIFIED);
		user.setCredentialsUpdatedMillis(System.currentTimeMillis());
	}
    
	/**
	 * Sends verification mail to a unverified user.
	 */
	protected void sendVerificationMail(final U user) {
		try {
			
			log.debug("Sending verification mail to: " + user);
			
			String verificationCode = greenTokenService.createToken(GreenTokenService.VERIFY_AUDIENCE,
					user.getId().toString(), properties.getJwt().getExpirationMillis(),
					SecurityUtils.mapOf("email", user.getEmail()));

			// make the link
			String verifyLink = properties.getApplicationUrl()
				+ "/users/" + user.getId() + "/verification?code=" + verificationCode;

			// send the mail
			sendVerificationMail(user, verifyLink);

			log.debug("Verification mail to " + user.getEmail() + " queued.");
			
		} catch (Throwable e) {
			// In case of exception, just log the error and keep silent
			log.error(ExceptionUtils.getStackTrace(e));
		}
	}	
	
	/**
	 * Sends verification mail to a unverified user.
	 * Override this method if you're using a different MailData
	 */
	protected void sendVerificationMail(final U user, String verifyLink) {
		
		// send the mail
		mailSender.send(SpringMailData.of(user.getEmail(),
			SpringExceptionUtils.getMessage("com.bpwizard.spring.verifySubject"),
			SpringExceptionUtils.getMessage(
				"com.bpwizard.spring.spring.verifyEmail",	verifyLink)));
	}	
	
	/**
	 * Mails the forgot password link.
	 * 
	 * @param user
	 */
	public void mailForgotPasswordLink(U user) {
		
		log.debug("Mailing forgot password link to user: " + user);

		String forgotPasswordCode = greenTokenService.createToken(
				GreenTokenService.FORGOT_PASSWORD_AUDIENCE,
				user.getEmail(), properties.getJwt().getExpirationMillis());

		// make the link
		String forgotPasswordLink =	properties.getApplicationUrl()
			    + "/reset-password?code=" + forgotPasswordCode;
		
		mailForgotPasswordLink(user, forgotPasswordLink);
		
		log.debug("Forgot password link mail queued.");
	}

	
	/**
	 * Mails the forgot password link.
	 * 
	 * Override this method if you're using a different MailData
	 */
	public void mailForgotPasswordLink(U user, String forgotPasswordLink) {
		
		// send the mail
		mailSender.send(SpringMailData.of(user.getEmail(),
				SpringExceptionUtils.getMessage("com.bpwizard.spring.forgotPasswordSubject"),
				SpringExceptionUtils.getMessage("com.bpwizard.spring.forgotPasswordEmail",
					forgotPasswordLink)));
	}
	
	/**
	 * Extracts the email id from user attributes received from OAuth2 provider, e.g. Google
	 * 
	 */
	public String getOAuth2Email(String registrationId, Map<String, Object> attributes) {

		return (String) attributes.get(StandardClaimNames.EMAIL);
	}

	
	/**
	 * Extracts additional fields, e.g. name from user attributes received from OAuth2 provider, e.g. Google
	 * Override this if you introduce more user fields, e.g. name
	 */
	public void fillAdditionalFields(String clientId, U user, Map<String, Object> attributes) {
		
	}

	
	/**
	 * Checks if the account at the OAuth2 provider is verified 
	 */
	public boolean getOAuth2AccountVerified(String registrationId, Map<String, Object> attributes) {

		// Facebook no more returns verified
		// https://developers.facebook.com/docs/graph-api/reference/user
		if ("facebook".equals(registrationId))
			return true;
		
		Object verified = attributes.get(StandardClaimNames.EMAIL_VERIFIED);
		if (verified == null)
			verified = attributes.get("verified");
		
		try {
			return (boolean) verified;
		} catch (Throwable t) {
			return false;
		}
	}
		
}