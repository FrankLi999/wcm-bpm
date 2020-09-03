package org.camunda.bpm.runtime.plugin;

import java.util.List;

/**
 * The service provider interface (SPI) that must be provided by
 * a cockpit plugin.
 *
 * A cockpit plugin may provide additional MyBatis mapping files, see {@link #getMappingFiles()}.
 *
 * Plugin developers should not use this interface directly but use
 * {@link org.camunda.bpm.cockpit.plugin.spi.impl.AbstractCockpitPlugin} as a base class.
 *
 * @see org.camunda.bpm.cockpit.plugin.spi.impl.AbstractCockpitPlugin
 */
public interface CamundaRuntimePlugin extends Plugin {

  /**
   * Returns a list of mapping files that define the custom queries
   * provided by this plugin.
   *
   * <p>
   *
   * The mapping files define additional MyBatis queries that can be executed by the plugin.
   *
   * <p>
   *
   * Inside the plugin the queries may be executed via the {@link org.camunda.bpm.cockpit.db.QueryService} that may be obtained through
   * {@link org.camunda.bpm.cockpit.Cockpit#getQueryService(java.lang.String) }.
   *
   * @return the list of additional mapping files
   */
  public List<String> getMappingFiles();


}
