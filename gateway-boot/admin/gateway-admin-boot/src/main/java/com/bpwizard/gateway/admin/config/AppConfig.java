package com.bpwizard.gateway.admin.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;
import org.springframework.web.client.RestTemplate;

import com.bpwizard.spring.boot.commons.web.filter.LocaleFilter;


@Configuration
@ComponentScan(basePackages={"com.bpwizard.spring.boot.commons"})
public class AppConfig {

	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder.build();
	}
	
	@Bean
    public FilterRegistrationBean<LocaleFilter> localeFilterRegistrationBean() {
     FilterRegistrationBean<LocaleFilter> registrationBean = new FilterRegistrationBean<>();
     LocaleFilter localeFilter = new LocaleFilter();

     registrationBean.setFilter(localeFilter);
     registrationBean.addUrlPatterns("/*");
     // registrationBean.setOrder(1); //set precedence
     return registrationBean;
    }
	
	@Bean
    public MethodValidationPostProcessor methodValidationPostProcessor() {
		//required to enable parameter validation
        return new MethodValidationPostProcessor();
    }
}
