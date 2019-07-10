package com.bpwizard.wcm.repo.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bpwizard.wcm.repo.webdav.ApplicationContextProvider;

@RestController
@RequestMapping("/hello")
public class HelloWorldController {
	@GetMapping("/world")
    public String greeting() {
		return "Hello";
    }
}
