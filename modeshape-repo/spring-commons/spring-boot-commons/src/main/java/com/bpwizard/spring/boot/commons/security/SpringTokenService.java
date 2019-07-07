package com.bpwizard.spring.boot.commons.security;

import java.util.Map;

import com.nimbusds.jwt.JWTClaimsSet;

public interface SpringTokenService {

	String BPW_IAT = "bpw-iat";

	String createToken(String aud, String subject, Long expirationMillis, Map<String, Object> claimMap);
	String createToken(String audience, String subject, Long expirationMillis);
	JWTClaimsSet parseToken(String token, String audience);
	JWTClaimsSet parseToken(String token, String audience, long issuedAfter);
	<T> T parseClaim(String token, String claim);
}