package com.bpwizard.spring.boot.commons.cache;

import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import com.hazelcast.config.Config;
import com.hazelcast.config.EntryListenerConfig;
import com.hazelcast.config.EvictionPolicy;
import com.hazelcast.config.ManagementCenterConfig;
import com.hazelcast.config.MapAttributeConfig;
import com.hazelcast.config.MapConfig;
import com.hazelcast.config.MapIndexConfig;
import com.hazelcast.config.MaxSizeConfig;
import com.hazelcast.config.TcpIpConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.ManagedContext;
import com.hazelcast.jet.Jet;
import com.hazelcast.jet.JetInstance;
import com.hazelcast.jet.config.JetConfig;
import com.hazelcast.spring.context.SpringManagedContext;

@Configuration
@EnableCaching
// @PropertySource("classpath:hazelcast.properties")
// @ConfigurationProperties(prefix="hazelcast")
@ConditionalOnProperty(name = "hazelcast.enabled", havingValue = "true")
public class HazelcastConfiguration {
	private static final Logger log = LogManager.getLogger(HazelcastConfiguration.class);

	@Bean
	// @ConditionalOnProperty(name="hazelcast.enabled", havingValue="true")
	public CacheManager cacheManager(JetInstance jetInstance) {
		log.debug("Starting Hazelcast CacheManager");
		return new com.hazelcast.spring.cache.HazelcastCacheManager(jetInstance.getHazelcastInstance());
	}

	@Bean
	@ConfigurationProperties(prefix = "hazelcast")
	// @ConditionalOnProperty(name="hazelcast.enabled", havingValue="true")
	public HazelcastProperties hazelCastProperties() {
		return new HazelcastProperties();
	}

	/**
	 * A {@code ManagedContext} implementation bean which enables
	 * {@code @SpringAware} annotation for de-serialized objects.
	 */
	@Bean
	// @ConditionalOnProperty(name="hazelcast.enabled", havingValue="true")
	public ManagedContext managedContext() {
		return new SpringManagedContext();
	}

	/**
	 * {@code JetInstance} bean which configured programmatically with
	 * {@code SpringManagedContext}
	 */
	@Bean
	// @ConditionalOnProperty(name="hazelcast.enabled", havingValue="true")
	public JetInstance jetInstance(HazelcastProperties hazelcastProperties) {
		Config config = hazelCastConfig(hazelcastProperties);
		config.setManagedContext(managedContext());
		JetConfig jetConfig = new JetConfig().setHazelcastConfig(config);
		JetInstance jetInstance = Jet.newJetInstance(jetConfig);
		if (hazelcastProperties.getCpMemberCounts() > 0) {
			try {
				jetInstance.getHazelcastInstance().getCPSubsystem().getCPSubsystemManagementService()
						.awaitUntilDiscoveryCompleted(1, TimeUnit.MINUTES);
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
		}
		return jetInstance;
	}

	@Bean
	// @ConditionalOnProperty(name="hazelcast.enabled", havingValue="true")
	public HazelcastInstance HazelcastInstance(JetInstance jetInstance) {
		return jetInstance.getHazelcastInstance();
	}

	private Config hazelCastConfig(HazelcastProperties hazelcastProperties) {
		Config config = new Config();
		if (hazelcastProperties.getCpMemberCounts() > 0) {
			config.getCPSubsystemConfig().setCPMemberCount(hazelcastProperties.getCpMemberCounts());
			config.getCPSubsystemConfig().setGroupSize(hazelcastProperties.getCpGroupSize());
		}
		config.setProperty("hazelcast.logging.type", hazelcastProperties.getLoggingType());
		config.setInstanceName(hazelcastProperties.getInstanceName());
		config.getNetworkConfig().getJoin().getMulticastConfig().setEnabled(false).setMulticastGroup("224.2.2.3")
				.setMulticastPort(54327);
		config.getNetworkConfig().setPortAutoIncrement(false).setPort(hazelcastProperties.getPort());
		TcpIpConfig tcpipConfig = config.getNetworkConfig().getJoin().getTcpIpConfig();
		tcpipConfig.setEnabled(true);

		for (String member : hazelcastProperties.getMembers().split(",")) {
			tcpipConfig.addMember(member);
		}

		if (StringUtils.hasText(hazelcastProperties.getManagementCenter().getUrl())) {
			ManagementCenterConfig managementCenterConfig = new ManagementCenterConfig();
			// managementCenterConfig.setEnabled(true);
			managementCenterConfig.setUrl(hazelcastProperties.getManagementCenter().getUrl());
			// managementCenterConfig.setUpdateInterval(updateInterval);
			// managementCenterConfig.setScriptingEnabled(scriptingEnabled);
			managementCenterConfig.setEnabled(hazelcastProperties.getManagementCenter().isEnabled());
			config.setManagementCenterConfig(managementCenterConfig);
		}

		if (config.getNetworkConfig().getSSLConfig() != null) {
			// Enterprise Edition Feature?
			config.getNetworkConfig().getSSLConfig().setFactoryClassName("com.hazelcast.nio.ssl.BasicSSLContextFactor")
					.setEnabled(hazelcastProperties.isEnableSSL()).setProperty("keyStore", hazelcastProperties.getKeyStore())
					.setProperty("keyStorePassword", hazelcastProperties.getKeyStorePassword())
					.setProperty("keyManagerAlgorithm", hazelcastProperties.getKeyManagerAlgorithm())
					.setProperty("trustManagerAlgorithm", hazelcastProperties.getTrustManagerAlgorithm())
					.setProperty("protocol", "TLS");
		}
		if (config.getNetworkConfig().getSymmetricEncryptionConfig() != null) {
			// Enterprise Edition Feature?
			config.getNetworkConfig().getSymmetricEncryptionConfig().setEnabled(hazelcastProperties.isEnableEncryption())
					.setAlgorithm(hazelcastProperties.getEncryptionAlgorithm()).setIterationCount(19)
					.setPassword(hazelcastProperties.getEncryptionPassword()).setSalt(hazelcastProperties.getEncryptionSalt());
		}
		for (CacheConfig cacheConfig : hazelcastProperties.getCaches()) {
			config.getMapConfigs().put(cacheConfig.getCacheName(), this.initCache(cacheConfig));
		}
		return config;
	}

	private MapConfig initCache(CacheConfig cacheConfig) {

		MapConfig mapConfig = new MapConfig().setName(cacheConfig.getCacheName())
				.setMaxSizeConfig(new MaxSizeConfig(cacheConfig.getMaxSize(),
						MaxSizeConfig.MaxSizePolicy.valueOf(cacheConfig.getMaxSizePolicy())))
				.setEvictionPolicy(EvictionPolicy.valueOf(cacheConfig.getEvictionPolicy()))
				.setTimeToLiveSeconds(cacheConfig.getTimeToLiveSeconds()).setBackupCount(cacheConfig.getBackupCount());

		for (CacheConfig.EntryListener entryListener : cacheConfig.getEntryListeners()) {
			EntryListenerConfig listenerConfig = new EntryListenerConfig();
			listenerConfig.setClassName(entryListener.getListener());
			listenerConfig.setLocal(entryListener.isLocal());
			listenerConfig.setIncludeValue(entryListener.isIncludeValue());
			mapConfig.addEntryListenerConfig(listenerConfig);
		}

		for (CacheConfig.Index index : cacheConfig.getIndexes()) {
			MapIndexConfig mapIndexConfig = new MapIndexConfig();
			mapIndexConfig.setAttribute(index.getAttribute());
			mapIndexConfig.setOrdered(index.isOrdered());
			mapConfig.addMapIndexConfig(mapIndexConfig);
		}

		for (CacheConfig.Attribute attribute : cacheConfig.getAttributes()) {
			MapAttributeConfig mapAttributeConfig = new MapAttributeConfig();
			mapAttributeConfig.setName(attribute.getName());
			mapAttributeConfig.setExtractor(attribute.getExtractor());
			mapConfig.addMapAttributeConfig(mapAttributeConfig);
		}
		// MapStoreConfig mapStoreConfig = new MapStoreConfig();
		// mapConfig.setMapStoreConfig(mapStoreConfig);
		return mapConfig;
	}
}
