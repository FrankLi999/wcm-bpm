package com.bpwizard.wcm.repo.config;
import org.modeshape.web.jcr.webdav.ModeShapeWebdavServlet;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.bpwizard.wcm.repo.rest.filter.CleanupFilter;
import com.bpwizard.wcm.repo.rest.filter.LoggingFilter;
@Configuration
//@ComponentScan(basePackages= {"com.bpwizard.spring.boot.commons.service.domain"})
public class AppConfig {
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
