package com.bpwizard.spring.boot.commons.service.util;

import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.bpwizard.spring.boot.commons.security.SpringPrincipal;
import com.bpwizard.spring.boot.commons.security.SpringTokenService;
import com.bpwizard.spring.boot.commons.service.domain.AbstractUser;
import com.bpwizard.spring.boot.commons.util.SecurityUtils;
import com.nimbusds.jwt.JWTClaimsSet;

/**
 * Useful helper methods
 */
public class ServiceUtils {
	
	private static final Logger logger = LoggerFactory.getLogger(ServiceUtils.class);

	public ServiceUtils() {
		
		logger.info("Created");
	}


	/**
	 * Signs a user in
	 * 
	 * @param user
	 */
	public static <U extends AbstractUser<ID>, ID extends Serializable>
	void login(U user) {
		
		SpringPrincipal principal = new SpringPrincipal(user.toUserDto());

		Authentication authentication = // make the authentication object
	    	new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities());

	    SecurityContextHolder.getContext().setAuthentication(authentication); // put that in the security context
	    principal.eraseCredentials();
	}
	
	
	/**
	 * Throws BadCredentialsException if 
	 * user's credentials were updated after the JWT was issued
	 */
	public static <U extends AbstractUser<ID>, ID extends Serializable>
	void ensureCredentialsUpToDate(JWTClaimsSet claims, U user) {
		
		long issueTime = (long) claims.getClaim(SpringTokenService.BPW_IAT);

		SecurityUtils.ensureCredentials(issueTime >= user.getCredentialsUpdatedMillis(),
				"com.bpwizard.spring.obsoleteToken");
	}
}
