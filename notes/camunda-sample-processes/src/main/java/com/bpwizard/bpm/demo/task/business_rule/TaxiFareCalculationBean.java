package com.bpwizard.bpm.demo.task.business_rule;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bpwizard.bpm.demo.drools.model.Fare;
import com.bpwizard.bpm.demo.drools.model.TaxiRide;
import com.bpwizard.bpm.demo.drools.service.TaxiFareCalculatorService;

@Component("taxiFareCalculationBean")
public class TaxiFareCalculationBean implements JavaDelegate {
	@Autowired
	TaxiFareCalculatorService taxiFareCalculatorService;
	
	@Override
	public void execute(DelegateExecution execution) throws Exception {
		TaxiRide taxiRide = new TaxiRide();
		taxiRide.setIsNightSurcharge(true);
		taxiRide.setDistanceInMile(190L);
		Fare rideFare = new Fare();
		Long result = taxiFareCalculatorService.calculateFare(taxiRide, rideFare);
		execution.setVariable("taxiFare", result);
	}
}
