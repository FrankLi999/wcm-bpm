package org.camunda.bpm.runtime.plugin;

import java.util.List;

/**
 * The holder of registered {@link AppPlugin AppPlugins}.
 */
public interface AppPluginRegistry<T extends Plugin> {

  /**
   * Returns all registered plugins
   *
   * @return
   */
  public List<T> getPlugins();

  /**
   * Returns the registered plugin with the given name or
   * <code>null</code> if the plugin does not exist.
   *
   * @param id
   * @return
   */
  public T getPlugin(String id);
}
