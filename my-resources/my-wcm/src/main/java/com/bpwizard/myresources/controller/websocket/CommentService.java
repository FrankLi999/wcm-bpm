package com.bpwizard.myresources.controller.websocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.publisher.Mono;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.socket.WebSocketSession;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author Greg Turnquist
 */
// tag::code[]
//@Service
//@EnableBinding(ChatServiceStreams.class)
public class CommentService extends AuthorizedWebSocketHandler {

	private final static Logger log =
		LoggerFactory.getLogger(CommentService.class);

	private ObjectMapper mapper;
	private Flux<Comment> flux;
	private FluxSink<Comment> webSocketCommentSink;

	CommentService(ObjectMapper mapper) {
		this.mapper = mapper;
		this.flux = Flux.<Comment>create(
			emitter -> this.webSocketCommentSink = emitter,
			FluxSink.OverflowStrategy.IGNORE)
			.publish()
			.autoConnect();
	}

	@StreamListener(ChatServiceStreams.NEW_COMMENTS)
	public void broadcast(Comment comment) {
		if (webSocketCommentSink != null) {
			log.info("Publishing " + comment.toString() +
				" to websocket...");
			webSocketCommentSink.next(comment);
		}
	}

	@Override
	public Mono<Void> doHandle(WebSocketSession session) {
		return session.send(this.flux
			.map(comment -> {
				try {
					return mapper.writeValueAsString(comment);
				} catch (JsonProcessingException e) {
					throw new RuntimeException(e);
				}
			})
			.log("encode-as-json")
			.map(session::textMessage)
			.log("wrap-as-websocket-message"))
			.log("publish-to-websocket");
	}
}
// end::code[]
