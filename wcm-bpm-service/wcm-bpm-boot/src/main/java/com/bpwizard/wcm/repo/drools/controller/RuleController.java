package com.bpwizard.wcm.repo.drools.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bpwizard.wcm.repo.drools.model.Customer;
import com.bpwizard.wcm.repo.drools.model.Fare;
import com.bpwizard.wcm.repo.drools.model.TaxiRide;
import com.bpwizard.wcm.repo.drools.service.CustomerService;
import com.bpwizard.wcm.repo.drools.service.TaxiFareCalculatorService;

@RestController
@RequestMapping("/drools/api")
public class RuleController {

	@Autowired
	TaxiFareCalculatorService taxiFareCalculatorService;

	@Autowired
	CustomerService customerService;
	
	@GetMapping("/taxi-fare")
	public Long taxiFare() {
		TaxiRide taxiRide = new TaxiRide();
		taxiRide.setIsNightSurcharge(true);
		taxiRide.setDistanceInMile(190L);
		Fare rideFare = new Fare();
		Long result = taxiFareCalculatorService.calculateFare(taxiRide, rideFare);
		return result;
	}
	
	@GetMapping("/customer-discount")
	public int customerDiscount(@RequestParam("type") Customer.CustomerType type, @RequestParam("age") int age) {
		Customer customer = new Customer(type, age);

		customerService.insertCustomer(customer);
		int discount = customer.getDiscount();
		return discount;
	}
}
