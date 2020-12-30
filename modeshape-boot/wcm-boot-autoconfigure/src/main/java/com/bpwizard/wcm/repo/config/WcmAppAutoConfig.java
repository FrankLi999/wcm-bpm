package com.bpwizard.wcm.repo.config;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
// import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;

import com.bpwizard.spring.boot.commons.service.AutoConfiguration;
import com.bpwizard.spring.boot.commons.web.filter.LocaleFilter;
import com.bpwizard.wcm.repo.rest.filter.CleanupFilter;
import com.bpwizard.wcm.repo.rest.filter.LoggingFilter;

// @Configuration
@Import({
	  ModeshapeAppConfig.class,
	  WebdavConfig.class,
	  WebSocketConfig.class,
	  SwaggerConfig.class,
	  AppSecurityConfig.class,
	  WcmAppConfig.class
	})
@AutoConfigureBefore({ModeshapeConfig.class, AutoConfiguration.class})
public class WcmAppAutoConfig {}
