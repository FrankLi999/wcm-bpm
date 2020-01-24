package io.flowing.retail.shipping.port.message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.flowing.retail.shipping.service.ShippingService;

@Component
public class MessageListener {

	@Autowired
	private MessageSender messageSender;

	@Autowired
	private ShippingService shippingService;

	@Value(value = "${message.topic.name}")
	private String retailTopic;

	@KafkaListener(topics = "${message.topic.name}", containerFactory = "kafkaListenerContainerFactory")
	@Transactional
	public void shipGoodsCommandReceived(String messageJson) throws Exception {
		Message<ShipGoodsCommandPayload> message = new ObjectMapper().readValue(messageJson,
				new TypeReference<Message<ShipGoodsCommandPayload>>() {
				});

		String shipmentId = shippingService.createShipment( //
				message.getPayload().getPickId(), //
				message.getPayload().getRecipientName(), //
				message.getPayload().getRecipientAddress(), //
				message.getPayload().getLogisticsProvider());

		messageSender.send( //
				new Message<GoodsShippedEventPayload>( //
						"GoodsShippedEvent", //
						message.getTraceId(), //
						new GoodsShippedEventPayload() //
								.setRefId(message.getPayload().getRefId()).setShipmentId(shipmentId)));
	}

}
