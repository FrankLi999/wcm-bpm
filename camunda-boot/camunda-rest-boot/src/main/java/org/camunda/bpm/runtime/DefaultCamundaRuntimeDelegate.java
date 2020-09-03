package org.camunda.bpm.runtime;

import java.util.ArrayList;
import java.util.List;

import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.impl.ProcessEngineImpl;
import org.camunda.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.camunda.bpm.runtime.db.CommandExecutor;
import org.camunda.bpm.runtime.db.CommandExecutorImpl;
import org.camunda.bpm.runtime.db.QueryService;
import org.camunda.bpm.runtime.db.QueryServiceImpl;
import org.camunda.bpm.runtime.plugin.CamundaRuntimePlugin;

public class DefaultCamundaRuntimeDelegate extends AbstractCamundaRuntimeDelegate<CamundaRuntimePlugin>
		implements CamundaRuntimeDelegate {

	// private Map<String, CommandExecutor> commandExecutors;
	private CommandExecutor commandExecutor;

	public DefaultCamundaRuntimeDelegate(ProcessEngine processEngine) {
	    super(CamundaRuntimePlugin.class, processEngine);
	    // this.commandExecutors = new HashMap<String, CommandExecutor>();
	    commandExecutor = this.createCommandExecutor();
	  }

	@Override
	public QueryService getQueryService() {
		CommandExecutor commandExecutor = getCommandExecutor();
		return new QueryServiceImpl(commandExecutor);
	}

	@Override
	public CommandExecutor getCommandExecutor() {
		return this.commandExecutor;
//		CommandExecutor commandExecutor = commandExecutors.get(this.processEngin);
//		if (commandExecutor == null) {
//			commandExecutor = createCommandExecutor(this.processEngine);
//			commandExecutors.put(processEngineName, commandExecutor);
//		}
//
//		return commandExecutor;
	}

	/**
//	 * Deprecated: use {@link #getAppPluginRegistry()}
//	 */
//	@Deprecated
//	public AppPluginRegistry getPluginRegistry() {
//		return new DefaultAppPluginRegistry(this.pluginRegistry);
//	}

	/**
	 * Returns the list of mapping files that should be used to create the session
	 * factory for this runtime.
	 *
	 * @return
	 */
	protected List<String> getMappingFiles() {
		List<CamundaRuntimePlugin> cockpitPlugins = pluginRegistry.getPlugins();

		List<String> mappingFiles = new ArrayList<String>();
		for (CamundaRuntimePlugin plugin : cockpitPlugins) {
			mappingFiles.addAll(plugin.getMappingFiles());
		}

		return mappingFiles;
	}

	/**
	 * Create command executor for the engine with the given name
	 *
	 * @param processEngineName
	 * @return
	 */
	protected CommandExecutor createCommandExecutor() {

		ProcessEngineConfigurationImpl processEngineConfiguration = ((ProcessEngineImpl) this.processEngine)
				.getProcessEngineConfiguration();
		List<String> mappingFiles = getMappingFiles();

		return new CommandExecutorImpl(processEngineConfiguration, mappingFiles);
	}

}
