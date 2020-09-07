package com.bpwizard.spring.boot.commons.security;

public interface JSONWebSignatureService extends SpringTokenService {

	String USER_CLAIM = "user";
	String AUTH_AUDIENCE = "auth";
}
