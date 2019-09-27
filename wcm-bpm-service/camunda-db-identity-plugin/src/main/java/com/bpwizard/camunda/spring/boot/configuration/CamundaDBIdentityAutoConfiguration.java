package com.bpwizard.camunda.spring.boot.configuration;

import org.camunda.bpm.engine.impl.cfg.ProcessEnginePlugin;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.bpwizard.camunda.plugin.impl.identity.db.DBIdentityProviderPlugin;
import com.bpwizard.spring.boot.commons.service.AutoConfiguration;
@Configuration
@ConditionalOnClass(DBIdentityProviderPlugin.class)
@AutoConfigureBefore({AutoConfiguration.class})
public class CamundaDBIdentityAutoConfiguration {
	@Bean
	//@ConditionalOnMissingBean(name = "dbIdentityProviderPlugin")
	public ProcessEnginePlugin dbIdentityProviderPlugin() {
		System.out.println(">>>>>>>>>>>>>>>>>>>> add dbIdentityProviderPlugin");
		return new DBIdentityProviderPlugin();
	}
}
