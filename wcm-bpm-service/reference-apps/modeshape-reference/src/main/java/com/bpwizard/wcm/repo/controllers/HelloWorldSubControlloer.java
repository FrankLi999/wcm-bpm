package com.bpwizard.wcm.repo.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hello/{id}")
public class HelloWorldSubControlloer {

	@GetMapping()
	public String getCreate(@PathVariable("id") String id) {
		return "Hello " + id + " create"; 
	}
	
	@GetMapping("/update")
	public String getUpdate(@PathVariable("id") String id) {
		return "Hello " + id + " update"; 
	}
}
