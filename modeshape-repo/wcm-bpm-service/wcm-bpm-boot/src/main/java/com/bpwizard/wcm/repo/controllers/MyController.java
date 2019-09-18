package com.bpwizard.wcm.repo.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bpwizard.spring.boot.commons.service.SpringController;
import com.bpwizard.wcm.repo.entities.User;

@RestController
@RequestMapping(MyController.BASE_URI)
public class MyController extends SpringController<User, Long> {
	
	public static final String BASE_URI = "/api/core";

}