package com.bpwizard.wcm.repo.config;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.bpwizard.wcm.repo.rest.filter.CleanupFilter;
import com.bpwizard.wcm.repo.rest.filter.LoggingFilter;

@Configuration
public class AppConfig {
	@Bean
    public FilterRegistrationBean <CleanupFilter> cleanupFilterRegistrationBean() {
     FilterRegistrationBean <CleanupFilter> registrationBean = new FilterRegistrationBean<>();
     CleanupFilter cleanupFilter = new CleanupFilter();

     registrationBean.setFilter(cleanupFilter);
     registrationBean.addUrlPatterns("/api/modeshape/*");
     //registrationBean.setOrder(2); //set precedence
     return registrationBean;
    }
	
	@Bean
    public FilterRegistrationBean <LoggingFilter> loggingFilterRegistrationBean() {
     FilterRegistrationBean <LoggingFilter> registrationBean = new FilterRegistrationBean<>();
     LoggingFilter loggingFilter = new LoggingFilter();

     registrationBean.setFilter(loggingFilter);
     registrationBean.addUrlPatterns("/api/modeshape/*");
     // registrationBean.setOrder(1); //set precedence
     return registrationBean;
    }
}