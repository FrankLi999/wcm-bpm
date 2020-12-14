package com.bpwizard.gateway.web.configuration;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.web.filter.reactive.HiddenHttpMethodFilter;
import org.springframework.web.reactive.result.view.ViewResolver;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilterChain;

import com.bpwizard.gateway.web.handler.GlobalErrorHandler;

import reactor.core.publisher.Mono;

/**
 * The type Error handler configuration.
 */
@EnableConfigurationProperties({ServerProperties.class, WebProperties.Resources.class})
public class ErrorHandlerConfiguration {

    private final ServerProperties serverProperties;

    private final ApplicationContext applicationContext;

    private final WebProperties.Resources resourceProperties;

    private final List<ViewResolver> viewResolvers;

    private final ServerCodecConfigurer serverCodecConfigurer;

    /**
     * Instantiates a new Error handler configuration.
     *
     * @param serverProperties      the server properties
     * @param resourceProperties    the resource properties
     * @param viewResolversProvider the view resolvers provider
     * @param serverCodecConfigurer the server codec configurer
     * @param applicationContext    the application context
     */
    public ErrorHandlerConfiguration(final ServerProperties serverProperties,
                                     final WebProperties.Resources resourceProperties,
                                     final ObjectProvider<List<ViewResolver>> viewResolversProvider,
                                     final ServerCodecConfigurer serverCodecConfigurer,
                                     final ApplicationContext applicationContext) {
        this.serverProperties = serverProperties;
        this.applicationContext = applicationContext;
        this.resourceProperties = resourceProperties;
        this.viewResolvers = viewResolversProvider.getIfAvailable(Collections::emptyList);
        this.serverCodecConfigurer = serverCodecConfigurer;
    }

    /**
     * Error web exception handler error web exception handler.
     *
     * @param errorAttributes the error attributes
     * @return the error web exception handler
     */
    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE + 1)
    public ErrorWebExceptionHandler errorWebExceptionHandler(final ErrorAttributes errorAttributes) {
        GlobalErrorHandler exceptionHandler = new GlobalErrorHandler(
                errorAttributes,
                this.resourceProperties,
                this.serverProperties.getError(),
                this.applicationContext);
        exceptionHandler.setViewResolvers(this.viewResolvers);
        exceptionHandler.setMessageWriters(this.serverCodecConfigurer.getWriters());
        exceptionHandler.setMessageReaders(this.serverCodecConfigurer.getReaders());
        return exceptionHandler;
    }

    /**
     * https://github.com/spring-cloud/spring-cloud-gateway/issues/541
     * Hidden http method filter hidden http method filter.
     *
     * @return the hidden http method filter
     */
    @Bean
    public HiddenHttpMethodFilter hiddenHttpMethodFilter() {
        return new HiddenHttpMethodFilter() {
            @Override
            public Mono<Void> filter(final ServerWebExchange exchange, final WebFilterChain chain) {
                return chain.filter(exchange);
            }
        };
    }

}
