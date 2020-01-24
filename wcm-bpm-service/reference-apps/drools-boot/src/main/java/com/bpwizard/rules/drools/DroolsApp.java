package com.bpwizard.rules.drools;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
// @ComponentScan("com.bpwizard.rules.drools.service")
public class DroolsApp {
	public static void main(String[] args) {
		SpringApplication.run(DroolsApp.class, args);
	}
}