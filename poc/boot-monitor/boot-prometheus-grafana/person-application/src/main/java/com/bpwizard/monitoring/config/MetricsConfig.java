package com.bpwizard.monitoring.config;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.config.MeterFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MetricsConfig {

	@Value("${host}")
	private String host;

	@Value("${service}")
	private String service;

	@Value("${region}")
	private String region;

	@Bean
	MeterRegistryCustomizer<MeterRegistry> meterRegistryCustomizer() {
		return registry -> registry.config().commonTags("host", host, "service", service, "region", region)
				.meterFilter(MeterFilter.deny(id -> {
					String uri = id.getTag("uri");
					return uri != null && uri.startsWith("/actuator");
				})).meterFilter(MeterFilter.deny(id -> {
					String uri = id.getTag("uri");
					return uri != null && uri.contains("favicon");
				}));
	}
}