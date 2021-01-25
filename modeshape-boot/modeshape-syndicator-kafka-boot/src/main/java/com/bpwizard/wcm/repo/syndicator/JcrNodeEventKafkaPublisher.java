package com.bpwizard.wcm.repo.syndicator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.jcr.RepositoryException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import com.bpwizard.wcm.repo.rest.JsonUtils;
import com.bpwizard.wcm.repo.rest.jcr.model.JcrNode;
import com.bpwizard.wcm.repo.rest.jcr.model.RootNodeKeys;
import com.bpwizard.wcm.repo.rest.jcr.model.Syndicator;
import com.bpwizard.wcm.repo.rest.jcr.model.WcmEvent;
import com.bpwizard.wcm.repo.rest.service.JcrNodeRepository;
import com.bpwizard.wcm.repo.rest.service.JcrNodeEventQueuePublisher;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

@Component
public class JcrNodeEventKafkaPublisher implements JcrNodeEventQueuePublisher {
	private static final byte FINAL_MESSAGE_SEGMENT[] = "~final~".getBytes(); 
	@Autowired
	private KafkaTemplate<String, byte[]> kafkaTemplate;
	
	@Autowired
	private JcrNodeRepository jcrNodeRepository;
	
	@Value("${bpw.wcm.kafka.topic.jcr-wcm-event}")
	private String wcmEventTopic;
	
	public void syndicateCndTypes(WcmEvent wcmEvent, Syndicator syndicator, String token) throws JsonProcessingException, IOException {
		this.sendMessage(wcmEventTopic, "cnd-types", cndToJsonBytes(wcmEvent, syndicator), token, true);
	}
	
	// public void syndicate(WcmEvent wcmEvent) throws IOException {
	public void syndicate(WcmEvent wcmEvent, Syndicator syndicator, String token, RootNodeKeys rootNodeKeys) throws RepositoryException, IOException {
		// Split large messages into 1 KB segments with the producing client, using partition keys to 
		// ensure that all segments are sent to the same Kafka partition in the correct order. 
		// The consuming client can then reconstruct the original large message. 
		
		List<String> descendantIds = getDescendantIds(wcmEvent);
		for (String id : descendantIds) {
			JcrNode jcrNode = this.getJcrNode(rootNodeKeys.getRootNodeKey(), id);
			this.sendMessage(
					wcmEventTopic, 
					String.format("wcm-element-%s-%s", wcmEvent.getId(), id), 
					jcrNodeToJsonBytes(wcmEvent, jcrNode, syndicator, rootNodeKeys),
					token,
					false);
			this.sendContent(String.format("wcm-element-%s-%s", wcmEvent.getId(), id), jcrNode.getContent(), token);
		}
		
		JcrNode jcrNode = this.getJcrNode(rootNodeKeys.getRootNodeKey(), wcmEvent.getId());
		this.sendMessage(
				wcmEventTopic, 
				String.format("wcm-item-%s", wcmEvent.getId()), 
				eventToJsonBytes(wcmEvent, syndicator, jcrNode, rootNodeKeys),
				token,
				false);
		if (!WcmEvent.Operation.delete.equals(wcmEvent.getOperation())) {
			this.sendContent(String.format("wcm-item-%s", wcmEvent.getId()), jcrNode.getContent(), token);
		}
		
	}
	
	private byte[] cndToJsonBytes(WcmEvent wcmEvent, Syndicator syndicator) throws JsonProcessingException, IOException {
		ObjectNode objectNode = JsonUtils.createObjectNode();
		objectNode.put("repository", wcmEvent.getRepository());
		objectNode.put("workspace", wcmEvent.getWorkspace());
		objectNode.put("content", wcmEvent.getContent());
		objectNode.put("wcm-server", String.format("http://%s:%d", syndicator.getCollector().getHost(), syndicator.getCollector().getPort()));
		return JsonUtils.writeValueAsBytes(objectNode);
	}
	
	private byte[] eventToJsonBytes(WcmEvent wcmEvent, Syndicator syndicator, JcrNode jcrNode, RootNodeKeys rootNodeKeys) throws JsonProcessingException, IOException {
		ObjectNode objectNode = JsonUtils.createObjectNode();
		objectNode.put("id", jcrNode.getId());
		objectNode.put("repository", wcmEvent.getRepository());
		objectNode.put("workspace", wcmEvent.getWorkspace());
		objectNode.put("library", wcmEvent.getLibrary());
		objectNode.put("nodePath", wcmEvent.getNodePath());
		objectNode.put("itemType", wcmEvent.getItemType().name());
		objectNode.put("operation", wcmEvent.getOperation().name());
		objectNode.put("timestamp", jcrNode.getLastUpdated().getTime());
		objectNode.put("wcm-server", String.format("http://%s:%d", syndicator.getCollector().getHost(), syndicator.getCollector().getPort()));
		objectNode.put("jcrSystemKey", rootNodeKeys.getJcrSystemKey());
		objectNode.put("rootNodeKey", rootNodeKeys.getRootNodeKey());
		

		ObjectNode contentNode = (ObjectNode) JsonUtils.bytesToJsonNode(wcmEvent.getContent());
		ArrayNode descendants = (ArrayNode) contentNode.get("descendants");
		if (descendants != null && descendants.size() > 0) {
			contentNode.set("descendants", descendants);
		}
		
		ArrayNode removedDescendants = (ArrayNode) contentNode.get("removedDescendants");
		if (removedDescendants != null && removedDescendants.size() > 0) {
			contentNode.set("removedDescendants", removedDescendants);
		}
		
		return JsonUtils.writeValueAsBytes(objectNode);
	}
	
	private byte[] jcrNodeToJsonBytes(WcmEvent wcmEvent, JcrNode jcrNode, Syndicator syndicator, RootNodeKeys rootNodeKeys) throws JsonProcessingException, IOException {
		
		ObjectNode objectNode = JsonUtils.createObjectNode();
		objectNode.put("repository", syndicator.getRepository());
		objectNode.put("workspace", syndicator.getWorkspace());
		objectNode.put("itemType", wcmEvent.getItemType().name());
		objectNode.put("operation", wcmEvent.getOperation().name());
		objectNode.put("id", jcrNode.getId());
		objectNode.put("timestamp", jcrNode.getLastUpdated().getTime());
		
		objectNode.put("jcrSystemKey", rootNodeKeys.getJcrSystemKey());
		objectNode.put("rootNodeKey", rootNodeKeys.getRootNodeKey());
		
		return JsonUtils.writeValueAsBytes(objectNode);
	}
	
	private List<String> getDescendantIds(WcmEvent wcmEvent) throws IOException {
		ObjectNode contentNode = (ObjectNode) JsonUtils.bytesToJsonNode(wcmEvent.getContent());
		ArrayNode descendants = (ArrayNode) contentNode.get("descendants");
		List<String> descendantIds = new ArrayList<>();
		if (descendants != null) {
			for (int i = 0; i < descendants.size(); i++) {
				descendantIds.add(descendants.get(i).textValue());
			}
		}
		return descendantIds;
	}

	private void sendContent(String key, byte[] payload, String token) throws IOException {
//			
//			int blockSize = 10240; //best chunk size is 10240
//			
//			for (byte[] dataChunk = content.readNBytes(blockSize); dataChunk.length > 0; dataChunk = content.readNBytes(blockSize)) {
//				if (dataChunk.length > 0) {
//					this.sendMessage(wcmEventTopic, key, dataChunk);
//				}
//			}
		this.sendMessage(wcmEventTopic, key, payload, token, false);
		this.sendMessage(wcmEventTopic, key, FINAL_MESSAGE_SEGMENT, token, true);
	}
		
	
	private void sendMessage(String topic, String key, byte[] payload, String token, boolean resolveSecrurity) {
		MessageBuilder<byte[]> messageBuilder = MessageBuilder.withPayload(payload)
				.setHeader(KafkaHeaders.TOPIC, topic)
				.setHeader(KafkaHeaders.MESSAGE_KEY, key);
		if (resolveSecrurity) {
			messageBuilder.setHeader("token", token.substring("Bearer ".length()));
		}
		
		if (FINAL_MESSAGE_SEGMENT == payload) {
			messageBuilder.setHeader("final", "final");
		}
	    kafkaTemplate.send(messageBuilder.build());
    }
	
	private JcrNode getJcrNode(String rootNodeKey, String nodeId) {
		String nodeKey =  String.format("%s%s", rootNodeKey, nodeId);
		return jcrNodeRepository.getJcrNode(nodeKey);
	}
}