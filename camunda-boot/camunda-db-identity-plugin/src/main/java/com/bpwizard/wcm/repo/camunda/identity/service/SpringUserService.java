package com.bpwizard.wcm.repo.camunda.identity.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bpwizard.camunda.identity.boot.plugin.SpringUserQuery;
import com.bpwizard.wcm.repo.camunda.identity.domain.CustomeSpringUserRepository;
import com.bpwizard.wcm.repo.camunda.identity.domain.SpringUser;

@Service
@Transactional
public class SpringUserService {

	@Autowired
	private CustomeSpringUserRepository repository;

	public SpringUser findById(String id) {
		return repository.findById(id);
	}

	public List<SpringUser> findAll(SpringUserQuery query) {
		return repository.findIdentityByQuery(query);
	}
	
	public long countAll(SpringUserQuery query) {
		return repository.countIdentityByQuery(query);
	}
}
