package org.camunda.bpm.runtime;


import java.util.List;

import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.runtime.plugin.AppPluginRegistry;
import org.camunda.bpm.runtime.plugin.DefaultAppPluginRegistry;
import org.camunda.bpm.runtime.plugin.Plugin;
import org.camunda.bpm.runtime.plugin.PluginResourceOverride;

public abstract class AbstractCamundaRuntimeDelegate<T extends Plugin> implements AppRuntimeDelegate<T> {

  protected final AppPluginRegistry<T> pluginRegistry;
  protected final ProcessEngine processEngine;

  protected List<PluginResourceOverride> resourceOverrides;
 
  public AbstractCamundaRuntimeDelegate(Class<T> pluginType, ProcessEngine processEngine) {
    this.pluginRegistry = new DefaultAppPluginRegistry<T>(pluginType);
    this.processEngine = processEngine;
  }

//  public ProcessEngine getProcessEngine(String processEngineName) {
//    try {
//      return processEngineProvider.getProcessEngine(processEngineName);
//    } catch (Exception e) {
//      throw new ProcessEngineException("No process engine with name " + processEngineName + " found.", e);
//    }
//  }
//
//  public Set<String> getProcessEngineNames() {
//    return processEngineProvider.getProcessEngineNames();
//  }
//
//  public ProcessEngine getDefaultProcessEngine() {
//    return processEngineProvider.getDefaultProcessEngine();
//  }

  public AppPluginRegistry<T> getAppPluginRegistry() {
    return pluginRegistry;
  }
  
	public ProcessEngine getProcessEngine() {
		return this.processEngine;
	}

  /**
   * Load the {@link ProcessEngineProvider} spi implementation.
   *
   * @return
   */
//  protected ProcessEngineProvider loadProcessEngineProvider() {
//    ServiceLoader<ProcessEngineProvider> loader = ServiceLoader.load(ProcessEngineProvider.class);
//
//    try {
//      return loader.iterator().next();
//    } catch (NoSuchElementException e) {
//      String message = String.format("No implementation for the %s spi found on classpath", ProcessEngineProvider.class.getName());
//      throw new IllegalStateException(message, e);
//    }
//  }

//  public List<PluginResourceOverride> getResourceOverrides() {
//    if(resourceOverrides == null) {
//      initResourceOverrides();
//    }
//    return resourceOverrides;
//  }
//
//  
//  protected synchronized void initResourceOverrides() {
//    if(resourceOverrides == null) { // double-checked sync, do not remove
//      resourceOverrides = new ArrayList<PluginResourceOverride>();
//      List<T> plugins = pluginRegistry.getPlugins();
//      for (T p : plugins) {
//        resourceOverrides.addAll(p.getResourceOverrides());
//      }
//    }
//  }

}