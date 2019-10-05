package com.bpwizard.wcm.repo.camunda.identity.domain;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("unchecked")
public abstract class CustomeSpringIdentityRepositoryImpl<T, Q> implements CustomeSpringIdentityRepository<T, Q> {
	@Autowired
    protected EntityManager entityManager;

	@Override
	public List<T> findIdentityByQuery(Q query) {
		Query jpaQuery = this.buildSearchQuery(query);
		return (List<T>)jpaQuery.getResultList();
		// return null;
	}

	@Override
	public long countIdentityByQuery(Q query) {
		Query jpaQuery = this.buildSearchQuery(query);
		return (long)jpaQuery.getSingleResult();
	}

	
	protected Query buildSearchQuery(Q q) {
		return null;
	}
	
	protected Query buildCountQuery(Q q) {
		return null;
	}

}
