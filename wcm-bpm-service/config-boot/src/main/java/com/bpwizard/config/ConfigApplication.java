package com.bpwizard.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
// import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.config.server.EnableConfigServer;

@SpringBootApplication
@EnableConfigServer
// @EnableDiscoveryClient
public class ConfigApplication {

	public static void main(String[] args) {
	  SpringApplication application = new SpringApplication(ConfigApplication.class);
      // application.setAdditionalProfiles("ssl");
      application.run(args);
	}
}
