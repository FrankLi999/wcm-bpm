package com.bpwizard.bpm.config;
import org.camunda.bpm.spring.boot.starter.CamundaBpmAutoConfiguration;
import org.camunda.bpm.spring.boot.starter.annotation.EnableProcessApplication;
import org.camunda.bpm.spring.boot.starter.configuration.CamundaMybatisConfiguration;
import org.camunda.bpm.spring.boot.starter.configuration.impl.DefaultMybatisConfiguration;
import org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;
import org.springframework.web.client.RestTemplate;

import com.bpwizard.spring.boot.commons.jdbc.CommonsJdbcAutoConfiguration;
import com.bpwizard.spring.boot.commons.web.filter.LocaleFilter;

@Configuration
@EnableProcessApplication("wcm-boot")
@ComponentScan(basePackages={"com.bpwizard.bpm", "com.bpwizard.wcm_bpm", "com.bpwizard.spring.boot.commons"})
@Import({
  CamundaConfig.class,
  MailConfig.class,
  SwaggerConfig.class,
  AppSecurityConfig.class
})
@AutoConfigureBefore({CamundaBpmAutoConfiguration.class, CommonsJdbcAutoConfiguration.class})
@AutoConfigureAfter({MybatisAutoConfiguration.class })
public class BpmAppAutoConfig {

	@Bean
	@ConditionalOnMissingBean(CamundaMybatisConfiguration.class)
	public DefaultMybatisConfiguration mybatisConfiguration() {
		return new DefaultMybatisConfiguration();
	}
	
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
