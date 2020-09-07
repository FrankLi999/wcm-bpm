package com.bpwizard.spring.boot.commons.reactive.service.util;

import java.io.Serializable;
import java.util.Collections;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpCookie;
import org.springframework.http.ResponseCookie;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;

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
	
	public static void deleteCookies(ServerWebExchange exchange, String ...cookiesToDelete) {
		
		MultiValueMap<String, HttpCookie> cookies = exchange.getRequest().getCookies();
		MultiValueMap<String, ResponseCookie> responseCookies = exchange.getResponse().getCookies();
		
		for (int i = 0; i < cookiesToDelete.length; i++)
			if (cookies.getFirst(cookiesToDelete[i]) != null) {
				
				ResponseCookie cookie = ResponseCookie.from(cookiesToDelete[i], "")
					.path("/")
					.maxAge(0L)
					.build();
				
				responseCookies.put(cookiesToDelete[i], Collections.singletonList(cookie));
			};			
	}
}
