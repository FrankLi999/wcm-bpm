package com.bpwizard.spring.boot.commons.service;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.web.client.RestTemplate;

import com.bpwizard.spring.boot.commons.SpringProperties;
import com.bpwizard.spring.boot.commons.domain.IdConverter;
import com.bpwizard.spring.boot.commons.jdbc.CommonsJdbcAutoConfiguration;
import com.bpwizard.spring.boot.commons.security.JSONWebSignatureService;
import com.bpwizard.spring.boot.commons.service.domain.Role;
import com.bpwizard.spring.boot.commons.service.domain.RoleService;
import com.bpwizard.spring.boot.commons.service.domain.User;
import com.bpwizard.spring.boot.commons.service.domain.UserService;
import com.bpwizard.spring.boot.commons.service.security.OAuth2AuthenticationFailureHandler;
import com.bpwizard.spring.boot.commons.service.security.OAuth2AuthenticationSuccessHandler;
import com.bpwizard.spring.boot.commons.service.security.SpringAuthenticationSuccessHandler;
import com.bpwizard.spring.boot.commons.service.security.SpringJdbcSecurityConfig;
import com.bpwizard.spring.boot.commons.service.security.SpringOAuth2UserService;
import com.bpwizard.spring.boot.commons.service.security.SpringOidcUserService;
import com.bpwizard.spring.boot.commons.service.security.SpringUserDetailsService;
import com.bpwizard.spring.boot.commons.service.util.ServiceUtils;
import com.bpwizard.spring.boot.commons.service.validation.UniqueEmailValidator;
import com.bpwizard.spring.boot.commons.validation.RetypePasswordValidator;
import com.bpwizard.spring.boot.commons.web.security.SpringWebSecurityConfig;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Spring Commons Auto Configuration
 */
@Configuration
@AutoConfigureBefore({CommonsJdbcAutoConfiguration.class})
public class AutoConfiguration {
	
	private static final Logger logger = LoggerFactory.getLogger(AutoConfiguration.class);
	
	public AutoConfiguration() {
		logger.info("Created");
	}

	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder.build();
	}
	
	
	@Bean
	@ConditionalOnMissingBean(IdConverter.class)
	public <ID extends Serializable>
	IdConverter<ID> idConverter(SpringService<?,ID> springService) {
		return id -> springService.toId(id);
	}
	
	/**
	 * Configures AuthenticationSuccessHandler if missing
	 */
	@Bean
	@ConditionalOnMissingBean(SpringAuthenticationSuccessHandler.class)
	public SpringAuthenticationSuccessHandler authenticationSuccessHandler(
			ObjectMapper objectMapper, SpringService<?, ?> springService, SpringProperties properties) {
		
        logger.info("Configuring AuthenticationSuccessHandler");       
		return new SpringAuthenticationSuccessHandler(objectMapper, springService, properties);
	}
	
	/**
	 * Configures OAuth2AuthenticationSuccessHandler if missing
	 */
	@Bean
	@ConditionalOnMissingBean(OAuth2AuthenticationSuccessHandler.class)
	public OAuth2AuthenticationSuccessHandler<?> oauth2AuthenticationSuccessHandler(
			SpringProperties properties, JSONWebSignatureService jwsTokenService) {
		
        logger.info("Configuring OAuth2AuthenticationSuccessHandler");       
		return new OAuth2AuthenticationSuccessHandler<>(properties, jwsTokenService);
	}
	
	/**
	 * Configures OAuth2AuthenticationFailureHandler if missing
	 */
	@Bean
	@ConditionalOnMissingBean(OAuth2AuthenticationFailureHandler.class)
	public OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler() {
		
        logger.info("Configuring OAuth2AuthenticationFailureHandler");       
		return new OAuth2AuthenticationFailureHandler();
	}

	/**
	 * Configures AuthenticationFailureHandler if missing
	 */
	@Bean
	@ConditionalOnMissingBean(AuthenticationFailureHandler.class)
    public AuthenticationFailureHandler authenticationFailureHandler() {
		
        logger.info("Configuring SimpleUrlAuthenticationFailureHandler");       
    	return new SimpleUrlAuthenticationFailureHandler();
    }	

	/**
	 * Configures UserDetailsService if missing
	 */
	@Bean
	@ConditionalOnMissingBean(UserDetailsService.class)
	public <U extends User<ID>, ID extends Serializable>
	SpringUserDetailsService<U, ID> userDetailService(UserService<U, ID> userService) {
		
        logger.info("Configuring SpringUserDetailsService");       
		return new SpringUserDetailsService<U, ID>(userService);
	}

	/**
	 * Configures SpringOidcUserService if missing
	 */
	@Bean
	@ConditionalOnMissingBean(SpringOidcUserService.class)	
	public SpringOidcUserService springOidcUserService(SpringOAuth2UserService<?, ?> springOAuth2UserService) {
		
        logger.info("Configuring SpringOidcUserService");       
		return new SpringOidcUserService(springOAuth2UserService);
	}

	/**
	 * Configures SpringOAuth2UserService if missing
	 */
	@Bean
	@ConditionalOnMissingBean(SpringOAuth2UserService.class)	
	public <U extends User<ID>, ID extends Serializable>
		SpringOAuth2UserService<U,ID> springOAuth2UserService(
			SpringUserDetailsService<U, ?> userDetailsService,
			SpringService<U, ?> springService,
			PasswordEncoder passwordEncoder) {
		
        logger.info("Configuring SpringOAuth2UserService");       
		return new SpringOAuth2UserService<U,ID>(userDetailsService, springService, passwordEncoder);
	}

	/**
	 * Configures SpringSecurityConfig if missing
	 */
	@Bean
	@ConditionalOnMissingBean(SpringWebSecurityConfig.class)	
	public SpringWebSecurityConfig springSecurityConfig() {
		
        logger.info("Configuring SpringJdbcSecurityConfig");       
		return new SpringJdbcSecurityConfig();
	}
	
	/**
	 * Configures ServiceUtils
	 */
	@Bean
	public ServiceUtils serviceUtils(ApplicationContext applicationContext,
			ObjectMapper objectMapper) {

        logger.info("Configuring ServiceUtils");       		
		return new ServiceUtils();
	}
	
	/**
	 * Configures RetypePasswordValidator if missing
	 */
	@Bean
	@ConditionalOnMissingBean(RetypePasswordValidator.class)
	public RetypePasswordValidator retypePasswordValidator() {
		
        logger.info("Configuring RetypePasswordValidator");       
		return new RetypePasswordValidator();
	}
	
	/**
	 * Configures UniqueEmailValidator if missing
	 */
	@Bean
	public UniqueEmailValidator uniqueEmailValidator(UserService<?, ?> userService) {
		
        logger.info("Configuring UniqueEmailValidator");       
		return new UniqueEmailValidator(userService);		
	}
	
	@Bean
	public Map<String, Role> preloadedRoles(RoleService<Role, Long> roleService, SpringProperties properties) {
		
        logger.info("preloadedRoles");   //TODO, load in batch
        Map<String, Role> roles = new HashMap<>();
        String[] roleNames = properties.getRolename();
        if (null != roleNames) {
			for (String roleName: roleNames) {
				//Optional<Role> role = 
				roleService.findByName(roleName).ifPresent(role -> roles.put(roleName, role));
			}
        }
        return roles;	
	}
	
//	@Bean
//	@ConditionalOnMissingBean(SpringService.class)
//	public SpringService springService() {
//		return new DefaultSpringService();
//	}
}
