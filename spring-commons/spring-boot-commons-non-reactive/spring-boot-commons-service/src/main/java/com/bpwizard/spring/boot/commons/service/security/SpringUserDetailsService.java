package com.bpwizard.spring.boot.commons.service.security;

import java.io.Serializable;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.bpwizard.spring.boot.commons.exceptions.util.SpringExceptionUtils;
import com.bpwizard.spring.boot.commons.security.SpringPrincipal;
import com.bpwizard.spring.boot.commons.service.domain.AbstractUser;
import com.bpwizard.spring.boot.commons.service.domain.AbstractUserService;

/**
 * UserDetailsService, as required by Spring Security.
 */
public class SpringUserDetailsService
	<U extends AbstractUser<ID>, ID extends Serializable>
implements UserDetailsService {

	private static final Logger logger = LoggerFactory.getLogger(SpringUserDetailsService.class);

	private final AbstractUserService<U,ID> userService;
	
	public SpringUserDetailsService(AbstractUserService<U, ID> userService) {
		
		this.userService = userService;
		logger.info("Created");
	}
	
	@Override
	public SpringPrincipal loadUserByUsername(String username)
			throws UsernameNotFoundException {
		
		logger.debug("Loading user having username: " + username);
		
		// delegates to findUserByUsername
		U user = findUserByUsername(username)
			.orElseThrow(() -> new UsernameNotFoundException(
				SpringExceptionUtils.getMessage("com.bpwizard.spring.userNotFound", username)));

		logger.debug("Loaded user having username: " + username);

		return new SpringPrincipal(user.toUserDto());
	}

	/**
	 * Finds a user by the given username. Override this
	 * if you aren't using email as the username.
	 */
	public Optional<U> findUserByUsername(String username) {
		return userService.findByEmail(username);
	}
	
	/**
	 * Finds a user by the given username. Override this
	 * if you aren't using email as the username.
	 */
	public Optional<U> findUserByEmail(String email) {
		return userService.findByEmail(email);
	}
}
