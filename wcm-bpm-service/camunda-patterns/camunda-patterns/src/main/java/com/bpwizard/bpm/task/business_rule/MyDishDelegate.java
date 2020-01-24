package com.bpwizard.bpm.task.business_rule;

import java.util.List;

import org.camunda.bpm.dmn.engine.DmnDecisionTableResult;
import org.camunda.bpm.engine.DecisionService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.variable.VariableMap;
import org.camunda.bpm.engine.variable.Variables;

public class MyDishDelegate implements JavaDelegate {

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		DecisionService decisionService = execution.getProcessEngine().getDecisionService();

		String season = (String) execution.getVariable("season");
		Integer guestCount = (Integer)execution.getVariable("guestCount");
		Boolean guestsWithChildren = (Boolean) execution.getVariable("guestsWithChildren");
		VariableMap variables = Variables.createVariables()
				.putValue("season", season)
				.putValue("guestCount", guestCount)
				.putValue("guestsWithChildren", guestsWithChildren);

		//DmnDecisionTableResult dishDecisionResult = 
		decisionService.evaluateDecisionTableByKey("dish", variables);
		//String desiredDish = dishDecisionResult.getSingleEntry();
		DmnDecisionTableResult beveragesDecisionResult = decisionService.evaluateDecisionTableByKey("beverages",
				variables);
		List<Object> beverages = beveragesDecisionResult.collectEntries("beverages");
		
		execution.setVariable("myBeberages", beverages);
	}
}