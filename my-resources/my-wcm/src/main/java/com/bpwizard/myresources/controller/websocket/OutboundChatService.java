package com.bpwizard.myresources.controller.websocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.publisher.Mono;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.socket.WebSocketSession;

/**
 * @author Greg Turnquist
 */
// tag::code-1[]
//@Service
//@EnableBinding(ChatServiceStreams.class)
public class OutboundChatService
				extends AuthorizedWebSocketHandler {
	//end::code-1[]
	
	private final static Logger log =
		LoggerFactory.getLogger(CommentService.class);

	private Flux<Message<String>> flux;
	private FluxSink<Message<String>> chatMessageSink;

	public OutboundChatService() {
		this.flux = Flux.<Message<String>>create(
			emitter -> this.chatMessageSink = emitter,
			FluxSink.OverflowStrategy.IGNORE)
			.publish()
			.autoConnect();
	}

	@StreamListener(ChatServiceStreams.BROKER_TO_CLIENT)
	public void listen(Message<String> message) {
		if (chatMessageSink != null) {
			log.info("Publishing " + message +
				" to websocket...");
			chatMessageSink.next(message);
		}
	}

	// tag::code-2[]
	@Override
	protected Mono<Void> doHandle(WebSocketSession session) {
		return session.send(this.flux
			.filter(s -> applicable(s, session))
			.map(this::transform)
			.map(session::textMessage)
			.log(session.getId() +
				"-outbound-wrap-as-websocket-message"))
			.log(session.getId() +
				"-outbound-publish-to-websocket");
	}
	// end::code-2[]

	// tag::code-3[]
	private boolean applicable(Message<String> message, WebSocketSession user) {
		if (message.getPayload().startsWith("@")) {
			String targetUser = message.getPayload()
				.substring(1, message.getPayload().indexOf(" "));

			String sender = message.getHeaders()
				.get(ChatServiceStreams.USER_HEADER, String.class);

			String userName = user.getHandshakeInfo().getPrincipal().block().getName();
			
			return userName.equals(sender) || userName.equals(targetUser);
		} else {
			return true;
		}
	}
	// end::code-3[]

	// tag::code-4[]
	private String transform(Message<String> message) {
		String user = message.getHeaders()
			.get(ChatServiceStreams.USER_HEADER, String.class);
		if (message.getPayload().startsWith("@")) {
			return "(" + user + "): " + message.getPayload();
		} else {
			return "(" + user + ")(all): " + message.getPayload();
		}
	}
	// end::code-4[]
}
