package com.bpwizard.wcm;

import java.io.InputStream;
import java.security.KeyStore;

import org.apache.http.client.HttpClient;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.beans.factory.annotation.Value;
//import org.springframework.cloud.bootstrap.BootstrapConfiguration;
//import org.springframework.cloud.config.client.ConfigClientProperties;
//import org.springframework.cloud.config.client.ConfigServicePropertySourceLocator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

// @Configuration
public class SslConfiguration {
	// @Value("${http.client.ssl.trust-store-type}")
	// private String trustStoreType;
	// @Value("${http.client.ssl.trust-store}")
	// private String trustStore;
	// @Value("${http.client.ssl.trust-store-password}")
	// private String trustStorePassword;

//	@Autowired
//	ConfigClientProperties clientProperties;
//	
//	@Bean
//    public ConfigServicePropertySourceLocator configServicePropertySourceLocator() {
//        ConfigServicePropertySourceLocator configServicePropertySourceLocator =  new ConfigServicePropertySourceLocator(clientProperties);
//        configServicePropertySourceLocator.setRestTemplate(getRestTemplate());
//        return configServicePropertySourceLocator;
//    }
	
	// @Bean
	// public RestTemplate getRestTemplate() {
	// 	RestTemplate restTemplate = new RestTemplate();
		
	// 	KeyStore keyStore;
	// 	HttpComponentsClientHttpRequestFactory requestFactory = null;
		
	// 	try {
	// 		keyStore = KeyStore.getInstance(trustStoreType);
	// 		ClassPathResource classPathResource = new ClassPathResource(trustStore);
	// 		InputStream inputStream = classPathResource.getInputStream();
	// 		keyStore.load(inputStream, trustStorePassword.toCharArray());
	// 		SSLConnectionSocketFactory socketFactory = new SSLConnectionSocketFactory(new SSLContextBuilder()
	// 				.loadTrustMaterial(null, new TrustSelfSignedStrategy())
	// 				.loadKeyMaterial(keyStore, trustStorePassword.toCharArray()).build(),
	// 				NoopHostnameVerifier.INSTANCE);
	// 		HttpClient httpClient = HttpClients.custom().setSSLSocketFactory(socketFactory)
	// 				.setMaxConnTotal(Integer.valueOf(5))
	// 				.setMaxConnPerRoute(Integer.valueOf(5))
	// 				.build();
	// 		requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
	// 		requestFactory.setReadTimeout(Integer.valueOf(10000));
	// 		requestFactory.setConnectTimeout(Integer.valueOf(10000));
	// 		restTemplate.setRequestFactory(requestFactory);
	// 	} catch (Exception exception) {
	// 		exception.printStackTrace();
	// 	}
	// 	return restTemplate;
	// }
}
