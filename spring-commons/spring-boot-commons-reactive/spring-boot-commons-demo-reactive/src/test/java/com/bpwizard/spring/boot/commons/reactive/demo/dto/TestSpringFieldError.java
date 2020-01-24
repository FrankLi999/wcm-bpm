package com.bpwizard.spring.boot.commons.reactive.demo.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public class TestSpringFieldError {
	
	private String field;
	private String code;
	private String message;
}
