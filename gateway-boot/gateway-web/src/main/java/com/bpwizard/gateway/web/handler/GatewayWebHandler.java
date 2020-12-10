package com.bpwizard.gateway.web.handler;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.lang.NonNull;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebHandler;

import com.bpwizard.gateway.metrics.api.HistogramMetricsTrackerDelegate;
import com.bpwizard.gateway.metrics.enums.MetricsLabelEnum;
import com.bpwizard.gateway.metrics.facade.MetricsTrackerFacade;
import com.bpwizard.gateway.plugin.api.GatewayPlugin;
import com.bpwizard.gateway.plugin.api.GatewayPluginChain;

import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

/**
 * this is web handler request starter.
 */
public final class GatewayWebHandler implements WebHandler {

    private List<GatewayPlugin> plugins;

    private Scheduler scheduler;

    /**
     * Instantiates a new Gateway web handler.
     *
     * @param plugins the plugins
     */
    public GatewayWebHandler(final List<GatewayPlugin> plugins) {
        this.plugins = plugins;
        String schedulerType = System.getProperty("gateway.scheduler.type", "fixed");
        if (Objects.equals(schedulerType, "fixed")) {
            int threads = Integer.parseInt(System.getProperty(
                    "gateway.work.threads", "" + Math.max((Runtime.getRuntime().availableProcessors() << 1) + 1, 16)));
            scheduler = Schedulers.newParallel("gateway-work-threads", threads);
        } else {
            scheduler = Schedulers.elastic();
        }
    }

    /**
     * Handle the web server exchange.
     *
     * @param exchange the current server exchange
     * @return {@code Mono<Void>} to indicate when request handling is complete
     */
    @Override
    public Mono<Void> handle(@NonNull final ServerWebExchange exchange) {
        MetricsTrackerFacade.getInstance().counterInc(MetricsLabelEnum.REQUEST_TOTAL.getName());
        Optional<HistogramMetricsTrackerDelegate> startTimer = MetricsTrackerFacade.getInstance().histogramStartTimer(MetricsLabelEnum.REQUEST_LATENCY.getName());
        return new DefaultGatewayPluginChain(plugins).execute(exchange).subscribeOn(scheduler)
                .doOnSuccess(t -> startTimer.ifPresent(time -> MetricsTrackerFacade.getInstance().histogramObserveDuration(time)));
    }

    private static class DefaultGatewayPluginChain implements GatewayPluginChain {

        private int index;

        private final List<GatewayPlugin> plugins;

        /**
         * Instantiates a new Default gateway plugin chain.
         *
         * @param plugins the plugins
         */
        DefaultGatewayPluginChain(final List<GatewayPlugin> plugins) {
            this.plugins = plugins;
        }

        /**
         * Delegate to the next {@code WebFilter} in the chain.
         *
         * @param exchange the current server exchange
         * @return {@code Mono<Void>} to indicate when request handling is complete
         */
        @Override
        public Mono<Void> execute(final ServerWebExchange exchange) {
            return Mono.defer(() -> {
                if (this.index < plugins.size()) {
                	GatewayPlugin plugin = plugins.get(this.index++);
                    Boolean skip = plugin.skip(exchange);
                    if (skip) {
                        return this.execute(exchange);
                    } else {
                        return plugin.execute(exchange, this);
                    }
                } else {
                    return Mono.empty();
                }
            });
        }
    }
}
