package com.bpwizard.spring.boot.commons.service;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.bpwizard.spring.boot.commons.SpringProperties;
import com.bpwizard.spring.boot.commons.domain.IdConverter;
import com.bpwizard.spring.boot.commons.jpa.CommonsJpaAutoConfiguration;
import com.bpwizard.spring.boot.commons.security.BlueTokenService;
import com.bpwizard.spring.boot.commons.service.domain.AbstractUser;
import com.bpwizard.spring.boot.commons.service.domain.AbstractUserRepository;
import com.bpwizard.spring.boot.commons.service.security.OAuth2AuthenticationFailureHandler;
import com.bpwizard.spring.boot.commons.service.security.OAuth2AuthenticationSuccessHandler;
import com.bpwizard.spring.boot.commons.service.security.SpringAuthenticationSuccessHandler;
import com.bpwizard.spring.boot.commons.service.security.SpringJpaSecurityConfig;
import com.bpwizard.spring.boot.commons.service.security.SpringOAuth2UserService;
import com.bpwizard.spring.boot.commons.service.security.SpringOidcUserService;
import com.bpwizard.spring.boot.commons.service.security.SpringUserDetailsService;
import com.bpwizard.spring.boot.commons.service.util.ServiceUtils;
import com.bpwizard.spring.boot.commons.service.validation.UniqueEmailValidator;
import com.bpwizard.spring.boot.commons.validation.RetypePasswordValidator;
import com.bpwizard.spring.boot.commons.web.security.SpringWebSecurityConfig;
import com.bpwizard.wcm.repo.domain.Role;
import com.bpwizard.wcm.repo.domain.RoleRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Spring Commons Auto Configuration
 * 
 * @author Sanjay Patel
 */
@Configuration
@EnableTransactionManagement
@EnableJpaAuditing
@AutoConfigureBefore({CommonsJpaAutoConfiguration.class})
public class AutoConfiguration {
	
	private static final Logger log = LogManager.getLogger(AutoConfiguration.class);
	
	public AutoConfiguration() {
		log.info("Created");
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
		
        log.info("Configuring AuthenticationSuccessHandler");       
		return new SpringAuthenticationSuccessHandler(objectMapper, springService, properties);
	}
	
	/**
	 * Configures OAuth2AuthenticationSuccessHandler if missing
	 */
	@Bean
	@ConditionalOnMissingBean(OAuth2AuthenticationSuccessHandler.class)
	public OAuth2AuthenticationSuccessHandler<?> oauth2AuthenticationSuccessHandler(
			SpringProperties properties, BlueTokenService blueTokenService) {
		
        log.info("Configuring OAuth2AuthenticationSuccessHandler");       
		return new OAuth2AuthenticationSuccessHandler<>(properties, blueTokenService);
	}
	
	/**
	 * Configures OAuth2AuthenticationFailureHandler if missing
	 */
	@Bean
	@ConditionalOnMissingBean(OAuth2AuthenticationFailureHandler.class)
	public OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler() {
		
        log.info("Configuring OAuth2AuthenticationFailureHandler");       
		return new OAuth2AuthenticationFailureHandler();
	}

	/**
	 * Configures AuthenticationFailureHandler if missing
	 */
	@Bean
	@ConditionalOnMissingBean(AuthenticationFailureHandler.class)
    public AuthenticationFailureHandler authenticationFailureHandler() {
		
        log.info("Configuring SimpleUrlAuthenticationFailureHandler");       
    	return new SimpleUrlAuthenticationFailureHandler();
    }	

	/**
	 * Configures UserDetailsService if missing
	 */
	@Bean
	@ConditionalOnMissingBean(UserDetailsService.class)
	public <U extends AbstractUser<ID>, ID extends Serializable>
	SpringUserDetailsService userDetailService(AbstractUserRepository<U, ID> userRepository) {
		
        log.info("Configuring SpringUserDetailsService");       
		return new SpringUserDetailsService<U, ID>(userRepository);
	}

	/**
	 * Configures SpringOidcUserService if missing
	 */
	@Bean
	@ConditionalOnMissingBean(SpringOidcUserService.class)	
	public SpringOidcUserService springOidcUserService(SpringOAuth2UserService<?, ?> springOAuth2UserService) {
		
        log.info("Configuring SpringOidcUserService");       
		return new SpringOidcUserService(springOAuth2UserService);
	}

	/**
	 * Configures SpringOAuth2UserService if missing
	 */
	@Bean
	@ConditionalOnMissingBean(SpringOAuth2UserService.class)	
	public <U extends AbstractUser<ID>, ID extends Serializable>
		SpringOAuth2UserService<U,ID> springOAuth2UserService(
			SpringUserDetailsService<U, ?> userDetailsService,
			SpringService<U, ?> springService,
			PasswordEncoder passwordEncoder) {
		
        log.info("Configuring SpringOAuth2UserService");       
		return new SpringOAuth2UserService<U,ID>(userDetailsService, springService, passwordEncoder);
	}

	/**
	 * Configures SpringSecurityConfig if missing
	 */
	@Bean
	@ConditionalOnMissingBean(SpringWebSecurityConfig.class)	
	public SpringWebSecurityConfig springSecurityConfig() {
		
        log.info("Configuring SpringJpaSecurityConfig");       
		return new SpringJpaSecurityConfig();
	}
	
	/**
	 * Configures ServiceUtils
	 */
	@Bean
	public ServiceUtils serviceUtils(ApplicationContext applicationContext,
			ObjectMapper objectMapper) {

        log.info("Configuring ServiceUtils");       		
		return new ServiceUtils();
	}
	
	/**
	 * Configures RetypePasswordValidator if missing
	 */
	@Bean
	@ConditionalOnMissingBean(RetypePasswordValidator.class)
	public RetypePasswordValidator retypePasswordValidator() {
		
        log.info("Configuring RetypePasswordValidator");       
		return new RetypePasswordValidator();
	}
	
	/**
	 * Configures UniqueEmailValidator if missing
	 */
	@Bean
	public UniqueEmailValidator uniqueEmailValidator(AbstractUserRepository<?, ?> userRepository) {
		
        log.info("Configuring UniqueEmailValidator");       
		return new UniqueEmailValidator(userRepository);		
	}
	
	@Bean
	public Map<String, Role> preloadedRoles(RoleRepository roleRepository, SpringProperties properties) {
		
        log.info("preloadedRoles");   //TODO, load in batch
        Map<String, Role> roles = new HashMap<>();
        return roles;	
	}
}
