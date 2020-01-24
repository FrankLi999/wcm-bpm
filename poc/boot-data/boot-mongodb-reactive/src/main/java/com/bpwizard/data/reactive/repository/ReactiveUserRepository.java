package com.bpwizard.data.reactive.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.bpwizard.data.model.User;


public interface ReactiveUserRepository extends ReactiveMongoRepository<User, String> {
}
