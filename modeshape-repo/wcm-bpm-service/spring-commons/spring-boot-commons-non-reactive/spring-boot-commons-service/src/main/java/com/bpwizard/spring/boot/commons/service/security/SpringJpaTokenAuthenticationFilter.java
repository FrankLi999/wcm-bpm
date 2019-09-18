package com.bpwizard.spring.boot.commons.service.security;

import java.io.Serializable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.bpwizard.spring.boot.commons.security.BlueTokenService;
import com.bpwizard.spring.boot.commons.security.UserDto;
import com.bpwizard.spring.boot.commons.service.domain.AbstractUser;
import com.bpwizard.spring.boot.commons.service.util.ServiceUtils;
import com.bpwizard.spring.boot.commons.web.security.SpringCommonsWebTokenAuthenticationFilter;
import com.nimbusds.jwt.JWTClaimsSet;

public class SpringJpaTokenAuthenticationFilter<U extends AbstractUser<ID>, ID extends Serializable>
	extends SpringCommonsWebTokenAuthenticationFilter {

    private static final Logger log = LogManager.getLogger(SpringJpaTokenAuthenticationFilter.class);

    private SpringUserDetailsService<U, ID> userDetailsService;
	
	public SpringJpaTokenAuthenticationFilter(BlueTokenService blueTokenService,
			SpringUserDetailsService<U, ID> userDetailsService) {
		
		super(blueTokenService);
		this.userDetailsService = userDetailsService;
		
		log.info("Created");		
	}

	@Override
	protected UserDto fetchUserDto(JWTClaimsSet claims) {
		
        String username = claims.getSubject();
        U user = userDetailsService.findUserByUsername(username)
        		.orElseThrow(() -> new UsernameNotFoundException(username));

        log.debug("User found ...");

        ServiceUtils.ensureCredentialsUpToDate(claims, user);
        UserDto userDto = user.toUserDto();
        userDto.setPassword(null);
        
        return userDto;
	}
}
