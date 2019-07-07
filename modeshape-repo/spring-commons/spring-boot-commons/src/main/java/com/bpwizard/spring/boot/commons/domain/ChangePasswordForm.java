package com.bpwizard.spring.boot.commons.domain;

import com.bpwizard.spring.boot.commons.validation.Password;
import com.bpwizard.spring.boot.commons.validation.RetypePassword;
import com.bpwizard.spring.boot.commons.validation.RetypePasswordForm;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Change password form.
 * 
 * @author Sanjay Patel
 */
@RetypePassword
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class ChangePasswordForm implements RetypePasswordForm {
	
	@Password
	private String oldPassword;

	@Password
	private String password;
	
	@Password
	private String retypePassword;
}
