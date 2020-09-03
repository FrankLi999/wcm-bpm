package org.camunda.bpm.runtime.plugin;

/**
 * The service provider interface (SPI) that must be provided by a webapplication plugin.
 *
 * <p>
 *
 * A implementation of this SPI publishes
 *
 * <ul>
 *   <li>a unique ID</li>
 *   <li>a directory that contains the plugins client-side assets (HTML + JavaScript files)</li>
 *   <li>a number of resource classes that extend the restful API</li>
 * </ul>
 *
 * <p>
 *
 * Plugin developers should not use this interface directly but use {@link AbstractPlugin} as a base class.
 *
 *
 */
public interface Plugin {

  /**
   * Returns the unique id of this plugin.
   *
   * @return
   */
  public String getId();

//  /**
//   * Returns a set of JAX-RS resource classes that extend the rest API.
//   *
//   * <p>
//   *
//   * Typically, a plugin publishes its API via a subclass of 
//   * {@link org.camunda.bpm.runtime.rest.controller.AbstractRuntimeRestController}.
//   *
//   * @return the set of resource classes provided by this plugin
//   */
//  public Set<Class<?>> getResourceClasses();
//
//  /**
//   * Returns a uri to a plugin resources directory.
//   * The directory must be unique across all plugins.
//   *
//   * @return the directory providing the plugins client side resources
//   */
//  public String getAssetDirectory();
//
//  /**
//   * Allows providing a list of {@link PluginResourceOverride resource overrides}. Resource overrides allow
//   * to conditionally override the static resources provided by other plugins.
//   *
//   * @return a list of {@link PluginResourceOverride} implementations.
//   */
//  public List<PluginResourceOverride> getResourceOverrides();


}
