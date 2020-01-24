package com.bpwizard.spring.boot.commons.service.repo.domain;

import java.util.Optional;

import javax.persistence.QueryHint;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;

@Repository
public interface TenantRepository extends JpaRepository<Tenant, Long> {
	@QueryHints ({ @QueryHint(name = "org.hibernate.cacheable", value ="true") })
	Optional<Tenant> findByName(String name);
}

