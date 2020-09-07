package com.bpwizard.spring.boot.commons.web.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.bpwizard.spring.boot.commons.exceptions.util.SpringExceptionUtils;
import com.bpwizard.spring.boot.commons.security.JSONWebSignatureService;
import com.bpwizard.spring.boot.commons.security.SpringPrincipal;
import com.bpwizard.spring.boot.commons.security.UserDto;
import com.bpwizard.spring.boot.commons.util.SecurityUtils;
import com.nimbusds.jwt.JWTClaimsSet;

import lombok.AllArgsConstructor;

/**
 * Filter for token authentication
 */
@AllArgsConstructor
public class SpringCommonsWebTokenAuthenticationFilter extends OncePerRequestFilter {
	
    private static final Logger log = LogManager.getLogger(SpringCommonsWebTokenAuthenticationFilter.class);
    
    private JSONWebSignatureService jwsTokenService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		log.debug("Inside SpringTokenAuthenticationFilter ...");
		
		String header = request.getHeader(HttpHeaders.AUTHORIZATION);				
		
    	if (header != null && header.startsWith(SecurityUtils.TOKEN_PREFIX)) { // token present
			
			log.debug("Found a token");			
		    String token = header.substring(7);
		    
		    try {
		    	
		    	Authentication auth = createAuthToken(token);
		    	SecurityContextHolder.getContext().setAuthentication(auth);
		    	
				log.debug("Token authentication successful");
				    		    	
		    } catch (Exception e) {
		    	
				log.debug("Token authentication failed - " + e.getMessage());
				
		    	response.sendError(HttpServletResponse.SC_UNAUTHORIZED,
						"Authentication Failed: " + e.getMessage());
		    	
		    	return;
		    }
		    
		} else
		
			log.debug("Token authentication skipped");
		
		filterChain.doFilter(request, response);
	}

	protected Authentication createAuthToken(String token) {
		
		JWTClaimsSet claims = jwsTokenService.parseToken(token, JSONWebSignatureService.AUTH_AUDIENCE);
		UserDto userDto = SecurityUtils.getUserDto(claims);
		if (userDto == null)
			userDto = fetchUserDto(claims);
		
        SpringPrincipal principal = new SpringPrincipal(userDto);
        		
        return new UsernamePasswordAuthenticationToken(principal, token, principal.getAuthorities());
	}

	/**
	 * Default behaviour is to throw error. To be overridden in auth service.
	 * 
	 * @param username
	 * @return
	 */
	protected UserDto fetchUserDto(JWTClaimsSet claims) {
		throw new AuthenticationCredentialsNotFoundException(
				SpringExceptionUtils.getMessage("com.bpwizard.spring.userClaimAbsent"));
	}
}
