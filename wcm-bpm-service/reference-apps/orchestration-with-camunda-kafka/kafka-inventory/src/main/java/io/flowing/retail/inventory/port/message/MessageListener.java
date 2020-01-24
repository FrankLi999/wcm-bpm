package io.flowing.retail.inventory.port.message;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.flowing.retail.inventory.service.InventoryService;

@Component
public class MessageListener {

	@Autowired
	private MessageSender messageSender;

	@Autowired
	private InventoryService inventoryService;

	@KafkaListener(topics = "${message.topic.name}", containerFactory = "kafkaListenerContainerFactory")
	@Transactional
	public void retrievePaymentCommandReceived(String messageJson)
			throws JsonParseException, JsonMappingException, IOException {
		Message<FetchGoodsCommandPayload> message = new ObjectMapper().readValue(messageJson,
				new TypeReference<Message<FetchGoodsCommandPayload>>() {
				});

		FetchGoodsCommandPayload fetchGoodsCommand = message.getPayload();
		String pickId = inventoryService.pickItems( //
				fetchGoodsCommand.getItems(), fetchGoodsCommand.getReason(), fetchGoodsCommand.getRefId());

		messageSender.send( //
				new Message<GoodsFetchedEventPayload>( //
						"GoodsFetchedEvent", //
						message.getTraceId(), //
						new GoodsFetchedEventPayload() //
								.setRefId(fetchGoodsCommand.getRefId()).setPickId(pickId)));
	}

}
