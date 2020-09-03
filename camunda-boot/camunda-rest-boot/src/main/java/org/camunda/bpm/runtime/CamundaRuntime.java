package org.camunda.bpm.runtime;

import org.camunda.bpm.runtime.db.CommandExecutor;
import org.camunda.bpm.runtime.db.QueryService;

public class CamundaRuntime {
	/**
	 * The {@link CockpitRuntimeDelegate} is an delegate that will be initialized by
	 * bootstrapping camunda cockpit with an specific instance
	 */
	protected static CamundaRuntimeDelegate COCKPIT_RUNTIME_DELEGATE;

	/**
	 * Returns a configured {@link QueryService} to execute custom statements to the
	 * corresponding process engine
	 *
	 * @param processEngineName
	 *
	 * @return a {@link QueryService}
	 */
	public static QueryService getQueryService() {
		return getRuntimeDelegate().getQueryService();
	}

	/**
	 * Returns a configured {@link CommandExecutor} to execute commands to the
	 * corresponding process engine
	 *
	 * @param processEngineName
	 *
	 * @return a {@link CommandExecutor}
	 */
	public static CommandExecutor getCommandExecutor() {
		return getRuntimeDelegate().getCommandExecutor();
	}

	/**
	 * Returns an instance of {@link CockpitRuntimeDelegate}
	 *
	 * @return
	 */
	public static CamundaRuntimeDelegate getRuntimeDelegate() {
		return COCKPIT_RUNTIME_DELEGATE;
	}

	/**
	 * A setter to set the {@link CockpitRuntimeDelegate}.
	 * 
	 * @param cockpitRuntimeDelegate
	 */
	public static void setCamundaRuntimeDelegate(CamundaRuntimeDelegate camundaRuntimeDelegate) {
		COCKPIT_RUNTIME_DELEGATE = camundaRuntimeDelegate;
	}

}
