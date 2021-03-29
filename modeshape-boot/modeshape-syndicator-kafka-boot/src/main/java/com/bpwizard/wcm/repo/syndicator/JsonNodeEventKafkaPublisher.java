package com.bpwizard.wcm.repo.syndicator;

import java.io.IOException;

import javax.jcr.RepositoryException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import com.bpwizard.wcm.repo.rest.JsonUtils;
import com.bpwizard.wcm.repo.rest.jcr.model.Syndicator;
import com.bpwizard.wcm.repo.rest.jcr.model.WcmEvent;
import com.bpwizard.wcm.repo.rest.service.JsonNodeEventQueuePublisher;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.node.ObjectNode;

@Component
public class JsonNodeEventKafkaPublisher implements JsonNodeEventQueuePublisher {

	@Autowired
	private KafkaTemplate<String, byte[]> kafkaTemplate;
	
	@Value("${bpw.wcm.kafka.topic.json-wcm-event:json-wcm-event}")
	private String wcmEventTopic;
	
	public void syndicateCndTypes(WcmEvent wcmEvent, Syndicator syndicator, String token) throws JsonProcessingException, IOException {
		this.sendMessage(wcmEventTopic, "cnd-types", cndToJsonBytes(wcmEvent, syndicator), token);
	}
	
	// public void syndicate(WcmEvent wcmEvent) throws IOException {
	public void syndicate(WcmEvent wcmEvent, Syndicator syndicator, String token) throws RepositoryException, IOException {
		// Split large messages into 1 KB segments with the producing client, using partition keys to 
		// ensure that all segments are sent to the same Kafka partition in the correct order. 
		// The consuming client can then reconstruct the original large message. 
		
		this.sendMessage(
				wcmEventTopic, 
				String.format("wcm-item-%s", wcmEvent.getId()), 
				eventToJsonBytes(wcmEvent, syndicator),
				token);
	}
	
	private byte[] cndToJsonBytes(WcmEvent wcmEvent, Syndicator syndicator) throws JsonProcessingException, IOException {
		ObjectNode objectNode = JsonUtils.createObjectNode();
		objectNode.put("id", wcmEvent.getId());
		objectNode.put("repository", wcmEvent.getRepository());
		objectNode.put("workspace", wcmEvent.getWorkspace());
		objectNode.put("content", wcmEvent.getContent());
		objectNode.put("wcm-server", String.format("http://%s:%d", syndicator.getCollector().getHost(), syndicator.getCollector().getPort()));
		return JsonUtils.writeValueAsBytes(objectNode);
	}
	
	private byte[] eventToJsonBytes(WcmEvent wcmEvent, Syndicator syndicator) throws JsonProcessingException, IOException {
		ObjectNode objectNode = JsonUtils.createObjectNode();
		objectNode.put("id", wcmEvent.getId());
		objectNode.put("repository", wcmEvent.getRepository());
		objectNode.put("workspace", wcmEvent.getWorkspace());
		objectNode.put("library", wcmEvent.getLibrary());
		objectNode.put("nodePath", wcmEvent.getNodePath());
		objectNode.put("itemType", wcmEvent.getItemType().name());
		objectNode.put("operation", wcmEvent.getOperation().name());
		objectNode.put("wcm-server", String.format("http://%s:%d", syndicator.getCollector().getHost(), syndicator.getCollector().getPort()));
		objectNode.put("wcm-server", wcmEvent.getContent());
		return JsonUtils.writeValueAsBytes(objectNode);
	}
	
	private void sendMessage(String topic, String key, byte[] payload, String token) {
		MessageBuilder<byte[]> messageBuilder = MessageBuilder.withPayload(payload)
				.setHeader(KafkaHeaders.TOPIC, topic)
				.setHeader(KafkaHeaders.MESSAGE_KEY, key)
				.setHeader("token", token.substring("Bearer ".length()));
	
	    kafkaTemplate.send(messageBuilder.build());
    }
}
