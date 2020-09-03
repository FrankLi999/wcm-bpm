package com.bpwizard.wcm.repo.config;

import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.spring.boot.starter.annotation.EnableProcessApplication;
import org.camunda.wcm.repo.bpm.rest.filter.StatelessUserAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableProcessApplication("wcm-boot")
@ComponentScan(basePackages={"org.camunda.bpm.engine.rest, org.camunda.bpm.runtime, com.bpwizard.bpm.demo"})
public class CamundaConfig {


	@Autowired
	ProcessEngine processEngine;
	

	@Bean
	public FilterRegistrationBean<StatelessUserAuthenticationFilter> statelessUserAuthenticationFilter(){
		FilterRegistrationBean<StatelessUserAuthenticationFilter> filterRegistration = new FilterRegistrationBean<>();
		filterRegistration.setFilter(new StatelessUserAuthenticationFilter(processEngine));
		filterRegistration.setOrder(102); // make sure the filter is registered after the Spring Security Filter Chain
		filterRegistration.addUrlPatterns("/camunda/api/engine/*");
		filterRegistration.addUrlPatterns("/rest/*");
		filterRegistration.addUrlPatterns("/content/server/*");
		return filterRegistration;
	}
}
