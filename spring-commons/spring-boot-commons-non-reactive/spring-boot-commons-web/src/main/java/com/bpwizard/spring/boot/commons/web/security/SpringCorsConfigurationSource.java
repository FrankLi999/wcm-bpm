package com.bpwizard.spring.boot.commons.web.security;

import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import com.bpwizard.spring.boot.commons.SpringProperties;
import com.bpwizard.spring.boot.commons.SpringProperties.Cors;

/**
 * CORS Configuration
 */
public class SpringCorsConfigurationSource implements CorsConfigurationSource {

	private static final Logger logger = LoggerFactory.getLogger(SpringCorsConfigurationSource.class);

	private Cors cors;

	public SpringCorsConfigurationSource(SpringProperties properties) {

		this.cors = properties.getCors();
		logger.info("Created");
	}

	@Override
	public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
		
		CorsConfiguration config = new CorsConfiguration();
		
		config.setAllowCredentials(true);
		config.setAllowedHeaders(Arrays.asList(cors.getAllowedHeaders()));
		config.setAllowedMethods(Arrays.asList(cors.getAllowedMethods()));
		config.setAllowedOrigins(Arrays.asList(cors.getAllowedOrigins()));
		config.setExposedHeaders(Arrays.asList(cors.getExposedHeaders()));
		config.setMaxAge(cors.getMaxAge());
		
		return config;
	}

}
