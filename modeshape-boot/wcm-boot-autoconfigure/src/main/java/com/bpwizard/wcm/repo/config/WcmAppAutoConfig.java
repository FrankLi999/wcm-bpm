package com.bpwizard.wcm.repo.config;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

import com.bpwizard.spring.boot.commons.service.AutoConfiguration;

// @Configuration
@Import({
	SwaggerConfig.class,
	AppSecurityConfig.class,
	ModeshapeAppConfig.class,
	WebdavConfig.class,
	WebSocketConfig.class,
	WcmAppConfig.class
	})
@AutoConfigureBefore({ModeshapeConfig.class, AutoConfiguration.class})
@ComponentScan(basePackages={"com.bpwizard.wcm", "com.bpwizard.spring.boot.commons"})
public class WcmAppAutoConfig {}
