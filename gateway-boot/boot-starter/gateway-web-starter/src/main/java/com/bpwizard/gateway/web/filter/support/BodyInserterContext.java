package com.bpwizard.gateway.web.filter.support;

import org.springframework.http.codec.HttpMessageWriter;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.client.ExchangeStrategies;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * The type Body inserter context.
 * * @see https://github.com/spring-cloud/spring-cloud-gateway/blob/master/spring-cloud-gateway-core/src/main/java/org/springframework/cloud/gateway/support/BodyInserterContext.java
 */
public class BodyInserterContext implements BodyInserter.Context {

    private final ExchangeStrategies exchangeStrategies;

    /**
     * Instantiates a new Body inserter context.
     */
    public BodyInserterContext() {
        this.exchangeStrategies = ExchangeStrategies.withDefaults();
    }

    @Override
    public List<HttpMessageWriter<?>> messageWriters() {
        return exchangeStrategies.messageWriters();
    }

    @Override
    public Optional<ServerHttpRequest> serverRequest() {
        return Optional.empty();
    }

    @Override
    public Map<String, Object> hints() {
        return Collections.emptyMap();
    }

}
