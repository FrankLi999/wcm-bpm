package com.bpwizard.wcm.repo.rest.service;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import javax.jcr.RepositoryException;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import com.bpwizard.wcm.repo.rest.JsonUtils;
import com.bpwizard.wcm.repo.rest.handler.RestNodeTypeHandler;
import com.bpwizard.wcm.repo.rest.jcr.model.JcrNode;
import com.bpwizard.wcm.repo.rest.jcr.model.WcmEvent;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;


@Component
public class WcmEventKafkaListener {
	private static final Logger logger = LogManager.getLogger(WcmEventKafkaListener.class);
	
	@Autowired
	protected RestNodeTypeHandler nodeTypeHandler;
	
	@Autowired
	protected JcrNodeService jcrNodeService;
	private String baseUrlPattern = "http://localhost:28082/bpwizard/default";
	
	private Map<String, ElementData> elementMap = new HashMap<>();
	private Map<String, ItemData> itemMap = new HashMap<>();
	
	@KafkaListener(topics = { "wcm-event" }, groupId = "wcm-event")
	void commonListenerForMultipleTopics(
			@Header(name=KafkaHeaders.RECEIVED_MESSAGE_KEY, required=true) String key, 
			@Payload byte message[]) throws RepositoryException {
		if (logger.isDebugEnabled()) {
			logger.traceEntry();
		}
		
		try {
			if ("cnd-types".equals(key)) {
				ObjectNode wcmEvent = (ObjectNode)JsonUtils.inputStreamToJsonNode(new ByteArrayInputStream(message));
				String repository = wcmEvent.get("repository").textValue();
				String workspace = wcmEvent.get("workspace").textValue();
				String baseUrl = String.format(baseUrlPattern, wcmEvent);
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
					ObjectNode itemNode = (ObjectNode)JsonUtils.inputStreamToJsonNode(new ByteArrayInputStream(message));
					itemData.operation = itemNode.get("operation").textValue();
					itemData.id = itemNode.get("id").textValue();
					if (WcmEvent.Operation.delete.name().equals(itemData.operation)) {
						jcrNodeService.deleteJcrNode(itemData.id);
					} else {
						itemData.repository = itemNode.get("repository").textValue();
						itemData.workspace = itemNode.get("workspace").textValue();
						itemData.nodePath = itemNode.get("nodePath").textValue();
						itemData.itemType = itemNode.get("itemType").textValue();
						itemData.operation = itemNode.get("operation").textValue();
						itemData.id = itemNode.get("id").textValue();
						itemData.timestamp = new Timestamp(itemNode.get("timestamp").longValue());
						itemMap.put(key, itemData);
					}
					
					ArrayNode removedDescendants = (ArrayNode) itemNode.get("removedDescendants");
					if (removedDescendants != null && removedDescendants.size() > 0) {
						for (int i = 0; i < removedDescendants.size(); i++) {
							jcrNodeService.deleteJcrNode(removedDescendants.get(i).textValue());
						}
					}
				} else {
					if (ObjectUtils.isEmpty(message)) {
						JcrNode jcrNode = new JcrNode();
						jcrNode.setId(itemData.id);
						jcrNode.setLastUpdated(itemData.timestamp);
						jcrNode.setContent(new ByteArrayInputStream(itemData.content));
						jcrNodeService.updateJcrNode(jcrNode);
						itemMap.remove(key);
					} else {
						itemData.content = ByteBuffer.allocate(itemData.content.length + message.length)
								.put(itemData.content)
								.put(message)
								.array();
					}
				}
				
			} else if (key.startsWith("wcm-element-")) {
				ElementData elementData = elementMap.get(key);
				if (elementData == null) {
					elementData = new ElementData();
					ObjectNode elementNode = (ObjectNode)JsonUtils.inputStreamToJsonNode(new ByteArrayInputStream(message));
					elementData.id = elementNode.get("id").textValue();
					elementData.timestamp = new Timestamp(elementNode.get("timestamp").longValue());
					elementMap.put(key, elementData);
				} else {
					if (ObjectUtils.isEmpty(message)) {
						JcrNode jcrNode = new JcrNode();
						jcrNode.setId(elementData.id);
						jcrNode.setLastUpdated(elementData.timestamp);
						jcrNode.setContent(new ByteArrayInputStream(elementData.content));
						jcrNodeService.updateJcrNode(jcrNode);
						elementMap.remove(key);
					} else {
						elementData.content = ByteBuffer.allocate(elementData.content.length + message.length)
								.put(elementData.content)
								.put(message)
								.array();
					}
				}
			}
		} catch (IOException e) {
			//TODO
		}
		if (logger.isDebugEnabled()) {
			logger.traceExit();
		}
	}
	
	class ItemData {
		String repository;
		String workspace;
		String nodePath;
		String itemType;
		String operation;
		String id;
		Timestamp timestamp;
		byte[] content = {};
	}
	
	class ElementData {
		String id;
		Timestamp timestamp;
		byte[] content = {};
	}

}
