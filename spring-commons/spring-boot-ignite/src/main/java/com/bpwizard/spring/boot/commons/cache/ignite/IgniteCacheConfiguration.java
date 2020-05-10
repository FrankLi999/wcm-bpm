package com.bpwizard.spring.boot.commons.cache.ignite;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.ignite.Ignite;
import org.apache.ignite.Ignition;
import org.apache.ignite.cache.CacheAtomicityMode;
import org.apache.ignite.cache.CacheMode;
import org.apache.ignite.cache.spring.SpringCacheManager;
import org.apache.ignite.configuration.CacheConfiguration;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.spi.communication.tcp.TcpCommunicationSpi;
import org.apache.ignite.spi.discovery.tcp.TcpDiscoverySpi;
import org.apache.ignite.spi.discovery.tcp.ipfinder.multicast.TcpDiscoveryMulticastIpFinder;
import org.apache.ignite.springdata.repository.config.EnableIgniteRepositories;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
@EnableIgniteRepositories
@ConditionalOnProperty(name = "ignite.enabled", havingValue = "true")
public class IgniteCacheConfiguration {
	private static final Logger log = LogManager.getLogger(IgniteCacheConfiguration.class);
    // https://github.com/Romeh/spring-boot-ignite/blob/master/src/main/java/com/romeh/ignitemanager/config/AlertManagerConfiguration.java
	@Bean
	@ConfigurationProperties(prefix = "ignite")
	public IgniteProperties igniteProperties() {
		return new IgniteProperties();
	}
	
//	@Bean
//	public Ignite igniteInstance(IgniteConfiguration igniteConfiguration) {
//		log.debug("Starting Ignite CacheManager");
//		
//		// return Ignition.start(igniteConfiguration);
//		try(Ignite ignite = Ignition.start(igniteConfiguration)) {         
//			System.out.println("Node name "+ignite.name());
//			return ignite;
//		}
//	}
//	
	@Bean
	public CacheManager cacheManager(IgniteConfiguration igniteConfiguration) {
    // public CacheManager cacheManager(Ignite ignite) {
		log.debug("Starting Ignite CacheManager");
        SpringCacheManager cacheManager = new SpringCacheManager();
        //cacheManager.setConfiguration(ignite.configuration());
        cacheManager.setConfiguration(igniteConfiguration);
        return cacheManager;
    }

    @Bean(name = "igniteConfiguration")
    public IgniteConfiguration igniteConfiguration(IgniteProperties igniteProperties) {
    	IgniteConfiguration igniteConfiguration = new IgniteConfiguration();
        // igniteConfiguration.setGridName("testGrid");
    	igniteConfiguration.setIgniteInstanceName(igniteProperties.getInstanceName());
        // igniteConfiguration.setClientMode(true);
        igniteConfiguration.setPeerClassLoadingEnabled(igniteProperties.isPeerClassLoadingEnabled());
        // igniteConfiguration.setLocalHost(igniteProperties.getLocalHost());

        TcpDiscoverySpi tcpDiscoverySpi = new TcpDiscoverySpi();
        TcpDiscoveryMulticastIpFinder ipFinder = new TcpDiscoveryMulticastIpFinder();
        ipFinder.setAddresses(Collections.singletonList(igniteProperties.getIpfinderAddresses()));
        tcpDiscoverySpi.setIpFinder(ipFinder);
        tcpDiscoverySpi.setLocalPort(igniteProperties.getTcpDiscoveryLocalPort());
        // Changing local port range. This is an optional action.
        tcpDiscoverySpi.setLocalPortRange(igniteProperties.getTcpDiscoveryLocalPortRange());
        //tcpDiscoverySpi.setLocalAddress("localhost");
        igniteConfiguration.setDiscoverySpi(tcpDiscoverySpi);

        TcpCommunicationSpi communicationSpi = new TcpCommunicationSpi();
        communicationSpi.setLocalAddress(igniteProperties.getCommunicationSpiLocalAddress());
        communicationSpi.setLocalPort(igniteProperties.getCommunicationSpiLocalPort());
        communicationSpi.setSlowClientQueueLimit(igniteProperties.getCommunicationSpiSlowClientQueueLimit());
        igniteConfiguration.setCommunicationSpi(communicationSpi);


        igniteConfiguration.setCacheConfiguration(cacheConfiguration(igniteProperties.getCaches()));

        return igniteConfiguration;

    }

    @SuppressWarnings({ "rawtypes"})
	//@Bean(name = "cacheConfiguration")
    public CacheConfiguration[] cacheConfiguration(List<CacheConfig> caches) {
        List<CacheConfiguration> cacheConfigurations = new ArrayList<>();
        for (CacheConfig cacheConfig: caches) {
        	CacheConfiguration cacheConfiguration = new CacheConfiguration();
            cacheConfiguration.setAtomicityMode(CacheAtomicityMode.valueOf(cacheConfig.getAtomicityMode()));
            cacheConfiguration.setCacheMode(CacheMode.valueOf(cacheConfig.getCacheMode()));
            cacheConfiguration.setName(cacheConfig.getCacheName());
            cacheConfiguration.setWriteThrough(cacheConfig.isWriteThrough());
            cacheConfiguration.setReadThrough(cacheConfig.isReadThrough());
            cacheConfiguration.setWriteBehindEnabled(cacheConfig.isWriteBehindEnabled());
            cacheConfiguration.setBackups(1);
            cacheConfiguration.setStatisticsEnabled(cacheConfig.isStatisticsEnabled());
            cacheConfigurations.add(cacheConfiguration);
        }
        return cacheConfigurations.toArray(new CacheConfiguration[cacheConfigurations.size()]);
    }
}
