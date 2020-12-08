package com.bpwizard.gateway.admin.config;

import com.bpwizard.gateway.admin.listener.http.HttpLongPollingDataChangedListener;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * The type Data sync configuration.
 */
@Configuration
public class DataSyncConfiguration {
    
    /**
     * http long polling(default strategy).
     */
    @Configuration
    @ConditionalOnProperty(name = "gateway.sync.http.enabled", havingValue = "true", matchIfMissing = true)
    @EnableConfigurationProperties(HttpSyncProperties.class)
    static class HttpLongPollingListener {

        @Bean
        @ConditionalOnMissingBean(HttpLongPollingDataChangedListener.class)
        public HttpLongPollingDataChangedListener httpLongPollingDataChangedListener(
        		final HttpSyncProperties httpSyncProperties) {
            return new HttpLongPollingDataChangedListener(httpSyncProperties);
        }

    }
}