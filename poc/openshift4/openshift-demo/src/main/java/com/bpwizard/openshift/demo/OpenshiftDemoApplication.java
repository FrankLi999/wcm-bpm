package com.bpwizard.openshift.demo;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class OpenshiftDemoApplication {
	private static final Logger logger = LogManager.getLogger(OpenshiftDemoApplication.class);
	public static void main(String[] args) {
		logger.traceEntry("Started openshift demo");
		SpringApplication.run(OpenshiftDemoApplication.class, args);
		logger.traceExit("Started openshift demo");
	}

}
