package com.bpwizard.data.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.bpwizard.data.model.Action;

public interface ActionRepository extends MongoRepository<Action, String> { }