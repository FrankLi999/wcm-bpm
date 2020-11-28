package com.bpwizard.spring.boot.commons.demo.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(MyController.BASE_URI)
public class MyController {
	
	public static final String BASE_URI = "/my";

	@GetMapping("/greeting")
	public String getGreeting() {
		return "Hello create";
	}

}