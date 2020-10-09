package com.bpwizard.wcm.repo.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hello")
public class HelloWorldControlloer {
	@GetMapping("/create")
	public String getCreate() {
		return "Hello create";
	}
	
	@GetMapping("/update")
	public String getUpdate() {
		return "Hello update";
	}
}
