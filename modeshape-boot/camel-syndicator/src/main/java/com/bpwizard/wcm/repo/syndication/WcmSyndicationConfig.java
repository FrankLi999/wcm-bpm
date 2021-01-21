package com.bpwizard.wcm.repo.syndication;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.bpwizard.spring.boot.commons.service.AutoConfiguration;

@Configuration
@Import({
	AppSecurityConfig.class
	})
@AutoConfigureBefore({AutoConfiguration.class})
@ComponentScan(basePackages={"com.bpwizard.wcm", "com.bpwizard.spring.boot.commons"})
public class WcmSyndicationConfig {}
