package com.bpwizard.spring.boot.commons.reactive.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter @AllArgsConstructor
public class TestUser {

	private String email, password, name;
}
