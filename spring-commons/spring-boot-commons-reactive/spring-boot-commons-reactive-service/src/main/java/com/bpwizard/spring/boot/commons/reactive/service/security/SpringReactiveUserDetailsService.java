package com.bpwizard.spring.boot.commons.reactive.service.security;

import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.bpwizard.spring.boot.commons.exceptions.util.SpringExceptionUtils;
import com.bpwizard.spring.boot.commons.reactive.service.domain.AbstractMongoUser;
import com.bpwizard.spring.boot.commons.reactive.service.domain.AbstractMongoUserRepository;
import com.bpwizard.spring.boot.commons.security.SpringPrincipal;

import reactor.core.publisher.Mono;

public class SpringReactiveUserDetailsService<U extends AbstractMongoUser<ID>, ID extends Serializable>
		implements ReactiveUserDetailsService {

	private static final Logger logger = LoggerFactory.getLogger(SpringReactiveUserDetailsService.class);

	private final AbstractMongoUserRepository<U, ID> userRepository;

	public SpringReactiveUserDetailsService(AbstractMongoUserRepository<U, ID> userRepository) {

		this.userRepository = userRepository;
		logger.info("Created");
	}

	@Override
	public Mono<UserDetails> findByUsername(String username) {

		logger.debug("Loading user having username: " + username);

		// delegates to findUserByUsername
		return findUserByUsername(username).switchIfEmpty(Mono.defer(() -> {
			logger.debug("Could not find user " + username);
			return Mono.error(new UsernameNotFoundException(
				SpringExceptionUtils.getMessage("com.bpwizard.spring.userNotFound", username)));
		})).map(U::toUserDto).map(SpringPrincipal::new);
	}

	/**
	 * Finds a user by the given username. Override this if you aren't using email
	 * as the username.
	 */
	public Mono<U> findUserByUsername(String username) {
		return userRepository.findByEmail(username);
	}
}
