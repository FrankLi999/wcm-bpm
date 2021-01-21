package com.bpwizard.wcm.repo.collector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.bpwizard.spring.boot.commons.security.JSONWebSignatureService;
import com.bpwizard.spring.boot.commons.security.SpringPrincipal;
import com.bpwizard.spring.boot.commons.security.UserDto;
import com.bpwizard.spring.boot.commons.service.domain.User;
import com.bpwizard.spring.boot.commons.service.security.SpringUserDetailsService;
import com.bpwizard.spring.boot.commons.service.util.ServiceUtils;
import com.bpwizard.spring.boot.commons.util.SecurityUtils;
import com.nimbusds.jwt.JWTClaimsSet;

@Service
public class KafkaSecurityService {
	private static final Logger logger = LoggerFactory.getLogger(KafkaSecurityService.class);
	@Autowired
	protected JSONWebSignatureService jwsTokenService;

	@Autowired
	protected SpringUserDetailsService<User<Long>, Long> userDetailsService;
	
	public void setAuthenticationContext(String token) {	
		SecurityContextHolder.getContext().setAuthentication(this.createAuthToken(token));
	}

	public void cleabAuthenticationContext() {	
		SecurityContextHolder.clearContext();
	}
	
	protected Authentication createAuthToken(String token) {
		
		JWTClaimsSet claims = jwsTokenService.parseToken(token, JSONWebSignatureService.AUTH_AUDIENCE);
		UserDto userDto = SecurityUtils.getUserDto(claims);
		if (userDto == null)
			userDto = fetchUserDto(claims);
		
        SpringPrincipal principal = new SpringPrincipal(userDto);
        		
        return new UsernamePasswordAuthenticationToken(principal, token, principal.getAuthorities());
	}

	protected UserDto fetchUserDto(JWTClaimsSet claims) {
		
        String username = claims.getSubject();
        User<Long> user = userDetailsService.findUserByUsername(username)
        		.orElseThrow(() -> new UsernameNotFoundException(username));

        logger.debug("User found ...");

        ServiceUtils.ensureCredentialsUpToDate(claims, user);
        UserDto userDto = user.toUserDto();
        userDto.setPassword(null);
        
        return userDto;
	}
}
