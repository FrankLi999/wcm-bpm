package com.bpwizard.spring.boot.commons.reactive.service.forms;

import com.bpwizard.spring.boot.commons.reactive.service.validation.UniqueEmail;
import com.bpwizard.spring.boot.commons.validation.Password;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class EmailForm {

	@UniqueEmail
	private String newEmail;
	
	@Password
	private String password;
}
