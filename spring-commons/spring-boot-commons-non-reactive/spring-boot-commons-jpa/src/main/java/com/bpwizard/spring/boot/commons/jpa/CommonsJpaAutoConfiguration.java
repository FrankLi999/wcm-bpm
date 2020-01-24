package com.bpwizard.spring.boot.commons.jpa;

import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.bpwizard.spring.boot.commons.web.CommonsWebAutoConfiguration;

@Configuration
@EnableTransactionManagement
@EnableJpaAuditing
@AutoConfigureBefore({CommonsWebAutoConfiguration.class})
public class CommonsJpaAutoConfiguration {

}
