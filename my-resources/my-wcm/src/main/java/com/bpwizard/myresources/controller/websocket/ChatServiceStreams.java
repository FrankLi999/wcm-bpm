package com.bpwizard.myresources.controller.websocket;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

/**
 * @author Greg Turnquist
 */
// tag::code[]
public interface ChatServiceStreams {

	String NEW_COMMENTS = "newComments";
	String CLIENT_TO_BROKER = "clientToBroker";
	String BROKER_TO_CLIENT = "brokerToClient";

	String USER_HEADER = "User";

	@Input(NEW_COMMENTS)
	SubscribableChannel newComments();

	@Output(CLIENT_TO_BROKER)
	MessageChannel clientToBroker();

	@Input(BROKER_TO_CLIENT)
	SubscribableChannel brokerToClient();
}
// end::code[]
