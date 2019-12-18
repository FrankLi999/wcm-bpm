package com.bpwizard.spring.boot.commons.service.repo.domain;

import javax.persistence.QueryHint;

import org.springframework.data.jpa.repository.QueryHints;

import com.bpwizard.spring.boot.commons.service.domain.AbstractUserRepository;

public interface UserRepository extends AbstractUserRepository<User, Long> {
	@QueryHints ({ @QueryHint(name = "org.hibernate.cacheable", value ="true") })
	Boolean existsByEmail(String email);
}