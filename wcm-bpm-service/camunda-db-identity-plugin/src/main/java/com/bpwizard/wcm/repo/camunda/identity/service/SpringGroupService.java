package com.bpwizard.wcm.repo.camunda.identity.service;

import com.bpwizard.wcm.repo.camunda.identity.domain.SpringGroup;
import com.bpwizard.wcm.repo.camunda.identity.domain.SpringGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

@Service
@Transactional
public class SpringGroupService {

	@Autowired
	private SpringGroupRepository repository;

	public SpringGroup findById(String id) {
		return repository.findById(id).get();
	}

	public Collection<SpringGroup> findAll() {
		return (Collection<SpringGroup>) repository.findAll();
	}
}
