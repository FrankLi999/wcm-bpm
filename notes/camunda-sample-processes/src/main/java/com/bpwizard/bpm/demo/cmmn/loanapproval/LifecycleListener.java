package com.bpwizard.bpm.demo.cmmn.loanapproval;

import org.camunda.bpm.engine.delegate.CaseExecutionListener;
import org.camunda.bpm.engine.delegate.DelegateCaseExecution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LifecycleListener implements CaseExecutionListener {

	private static final Logger logger = LoggerFactory.getLogger(LifecycleListener.class);

	  public void notify(DelegateCaseExecution caseExecution) throws Exception {
		  logger.info("Plan Item '" + caseExecution.getActivityId() + "' labeled '" + caseExecution.getActivityName() + "' has performed transition: "
	        + caseExecution.getEventName());
		  System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>> Plan Item '" + caseExecution.getActivityId() + "' labeled '" + caseExecution.getActivityName() + "' has performed transition: "
			        + caseExecution.getEventName());
	  }

	}