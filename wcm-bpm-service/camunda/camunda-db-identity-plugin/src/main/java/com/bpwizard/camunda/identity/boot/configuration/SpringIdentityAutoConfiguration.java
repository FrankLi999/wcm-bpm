package com.bpwizard.camunda.identity.boot.configuration;

import org.camunda.bpm.engine.impl.cfg.ProcessEnginePlugin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.bpwizard.camunda.identity.boot.plugin.SpringIdentityProviderPlugin;
import com.bpwizard.spring.boot.commons.service.AutoConfiguration;
import com.bpwizard.wcm.repo.camunda.identity.service.SpringGroupService;
import com.bpwizard.wcm.repo.camunda.identity.service.SpringTenantService;
import com.bpwizard.wcm.repo.camunda.identity.service.SpringUserService;
@Configuration
@ConditionalOnClass(SpringIdentityProviderPlugin.class)
@AutoConfigureBefore({AutoConfiguration.class})
public class SpringIdentityAutoConfiguration {
	@Autowired
    private SpringUserService userService;

	@Autowired
    private SpringGroupService groupService;
	
	@Autowired
    private SpringTenantService tenantService;
    
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Bean
	public ProcessEnginePlugin dbIdentityProviderPlugin() {
		return new SpringIdentityProviderPlugin(userService, groupService, tenantService, passwordEncoder);
	}
}
