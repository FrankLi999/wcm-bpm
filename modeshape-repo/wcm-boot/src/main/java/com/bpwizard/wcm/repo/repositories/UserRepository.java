package com.bpwizard.wcm.repo.repositories;

import com.bpwizard.spring.boot.commons.service.domain.AbstractUserRepository;
import com.bpwizard.wcm.repo.entities.User;

public interface UserRepository extends AbstractUserRepository<User, Long> {
    Boolean existsByEmail(String email);
}