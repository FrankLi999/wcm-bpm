package com.bpwizard.spring.boot.commons.reactive.service;

import java.io.Serializable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.security.reactive.ReactiveUserDetailsServiceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.bpwizard.spring.boot.commons.SpringProperties;
import com.bpwizard.spring.boot.commons.domain.IdConverter;
import com.bpwizard.spring.boot.commons.exceptions.util.SpringExceptionUtils;
import com.bpwizard.spring.boot.commons.mongo.CommonsMongoAutoConfiguration;
import com.bpwizard.spring.boot.commons.reactive.service.domain.AbstractMongoUser;
import com.bpwizard.spring.boot.commons.reactive.service.domain.AbstractMongoUserRepository;
import com.bpwizard.spring.boot.commons.reactive.service.security.SpringReactiveSecurityConfig;
import com.bpwizard.spring.boot.commons.reactive.service.security.SpringReactiveUserDetailsService;
import com.bpwizard.spring.boot.commons.reactive.service.security.ReactiveOAuth2AuthenticationSuccessHandler;
import com.bpwizard.spring.boot.commons.reactive.service.util.ReactiveServiceUtils;
import com.bpwizard.spring.boot.commons.security.JSONWebSignatureService;

@Configuration
@AutoConfigureBefore({
	ReactiveUserDetailsServiceAutoConfiguration.class,
	CommonsMongoAutoConfiguration.class})
public class ReactiveAutoConfiguration {
	
	private static final Logger log = LogManager.getLogger(ReactiveAutoConfiguration.class);
	
	public ReactiveAutoConfiguration() {
		log.info("Created");
	}

	@Bean
	@ConditionalOnMissingBean(IdConverter.class)
	public <ID extends Serializable>
	IdConverter<ID> idConverter(SpringReactiveService<?,ID> springService) {
		return id -> springService.toId(id);
	}
	
	@Bean
	@ConditionalOnMissingBean(ReactiveOAuth2AuthenticationSuccessHandler.class)
	public <U extends AbstractMongoUser<ID>, ID extends Serializable>
		ReactiveOAuth2AuthenticationSuccessHandler<U, ID> reactiveOAuth2AuthenticationSuccessHandler(
				JSONWebSignatureService jwsTokenService,
				AbstractMongoUserRepository<U, ID> userRepository,
				SpringReactiveUserDetailsService<U, ID> userDetailsService,
				SpringReactiveService<U, ID> lemonService,
				PasswordEncoder passwordEncoder,
				SpringProperties properties) {
		
		log.info("Configuring ReactiveOAuth2AuthenticationSuccessHandler ...");

		return new ReactiveOAuth2AuthenticationSuccessHandler<U,ID>(
				jwsTokenService,
				userRepository,
				userDetailsService,
				lemonService,
				passwordEncoder,
				properties);
	}
	
	@Bean
	@ConditionalOnMissingBean(SpringReactiveSecurityConfig.class)
	public <U extends AbstractMongoUser<ID>, ID extends Serializable>
		SpringReactiveSecurityConfig<U,ID> springReactiveSecurityConfig(
				JSONWebSignatureService jwsTokenService,
				SpringReactiveUserDetailsService<U, ID> userDetailsService,
				ReactiveOAuth2AuthenticationSuccessHandler<U,ID> reactiveOAuth2AuthenticationSuccessHandler,
				SpringProperties properties) {
		
		log.info("Configuring SpringReactiveSecurityConfig ...");

		return new SpringReactiveSecurityConfig<U,ID>(
				jwsTokenService, 
				userDetailsService,
				reactiveOAuth2AuthenticationSuccessHandler,
				properties);
	}
	
	
	/**
	 * Configures UserDetailsService if missing
	 */
	@Bean
	@ConditionalOnMissingBean(UserDetailsService.class)
	public <U extends AbstractMongoUser<ID>, ID extends Serializable>
	SpringReactiveUserDetailsService<U, ID> userDetailService(AbstractMongoUserRepository<U, ID> userRepository) {
		
        log.info("Configuring SpringUserDetailsService");       
		return new SpringReactiveUserDetailsService<U, ID>(userRepository);
	}

//	@Bean	
//	@ConditionalOnMissingBean(ReactiveAuthenticationManager.class)
//	public <U extends AbstractMongoUser<ID>, ID extends Serializable> ReactiveAuthenticationManager ReactiveAuthenticationManager(SpringReactiveUserDetailsService<U, ID> userDetailsService) {
//		log.info("Configuring ReactiveAuthenticationManager");     
//		return new UserDetailsRepositoryReactiveAuthenticationManager(userDetailsService);
//	}
	
	/**
	 * Configures ReactiveServiceUtils
	 */
	@Bean
	public ReactiveServiceUtils reactiveServiceUtils(SpringExceptionUtils lexUtils) {

        log.info("Configuring ReactiveServiceUtils");
		return new ReactiveServiceUtils();
	}
}
