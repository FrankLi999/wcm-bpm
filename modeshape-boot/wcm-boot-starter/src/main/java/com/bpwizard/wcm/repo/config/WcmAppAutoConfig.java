package com.bpwizard.wcm.repo.config;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
// import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;

import com.bpwizard.spring.boot.commons.service.AutoConfiguration;
import com.bpwizard.spring.boot.commons.web.filter.LocaleFilter;
import com.bpwizard.wcm.repo.rest.filter.CleanupFilter;
import com.bpwizard.wcm.repo.rest.filter.LoggingFilter;

@Configuration
@ComponentScan(basePackages={"com.bpwizard.spring.boot.commons"})
@Import({
	  AppSecurityConfig.class,
	  MailConfig.class,
	  ModeshapeAppConfig.class,
	  SwaggerConfig.class,
	  WebdavConfig.class,
	  WebSocketConfig.class
	})
@AutoConfigureBefore({ModeshapeConfig.class, AutoConfiguration.class})
public class WcmAppAutoConfig {


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
