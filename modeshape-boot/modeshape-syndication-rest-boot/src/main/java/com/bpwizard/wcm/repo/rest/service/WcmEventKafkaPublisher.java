package com.bpwizard.wcm.repo.rest.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;

import com.bpwizard.wcm.repo.rest.JsonUtils;
import com.bpwizard.wcm.repo.rest.jcr.model.JcrNode;
import com.bpwizard.wcm.repo.rest.jcr.model.WcmEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.base.Charsets;

@Component
public class WcmEventKafkaPublisher implements WcmEventQueuePublisher {
	@Autowired
	private KafkaTemplate<String, Object> kafkaTemplate;
	
	@Autowired
	private JcrNodeService jcrNodeService;
	
	@Value("${kafka.topic.wcm-event}")
	private String wcmEventTopic;
	
	public void syndicateCndTypes(WcmEvent wcmEvent) throws IOException {
		this.sendMessage(wcmEventTopic, "cnd-types", cndToJsonString(wcmEvent).getBytes());
	}
	
	public void syndicate(WcmEvent wcmEvent) throws IOException {
		// Split large messages into 1 KB segments with the producing client, using partition keys to 
		// ensure that all segments are sent to the same Kafka partition in the correct order. 
		// The consuming client can then reconstruct the original large message. 
		
		List<String> descendantIds = getDescendantIds(wcmEvent);
		for (String id : descendantIds) {
			JcrNode jcrNode = jcrNodeService.getJcrNode(id);
			this.sendMessage(wcmEventTopic, String.format("wcm-element-%s-%s", wcmEvent.getId(), id), jcrNodeToJsonString(jcrNode).getBytes());
			this.sendContent(String.format("wcm-element-%s-%s", wcmEvent.getId(), id), jcrNode.getContent());
		}
		JcrNode jcrNode = jcrNodeService.getJcrNode(wcmEvent.getId());
		this.sendMessage(wcmEventTopic, String.format("wcm-item-%s", wcmEvent.getId()), eventToJsonString(wcmEvent, jcrNode).getBytes());
		this.sendContent(String.format("wcm-item-%s", wcmEvent.getId()), jcrNode.getContent());
	}
	
	private String cndToJsonString(WcmEvent wcmEvent) throws JsonProcessingException, IOException {
		ObjectNode objectNode = JsonUtils.createObjectNode();
		objectNode.put("id", wcmEvent.getId());
		objectNode.put("repository", wcmEvent.getRepository());
		objectNode.put("workspace", wcmEvent.getWorkspace());
		objectNode.put("library", wcmEvent.getLibrary());
		objectNode.put("nodePath", wcmEvent.getNodePath());
		
		objectNode.put("itemType", wcmEvent.getItemType().name());
		objectNode.put("operation", wcmEvent.getOperation().name());
		objectNode.put("timestamp", wcmEvent.getTimeCreated().getTime());
		objectNode.put("content", StreamUtils.copyToString(wcmEvent.getContent(), Charsets.UTF_8));
		return JsonUtils.writeValueAsString(objectNode);
	}
	
	private void sendContent(String key, InputStream content) throws IOException {
		
		int blockSize = 10240; //best chunk size is 10240
		
		for (byte[] dataChunk = content.readNBytes(blockSize); dataChunk.length > 0; dataChunk = content.readNBytes(blockSize)) {
			if (dataChunk.length > 0) {
				this.sendMessage(wcmEventTopic, key, dataChunk);
			}
		}
		this.sendMessage(wcmEventTopic, key, null);
	}
		
	private String eventToJsonString(WcmEvent wcmEvent, JcrNode jcrNode) throws JsonProcessingException, IOException {
		ObjectNode objectNode = JsonUtils.createObjectNode();
		objectNode.put("id", jcrNode.getId());
		objectNode.put("repository", wcmEvent.getRepository());
		objectNode.put("workspace", wcmEvent.getWorkspace());
		objectNode.put("library", wcmEvent.getLibrary());
		objectNode.put("nodePath", wcmEvent.getNodePath());
		
		objectNode.put("itemType", wcmEvent.getItemType().name());
		objectNode.put("operation", wcmEvent.getOperation().name());
		objectNode.put("timestamp", jcrNode.getLastUpdated().getTime());

		ObjectNode contentNode = (ObjectNode) JsonUtils.inputStreamToJsonNode(wcmEvent.getContent());
		ArrayNode descendants = (ArrayNode) contentNode.get("descendants");
		if (descendants != null && descendants.size() > 0) {
			contentNode.set("descendants", descendants);
		}
		
		ArrayNode removedDescendants = (ArrayNode) contentNode.get("removedDescendants");
		if (removedDescendants != null && removedDescendants.size() > 0) {
			contentNode.set("removedDescendants", removedDescendants);
		}
		
		return JsonUtils.writeValueAsString(objectNode);
	}
	
	private String jcrNodeToJsonString(JcrNode jcrNode) throws JsonProcessingException, IOException {
		
		ObjectNode objectNode = JsonUtils.createObjectNode();
		objectNode.put("id", jcrNode.getId());
		objectNode.put("lastUpdated", jcrNode.getLastUpdated().getTime());
		return JsonUtils.writeValueAsString(objectNode);
	}
	
	private List<String> getDescendantIds(WcmEvent wcmEvent) throws IOException {
		ObjectNode contentNode = (ObjectNode) JsonUtils.inputStreamToJsonNode(wcmEvent.getContent());
		ArrayNode descendants = (ArrayNode) contentNode.get("descendants");
		List<String> descendantIds = new ArrayList<>();
		if (descendants != null) {
			for (int i = 0; i < descendants.size(); i++) {
				descendantIds.add(descendants.get(i).textValue());
			}
		}
		return descendantIds;
	}
	
	private void sendMessage(String topic, String key, byte[] message) {
	    kafkaTemplate.send(topic, key, message);
    }
}
