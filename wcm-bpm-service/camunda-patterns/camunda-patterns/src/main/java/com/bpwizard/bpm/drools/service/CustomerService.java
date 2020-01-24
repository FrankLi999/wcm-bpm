package com.bpwizard.bpm.drools.service;

import org.kie.api.runtime.KieSession;
import org.springframework.stereotype.Service;

import com.bpwizard.bpm.drools.config.CustomerConfiguration;
import com.bpwizard.bpm.drools.model.Customer;

@Service
public class CustomerService {

	private KieSession kieSession = new CustomerConfiguration().getKieSession();

	public int customerDiscount(Customer customer) {
		kieSession.insert(customer);
		kieSession.fireAllRules();
		return customer.getDiscount();
	}

}