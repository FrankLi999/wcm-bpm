package com.bpwizard.spring.boot.commons;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.bpwizard.spring.boot.commons.exceptions.CommonsExceptionsAutoConfiguration;
import com.bpwizard.spring.boot.commons.exceptions.handlers.AccessDeniedExceptionHandler;
import com.bpwizard.spring.boot.commons.exceptions.handlers.BadCredentialsExceptionHandler;
import com.bpwizard.spring.boot.commons.exceptions.handlers.JsonParseExceptionHandler;
import com.bpwizard.spring.boot.commons.exceptions.handlers.JsonPatchExceptionHandler;
import com.bpwizard.spring.boot.commons.exceptions.handlers.JsonProcessingExceptionHandler;
import com.bpwizard.spring.boot.commons.exceptions.handlers.UsernameNotFoundExceptionHandler;
import com.bpwizard.spring.boot.commons.mail.MailSender;
import com.bpwizard.spring.boot.commons.mail.MockMailSender;
import com.bpwizard.spring.boot.commons.mail.SmtpMailSender;
import com.bpwizard.spring.boot.commons.security.JSONWebEncryptionService;
import com.bpwizard.spring.boot.commons.security.JSONWebSignatureService;
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
// @ComponentScan(basePackageClasses= {BadCredentialsExceptionHandler.class})
@EnableAsync
@EnableEncryptableProperties
// @PropertySource("classpath:hazelcast.properties")
// @ConfigurationProperties(prefix="hazelcast")
@AutoConfigureBefore({
	CommonsExceptionsAutoConfiguration.class})
public class CommonsAutoConfiguration {

	private static final Logger logger = LoggerFactory.getLogger(CommonsAutoConfiguration.class);
	
	public CommonsAutoConfiguration() {
		logger.info("Created");
	}
	
	
	/**
	 * Spring Commons related properties
	 */	
	@Bean
	public SpringProperties springProperties() {
		
        logger.info("Configuring SpringProperties");       
		return new SpringProperties();
	}
	

	/**
	 * Configures AuthTokenService if missing
	 */
	@Bean
	@ConditionalOnMissingBean(JSONWebSignatureService.class)
	public JSONWebSignatureService jwsTokenService(SpringProperties properties) throws JOSEException {
		
        logger.info("Configuring AuthTokenService");       
		return new SpringJwsService(properties.getJwt().getSecret());
	}


	/**
	 * Configures ExternalTokenService if missing
	 */
	@Bean
	@ConditionalOnMissingBean(JSONWebEncryptionService.class)
	public JSONWebEncryptionService jweTokenService(SpringProperties properties) throws KeyLengthException {
		
        logger.info("Configuring ExternalTokenService");       
		return new SpringJweService(properties.getJwt().getSecret());
	}


	/**
	 * Configures Password encoder if missing
	 */
	@Bean
	@ConditionalOnMissingBean(PasswordEncoder.class)
    public PasswordEncoder passwordEncoder() {
	
		logger.info("Configuring PasswordEncoder");		
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
	
	
	/**
	 * Configures PermissionEvaluator if missing
	 */
	@Bean
	@ConditionalOnMissingBean(PermissionEvaluator.class)
	public PermissionEvaluator permissionEvaluator() {
		
        logger.info("Configuring SpringPermissionEvaluator");       
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

        logger.info("Configuring MockMailSender");       
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
		
        logger.info("Configuring SmtpMailSender");       
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
		
        logger.info("Configuring SpringUserDetailsService");       
		return new CaptchaValidator(properties, restTemplateBuilder);
	}
	
	@Bean
	@ConditionalOnMissingBean(BadCredentialsExceptionHandler.class)
	public BadCredentialsExceptionHandler badCredentialsExceptionHandler() {
		return new BadCredentialsExceptionHandler();
	}
	
	@Bean
	@ConditionalOnMissingBean(AccessDeniedExceptionHandler.class)
	public AccessDeniedExceptionHandler accessDeniedExceptionHandler() {
		return new AccessDeniedExceptionHandler();
	}
	
	@Bean
	@ConditionalOnMissingBean(JsonParseExceptionHandler.class)
	public JsonParseExceptionHandler jsonParseExceptionHandler() {
		return new JsonParseExceptionHandler();
	}
	
	@Bean
	@ConditionalOnMissingBean(JsonPatchExceptionHandler.class)
	public JsonPatchExceptionHandler jsonPatchExceptionHandler() {
		return new JsonPatchExceptionHandler();
	}
	
	@Bean
	@ConditionalOnMissingBean(JsonProcessingExceptionHandler.class)
	public JsonProcessingExceptionHandler jsonProcessingExceptionHandler() {
		return new JsonProcessingExceptionHandler();
	}
	
	@Bean
	@ConditionalOnMissingBean(UsernameNotFoundExceptionHandler.class)
	public UsernameNotFoundExceptionHandler usernameNotFoundExceptionHandler() {
		return new UsernameNotFoundExceptionHandler();
	}	
}
