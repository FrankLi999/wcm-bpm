package com.bpwizard.wcm.modeshape.user.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import com.bpwizard.wcm.modeshape.user.domain.User;

import reactor.core.publisher.Mono;

/**
 * @author duc-d
 */
@Repository
public interface UserRepository extends ReactiveMongoRepository<User, String> {
    Mono<User> findByLogin(String login);
}
