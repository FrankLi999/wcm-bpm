package com.bpwizard.wcm.repo.camunda.identity.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import com.google.common.base.Optional;

public interface SpringTenantRepository extends JpaRepository<SpringTenant, String> {
	Optional<SpringTenant> findByName(String id);
}
