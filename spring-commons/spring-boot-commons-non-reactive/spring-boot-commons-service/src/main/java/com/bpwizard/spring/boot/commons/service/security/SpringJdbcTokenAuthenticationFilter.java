package com.bpwizard.spring.boot.commons.service.security;

import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.bpwizard.spring.boot.commons.security.JSONWebSignatureService;
import com.bpwizard.spring.boot.commons.security.UserDto;
import com.bpwizard.spring.boot.commons.service.domain.User;
import com.bpwizard.spring.boot.commons.service.util.ServiceUtils;
import com.bpwizard.spring.boot.commons.web.security.SpringCommonsWebTokenAuthenticationFilter;
import com.nimbusds.jwt.JWTClaimsSet;

public class SpringJdbcTokenAuthenticationFilter<U extends User<ID>, ID extends Serializable>
	extends SpringCommonsWebTokenAuthenticationFilter {

    private static final Logger logger = LoggerFactory.getLogger(SpringJdbcTokenAuthenticationFilter.class);

    private SpringUserDetailsService<U, ID> userDetailsService;
	
	public SpringJdbcTokenAuthenticationFilter(JSONWebSignatureService jwsTokenService,
			SpringUserDetailsService<U, ID> userDetailsService) {
		
		super(jwsTokenService);
		this.userDetailsService = userDetailsService;
		
		logger.info("Created");		
	}

	@Override
	protected UserDto fetchUserDto(JWTClaimsSet claims) {
		
        String username = claims.getSubject();
        U user = userDetailsService.findUserByUsername(username)
        		.orElseThrow(() -> new UsernameNotFoundException(username));

        logger.debug("User found ...");

        ServiceUtils.ensureCredentialsUpToDate(claims, user);
        UserDto userDto = user.toUserDto();
        userDto.setPassword(null);
        
        return userDto;
	}
}
