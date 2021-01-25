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
import com.bpwizard.wcm.repo.rest.handler.JcrNodeCollectionHandler;
import com.bpwizard.wcm.repo.rest.handler.RestNodeTypeHandler;
import com.bpwizard.wcm.repo.rest.jcr.model.WcmEvent;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;


@Component
public class JcrNodeEventKafkaListener {
	private static final Logger logger = LoggerFactory.getLogger(JcrNodeEventKafkaListener.class);
	@Autowired
	private JcrNodeCollectionHandler jcrNodeCollectionHandler;
	@Autowired
	protected RestNodeTypeHandler nodeTypeHandler;

	private String baseUrlPattern = "%s/%s/%s";
	
	private Map<String, ElementData> elementMap = new HashMap<>();
	private Map<String, ItemData> itemMap = new HashMap<>();
	
	@KafkaListener(containerFactory = "kafkaListenerContainerFactory", topics = { "jcr-wcm-event" }, groupId = "wcm-event")
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
					itemData.authoringSiteRootNodeKey = itemNode.get("rootNodeKey").textValue();
					itemData.removedDescendants = (ArrayNode) itemNode.get("removedDescendants");
					itemData.repository = itemNode.get("repository").textValue();
					itemData.workspace = itemNode.get("workspace").textValue();
					itemData.nodePath = itemNode.get("nodePath").textValue();
					itemData.itemType = itemNode.get("itemType").textValue();
					itemData.id = itemNode.get("id").textValue();
					itemData.authoringSiteJcrSystemKey = itemNode.get("jcrSystemKey").textValue();
					itemData.timestamp = new Timestamp(itemNode.get("timestamp").longValue());
					itemData.wcmServer = itemNode.get("wcm-server").textValue();
					if (WcmEvent.Operation.delete.name().equals(itemData.operation)) {	
						String baseUrl = String.format(baseUrlPattern, itemData.wcmServer, itemData.repository, itemData.workspace);
						jcrNodeCollectionHandler.collectJcrItem(
								itemData.repository, 
								itemData.workspace,
								itemData.id,
								itemData.nodePath,
								itemData.itemType,
								itemData.operation,
								JsonUtils.writeToStrings(itemData.removedDescendants),
								itemData.timestamp.getTime(),
								itemData.authoringSiteRootNodeKey,
								itemData.authoringSiteJcrSystemKey,
					    		null,
					    		baseUrl);
						itemMap.remove(key);
					} else {
						itemMap.put(key, itemData);
					}
				} else {
					// if (ObjectUtils.isEmpty(message)) {
					if (!ObjectUtils.isEmpty(finalMessageKey)) {	
						String baseUrl = String.format(baseUrlPattern, itemData.wcmServer, itemData.repository, itemData.workspace);
						jcrNodeCollectionHandler.collectJcrItem(
								itemData.repository, 
								itemData.workspace,
								itemData.id,
								itemData.nodePath,
								itemData.itemType,
								itemData.operation,
								JsonUtils.writeToStrings(itemData.removedDescendants),
								itemData.timestamp.getTime(),
								itemData.authoringSiteRootNodeKey,
								itemData.authoringSiteJcrSystemKey,
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
				
			} else if (key.startsWith("wcm-element-")) {
				ElementData elementData = elementMap.get(key);
				if (elementData == null) {
					elementData = new ElementData();
					ObjectNode elementNode = (ObjectNode)JsonUtils.bytesToJsonNode(message);
					elementData.id = elementNode.get("id").textValue();
					elementData.timestamp = new Timestamp(elementNode.get("timestamp").longValue());
					elementData.authoringSiteRootNodeKey = elementNode.get("rootNodeKey").textValue();;
					elementData.authoringSiteJcrSystemKey = elementNode.get("jcrSystemKey").textValue();;
					elementData.repository = elementNode.get("repository").textValue();
					elementData.workspace = elementNode.get("workspace").textValue();
					elementData.itemType = elementNode.get("itemType").textValue();
					elementData.operation = elementNode.get("operation").textValue();
					elementMap.put(key, elementData);
				} else {
					if (!ObjectUtils.isEmpty(finalMessageKey)) {	
						this.jcrNodeCollectionHandler.collectJcrElement(
								elementData.repository,
								elementData.workspace,
								elementData.id,
								elementData.operation,
								elementData.timestamp.getTime(),
								elementData.authoringSiteRootNodeKey,
								elementData.authoringSiteJcrSystemKey,
					    		new ByteArrayInputStream(elementData.content));
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
	
	class ElementData {
		String id;
		Timestamp timestamp;
		String itemType;
		String operation;
		String repository;
		String workspace;
		String authoringSiteRootNodeKey;
		String authoringSiteJcrSystemKey;
		byte[] content = {};
	}

}