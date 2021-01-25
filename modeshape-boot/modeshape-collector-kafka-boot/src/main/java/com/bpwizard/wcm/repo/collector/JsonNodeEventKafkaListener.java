package com.bpwizard.wcm.repo.collector;


import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import javax.jcr.RepositoryException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import com.bpwizard.wcm.repo.rest.JsonUtils;
import com.bpwizard.wcm.repo.rest.handler.JsonNodeCollectionHandler;
import com.bpwizard.wcm.repo.rest.handler.RestNodeTypeHandler;
import com.bpwizard.wcm.repo.rest.jcr.model.WcmEvent;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;


@Component
public class JsonNodeEventKafkaListener {
	private static final Logger logger = LoggerFactory.getLogger(JsonNodeEventKafkaListener.class);
	
	@Autowired
	private JsonNodeCollectionHandler jsonNodeCollectionHandler;
	
	@Autowired
	protected RestNodeTypeHandler nodeTypeHandler;
	
	private String baseUrlPattern = "%s/%s/%s";
	private Map<String, ItemData> itemMap = new HashMap<>();
	
	@KafkaListener(containerFactory = "kafkaListenerContainerFactory", topics = { "json-wcm-event" }, groupId = "wcm-event")
	void commonListenerForMultipleTopics(
			@Header(name=KafkaHeaders.RECEIVED_MESSAGE_KEY, required=true) String key,
			@Header(name="final", required=false) String finalMessageKey, 
			@Payload byte message[]) throws RepositoryException {
		if (logger.isDebugEnabled()) {
			logger.debug("Entry");
		}
		
		try {
			if ("cnd-types".equals(key)) {
				ObjectNode wcmEvent = (ObjectNode)JsonUtils.bytesToJsonNode(message);
				String repository = wcmEvent.get("repository").textValue();
				String workspace = wcmEvent.get("workspace").textValue();
				
				String baseUrl = String.format(baseUrlPattern, wcmEvent.get("wcm-server").textValue(), repository, workspace);
				
				this.nodeTypeHandler.importCND(
						baseUrl, 
						repository, 
						workspace, 
						true, 
						new ByteArrayInputStream(wcmEvent.get("content").binaryValue()));
			} else if (key.startsWith("wcm-item-")) {
				ItemData itemData = itemMap.get(key);
				if (itemData == null) {
					itemData = new ItemData();
					ObjectNode itemNode = (ObjectNode)JsonUtils.bytesToJsonNode(message);
					itemData.operation = itemNode.get("operation").textValue();
					itemData.id = itemNode.get("id").textValue();
					itemData.repository = itemNode.get("repository").textValue();
					itemData.workspace = itemNode.get("workspace").textValue();
					itemData.nodePath = itemNode.get("nodePath").textValue();
					itemData.itemType = itemNode.get("itemType").textValue();
					itemData.wcmServer = itemNode.get("wcm-server").textValue();
					if (WcmEvent.Operation.delete.name().equals(itemData.operation)) {	
						String baseUrl = String.format(baseUrlPattern, itemData.wcmServer, itemData.repository, itemData.workspace);
						jsonNodeCollectionHandler.collectJsonItem(
							itemData.repository, 
							itemData.workspace, 
							itemData.nodePath,
							itemData.itemType,
							itemData.operation,
							null,
							baseUrl);
						itemMap.remove(key);
					} else {
						itemMap.put(key, itemData);
					}
				} else {
					if (!ObjectUtils.isEmpty(finalMessageKey)) {	
						String baseUrl = String.format(baseUrlPattern, itemData.wcmServer, itemData.repository, itemData.workspace);
						jsonNodeCollectionHandler.collectJsonItem(
							itemData.repository, 
							itemData.workspace, 
							itemData.nodePath,
							itemData.itemType,
							itemData.operation,
							new ByteArrayInputStream(itemData.content),
							baseUrl);
						itemMap.remove(key);
					} else {
						itemData.content = ByteBuffer.allocate(itemData.content.length + message.length)
								.put(itemData.content)
								.put(message)
								.array();
					}
				}
				
			} 
		} catch (IOException e) {
			//TODO
		}
		if (logger.isDebugEnabled()) {
			logger.debug("Exit");
		}
	}
	
	class ItemData {
		String repository;
		String workspace;
		String nodePath;
		String itemType;
		String operation;
		String id;
		String authoringSiteRootNodeKey;
		String authoringSiteJcrSystemKey;
		Timestamp timestamp;
		String wcmServer;
		ArrayNode removedDescendants;
		byte[] content = {};
	}
}