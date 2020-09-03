package org.camunda.bpm.runtime.plugin;

public abstract class AbstractPlugin implements Plugin {

	  /**
	   * Returns a uri to a plugin resources directory.
	   * The directory must be unique across all plugins.
	   *
	   * <p>
	   *
	   * This implementation assumes that the resources are provided in the directory <code>plugin-webapp/PLUGIN_ID</code>,
	   * absolute to the root directory.
	   *
	   * @return the directory providing the plugins client side resources
	   */
//	  public String getAssetDirectory() {
//	    return String.format("plugin-webapp/%s", getId());
//	  }
//
//	  public Set<Class<?>> getResourceClasses() {
//	    return Collections.emptySet();
//	  }
//
//	  public List<PluginResourceOverride> getResourceOverrides() {
//	    return Collections.emptyList();
//	  }

	}
