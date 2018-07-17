package com.bpwizard.myresources;

import java.io.IOException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.boot.env.YamlPropertySourceLoader;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
 
/**
 * Take a look at /src/main/resources/META-INF/spring.factories
 * @author fli
 *
 */
public class MyServiceAutoConfig implements EnvironmentPostProcessor {

	private final YamlPropertySourceLoader loader = new YamlPropertySourceLoader();
	private final String filePrefix = "my-service";
	@Override
	public void postProcessEnvironment(ConfigurableEnvironment environment,
			SpringApplication application) {
		for (String activeProfile : environment.getActiveProfiles()) {
			Resource classPathResource = new ClassPathResource(filePrefix + "-" + activeProfile + ".yml");
			loadYaml(environment, classPathResource);
			
		}
		Resource classPathResource = new ClassPathResource(filePrefix + ".yml");
		loadYaml(environment, classPathResource);
	}

	private void loadYaml(ConfigurableEnvironment environment, Resource path) {
//		if (!path.exists()) {
//			throw new IllegalArgumentException("Resource " + path + " does not exist");
//		}
		if (path.exists()) {
			try {
				PropertySource<?> propertySource =  this.loader.load("custom-resource", path).get(0);
				environment.getPropertySources().addLast(propertySource);
			} catch (IOException ex) {
				throw new IllegalStateException(
						"Failed to load yaml configuration from " + path, ex);
			}
		}
	}
}