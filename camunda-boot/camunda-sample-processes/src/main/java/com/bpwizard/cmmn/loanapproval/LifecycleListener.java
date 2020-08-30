package com.bpwizard.cmmn.loanapproval;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.camunda.bpm.engine.delegate.CaseExecutionListener;
import org.camunda.bpm.engine.delegate.DelegateCaseExecution;

public class LifecycleListener implements CaseExecutionListener {

	private static final Logger logger = LogManager.getLogger(LifecycleListener.class);

	  public void notify(DelegateCaseExecution caseExecution) throws Exception {
		  logger.info("Plan Item '" + caseExecution.getActivityId() + "' labeled '" + caseExecution.getActivityName() + "' has performed transition: "
	        + caseExecution.getEventName());
		  System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>> Plan Item '" + caseExecution.getActivityId() + "' labeled '" + caseExecution.getActivityName() + "' has performed transition: "
			        + caseExecution.getEventName());
	  }

	}