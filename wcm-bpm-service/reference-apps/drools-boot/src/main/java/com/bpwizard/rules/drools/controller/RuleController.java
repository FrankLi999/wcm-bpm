package com.bpwizard.rules.drools.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bpwizard.rules.drools.model.Fare;
import com.bpwizard.rules.drools.model.TaxiRide;
import com.bpwizard.rules.drools.service.TaxiFareCalculatorService;

@RestController
@RequestMapping("/rule/api")
public class RuleController {

	@Autowired
	TaxiFareCalculatorService taxiFareCalculatorService;

	@GetMapping("/hello-world")
	public Long submitJob() {
		TaxiRide taxiRide = new TaxiRide();
		taxiRide.setIsNightSurcharge(true);
		taxiRide.setDistanceInMile(190L);
		Fare rideFare = new Fare();
		Long result = taxiFareCalculatorService.calculateFare(taxiRide, rideFare);
		return result;
	}
}
