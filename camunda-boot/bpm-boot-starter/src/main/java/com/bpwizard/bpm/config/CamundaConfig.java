package com.bpwizard.bpm.config;

import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.spring.boot.starter.CamundaBpmAutoConfiguration;
import org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.bpwizard.bpm.rest.filter.StatelessUserAuthenticationFilter;

@Configuration
@ComponentScan(basePackages={"org.camunda.bpm.engine.rest, org.camunda.bpm.runtime"})
@AutoConfigureAfter({MybatisAutoConfiguration.class, CamundaBpmAutoConfiguration.class })
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
