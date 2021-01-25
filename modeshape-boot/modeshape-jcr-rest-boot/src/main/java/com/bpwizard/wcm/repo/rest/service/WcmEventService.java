package com.bpwizard.wcm.repo.rest.service;


import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import com.bpwizard.wcm.repo.rest.JsonUtils;
import com.bpwizard.wcm.repo.rest.WcmUtils;
import com.bpwizard.wcm.repo.rest.jcr.model.WcmEventEntry;
import com.bpwizard.wcm.repo.rest.modeshape.model.RestNode;
import com.bpwizard.wcm.repo.rest.modeshape.model.RestProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

@Service
public class WcmEventService { 
	private static final Logger logger = LoggerFactory.getLogger(WcmEventService.class);
	
	@Autowired
	private JcrNodeEventRepository jcrNodeEventRepo;

	@Autowired
	private JsonNodeEventRepository jsonNodeEventRepo;
	
	@Transactional
	public void addCNDEvent(
			String repositoryName,
			String workspace,
			InputStream is,
			WcmEventEntry.WcmItemType itemType) throws JsonProcessingException {
		
		Long timestamp = System.currentTimeMillis();

	    WcmEventEntry wcmEvent = new WcmEventEntry();
	    wcmEvent.setId(String.format("cnd_%s", timestamp));
	    wcmEvent.setRepository(repositoryName);
	    wcmEvent.setWorkspace(workspace);
	    wcmEvent.setItemType(itemType);
	    wcmEvent.setOperation(WcmEventEntry.Operation.create);
	    wcmEvent.setTimeCreated(new Timestamp(timestamp));
	    wcmEvent.setContent(is);
	    jcrNodeEventRepo.addWcmEvent(wcmEvent);
	    jsonNodeEventRepo.addWcmEvent(wcmEvent);
	}
	
	@Transactional
	public void addNewItemEvent(
			RestNode restNode, 
			String repositoryName,
			String workspace,
			String path,
			WcmEventEntry.WcmItemType itemType,
			JsonNode requestBody) throws JsonProcessingException {
		
		WcmEventEntry event = this.createNewItemEvent(
				restNode, 
				repositoryName, 
				workspace, 
				path, 
				itemType,
				requestBody);
		jcrNodeEventRepo.addWcmEvent(event);
		jsonNodeEventRepo.addWcmEvent(event);
	}
	
	@Transactional
	public void addNewItemEvents(List<WcmEventEntry> events) {
		jcrNodeEventRepo.batchInsert(events);
		jsonNodeEventRepo.batchInsert(events);
	}
	
	@Transactional
	public  void addUpdateItemEvents(List<WcmEventEntry> events) throws JsonProcessingException {
		jcrNodeEventRepo.batchUpdate(events);
		jsonNodeEventRepo.batchUpdate(events);
	}
	
	@Transactional
	public void addDeleteItemEvents(List<WcmEventEntry> events) throws JsonProcessingException {
		jcrNodeEventRepo.batchUpdate(events);
		jsonNodeEventRepo.batchUpdate(events);
	}
	
	@Transactional
	public void addUpdateItemEvent(
			RestNode restNode, 
			String repositoryName,
			String workspace,
			String path,
			WcmEventEntry.WcmItemType itemType,
			Set<String> previousDescendants,
			JsonNode requestBody) throws JsonProcessingException {
		
		WcmEventEntry event = this.createUpdateItemEvent(
				restNode, 
				repositoryName, 
				workspace, path, 
				itemType, 
				previousDescendants,
				requestBody);
		jcrNodeEventRepo.updateWcmEvent(event);	
		jsonNodeEventRepo.updateWcmEvent(event);	
	}
	
	@Transactional
	public void addDeleteItemEvent(
			String nodeId, 
			String repositoryName,
			String workspace,
			String path,
			WcmEventEntry.WcmItemType itemType,
			Set<String> previousDescendants) throws JsonProcessingException {
		
		WcmEventEntry event = this.createDeleteItemEvent(
				nodeId, 
				repositoryName, 
				workspace, 
				path, 
				itemType, 
				previousDescendants);
		jcrNodeEventRepo.updateWcmEvent(event);
		jsonNodeEventRepo.updateWcmEvent(event);
	}
	
	public WcmEventEntry createNewItemEvent(
			RestNode restNode, 
			String repositoryName,
			String workspace,
			String nodePath,
			WcmEventEntry.WcmItemType itemType,
			JsonNode requestBody) {
		
		WcmEventEntry event = new WcmEventEntry();
		event.setId(restNode.getId());
		event.setRepository(repositoryName);
		event.setWorkspace(workspace);
		event.setLibrary(WcmUtils.library(nodePath));
		event.setNodePath(nodePath);
		event.setOperation(WcmEventEntry.Operation.create);
		event.setItemType(itemType);
		event.setJsonNode(requestBody);
		for (RestProperty prop: restNode.getJcrProperties()) {
		    if ("jcr:lastModified".equals(prop.getName())) {
		    	event.setTimeCreated(Timestamp.valueOf(prop.getValues().get(0)));
		    }
		}
		
		Set<String> descendants = new HashSet<>();
		this.populateDescendantIds(restNode, descendants);
		// event.setDescendants(descendants);
		event.setContent(content(descendants, null));
		return event;
	}
	
	public WcmEventEntry createUpdateItemEvent(
			RestNode restNode, 
			String repositoryName,
			String workspace,
			String nodePath,
			WcmEventEntry.WcmItemType itemType,
			Set<String> previousDescendants,
			JsonNode requestBody) {
		
		WcmEventEntry event = new WcmEventEntry();
		event.setId(restNode.getId());
		event.setRepository(repositoryName);
		event.setWorkspace(workspace);
		event.setLibrary(WcmUtils.library(nodePath));
		event.setNodePath(nodePath);
		event.setOperation(WcmEventEntry.Operation.update);
		event.setItemType(itemType);
		event.setJsonNode(requestBody);
		
		for (RestProperty prop: restNode.getJcrProperties()) {
		    if ("jcr:lastModified".equals(prop.getName())) {
		    	event.setTimeCreated(Timestamp.valueOf(prop.getValues().get(0)));
		    }
		}
		
		Set<String> descendants = new HashSet<>();
		this.populateDescendantIds(restNode, descendants);
		// event.setDescendants(descendants);
		
		Set<String> removedDescendants = new HashSet<>();
		
		for (String previousDescendant: previousDescendants) {
			if (!descendants.contains(previousDescendant)) {
				removedDescendants.add(previousDescendant);
			}
		}
		// event.setRemovedDescendants(removedDescendants);
		event.setContent(content(descendants, removedDescendants));
		return event;
	}
	
	public WcmEventEntry createDeleteItemEvent(
			String nodeId, 
			String repositoryName,
			String workspace,
			String nodePath,
			WcmEventEntry.WcmItemType itemType,
			Set<String> previousDescendants) {
		
		WcmEventEntry event = new WcmEventEntry();
		
		event.setRepository(repositoryName);
		event.setWorkspace(workspace);
		event.setLibrary(WcmUtils.library(nodePath));
		event.setNodePath(nodePath);
		event.setOperation(WcmEventEntry.Operation.delete);
		event.setItemType(itemType);
		event.setId(nodeId);
		event.setTimeCreated(new Timestamp(System.currentTimeMillis()));
		event.setContent(content(null, previousDescendants));
//		event.setDescendants(null);
//		event.setRemovedDescendants(previousDescendants);
		return event;
	}
	
	public void populateDescendantIds(RestNode restNode, Set<String> nodeIds) {
		if (logger.isDebugEnabled()) {
			logger.debug("Entry");
		}
		for (RestNode child: restNode.getChildren()) {
			nodeIds.add(child.getId());
			populateDescendantIds(child, nodeIds);
		}
		if (logger.isDebugEnabled()) {
			logger.debug("Exit");
		}
	}

	protected ByteArrayInputStream content(Set<String> descendantIds, Set<String> removedDescendantIds) {
    	
		ObjectNode contentNode = JsonUtils.createObjectNode();
		if (!ObjectUtils.isEmpty(descendantIds)) {
			ArrayNode descendants = JsonUtils.creatArrayNode();
			for (String value : descendantIds) {
				descendants.add(value);
			}
			contentNode.set("descendants", descendants);
		}
		if (!ObjectUtils.isEmpty(removedDescendantIds)) {
			ArrayNode removedDescendants = JsonUtils.creatArrayNode();
			for (String value : removedDescendantIds) {
				removedDescendants.add(value);
				contentNode.set("removedDescendants", removedDescendants);
			}
		}
		try {
			return new ByteArrayInputStream(JsonUtils.writeValueAsString(contentNode).getBytes());
		} catch (JsonProcessingException e) {
			// TODO
			throw new RuntimeException(e);
		}
    }
}
