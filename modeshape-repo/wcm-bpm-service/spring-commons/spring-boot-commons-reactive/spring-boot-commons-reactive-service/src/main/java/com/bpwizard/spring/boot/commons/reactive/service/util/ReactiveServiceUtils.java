package com.bpwizard.spring.boot.commons.reactive.service.util;

import java.io.Serializable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.bpwizard.spring.boot.commons.reactive.service.domain.AbstractMongoUser;
import com.bpwizard.spring.boot.commons.security.SpringTokenService;
import com.bpwizard.spring.boot.commons.util.SecurityUtils;
import com.nimbusds.jwt.JWTClaimsSet;

/**
 * Useful helper methods
 * 
 * @author Sanjay Patel
 */
public class ReactiveServiceUtils {
	
	private static final Logger log = LogManager.getLogger(ReactiveServiceUtils.class);
	
	/**
	 * Throws BadCredentialsException if 
	 * user's credentials were updated after the JWT was issued
	 */
	public static <U extends AbstractMongoUser<ID>, ID extends Serializable>
	void ensureCredentialsUpToDate(JWTClaimsSet claims, U user) {
		
		long issueTime = (long) claims.getClaim(SpringTokenService.BPW_IAT);

		log.debug("Ensuring credentials up to date. Issue time = "
				+ issueTime + ". User's credentials updated at" + user.getCredentialsUpdatedMillis());
		
		SecurityUtils.ensureCredentials(issueTime >= user.getCredentialsUpdatedMillis(),
				"com.bpwizard.spring.obsoleteToken");
	}
}
