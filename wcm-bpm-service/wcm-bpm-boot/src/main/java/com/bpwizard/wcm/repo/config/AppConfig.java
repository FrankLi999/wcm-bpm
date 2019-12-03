package com.bpwizard.wcm.repo.config;
import org.camunda.bpm.engine.ProcessEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.bpwizard.wcm.repo.rest.filter.CleanupFilter;
import com.bpwizard.wcm.repo.rest.filter.LoggingFilter;


@Configuration
@EntityScan(basePackages= {"com.bpwizard.spring.boot.commons.service.repo"})
@EnableJpaRepositories({"com.bpwizard.spring.boot.commons.service.repo"})
@ComponentScan(basePackages={"com.bpwizard.spring.boot.commons.service.repo"})
public class AppConfig {
	@Autowired 
	ProcessEngine processEngine;
	
//	
//	@Value("${spring.datasource.username:xxx}")
//	private String userValue;
//
//	
//	@Value("${spring.datasource.password:yyy}")
//	private String pwdValue;
	
	@Bean
    public FilterRegistrationBean <CleanupFilter> cleanupFilterRegistrationBean() {
     FilterRegistrationBean <CleanupFilter> registrationBean = new FilterRegistrationBean<>();
     CleanupFilter cleanupFilter = new CleanupFilter();

     registrationBean.setFilter(cleanupFilter);
     registrationBean.addUrlPatterns("/modeshape/api/*");
     //registrationBean.setOrder(2); //set precedence
     return registrationBean;
    }
	
	@Bean
    public FilterRegistrationBean <LoggingFilter> loggingFilterRegistrationBean() {
     FilterRegistrationBean <LoggingFilter> registrationBean = new FilterRegistrationBean<>();
     LoggingFilter loggingFilter = new LoggingFilter();

     registrationBean.setFilter(loggingFilter);
     registrationBean.addUrlPatterns("/modeshape/api/*");
     // registrationBean.setOrder(1); //set precedence
     return registrationBean;
    }
}
