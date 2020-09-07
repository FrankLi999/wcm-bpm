package com.bpwizard.spring.boot.commons.security;

public interface JSONWebEncryptionService extends SpringTokenService {

	String VERIFY_AUDIENCE = "verify";
	String FORGOT_PASSWORD_AUDIENCE = "forgot-password";
	String CHANGE_EMAIL_AUDIENCE = "change-email";
}
