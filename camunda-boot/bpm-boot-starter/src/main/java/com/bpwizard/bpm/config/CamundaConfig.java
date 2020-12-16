package com.bpwizard.bpm.config;

import org.camunda.bpm.engine.ProcessEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import com.bpwizard.bpm.rest.filter.StatelessUserAuthenticationFilter;

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
