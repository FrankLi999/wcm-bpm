package com.bpwizard.wcm.repo.camunda.identity.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bpwizard.camunda.identity.boot.plugin.SpringGroupQuery;
import com.bpwizard.wcm.repo.camunda.identity.domain.CustomeSpringGroupRepository;
import com.bpwizard.wcm.repo.camunda.identity.domain.SpringGroup;

@Service
@Transactional
public class SpringGroupService {

	@Autowired
	private CustomeSpringGroupRepository repository;

	public SpringGroup findById(String id) {
		return repository.findById(id);
	}

	public List<SpringGroup> findAll(SpringGroupQuery query) {
		return repository.findIdentityByQuery(query);
	}
	
	public long countAll(SpringGroupQuery query) {
		return repository.countIdentityByQuery(query);
	}
}
