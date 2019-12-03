package com.bpwizard.spring.boot.commons.service.repo.domain;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TenantRepository extends JpaRepository<Tenant, Long> {
	Optional<Tenant> findByName(String name);
}

