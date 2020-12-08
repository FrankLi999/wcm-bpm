package com.bpwizard.spring.boot.commons.reactive.service.domain;

import java.io.Serializable;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.repository.NoRepositoryBean;

import reactor.core.publisher.Mono;

/**
 * Abstract UserRepository interface
 */
@NoRepositoryBean
public interface AbstractMongoUserRepository
	<U extends AbstractMongoUser<ID>, ID extends Serializable>
	extends ReactiveMongoRepository<U, ID> {
	
	Mono<U> findByEmail(String email);
}
