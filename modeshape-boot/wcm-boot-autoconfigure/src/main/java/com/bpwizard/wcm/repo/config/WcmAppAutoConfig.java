package com.bpwizard.wcm.repo.config;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;

import com.bpwizard.spring.boot.commons.service.AutoConfiguration;
import com.bpwizard.spring.boot.commons.web.filter.LocaleFilter;
import com.bpwizard.wcm.repo.rest.filter.CleanupFilter;
import com.bpwizard.wcm.repo.rest.filter.LoggingFilter;

// @Configuration
@Import({
	SwaggerConfig.class,
	AppSecurityConfig.class,
	ModeshapeAppConfig.class,
	WebdavConfig.class,
	WebSocketConfig.class,
	WcmAppConfig.class
	})
@AutoConfigureBefore({ModeshapeConfig.class, AutoConfiguration.class})
public class WcmAppAutoConfig {}
