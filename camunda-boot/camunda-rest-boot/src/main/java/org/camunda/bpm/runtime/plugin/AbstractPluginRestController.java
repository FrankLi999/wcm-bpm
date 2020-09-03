package org.camunda.bpm.runtime.plugin;

import org.camunda.bpm.engine.rest.controller.AbstractRestProcessEngineAware;
import org.camunda.bpm.runtime.AppRuntimeDelegate;

/**
 * A abstract plugin resource class that may be used to implement
 * REST resources which are added to the REST application of the app.
 *
 */
public abstract class AbstractPluginRestController<T extends Plugin> extends AbstractRestProcessEngineAware {

  protected AppRuntimeDelegate<T> runtimeDelegate;

  public AbstractPluginRestController(AppRuntimeDelegate<T> runtimeDelegate) {
    this.runtimeDelegate = runtimeDelegate;
  }


}
