package org.camunda.bpm.engine.rest.sub.task.controller;

import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.history.HistoricTaskInstance;
import org.camunda.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.camunda.bpm.engine.impl.identity.Authentication;
import org.camunda.bpm.engine.rest.exception.InvalidRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

public class AbstractTaskResourceRestController {
	@Autowired
	protected ProcessEngine engine;
	protected String rootResourcePath = "http://localhost:8080";
	protected boolean isHistoryEnabled() {
		IdentityService identityService = engine.getIdentityService();
		Authentication currentAuthentication = identityService.getCurrentAuthentication();
		try {
			identityService.clearAuthentication();
			int historyLevel = engine.getManagementService().getHistoryLevel();
			return historyLevel > ProcessEngineConfigurationImpl.HISTORYLEVEL_NONE;
		} finally {
			identityService.setAuthentication(currentAuthentication);
		}
	}

	protected void ensureHistoryEnabled(HttpStatus status) {
		if (!isHistoryEnabled()) {
			throw new InvalidRequestException(status, "History is not enabled");
		}
	}

	protected void ensureTaskExists(String taskId, HttpStatus status) {
		HistoricTaskInstance historicTaskInstance = engine.getHistoryService().createHistoricTaskInstanceQuery()
				.taskId(taskId).singleResult();
		if (historicTaskInstance == null) {
			throw new InvalidRequestException(status, "No task found for task id " + taskId);
		}
	}
}
