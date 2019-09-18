package com.bpwizard.spring.boot.commons.demo.repositories;

import com.bpwizard.spring.boot.commons.demo.entities.User;
import com.bpwizard.spring.boot.commons.service.domain.AbstractUserRepository;

public interface UserRepository extends AbstractUserRepository<User, Long> {

}