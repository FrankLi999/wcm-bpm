package com.bpwizard.spring.boot.commons.validation;

/**
 * A form using RetypePassword constraint
 * should implement this interface
 */
public interface RetypePasswordForm {

	String getPassword();
	String getRetypePassword();
}
