package com.bpwizard.bpm.config;

import org.camunda.bpm.config.CamundaConfig;
import org.camunda.bpm.spring.boot.starter.annotation.EnableProcessApplication;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

// @Configuration
@EnableProcessApplication("bpm-server")
@ComponentScan(basePackages={"com.bpwizard.bpm", "com.bpwizard.wcm", "com.bpwizard.spring.boot.commons", "org.camunda.bpm.engine.rest", "org.camunda.bpm.runtime"})
@Import({
  AppSecurityConfig.class,
  SwaggerConfig.class,
  CamundaConfig.class
})
@AutoConfigureAfter({BpmMybatisAutoConfig.class })
public class CamundaAutoConfig {
}
