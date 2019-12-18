package com.bpwizard.spring.boot.commons;

import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;

import com.bpwizard.spring.boot.commons.cache.CacheConfig;
import com.bpwizard.spring.boot.commons.cache.HazelcastProperties;
import com.bpwizard.spring.boot.commons.exceptions.CommonsExceptionsAutoConfiguration;
import com.bpwizard.spring.boot.commons.exceptions.handlers.BadCredentialsExceptionHandler;
import com.bpwizard.spring.boot.commons.mail.MailSender;
import com.bpwizard.spring.boot.commons.mail.MockMailSender;
import com.bpwizard.spring.boot.commons.mail.SmtpMailSender;
import com.bpwizard.spring.boot.commons.security.BlueTokenService;
import com.bpwizard.spring.boot.commons.security.GreenTokenService;
import com.bpwizard.spring.boot.commons.security.SpringJweService;
import com.bpwizard.spring.boot.commons.security.SpringJwsService;
import com.bpwizard.spring.boot.commons.security.SpringPermissionEvaluator;
import com.bpwizard.spring.boot.commons.util.SecurityUtils;
import com.bpwizard.spring.boot.commons.vlidation.CaptchaValidator;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.KeyLengthException;
import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;

@Configuration
@ComponentScan(basePackageClasses= {BadCredentialsExceptionHandler.class})
@EnableAsync
@EnableCaching
@EnableEncryptableProperties
// @PropertySource("classpath:hazelcast.properties")
// @ConfigurationProperties(prefix="hazelcast")
@AutoConfigureBefore({
	CommonsExceptionsAutoConfiguration.class})
public class CommonsAutoConfiguration {

	private static final Logger log = LogManager.getLogger(CommonsAutoConfiguration.class);
	
	public CommonsAutoConfiguration() {
		log.info("Created");
	}
	
	
	/**
	 * Spring Commons related properties
	 */	
	@Bean
	public SpringProperties springProperties() {
		
        log.info("Configuring SpringProperties");       
		return new SpringProperties();
	}
	

	/**
	 * Configures AuthTokenService if missing
	 */
	@Bean
	@ConditionalOnMissingBean(BlueTokenService.class)
	public BlueTokenService blueTokenService(SpringProperties properties) throws JOSEException {
		
        log.info("Configuring AuthTokenService");       
		return new SpringJwsService(properties.getJwt().getSecret());
	}


	/**
	 * Configures ExternalTokenService if missing
	 */
	@Bean
	@ConditionalOnMissingBean(GreenTokenService.class)
	public GreenTokenService greenTokenService(SpringProperties properties) throws KeyLengthException {
		
        log.info("Configuring ExternalTokenService");       
		return new SpringJweService(properties.getJwt().getSecret());
	}


	/**
	 * Configures Password encoder if missing
	 */
	@Bean
	@ConditionalOnMissingBean(PasswordEncoder.class)
    public PasswordEncoder passwordEncoder() {
	
		log.info("Configuring PasswordEncoder");		
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
	
	
	/**
	 * Configures PermissionEvaluator if missing
	 */
	@Bean
	@ConditionalOnMissingBean(PermissionEvaluator.class)
	public PermissionEvaluator permissionEvaluator() {
		
        log.info("Configuring SpringPermissionEvaluator");       
		return new SpringPermissionEvaluator();
	}


	/**
	 * Configures a MockMailSender when the property
	 * <code>spring.mail.host</code> isn't defined.
	 */
	@Bean
	@ConditionalOnMissingBean(MailSender.class)
	@ConditionalOnProperty(name="spring.mail.host", havingValue="foo", matchIfMissing=true)
	public MailSender<?> mockMailSender() {

        log.info("Configuring MockMailSender");       
        return new MockMailSender();
	}

	
	/**
	 * Configures an SmtpMailSender when the property
	 * <code>spring.mail.host</code> is defined.
	 */
	@Bean
	@ConditionalOnMissingBean(MailSender.class)
	@ConditionalOnProperty("spring.mail.host")
	public MailSender<?> smtpMailSender(JavaMailSender javaMailSender) {
		
        log.info("Configuring SmtpMailSender");       
		return new SmtpMailSender(javaMailSender);
	}
	
	@Bean
	public SecurityUtils securityUtils(ApplicationContext applicationContext, ObjectMapper objectMapper) {
		return new SecurityUtils(applicationContext, objectMapper);
	}
	
//	@Bean
//	@ConditionalOnMissingBean(RestTemplateBuilder.class)
//	public RestTemplateBuilder restTemplateBuilder() {
//		return new RestTemplateBuilder();
//	}
	
	/**
	 * Configures CaptchaValidator if missing
	 */
	@Bean
	@ConditionalOnMissingBean(CaptchaValidator.class)
	public CaptchaValidator captchaValidator(SpringProperties properties, RestTemplateBuilder restTemplateBuilder) {
		
        log.info("Configuring SpringUserDetailsService");       
		return new CaptchaValidator(properties, restTemplateBuilder);
	}
	
	@Bean
	// @ConditionalOnProperty(name="hazelcast.port", havingValue="")
	//public CacheManager cacheManager(HazelcastInstance hazelcastInstance) {
	public CacheManager cacheManager(JetInstance jetInstance) {
	
		log.debug("Starting Hazelcast CacheManager");
		return new com.hazelcast.spring.cache.HazelcastCacheManager(jetInstance.getHazelcastInstance());
	}

	@Bean
	@ConfigurationProperties(prefix="hazelcast")
	// @ConditionalOnProperty(name="hazelcast.port", havingValue="")
	public HazelcastProperties hazelCastProperties() {
		return new HazelcastProperties();
	}
	
	// @Bean
	// @ConditionalOnProperty(name="hazelcast.port", havingValue="")
	private Config hazelCastConfig(HazelcastProperties hazelcastProperties) {
		Config config = new Config();
		if (hazelcastProperties.getCpMemberCounts() > 0) {
			config.getCPSubsystemConfig().setCPMemberCount(hazelcastProperties.getCpMemberCounts());
			config.getCPSubsystemConfig().setGroupSize(hazelcastProperties.getCpGroupSize());
		}
		config.setProperty("hazelcast.logging.type", hazelcastProperties.getLoggingType());
		config.setInstanceName(hazelcastProperties.getInstanceName());
		config.getNetworkConfig().getJoin().getMulticastConfig().setEnabled(false).setMulticastGroup("224.2.2.3").setMulticastPort(54327);
		config.getNetworkConfig().setPortAutoIncrement(false).setPort(hazelcastProperties.getPort());
		TcpIpConfig tcpipConfig = config.getNetworkConfig().getJoin().getTcpIpConfig();
		tcpipConfig.setEnabled(true);
		
		for (String member: hazelcastProperties.getMembers().split(",")) {
			tcpipConfig.addMember(member);
		}
		
		if (StringUtils.hasText(hazelcastProperties.getManagementCenter().getUrl())) {
			ManagementCenterConfig managementCenterConfig = new ManagementCenterConfig();
			//managementCenterConfig.setEnabled(true);
			managementCenterConfig.setUrl(hazelcastProperties.getManagementCenter().getUrl());
	//		managementCenterConfig.setUpdateInterval(updateInterval);
	//		managementCenterConfig.setScriptingEnabled(scriptingEnabled);
			managementCenterConfig.setEnabled(hazelcastProperties.getManagementCenter().isEnabled());
			config.setManagementCenterConfig(managementCenterConfig);
		}
		
		if (config.getNetworkConfig().getSSLConfig() != null) {
			//Enterprise Edition Feature?
			config.getNetworkConfig().getSSLConfig()
				.setFactoryClassName("com.hazelcast.nio.ssl.BasicSSLContextFactor")
				.setEnabled(hazelcastProperties.isEnableSSL())			
				.setProperty("keyStore", hazelcastProperties.getKeyStore())
				.setProperty("keyStorePassword", hazelcastProperties.getKeyStorePassword())
				.setProperty("keyManagerAlgorithm", hazelcastProperties.getKeyManagerAlgorithm())
				.setProperty("trustManagerAlgorithm", hazelcastProperties.getTrustManagerAlgorithm())
				.setProperty("protocol", "TLS");
		}
		if (config.getNetworkConfig().getSymmetricEncryptionConfig() != null) {
			//Enterprise Edition Feature?
			config.getNetworkConfig().getSymmetricEncryptionConfig()
				.setEnabled(hazelcastProperties.isEnableEncryption())
				.setAlgorithm(hazelcastProperties.getEncryptionAlgorithm())
				.setIterationCount(19)
				.setPassword(hazelcastProperties.getEncryptionPassword())
				.setSalt(hazelcastProperties.getEncryptionSalt());
		}
		for (CacheConfig cacheConfig: hazelcastProperties.getCaches()) {
			config.getMapConfigs().put(cacheConfig.getCacheName(), this.initCache(cacheConfig));
		}
		return config;
	}
	
	private MapConfig initCache(CacheConfig cacheConfig) {
		
		MapConfig mapConfig = new MapConfig()
			.setName(cacheConfig.getCacheName())	
			.setMaxSizeConfig(new MaxSizeConfig(cacheConfig.getMaxSize(), 
					MaxSizeConfig.MaxSizePolicy.valueOf(cacheConfig.getMaxSizePolicy())))
			.setEvictionPolicy(EvictionPolicy.valueOf(cacheConfig.getEvictionPolicy()))
			.setTimeToLiveSeconds(cacheConfig.getTimeToLiveSeconds())
			.setBackupCount(cacheConfig.getBackupCount());
		
		for (CacheConfig.EntryListener entryListener: cacheConfig.getEntryListeners()) {
			EntryListenerConfig listenerConfig = new EntryListenerConfig();
			listenerConfig.setClassName(entryListener.getListener());
			listenerConfig.setLocal(entryListener.isLocal());
			listenerConfig.setIncludeValue(entryListener.isIncludeValue());
			mapConfig.addEntryListenerConfig(listenerConfig);
		}
		
		for (CacheConfig.Index index: cacheConfig.getIndexes()) {
			MapIndexConfig mapIndexConfig = new MapIndexConfig();
			mapIndexConfig.setAttribute(index.getAttribute());
			mapIndexConfig.setOrdered(index.isOrdered());
			mapConfig.addMapIndexConfig(mapIndexConfig);
		}
		
		for (CacheConfig.Attribute attribute: cacheConfig.getAttributes()) {
			MapAttributeConfig mapAttributeConfig = new MapAttributeConfig();
			mapAttributeConfig.setName(attribute.getName());
			mapAttributeConfig.setExtractor(attribute.getExtractor());
			mapConfig.addMapAttributeConfig(mapAttributeConfig);
		}
//		MapStoreConfig mapStoreConfig = new MapStoreConfig();
//		mapConfig.setMapStoreConfig(mapStoreConfig);
		return mapConfig;
		
		
	}
	
	/**
     * A {@code ManagedContext} implementation bean which enables {@code @SpringAware}
     * annotation for de-serialized objects.
     */
    @Bean
    public ManagedContext managedContext() {
        return new SpringManagedContext();
    }

    /**
     * {@code JetInstance} bean which configured programmatically with {@code SpringManagedContext}
     */
    @Bean
    public JetInstance jetInstance(HazelcastProperties hazelcastProperties) {
    	Config config = hazelCastConfig(hazelcastProperties);
        config.setManagedContext(managedContext());
        JetConfig jetConfig = new JetConfig().setHazelcastConfig(config);
        JetInstance jetInstance = Jet.newJetInstance(jetConfig);
        if (hazelcastProperties.getCpMemberCounts() > 0) {
	        try {
	        	jetInstance.getHazelcastInstance().getCPSubsystem().getCPSubsystemManagementService().awaitUntilDiscoveryCompleted(1, TimeUnit.MINUTES);
	        } catch (InterruptedException e) {
	        	throw new RuntimeException(e);
	        }
        }
		return jetInstance;
    }
    
    @Bean
    public HazelcastInstance HazelcastInstance(JetInstance jetInstance) {
    	return jetInstance.getHazelcastInstance();
    }
}
