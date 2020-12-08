package com.bpwizard.spring.boot.commons.demo.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(MyController.BASE_URI)
public class MyController {
	
	public static final String BASE_URI = "/my";
	private static final Logger logger = LoggerFactory.getLogger(MyController.class);
	@GetMapping("/greeting")
	public String getGreeting() {
		logger.info("Called");
		return "Hello create";
	}

}