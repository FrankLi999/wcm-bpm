package com.bpwizard.spring.boot.commons.reactive.demo.repositories;
import org.bson.types.ObjectId;

import com.bpwizard.spring.boot.commons.reactive.demo.domain.User;
import com.bpwizard.spring.boot.commons.reactive.service.domain.AbstractMongoUserRepository;

public interface UserRepository extends AbstractMongoUserRepository<User, ObjectId> {

}
