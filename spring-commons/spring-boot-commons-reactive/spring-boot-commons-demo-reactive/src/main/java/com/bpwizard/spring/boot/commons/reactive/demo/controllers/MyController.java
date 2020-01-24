package com.bpwizard.spring.boot.commons.reactive.demo.controllers;

import org.bson.types.ObjectId;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bpwizard.spring.boot.commons.reactive.demo.domain.User;
import com.bpwizard.spring.boot.commons.reactive.service.SpringReactiveController;
import com.bpwizard.spring.boot.commons.security.UserDto;
import com.bpwizard.spring.boot.commons.util.UserUtils;
import com.bpwizard.spring.boot.commons.util.UserUtils.SignUpValidation;
import com.fasterxml.jackson.annotation.JsonView;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping(MyController.BASE_URI)
public class MyController extends SpringReactiveController<User, ObjectId> {

	public static final String BASE_URI = "/api/core";

	@Override
	public Mono<UserDto> signup(
			@RequestBody @JsonView(UserUtils.SignupInput.class)
			@Validated(SignUpValidation.class) Mono<User> user,
			ServerHttpResponse response) {
		
		return super.signup(user, response);
	}
}
