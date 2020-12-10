package com.bpwizard.gateway.web.configuration;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import com.bpwizard.gateway.plugin.api.RemoteAddressResolver;
import com.bpwizard.gateway.plugin.api.GatewayPlugin;
import com.bpwizard.gateway.plugin.base.cache.CommonPluginDataSubscriber;
import com.bpwizard.gateway.plugin.base.handler.PluginDataHandler;
import com.bpwizard.gateway.sync.data.api.PluginDataSubscriber;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.SearchStrategy;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.Order;
import org.springframework.web.reactive.DispatcherHandler;
import org.springframework.web.server.WebFilter;

import com.bpwizard.gateway.web.config.GatewayConfig;
import com.bpwizard.gateway.web.filter.CrossFilter;
import com.bpwizard.gateway.web.filter.FileSizeFilter;
import com.bpwizard.gateway.web.filter.TimeWebFilter;
import com.bpwizard.gateway.web.filter.WebSocketParamFilter;
import com.bpwizard.gateway.web.forwarde.ForwardedRemoteAddressResolver;
import com.bpwizard.gateway.web.handler.GatewayWebHandler;

/**
 * GatewayConfiguration.
 */
@Configuration
@ComponentScan("com.bpwizard.gateway")
@Import(value = {ErrorHandlerConfiguration.class, GatewayExtConfiguration.class, SpringExtConfiguration.class})
@Slf4j
public class GatewayConfiguration {

    /**
     * init GatewayWebHandler.
     *
     * @param plugins this plugins is All impl GatewayPlugin.
     * @return {@linkplain GatewayWebHandler}
     */
    @Bean("webHandler")
    public GatewayWebHandler gatewayWebHandler(final ObjectProvider<List<GatewayPlugin>> plugins) {
        List<GatewayPlugin> pluginList = plugins.getIfAvailable(Collections::emptyList);
        final List<GatewayPlugin> gatewayPlugins = pluginList.stream()
                .sorted(Comparator.comparingInt(GatewayPlugin::getOrder)).collect(Collectors.toList());
        gatewayPlugins.forEach(gatewayPlugin -> log.info("loader plugin:[{}] [{}]", gatewayPlugin.named(), gatewayPlugin.getClass().getName()));
        return new GatewayWebHandler(gatewayPlugins);
    }

    /**
     * init dispatch handler.
     * @return {@link DispatcherHandler}.
     */
    @Bean("dispatcherHandler")
    public DispatcherHandler dispatcherHandler() {
        return new DispatcherHandler();
    }

    /**
     * Plugin data subscriber plugin data subscriber.
     *
     * @param pluginDataHandlerList the plugin data handler list
     * @return the plugin data subscriber
     */
    @Bean
    public PluginDataSubscriber pluginDataSubscriber(final ObjectProvider<List<PluginDataHandler>> pluginDataHandlerList) {
        return new CommonPluginDataSubscriber(pluginDataHandlerList.getIfAvailable(Collections::emptyList));
    }

    /**
     * Remote address resolver remote address resolver.
     *
     * @return the remote address resolver
     */
    @Bean
    @ConditionalOnMissingBean(RemoteAddressResolver.class)
    public RemoteAddressResolver remoteAddressResolver() {
        return new ForwardedRemoteAddressResolver(1);
    }

    /**
     * Cross filter web filter.
     * if you application has cross-domain.
     * this is demo.
     * 1. Customize webflux's cross-domain requests.
     * 2. Spring bean Sort is greater than -1.
     *
     * @return the web filter
     */
    @Bean
    @Order(-100)
    @ConditionalOnProperty(name = "gateway.cross.enabled", havingValue = "true")
    public WebFilter crossFilter() {
        return new CrossFilter();
    }

    /**
     * Body web filter web filter.
     *
     * @return the web filter
     */
    @Bean
    @Order(-10)
    @ConditionalOnProperty(name = "gateway.file.enabled", havingValue = "true")
    public WebFilter fileSizeFilter() {
        return new FileSizeFilter();
    }


    /**
     * Gateway config gateway config.
     *
     * @return the gateway config
     */
    @Bean
    @ConfigurationProperties(prefix = "gateway")
    public GatewayConfig gatewayConfig() {
        return new GatewayConfig();
    }

    /**
     * init time web filter.
     *
     * @param gatewayConfig the gateway config
     * @return {@linkplain TimeWebFilter}
     */
    @Bean
    @Order(30)
    @ConditionalOnProperty(name = "gateway.filterTimeEnable")
    public WebFilter timeWebFilter(final GatewayConfig gatewayConfig) {
        return new TimeWebFilter(gatewayConfig);
    }

    /**
     * Web socket web filter web filter.
     *
     * @return the web filter
     */
    @Bean
    @Order(4)
    public WebFilter webSocketWebFilter() {
        return new WebSocketParamFilter();
    }
}
