package org.camunda.bpm.runtime;

import org.camunda.bpm.runtime.plugin.AppPluginRegistry;
import org.camunda.bpm.runtime.plugin.Plugin;

public interface AppRuntimeDelegate<T extends Plugin> {

	/**
	 * A registry that provides access to the plugins registered in the application.
	 *
	 * @return
	 */
	public AppPluginRegistry<T> getAppPluginRegistry();

//	/**
//	 * A list of resource overrides.
//	 * 
//	 * @return the list of registered resource overrides
//	 */
//	public List<PluginResourceOverride> getResourceOverrides();

}
