package com.bpwizard.bpm.config;

import org.camunda.bpm.config.CamundaConfig;
import org.camunda.bpm.spring.boot.starter.annotation.EnableProcessApplication;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

import com.bpwizard.wcm.repo.config.ModeshapeAppConfig;
import com.bpwizard.wcm.repo.config.WcmAppConfig;
import com.bpwizard.wcm.repo.config.WebSocketConfig;
import com.bpwizard.wcm.repo.config.WebdavConfig;

// @Configuration
@EnableProcessApplication("bpm-server")
@ComponentScan(basePackages={"com.bpwizard.bpm", "com.bpwizard.wcm", "com.bpwizard.spring.boot.commons", "org.camunda.bpm.engine.rest", "org.camunda.bpm.runtime"})
@Import({
  SwaggerConfig.class,
  AppSecurityConfig.class,
  ModeshapeAppConfig.class,
  WebdavConfig.class,
  WebdavConfig.class,
  WebSocketConfig.class,
  WcmAppConfig.class,
  CamundaConfig.class
})
@AutoConfigureAfter({BpmMybatisAutoConfig.class })
public class CamundaModeshapeAutoConfig {
}
