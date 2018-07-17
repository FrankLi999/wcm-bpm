package com.bpwizard.myresources.gateway;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.SaveSessionGatewayFilterFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.server.WebSession;

/**
 * @author Greg Turnquist
 */
// tag::code[]
@Configuration
public class GatewayConfig {

	private static final Logger log =
		LoggerFactory.getLogger(GatewayConfig.class);

	/**
	 * Force the current WebSession to get saved
	 */
	static class MySaveSessionGatewayFilterFactory
								extends SaveSessionGatewayFilterFactory {
		@Override
		public GatewayFilter apply(Object args) {
			return (exchange, chain) -> exchange.getSession()
				.map(webSession -> {
					log.debug("Session id: " + webSession.getId());
					webSession.getAttributes().entrySet()
						.forEach(entry ->
						log.debug(entry.getKey() + " => " +
											entry.getValue()));
					return webSession;
				})
				.map(WebSession::save)
				.then(chain.filter(exchange));
		}
	}

	@Bean
	MySaveSessionGatewayFilterFactory saveSessionWebFilterFactory() {
		return new MySaveSessionGatewayFilterFactory();
	}
}
// end::code[]
