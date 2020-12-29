package com.bpwizard.wcm_bpm.setup.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;


@Configuration
@ConfigurationProperties(prefix="bpw.account")
public class AccountDBConfiguration extends BaseMysqlConfiguration {
}
