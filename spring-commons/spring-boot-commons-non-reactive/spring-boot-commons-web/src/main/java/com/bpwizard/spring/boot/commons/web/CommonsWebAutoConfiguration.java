package com.bpwizard.spring.boot.commons.web;

import java.io.Serializable;
import java.util.List;

import javax.validation.Validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration;

import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorViewResolver;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.cors.CorsConfigurationSource;

import com.bpwizard.spring.boot.commons.CommonsAutoConfiguration;
import com.bpwizard.spring.boot.commons.SpringProperties;
import com.bpwizard.spring.boot.commons.exceptions.ErrorResponseComposer;
import com.bpwizard.spring.boot.commons.web.exceptions.DefaultExceptionHandlerControllerAdvice;
import com.bpwizard.spring.boot.commons.web.exceptions.SpringErrorAttributes;
import com.bpwizard.spring.boot.commons.web.exceptions.SpringErrorController;
import com.bpwizard.spring.boot.commons.web.security.SpringCorsConfigurationSource;
import com.bpwizard.spring.boot.commons.web.security.SpringWebAuditorAware;
import com.bpwizard.spring.boot.commons.web.security.SpringWebSecurityConfig;
import com.bpwizard.spring.boot.commons.web.util.WebUtils;
import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
@EnableSpringDataWebSupport
@EnableGlobalMethodSecurity(
        securedEnabled = true,
        jsr250Enabled = true,
        prePostEnabled = true
)
@AutoConfigureBefore({
	WebMvcAutoConfiguration.class,
	ErrorMvcAutoConfiguration.class,
	SecurityAutoConfiguration.class,
	SecurityFilterAutoConfiguration.class,
	CommonsAutoConfiguration.class})
@Import(JsonValidationConfiguration.class) 
public class CommonsWebAutoConfiguration {

	/**
	 * For handling JSON vulnerability,
	 * JSON response bodies would be prefixed with
	 * this string.
	 */
	public final static String JSON_PREFIX = ")]}',\n";

	private static final Logger logger = LoggerFactory.getLogger(CommonsWebAutoConfiguration.class);
	
	public CommonsWebAutoConfiguration() {
		logger.info("Created");
	}
	
	/**
	 * Prefixes JSON responses for JSON vulnerability. Disabled by default.
	 * To enable, add this to your application properties:
	 *     bpw.enabled.json-prefix: true
	 */
	@Bean
	@ConditionalOnProperty(name="bpw.enabled.json-prefix")
	public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter(
			ObjectMapper objectMapper) {
		
        logger.info("Configuring JSON vulnerability prefix");       

        MappingJackson2HttpMessageConverter converter =
        		new MappingJackson2HttpMessageConverter(objectMapper);
        converter.setJsonPrefix(JSON_PREFIX);
        
        return converter;
	}
	
	/**
	 * Configures DefaultExceptionHandlerControllerAdvice if missing
	 */	
	@Bean
	@ConditionalOnMissingBean(DefaultExceptionHandlerControllerAdvice.class)
	public <T extends Throwable>
	DefaultExceptionHandlerControllerAdvice<T> defaultExceptionHandlerControllerAdvice(
    		ErrorResponseComposer<T> errorResponseComposer) {
		
        logger.info("Configuring DefaultExceptionHandlerControllerAdvice");       
		return new DefaultExceptionHandlerControllerAdvice<T>(errorResponseComposer);
	}
	
	/**
	 * Configures an Error Attributes if missing
	 */	
	@Bean
	@ConditionalOnMissingBean(ErrorAttributes.class)
	public <T extends Throwable>
	ErrorAttributes errorAttributes(ErrorResponseComposer<T> errorResponseComposer) {
		
        logger.info("Configuring SpringErrorAttributes");       
		return new SpringErrorAttributes<T>(errorResponseComposer);
	}
	
	/**
	 * Configures an Error Controller if missing
	 */	
	@Bean
	@ConditionalOnMissingBean(ErrorController.class)
	public ErrorController errorController(ErrorAttributes errorAttributes,
			ServerProperties serverProperties,
			List<ErrorViewResolver> errorViewResolvers) {
		
        logger.info("Configuring SpringErrorController");       
		return new SpringErrorController(errorAttributes, serverProperties, errorViewResolvers);	
	}	

	/**
	 * Configures SpringCorsConfig if missing and bpw.cors.allowed-origins is provided
	 */
	@Bean
	@ConditionalOnProperty(name="bpw.cors.allowed-origins")
	@ConditionalOnMissingBean(CorsConfigurationSource.class)
	public SpringCorsConfigurationSource corsConfigurationSource(SpringProperties properties) {
		
        logger.info("Configuring SpringCorsConfigurationSource");       
		return new SpringCorsConfigurationSource(properties);		
	}
	
	/**
	 * Configures SpringSecurityConfig if missing
	 */
	@Bean
	@ConditionalOnMissingBean(SpringWebSecurityConfig.class)	
	public SpringWebSecurityConfig springSecurityConfig() {
		
        logger.info("Configuring SpringWebSecurityConfig");       
		return new SpringWebSecurityConfig();
	}
	
	/**
	 * Configures an Auditor Aware if missing
	 */	
	@Bean
	@ConditionalOnMissingBean(AuditorAware.class)
	public <ID extends Serializable>
	AuditorAware<ID> auditorAware() {
		
        logger.info("Configuring SpringAuditorAware");       
		return new SpringWebAuditorAware<ID>();
	}

	/**
	 * Configures WebUtils
	 */
	@Bean
	public WebUtils webUtils(ApplicationContext applicationContext,
			ObjectMapper objectMapper) {

        logger.info("Configuring WebUtils");       		
		return new WebUtils();
	}
	
	/**
	 * Merge ValidationMessages.properties into messages.properties
	 */	
    @Bean
	@ConditionalOnMissingBean(Validator.class)
    public Validator validator(MessageSource messageSource) {

        LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
        localValidatorFactoryBean.setValidationMessageSource(messageSource);
        return localValidatorFactoryBean;
    }
}
