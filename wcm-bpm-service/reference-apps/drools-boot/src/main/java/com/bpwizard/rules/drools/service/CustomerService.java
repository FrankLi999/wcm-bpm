package com.bpwizard.rules.drools.service;

import org.kie.api.runtime.KieSession;
import org.springframework.stereotype.Service;
import com.bpwizard.rules.drools.model.Customer;
import com.bpwizard.rules.drools.config.CustomerConfiguration;

@Service
public class CustomerService {

	private KieSession kieSession = new CustomerConfiguration().getKieSession();

	public Customer insertCustomer(Customer customer) {
		kieSession.insert(customer);
		kieSession.fireAllRules();
		return customer;
	}

}