package com.bpwizard.wcm.repo.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;

import com.bpwizard.spring.boot.commons.web.filter.LocaleFilter;
import com.bpwizard.wcm.repo.rest.filter.CleanupFilter;
import com.bpwizard.wcm.repo.rest.filter.LoggingFilter;

public class WcmAppConfig {
	@Bean
	@ConditionalOnMissingBean
    public FilterRegistrationBean <CleanupFilter> cleanupFilterRegistrationBean() {
     FilterRegistrationBean <CleanupFilter> registrationBean = new FilterRegistrationBean<>();
     CleanupFilter cleanupFilter = new CleanupFilter();

     registrationBean.setFilter(cleanupFilter);
     registrationBean.addUrlPatterns("/modeshape/api/*");
     //registrationBean.setOrder(2); //set precedence
     return registrationBean;
    }
	
	@Bean
	@ConditionalOnMissingBean
    public FilterRegistrationBean <LoggingFilter> loggingFilterRegistrationBean() {
     FilterRegistrationBean <LoggingFilter> registrationBean = new FilterRegistrationBean<>();
     LoggingFilter loggingFilter = new LoggingFilter();

     registrationBean.setFilter(loggingFilter);
     registrationBean.addUrlPatterns("/modeshape/api/*");
     // registrationBean.setOrder(1); //set precedence
     return registrationBean;
    }

	@Bean
	@ConditionalOnMissingBean
    public FilterRegistrationBean<LocaleFilter> localeFilterRegistrationBean() {
     FilterRegistrationBean<LocaleFilter> registrationBean = new FilterRegistrationBean<>();
     LocaleFilter localeFilter = new LocaleFilter();

     registrationBean.setFilter(localeFilter);
     registrationBean.addUrlPatterns("/*");
     // registrationBean.setOrder(1); //set precedence
     return registrationBean;
    }
	
	@Bean
	@ConditionalOnMissingBean
    public MethodValidationPostProcessor methodValidationPostProcessor() {
		//required to enable parameter validation
        return new MethodValidationPostProcessor();
    }
}
