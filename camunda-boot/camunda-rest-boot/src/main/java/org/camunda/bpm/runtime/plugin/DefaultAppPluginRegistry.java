package org.camunda.bpm.runtime.plugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;

/**
 * Default implementation of {@link AppPluginRegistry} that loads Plugins via
 * the {@link ServiceLoader} facilities provided by the Java SE platform.
 *
 */
public class DefaultAppPluginRegistry<T extends Plugin> implements AppPluginRegistry<T> {

	/** the interface type of plugins managed by this registry */
	protected final Class<T> pluginType;

	protected Map<String, T> pluginsMap;

	public DefaultAppPluginRegistry(Class<T> pluginType) {
		this.pluginType = pluginType;
	}

	protected void loadPlugins() {

		ServiceLoader<T> loader = ServiceLoader.load(pluginType);

		Iterator<T> iterator = loader.iterator();

		Map<String, T> map = new HashMap<String, T>();

		while (iterator.hasNext()) {
			T plugin = iterator.next();
			map.put(plugin.getId(), plugin);
		}

		this.pluginsMap = map;
	}

	@Override
	public List<T> getPlugins() {
		if (pluginsMap == null) {
			loadPlugins();
		}

		return new ArrayList<T>(pluginsMap.values());
	}

	@Override
	public T getPlugin(String id) {

		if (pluginsMap == null) {
			loadPlugins();
		}

		return pluginsMap.get(id);
	}
}
