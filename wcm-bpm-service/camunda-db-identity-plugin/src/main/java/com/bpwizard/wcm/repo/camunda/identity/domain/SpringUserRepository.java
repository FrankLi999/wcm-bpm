package com.bpwizard.wcm.repo.camunda.identity.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.google.common.base.Optional;

@Repository
public interface SpringUserRepository extends JpaRepository<SpringUser, String> {
	Optional<SpringUser> findByName(String id);
}
