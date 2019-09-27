package com.bpwizard.wcm.repo.domain;

import com.bpwizard.spring.boot.commons.service.domain.AbstractUserRepository;

public interface UserRepository extends AbstractUserRepository<User, Long> {
    Boolean existsByEmail(String email);
}