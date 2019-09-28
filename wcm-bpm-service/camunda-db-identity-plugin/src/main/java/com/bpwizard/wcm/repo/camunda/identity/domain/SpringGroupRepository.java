package com.bpwizard.wcm.repo.camunda.identity.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.google.common.base.Optional;

@Repository
public interface SpringGroupRepository extends JpaRepository<SpringGroup, String> {
	Optional<SpringGroup> findByName(String id);
}
