package com.bpwizard.wcm.repo.camunda.identity.service;

import com.bpwizard.wcm.repo.camunda.identity.domain.SpringUser;
import com.bpwizard.wcm.repo.camunda.identity.domain.SpringUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

@Service
@Transactional
public class SpringUserService {

	private final SpringUserRepository repository;

	@Autowired
	public SpringUserService(SpringUserRepository repository) {
		this.repository = repository;
	}

	public SpringUser findById(String id) {
		return repository.findById(id).get();
	}

	public Collection<SpringUser> findAll() {
		return (Collection<SpringUser>) repository.findAll();
	}

}
