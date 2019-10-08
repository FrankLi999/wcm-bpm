package com.bpwizard.wcm.repo.config;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.rest.filter.StatelessUserAuthenticationFilter;
import org.modeshape.web.jcr.webdav.ModeShapeWebdavServlet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.bpwizard.wcm.repo.rest.filter.CleanupFilter;
import com.bpwizard.wcm.repo.rest.filter.LoggingFilter;
import  org.camunda.bpm.engine.rest.filter.StatelessUserAuthenticationFilter;

@Configuration
//@ComponentScan(basePackages= {"com.bpwizard.spring.boot.commons.service.domain"})
public class AppConfig {
	@Autowired 
	ProcessEngine processEngine;
	
	@Bean
	public ServletRegistrationBean<ModeShapeWebdavServlet> webDavDispatcherServletRegistration1() {
	   ServletRegistrationBean<ModeShapeWebdavServlet> registrationBean = new ServletRegistrationBean<>(
	   new ModeShapeWebdavServlet(), "/webdav/*");
	   registrationBean.setName("webdavServlet");
	   registrationBean.addInitParameter("rootpath", ".");
	   return registrationBean;
	}
	
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
	
	@Bean
    public FilterRegistrationBean statelessUserAuthenticationFilter(){
        FilterRegistrationBean filterRegistration = new FilterRegistrationBean();
        filterRegistration.setFilter(new StatelessUserAuthenticationFilter(processEngine));
        filterRegistration.setOrder(102); // make sure the filter is registered after the Spring Security Filter Chain
        filterRegistration.addUrlPatterns("/camunda/api/engine/*");
        return filterRegistration;
    }
}
