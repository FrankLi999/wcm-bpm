package com.bpwizard.spring.boot.commons;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.bpwizard.spring.boot.commons.exceptions.CommonsExceptionsAutoConfiguration;
import com.bpwizard.spring.boot.commons.exceptions.handlers.BadCredentialsExceptionHandler;
import com.bpwizard.spring.boot.commons.mail.MailSender;
import com.bpwizard.spring.boot.commons.mail.MockMailSender;
import com.bpwizard.spring.boot.commons.mail.SmtpMailSender;
import com.bpwizard.spring.boot.commons.security.JSONWebSignatureService;
import com.bpwizard.spring.boot.commons.security.JSONWebEncryptionService;
import com.bpwizard.spring.boot.commons.security.SpringJweService;
import com.bpwizard.spring.boot.commons.security.SpringJwsService;
import com.bpwizard.spring.boot.commons.security.SpringPermissionEvaluator;
import com.bpwizard.spring.boot.commons.util.SecurityUtils;
import com.bpwizard.spring.boot.commons.validation.CaptchaValidator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.KeyLengthException;
import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;

@Configuration
@ComponentScan(basePackageClasses= {BadCredentialsExceptionHandler.class})
@EnableAsync
@EnableEncryptableProperties
// @PropertySource("classpath:hazelcast.properties")
// @ConfigurationProperties(prefix="hazelcast")
@AutoConfigureBefore({
	CommonsExceptionsAutoConfiguration.class})
public class CommonsAutoConfiguration {

	private static final Logger log = LogManager.getLogger(CommonsAutoConfiguration.class);
	
	public CommonsAutoConfiguration() {
		log.info("Created");
	}
	
	
	/**
	 * Spring Commons related properties
	 */	
	@Bean
	public SpringProperties springProperties() {
		
        log.info("Configuring SpringProperties");       
		return new SpringProperties();
	}
	

	/**
	 * Configures AuthTokenService if missing
	 */
	@Bean
	@ConditionalOnMissingBean(JSONWebSignatureService.class)
	public JSONWebSignatureService jwsTokenService(SpringProperties properties) throws JOSEException {
		
        log.info("Configuring AuthTokenService");       
		return new SpringJwsService(properties.getJwt().getSecret());
	}


	/**
	 * Configures ExternalTokenService if missing
	 */
	@Bean
	@ConditionalOnMissingBean(JSONWebEncryptionService.class)
	public JSONWebEncryptionService jweTokenService(SpringProperties properties) throws KeyLengthException {
		
        log.info("Configuring ExternalTokenService");       
		return new SpringJweService(properties.getJwt().getSecret());
	}


	/**
	 * Configures Password encoder if missing
	 */
	@Bean
	@ConditionalOnMissingBean(PasswordEncoder.class)
    public PasswordEncoder passwordEncoder() {
	
		log.info("Configuring PasswordEncoder");		
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
	
	
	/**
	 * Configures PermissionEvaluator if missing
	 */
	@Bean
	@ConditionalOnMissingBean(PermissionEvaluator.class)
	public PermissionEvaluator permissionEvaluator() {
		
        log.info("Configuring SpringPermissionEvaluator");       
		return new SpringPermissionEvaluator();
	}


	/**
	 * Configures a MockMailSender when the property
	 * <code>spring.mail.host</code> isn't defined.
	 */
	@Bean
	@ConditionalOnMissingBean(MailSender.class)
	@ConditionalOnProperty(name="spring.mail.host", havingValue="foo", matchIfMissing=true)
	public MailSender<?> mockMailSender() {

        log.info("Configuring MockMailSender");       
        return new MockMailSender();
	}

	
	/**
	 * Configures an SmtpMailSender when the property
	 * <code>spring.mail.host</code> is defined.
	 */
	@Bean
	@ConditionalOnMissingBean(MailSender.class)
	@ConditionalOnProperty("spring.mail.host")
	public MailSender<?> smtpMailSender(JavaMailSender javaMailSender) {
		
        log.info("Configuring SmtpMailSender");       
		return new SmtpMailSender(javaMailSender);
	}
	
	@Bean
	public SecurityUtils securityUtils(ApplicationContext applicationContext, ObjectMapper objectMapper) {
		return new SecurityUtils(applicationContext, objectMapper);
	}
	
//	@Bean
//	@ConditionalOnMissingBean(RestTemplateBuilder.class)
//	public RestTemplateBuilder restTemplateBuilder() {
//		return new RestTemplateBuilder();
//	}
	
	/**
	 * Configures CaptchaValidator if missing
	 */
	@Bean
	@ConditionalOnMissingBean(CaptchaValidator.class)
	public CaptchaValidator captchaValidator(SpringProperties properties, RestTemplateBuilder restTemplateBuilder) {
		
        log.info("Configuring SpringUserDetailsService");       
		return new CaptchaValidator(properties, restTemplateBuilder);
	}
}
