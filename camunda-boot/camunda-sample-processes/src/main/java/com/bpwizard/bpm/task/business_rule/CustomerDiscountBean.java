package com.bpwizard.bpm.task.business_rule;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bpwizard.bpm.drools.model.Customer;
import com.bpwizard.bpm.drools.service.CustomerService;

@Component("customerDiscountBean")
public class CustomerDiscountBean {
	@Autowired
	CustomerService customerService;
	
	/*
	 * called from business_rule.bpmn
	 */
	public int customerDiscount() {
		Customer customer = new Customer(Customer.CustomerType.INDIVIDUAL, 2);
		int discount = customerService.customerDiscount(customer);
		return discount;
	}
}
