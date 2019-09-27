package com.bpwizard.wcm.repo.controllers;

import com.bpwizard.wcm.repo.domain.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bpwizard.spring.boot.commons.service.SpringController;

@RestController
@RequestMapping(MyController.BASE_URI)
public class MyController extends SpringController {
	
	public static final String BASE_URI = "/api/core";

}