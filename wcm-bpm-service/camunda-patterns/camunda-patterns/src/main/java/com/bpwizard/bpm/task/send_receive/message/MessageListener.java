package com.bpwizard.bpm.task.send_receive.message;

import java.io.IOException;
import java.util.UUID;

import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.spin.plugin.variable.SpinValues;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.bpwizard.bpm.task.send_receive.FetchGoodsCommandPayload;
import com.bpwizard.bpm.task.send_receive.GoodsFetchedEventPayload;
import com.bpwizard.bpm.task.send_receive.GoodsShippedEventPayload;
import com.bpwizard.bpm.task.send_receive.PaymentReceivedEventPayload;
import com.bpwizard.bpm.task.send_receive.RetrievePaymentCommandPayload;
import com.bpwizard.bpm.task.send_receive.ShipGoodsCommandPayload;
import com.bpwizard.bpm.task.send_receive.domain.Order;
import com.bpwizard.bpm.task.send_receive.persistence.OrderRepository;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class MessageListener {

	@Autowired
	private OrderRepository repository;

	@Autowired
	private ProcessEngine camunda;
	
	@Autowired
	private MessageSender messageSender;

	/**
	 * Very generic listener for simplicity. It takes all events and checks, if a
	 * flow instance is interested. If yes, they are correlated, otherwise they are
	 * just discarded.
	 * 
	 * It might make more sense to handle each and every message type individually.
	 */
	@KafkaListener(
			topics = "${message.topic.name}", 
			containerFactory = "kafkaListenerContainerFactory")
	@Transactional
	public void messageReceived(
			@Payload  String jsonMessage,
			@Header("type") String messageType) throws Exception {
		
		System.out.println(">>>>>>>>>>>>>>>>>>>>>>> 1 messageJson");
		if (messageType.equals("OrderPlacedEvent")) {
			this.orderPlacedReceived(jsonMessage);
		} else if (messageType.equals("RetrievePaymentCommand")) {
			this.handleRetrievePaymentCommand(jsonMessage);
		} else if (messageType.equals("FetchGoodsCommand")) {
			this.handleFetchGoodsCommand(jsonMessage);
		} else if (messageType.equals("ShipGoodsCommand")) {
			this.handleShipGoodsCommand(jsonMessage);
		} else if (messageType.endsWith("Event")) {	
			this.handleOtherMessages(jsonMessage);
		}
	}
	
	protected void handleOtherMessages(String jsonMessage) throws Exception {
		
		Message<JsonNode> message = new ObjectMapper().readValue(
				jsonMessage,
				new TypeReference<Message<JsonNode>>() {});
		System.out.println(message);
		
		long correlatingInstances = camunda.getRuntimeService().createExecutionQuery()
				.messageEventSubscriptionName(message.getMessageType())
				.processInstanceBusinessKey(message.getTraceId())
				.count();

		if (correlatingInstances == 1) {
			System.out.println("Correlating " + message + " to waiting flow instance");

			camunda.getRuntimeService().createMessageCorrelation(message.getMessageType())
					.processInstanceBusinessKey(message.getTraceId())
					.setVariable("PAYLOAD_" + message.getMessageType(), //
							SpinValues.jsonValue(message.getPayload().toString()).create())
					.correlateWithResult();
		} else {
			System.out.println("Order context ignores event '" + message.getMessageType() + "'");
		}
	}
	
	protected void handleShipGoodsCommand(String jsonMessage) throws Exception {
		Message<ShipGoodsCommandPayload> message = new ObjectMapper().readValue(
				jsonMessage, new TypeReference<Message<ShipGoodsCommandPayload>>() {});
		
		String refId = (String) message.getPayload().getRefId();
	    String traceId = message.getTraceId();
	    
		messageSender.send(new Message<GoodsShippedEventPayload>(
		            "GoodsShippedEvent",
		            traceId,
		            new GoodsShippedEventPayload()
		                .setRefId(refId).setShipmentId(UUID.randomUUID().toString())));
	}
	
	protected void handleFetchGoodsCommand(String jsonMessage) throws Exception {
		Message<FetchGoodsCommandPayload> message = new ObjectMapper().readValue(
				jsonMessage, new TypeReference<Message<FetchGoodsCommandPayload>>() {});
		
		String refId = (String) message.getPayload().getRefId();
	    String traceId = message.getTraceId();
	    
		messageSender.send( //
		        new Message<GoodsFetchedEventPayload>( //
		            "GoodsFetchedEvent", //
		            traceId, //
		            new GoodsFetchedEventPayload() //
		                .setRefId(refId).setPickId(UUID.randomUUID().toString())));
	}
	
	protected void handleRetrievePaymentCommand(String jsonMessage) throws Exception {
		
		Message<RetrievePaymentCommandPayload> message = new ObjectMapper().readValue(
				jsonMessage, new TypeReference<Message<RetrievePaymentCommandPayload>>() {});
		
		String refId = (String) message.getPayload().getRefId();
	    String traceId = message.getTraceId();
	    
		messageSender.send( //
		        new Message<PaymentReceivedEventPayload>( //
		            "PaymentReceivedEvent", //
		            traceId, //
		            new PaymentReceivedEventPayload() //
		                .setRefId(refId)));
	}
	
	/**  
	 * Handles incoming OrderPlacedEvents.
	 * 
	 * Using the conditional {@link StreamListener} from
	 * https://github.com/spring-cloud/spring-cloud-stream/blob/master/spring-cloud-stream-core-docs/src/main/asciidoc/spring-cloud-stream-overview.adoc
	 * in a way close to what Axion would do (see e.g.
	 * https://dturanski.wordpress.com/2017/03/26/spring-cloud-stream-for-event-driven-architectures/)
	 */
	protected void orderPlacedReceived(@Payload String jsonMessage) throws JsonParseException, JsonMappingException, IOException {
		System.out.println(">>>>>>>>>>>>>>>>>>>>>>> messageJson:" + jsonMessage);
		
		Message<Order> message = new ObjectMapper().readValue(
				jsonMessage, new TypeReference<Message<Order>>() {});
		System.out.println(message);
		System.out.println(message.getPayload());
		Order order = message.getPayload();
		System.out.println("New order placed, start flow. " + order);

		// persist domain entity
		repository.persistOrder(order);

		// and kick of a new flow instance
		camunda.getRuntimeService().createMessageCorrelation(message.getMessageType())
				.processInstanceBusinessKey(message.getTraceId())
				.setVariable("orderId", order.getId())
				.correlateWithResult();
		System.out.println(">>>>>>>>>>>>>>>>>>>>>>> created order process");
	}
}
