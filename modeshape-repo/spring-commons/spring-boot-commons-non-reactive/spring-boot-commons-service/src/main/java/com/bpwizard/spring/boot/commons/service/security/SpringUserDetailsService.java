package com.bpwizard.spring.boot.commons.service.security;

import java.io.Serializable;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.bpwizard.spring.boot.commons.exceptions.util.SpringExceptionUtils;
import com.bpwizard.spring.boot.commons.security.SpringPrincipal;
import com.bpwizard.spring.boot.commons.service.domain.AbstractUser;
import com.bpwizard.spring.boot.commons.service.domain.AbstractUserRepository;

/**
 * UserDetailsService, as required by Spring Security.
 * 
 * @author Sanjay Patel
 */
public class SpringUserDetailsService
	<U extends AbstractUser<ID>, ID extends Serializable>
implements UserDetailsService {

	private static final Logger log = LogManager.getLogger(SpringUserDetailsService.class);

	private final AbstractUserRepository<U,ID> userRepository;
	
	public SpringUserDetailsService(AbstractUserRepository<U, ID> userRepository) {
		
		this.userRepository = userRepository;
		log.info("Created");
	}
	
	@Override
	public SpringPrincipal loadUserByUsername(String username)
			throws UsernameNotFoundException {
		
		log.debug("Loading user having username: " + username);
		
		// delegates to findUserByUsername
		U user = findUserByUsername(username)
			.orElseThrow(() -> new UsernameNotFoundException(
				SpringExceptionUtils.getMessage("com.bpwizard.spring.userNotFound", username)));

		log.debug("Loaded user having username: " + username);

		return new SpringPrincipal(user.toUserDto());
	}

	/**
	 * Finds a user by the given username. Override this
	 * if you aren't using email as the username.
	 */
	public Optional<U> findUserByUsername(String username) {
		return userRepository.findByEmail(username);
	}
	
	/**
	 * Finds a user by the given username. Override this
	 * if you aren't using email as the username.
	 */
	public Optional<U> findUserByEmail(String email) {
		return userRepository.findByEmail(email);
	}
}
