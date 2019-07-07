package com.bpwizard.spring.boot.commons.cache;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.validation.annotation.Validated;

import lombok.Getter;
import lombok.Setter;

@Validated
@PropertySource("classpath:hazelcast.properties")
@ConfigurationProperties(prefix="hazelcast")
@Getter @Setter
public class HazelcastProperties {
	private int port;
	private String members;
	private String groupName;
	private String instanceName;
	
	private String keyStorePassword;
	private boolean enableSSL;
	private String keyStore;
	private String keyManagerAlgorithm;
	private String trustManagerAlgorithm;
	
	private boolean enableEncryption;
	private String encryptionAlgorithm;
	private String encryptionPassword;
	private String encryptionSalt;
	
	List<CacheConfig> caches;
}