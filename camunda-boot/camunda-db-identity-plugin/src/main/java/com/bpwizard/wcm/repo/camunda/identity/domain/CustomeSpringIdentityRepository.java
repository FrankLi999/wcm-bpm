package com.bpwizard.wcm.repo.camunda.identity.domain;

import java.util.List;

public interface CustomeSpringIdentityRepository<T, Q> {
	T findById(String id);
    List<T> findIdentityByQuery(Q query);
    long countIdentityByQuery(Q query);
}
