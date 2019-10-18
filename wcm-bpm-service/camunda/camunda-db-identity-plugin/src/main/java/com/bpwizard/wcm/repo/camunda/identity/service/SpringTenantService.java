package com.bpwizard.wcm.repo.camunda.identity.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.bpwizard.camunda.identity.boot.plugin.SpringTenantQuery;
import com.bpwizard.wcm.repo.camunda.identity.domain.CustomeSpringTenantRepository;
import com.bpwizard.wcm.repo.camunda.identity.domain.SpringTenant;

public class SpringTenantService {
	@Autowired
	private CustomeSpringTenantRepository repository;

	public SpringTenant findById(String id) {
		return repository.findById(id);
	}

	public List<SpringTenant> findAll(SpringTenantQuery query) {
		return repository.findIdentityByQuery(query);
	}
	
	public long countAll(SpringTenantQuery query) {
		return repository.countIdentityByQuery(query);
	}
}
