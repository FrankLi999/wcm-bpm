package com.bpwizard.myresources.controller.websocket;

import java.security.Principal;

import reactor.core.publisher.Mono;
import org.springframework.security.core.Authentication;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketSession;

/**
 * @author Greg Turnquist
 */
// tag::code[]
abstract class AuthorizedWebSocketHandler implements WebSocketHandler {
	
	@Override
	public final Mono<Void> handle(WebSocketSession session) {
		return session.getHandshakeInfo().getPrincipal()
			.filter(this::isAuthorized)
			.then(doHandle(session));
	}

	private boolean isAuthorized(Principal principal) {
		Authentication authentication = (Authentication) principal;
		return authentication.isAuthenticated() &&
			authentication.getAuthorities().contains("ROLE_USER");
	}
	
	abstract protected Mono<Void> doHandle(
							WebSocketSession session);
}
// end::code[]
