package com.bpwizard.wcm.repo.collector;


import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import javax.jcr.RepositoryException;

import org.modeshape.schematic.document.Document;
import org.modeshape.schematic.internal.document.BsonWriter;
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
import com.bpwizard.wcm.repo.rest.WcmUtils;
import com.bpwizard.wcm.repo.rest.handler.RestNodeTypeHandler;
import com.bpwizard.wcm.repo.rest.handler.WcmRequestHandler;
import com.bpwizard.wcm.repo.rest.jcr.model.AuthoringTemplate;
import com.bpwizard.wcm.repo.rest.jcr.model.JcrNodeEntry;
import com.bpwizard.wcm.repo.rest.jcr.model.RootNodeKeys;
import com.bpwizard.wcm.repo.rest.jcr.model.WcmEvent;
import com.bpwizard.wcm.repo.rest.jcr.model.WcmEventEntry;
import com.bpwizard.wcm.repo.rest.service.BsonSyndicationReader;
import com.bpwizard.wcm.repo.rest.service.JcrNodeService;
import com.bpwizard.wcm.repo.rest.service.RootNodeKeyService;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;


@Component
public class WcmEventKafkaListener {
	private static final Logger logger = LoggerFactory.getLogger(WcmEventKafkaListener.class);
	
	@Autowired
	protected RestNodeTypeHandler nodeTypeHandler;
	
	@Autowired
	protected JcrNodeService jcrNodeService;
	
	@Autowired
	protected RootNodeKeyService rootNodeKeyService;
	
	@Autowired
	private WcmRequestHandler wcmRequestHandler;
	
	@Autowired
	protected WcmUtils wcmUtils;
	
	private final BsonSyndicationReader BSON_READER = new BsonSyndicationReader();
	private final BsonWriter BSON_WRITER = new BsonWriter();
	
	private String baseUrlPattern = "%s/%s/%s";
	
	private Map<String, ElementData> elementMap = new HashMap<>();
	private Map<String, ItemData> itemMap = new HashMap<>();
	
	@KafkaListener(containerFactory = "kafkaListenerContainerFactory", topics = { "wcm-event" }, groupId = "wcm-event")
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

					if (WcmEvent.Operation.delete.name().equals(itemData.operation)) {		
						RootNodeKeys renderSiteRootNodeKeys = this.rootNodeKeyService.getRootNodeKeys(
								itemData.repository, itemData.workspace);
						this.deleteJcrNode(itemData.id, itemData.authoringSiteRootNodeKey, renderSiteRootNodeKeys);
						this.removedDescendants(itemData.removedDescendants, itemData.authoringSiteRootNodeKey, renderSiteRootNodeKeys);
					} else {
						itemData.nodePath = itemNode.get("nodePath").textValue();
						itemData.itemType = itemNode.get("itemType").textValue();
						itemData.id = itemNode.get("id").textValue();
						itemData.authoringSiteJcrSystemKey = itemNode.get("jcrSystemKey").textValue();
						itemData.timestamp = new Timestamp(itemNode.get("timestamp").longValue());
						itemData.wcmServer = itemNode.get("wcm-server").textValue();
						itemMap.put(key, itemData);
					}
				} else {
					// if (ObjectUtils.isEmpty(message)) {
					if (!ObjectUtils.isEmpty(finalMessageKey)) {		
						RootNodeKeys renderSiteRootNodeKeys = this.rootNodeKeyService.getRootNodeKeys(
								itemData.repository, itemData.workspace);
						this.removedDescendants(itemData.removedDescendants, itemData.authoringSiteRootNodeKey, renderSiteRootNodeKeys);
						this.updateJcrNode(itemData.id, itemData.timestamp, itemData.content, itemData.authoringSiteRootNodeKey, itemData.authoringSiteJcrSystemKey, renderSiteRootNodeKeys);
						itemMap.remove(key);
						if (WcmEventEntry.WcmItemType.authoringTemplate.name().equals(itemData.itemType)) {
							// get at, create the corresponding JCR type
							String baseUrl = String.format(baseUrlPattern, itemData.wcmServer, itemData.repository, itemData.workspace);
							AuthoringTemplate at = this.wcmRequestHandler.getAuthoringTemplate(
									itemData.repository, 
									itemData.workspace, 
									itemData.nodePath.substring("/library".length()), 
									baseUrl);
							this.wcmUtils.registerNodeType(at.getWorkspace(), at);
						}
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
					elementData.renderSiteRootNodeKeys = this.rootNodeKeyService.getRootNodeKeys(
							elementData.repository, elementData.workspace);
					elementMap.put(key, elementData);
				} else {
					if (!ObjectUtils.isEmpty(finalMessageKey)) {	
						this.updateJcrNode(elementData.id, elementData.timestamp, elementData.content, 
								elementData.authoringSiteRootNodeKey, elementData.authoringSiteJcrSystemKey, 
								elementData.renderSiteRootNodeKeys);
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
	
	private void updateJcrNode(String id, Timestamp lastUpdated, byte[] content, String rootNodeKey, String jcrSystemKey, RootNodeKeys rootNodeKeys) throws RepositoryException, IOException {
		Map<String, String> rootNodeKeyMap = new HashMap<>();
		rootNodeKeyMap.put(rootNodeKey, rootNodeKeys.getRootNodeKey());
		rootNodeKeyMap.put(jcrSystemKey, rootNodeKeys.getJcrSystemKey());		
		JcrNodeEntry jcrNode = new JcrNodeEntry();
		jcrNode.setId(this.translateId(id, rootNodeKey, rootNodeKeys.getRootNodeKey()));
		jcrNode.setLastUpdated(lastUpdated);
		Document document = BSON_READER.read(new ByteArrayInputStream(content), rootNodeKeyMap);
		jcrNode.setContent(new ByteArrayInputStream(BSON_WRITER.write(document)));
		jcrNodeService.updateJcrNode(jcrNode);
	}
	
	private void deleteJcrNode(String id, String authoringRootNodeKey, RootNodeKeys renderingRootNodeKeys) throws RepositoryException, IOException {
		jcrNodeService.deleteJcrNode(this.translateId(id, authoringRootNodeKey, renderingRootNodeKeys.getRootNodeKey()));
	}
	
	private String translateId(String id, String authoringRootKey, String renderingRootKey) {
		return id.replaceFirst(authoringRootKey, renderingRootKey);
	}
	
	private void removedDescendants(ArrayNode removedDescendants, String authoringRootNodeKey, RootNodeKeys renderingRootNodeKeys) throws RepositoryException, IOException {
		if (removedDescendants != null && removedDescendants.size() > 0) {
			for (int i = 0; i < removedDescendants.size(); i++) {
				this.deleteJcrNode(removedDescendants.get(i).textValue(), authoringRootNodeKey, renderingRootNodeKeys);
			}
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
		RootNodeKeys renderSiteRootNodeKeys;
		byte[] content = {};
	}

}