package io.flowing.retail.order.port.message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.kafka.support.SendResult;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

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
			// wrap into a proper message for the transport (Kafka/Rabbit) and send it
			//output.send(MessageBuilder.withPayload(jsonMessage).setHeader("type", m.getMessageType()).build());
			org.springframework.messaging.Message<String> kafkaMessage = MessageBuilder
					.withPayload(jsonMessage)
					.setHeader(KafkaHeaders.TOPIC, retailTopic)
					.setHeader("type", m.getMessageType())
					.build();
			
			ListenableFuture<SendResult<String, String>> future = kafkaTemplate.send(kafkaMessage);
			future.addCallback(new ListenableFutureCallback<SendResult<String, String>>() {
				 
		        @Override
		        public void onSuccess(SendResult<String, String> result) {
		            System.out.println("Sent message=[" + jsonMessage + 
		              "] with offset=[" + result.getRecordMetadata().offset() + "]");
		        }
		        @Override
		        public void onFailure(Throwable ex) {
		            System.out.println("Unable to send message=["
		              + jsonMessage + "] due to : " + ex.getMessage());
		        }
		    });
		} catch (Exception e) {
			throw new RuntimeException("Could not tranform and send message due to: " + e.getMessage(), e);
		}
	}
}
