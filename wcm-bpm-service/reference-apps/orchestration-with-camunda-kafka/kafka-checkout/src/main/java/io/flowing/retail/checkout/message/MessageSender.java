package io.flowing.retail.checkout.message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.kafka.support.SendResult;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class MessageSender {

	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;
	
	@Value(value = "${message.topic.name}")
	private String retailTopic;
	
	public void send(Message<?> m) {
		try {
			// avoid too much magic and transform ourselves
			ObjectMapper mapper = new ObjectMapper();
			String jsonMessage = mapper.writeValueAsString(m);
//			org.springframework.messaging.Message<String> gmessae = MessageBuilder
//					.setHeader(KafkaHeaders.TOPIC, retailTopic)
//					.withPayload(jsonMessage)
//	                .build();
//			kafkaTemplate.send(message);
			org.springframework.messaging.Message<String> kafkaMessage = MessageBuilder
					.withPayload(jsonMessage)
					.setHeader(KafkaHeaders.TOPIC, retailTopic)
					.setHeader("type", m.getMessageType())
					.build();
			
			SendResult<String, String> result = kafkaTemplate.send(kafkaMessage).get();
			System.out.println(">>>>>>>>>> Checkout send result:" + result.getProducerRecord());
			System.out.println(">>>>>>>>>> Checkout send result:" + result.getRecordMetadata());
		} catch (Exception e) {
			throw new RuntimeException("Could not tranform and send message due to: " + e.getMessage(), e);
		}
	}
}
