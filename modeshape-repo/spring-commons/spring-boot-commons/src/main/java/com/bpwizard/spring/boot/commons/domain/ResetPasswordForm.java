package com.bpwizard.spring.boot.commons.domain;

import javax.validation.constraints.NotBlank;

import com.bpwizard.spring.boot.commons.validation.Password;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ResetPasswordForm {
	
	@NotBlank
	private String code;
	
	@Password
	private String newPassword;
}
