package org.camunda.bpm.runtime;

import org.camunda.bpm.runtime.db.CommandExecutor;
import org.camunda.bpm.runtime.db.QueryService;
import org.camunda.bpm.runtime.plugin.CamundaRuntimePlugin;

/**
 * The list of services provided by a camunda webapplication, providing a plugin
 * registry and access to the process engine.
 *
 */
public interface CamundaRuntimeDelegate extends AppRuntimeDelegate<CamundaRuntimePlugin> {

	/**
	 * Returns a configured {@link QueryService} to execute custom statements to the
	 * corresponding process engine.
	 * 
	 * @param processEngineName
	 * @return a {@link QueryService}
	 */
	// public QueryService getQueryService(ProcessEngine processEngine);
	public QueryService getQueryService();

	/**
	 * Returns a configured {@link CommandExecutor} to execute commands to the
	 * corresponding process engine.
	 * 
	 * @param processEngineName
	 * @return a {@link CommandExecutor}
	 */
	// public CommandExecutor getCommandExecutor(ProcessEngine processEngine);
	public CommandExecutor getCommandExecutor();

}
