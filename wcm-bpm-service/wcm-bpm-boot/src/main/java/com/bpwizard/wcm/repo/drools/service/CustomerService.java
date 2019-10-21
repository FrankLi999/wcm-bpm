package com.bpwizard.wcm.repo.drools.service;

import org.kie.api.runtime.KieSession;
import org.springframework.stereotype.Service;

import com.bpwizard.wcm.repo.drools.config.CustomerConfiguration;
import com.bpwizard.wcm.repo.drools.model.Customer;

@Service
public class CustomerService {

	private KieSession kieSession = new CustomerConfiguration().getKieSession();

	public Customer insertCustomer(Customer customer) {
		kieSession.insert(customer);
		kieSession.fireAllRules();
		return customer;
	}

}