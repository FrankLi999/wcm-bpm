package org.camunda.bpm.config;

import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.rest.filter.StatelessUserAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;
import org.springframework.web.client.RestTemplate;

import com.bpwizard.spring.boot.commons.web.filter.LocaleFilter;

public class CamundaConfig {

	@Autowired
	ProcessEngine processEngine;
	
	@Bean
    @ConditionalOnMissingBean
	public FilterRegistrationBean<StatelessUserAuthenticationFilter> statelessUserAuthenticationFilter(){
		FilterRegistrationBean<StatelessUserAuthenticationFilter> filterRegistration = new FilterRegistrationBean<>();
		filterRegistration.setFilter(new StatelessUserAuthenticationFilter(processEngine));
		filterRegistration.setOrder(102); // make sure the filter is registered after the Spring Security Filter Chain
		filterRegistration.addUrlPatterns("/camunda/api/engine/*");
		filterRegistration.addUrlPatterns("/rest/*");
		filterRegistration.addUrlPatterns("/content/server/*");
		return filterRegistration;
	}
	
	@Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }
	
    @Bean()
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
