package com.bpwizard.spring.boot.commons.cache.ignite;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.ignite.cache.CacheAtomicityMode;
import org.apache.ignite.cache.CacheMode;
import org.apache.ignite.cache.eviction.lru.LruEvictionPolicyFactory;
import org.apache.ignite.cache.spring.SpringCacheManager;
import org.apache.ignite.cache.store.jdbc.CacheJdbcBlobStoreFactory;
import org.apache.ignite.configuration.CacheConfiguration;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.logger.slf4j.Slf4jLogger;
import org.apache.ignite.spi.communication.tcp.TcpCommunicationSpi;
import org.apache.ignite.spi.discovery.tcp.TcpDiscoverySpi;
import org.apache.ignite.spi.discovery.tcp.ipfinder.multicast.TcpDiscoveryMulticastIpFinder;
import org.apache.ignite.springdata.repository.config.EnableIgniteRepositories;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
	private static final Logger logger = LoggerFactory.getLogger(IgniteCacheConfiguration.class);
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
	public CacheManager cacheManager(IgniteProperties igniteProperties) {
    // public CacheManager cacheManager(Ignite ignite) {
		logger.debug("Starting Ignite CacheManager");
        //BpwCacheManager cacheManager = new BpwCacheManager();
		SpringCacheManager cacheManager = new SpringCacheManager();
        cacheManager.setConfiguration(igniteConfiguration(igniteProperties));
        return cacheManager;
    }

    // @Bean(name = "igniteConfiguration")
    public IgniteConfiguration igniteConfiguration(IgniteProperties igniteProperties) {
    	IgniteConfiguration igniteConfiguration = new IgniteConfiguration();
        // igniteConfiguration.setGridName("testGrid");
    	igniteConfiguration.setIgniteInstanceName(igniteProperties.getInstanceName());
        // igniteConfiguration.setClientMode(true);
        // igniteConfiguration.setPeerClassLoadingEnabled(igniteProperties.isPeerClassLoadingEnabled());
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
        igniteConfiguration.setGridLogger( new Slf4jLogger());

        //storage configuration
//        DataStorageConfiguration dsCfgn = new DataStorageConfiguration();
//        igniteConfiguration.setDataStorageConfiguration(dsCfgn);
//        DataRegionConfiguration dataRegConf = new DataRegionConfiguration();
//        dataRegConf.setPersistenceEnabled(true);
////        dataRegConf.setName("2GB_Region");
////        dataRegConf.setInitialSize(50L * 1024 * 1024); //50M
////        dataRegConf.setInitialSize(2L * 1024 * 1024 * 1024); //2GB
////        dataRegConf.setPageEvictionMode(DataPageEvictionMode.RANDOM_2_LRU); //RANDOM_LRU
////        dsCfgn.setDataRegionConfigurations(dataRegConf);
//        dsCfgn.setDefaultDataRegionConfiguration(dataRegConf);
        igniteConfiguration.setCacheConfiguration(cacheConfiguration(igniteProperties.getCaches()));
        
        
        return igniteConfiguration;

    }

    @SuppressWarnings({ "rawtypes", "unchecked"})
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
            if (cacheConfig.isWriteBehindEnabled()) {
            	cacheConfiguration.setWriteBehindFlushFrequency(cacheConfig.getWriteBehindFlushFrequency());
            }
            cacheConfiguration.setBackups(1);
            cacheConfiguration.setStatisticsEnabled(cacheConfig.isStatisticsEnabled());
            cacheConfiguration.setEvictionPolicyFactory(new LruEvictionPolicyFactory(100, 10, 50*1024*1024));
            cacheConfiguration.setOnheapCacheEnabled(true);
            if (cacheConfig.isWriteThrough() || cacheConfig.isReadThrough()) {
            	CacheJdbcBlobStoreFactory storeFactory = new CacheJdbcBlobStoreFactory();
            	storeFactory.setInitSchema(cacheConfig.isInitSchema());
            	//storeFactory.setDataSource(dataSource);
            	storeFactory.setDataSourceBean(cacheConfig.getDataSourceBean());
            	storeFactory.setCreateTableQuery(cacheConfig.getCreateTableQuery());
            	storeFactory.setLoadQuery(cacheConfig.getLoadQuery());
            	storeFactory.setUpdateQuery(cacheConfig.getUpdateQuery());
            	storeFactory.setInsertQuery(cacheConfig.getInsertQuery());
            	storeFactory.setDeleteQuery(cacheConfig.getDeleteQuery());
            	cacheConfiguration.setCacheStoreFactory(storeFactory);
            }
            
            cacheConfigurations.add(cacheConfiguration);
        }
        return cacheConfigurations.toArray(new CacheConfiguration[cacheConfigurations.size()]);
    }
    
//    @Bean(name = "igniteDataSource")
//    public DataSource igniteSqlDataSource() {
//        @SuppressWarnings("rawtypes")
//		DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
//        dataSourceBuilder.driverClassName("com.mysql.jdbc.Driver");
//        dataSourceBuilder.url("jdbc:mysql://mysql:3306/wcm_bpm?allowPublicKeyRetrieval=true&useSSL=false&autoReconnect=true&nullNamePatternMatchesAll=true");
//        dataSourceBuilder.username("wcmbpm");
//        dataSourceBuilder.password("P@ssw0rd");
//        return dataSourceBuilder.build();
//    }
}
