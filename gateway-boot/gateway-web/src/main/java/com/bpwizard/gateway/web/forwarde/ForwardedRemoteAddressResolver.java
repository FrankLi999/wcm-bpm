package com.bpwizard.gateway.web.forwarde;

import com.bpwizard.gateway.plugin.api.RemoteAddressResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;

import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Parses the client address from the X-Forwarded-For header. If header is not present.
 * falls back to {@link RemoteAddressResolver} and
 * {@link ServerHttpRequest#getRemoteAddress()}. Use the static constructor methods which
 * meets your security requirements.
 */
public class ForwardedRemoteAddressResolver implements RemoteAddressResolver {
    
    /**
     * Forwarded-For header name.
     */
    public static final String X_FORWARDED_FOR = "X-Forwarded-For";
    
    private static final Logger LOGGER = LoggerFactory
            .getLogger(ForwardedRemoteAddressResolver.class);
    
    private final RemoteAddressResolver defaultRemoteIpResolver = new RemoteAddressResolver() {
    };
    
    private final int maxTrustedIndex;
    
    /**
     * Instantiates a new Forwarded remote address resolver.
     *
     * @param maxTrustedIndex the max trusted index
     */
    public ForwardedRemoteAddressResolver(final int maxTrustedIndex) {
        this.maxTrustedIndex = maxTrustedIndex;
    }
    
    /**
     * Trust all forwarded remote address resolver.
     *
     * @return the forwarded remote address resolver
     */
    public static ForwardedRemoteAddressResolver trustAll() {
        return new ForwardedRemoteAddressResolver(Integer.MAX_VALUE);
    }
    
    /**
     * Max trusted index forwarded remote address resolver.
     *
     * @param maxTrustedIndex the max trusted index
     * @return the forwarded remote address resolver
     */
    public static ForwardedRemoteAddressResolver maxTrustedIndex(final int maxTrustedIndex) {
        Assert.isTrue(maxTrustedIndex > 0, "An index greater than 0 is required");
        return new ForwardedRemoteAddressResolver(maxTrustedIndex);
    }
    
    @Override
    public InetSocketAddress resolve(final ServerWebExchange exchange) {
        List<String> xForwardedValues = extractXForwardedValues(exchange);
        Collections.reverse(xForwardedValues);
        if (!xForwardedValues.isEmpty()) {
            int index = Math.min(xForwardedValues.size(), maxTrustedIndex) - 1;
            return new InetSocketAddress(xForwardedValues.get(index), 0);
        }
        return defaultRemoteIpResolver.resolve(exchange);
    }
    
    private List<String> extractXForwardedValues(final ServerWebExchange exchange) {
        List<String> xForwardedValues = exchange.getRequest().getHeaders()
                .get(X_FORWARDED_FOR);
        if (xForwardedValues == null || xForwardedValues.isEmpty()) {
            return Collections.emptyList();
        }
        if (xForwardedValues.size() > 1) {
            LOGGER.warn("Multiple X-Forwarded-For headers found, discarding all");
            return Collections.emptyList();
        }
        List<String> values = Arrays.asList(xForwardedValues.get(0).split(", "));
        if (values.size() == 1 && !StringUtils.hasText(values.get(0))) {
            return Collections.emptyList();
        }
        return values;
    }
    
}
