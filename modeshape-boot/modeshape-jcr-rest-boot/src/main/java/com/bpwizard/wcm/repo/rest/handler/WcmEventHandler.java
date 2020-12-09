package com.bpwizard.wcm.repo.rest.handler;


import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import com.bpwizard.wcm.repo.rest.JsonUtils;
import com.bpwizard.wcm.repo.rest.WcmUtils;
import com.bpwizard.wcm.repo.rest.jcr.model.SyndicationRequest;
import com.bpwizard.wcm.repo.rest.jcr.model.Syndicator;
import com.bpwizard.wcm.repo.rest.jcr.model.WcmEvent;
import com.bpwizard.wcm.repo.rest.modeshape.model.RestNode;
import com.bpwizard.wcm.repo.rest.modeshape.model.RestProperty;
import com.bpwizard.wcm.repo.rest.service.SyndicatorService;
import com.bpwizard.wcm.repo.rest.service.WcmEventKafkaPublisher;
import com.bpwizard.wcm.repo.rest.service.WcmEventRestPublisher;
import com.bpwizard.wcm.repo.rest.service.WcmEventService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

@Service
public class WcmEventHandler {
	private static final Logger logger = LoggerFactory.getLogger(SyndicatorService.class);
	
	@Autowired
	private WcmEventService wcmEventService;
	
	@Autowired
	private SyndicatorService syndicatorService;
	
	@Autowired
	private WcmEventKafkaPublisher wcmEventKafkaPublisher;
	
	@Autowired
	private WcmEventRestPublisher wcmEventRestPublisher;
	
	@Value("${syndication.strategy}")
	private String syndicationStrategy; //rest or Kafka
	
	@Transactional
	public void syndicate(SyndicationRequest syndicationRequest, String token) throws IOException {
		Syndicator syndicator = syndicatorService.getSyndicator(syndicationRequest.getSyndicationId());
		List<WcmEvent> cndEvents = wcmEventService.getCndAfter(syndicator, syndicationRequest.getEndTime());
		if (cndEvents != null) {
			for (WcmEvent cndEvent: cndEvents) {
				if ("kafka".equals(syndicationStrategy)) {
					wcmEventKafkaPublisher.syndicateCndTypes(cndEvent);
				} else {
					wcmEventRestPublisher.syndicateCndTypes(cndEvent, syndicator, token);
				}
			}
		}
		int pageSize = 20;
		int pageIndex = 0;
		int actualBatchSize = 0;
		List<WcmEvent> wcmEvents = null;
		do {
			
			wcmEvents = wcmEventService.getWcmEventAfter(syndicator, syndicationRequest.getEndTime(), pageIndex, pageSize);
			actualBatchSize = wcmEvents.size();
			if (actualBatchSize > 0) {
				for (WcmEvent wcmEvent: wcmEvents) {
					if ("kafka".equals(syndicationStrategy)) {
						wcmEventKafkaPublisher.syndicate(wcmEvent);
					} else {
						wcmEventRestPublisher.syndicate(wcmEvent, syndicator, token);
					}
					
				}
			}
		} while (actualBatchSize >= pageSize);	
		
	}
	
	@Transactional
	public void addCNDEvent(
			String repositoryName,
			String workspace,
			InputStream is,
			WcmEvent.WcmItemType itemType) {
		
		Long timestamp = System.currentTimeMillis();

	    WcmEvent wcmEvent = new WcmEvent();
	    wcmEvent.setId(String.format("cnd_%s", timestamp));
	    wcmEvent.setRepository(repositoryName);
	    wcmEvent.setWorkspace(workspace);
	    wcmEvent.setItemType(itemType);
	    wcmEvent.setOperation(WcmEvent.Operation.create);
	    wcmEvent.setTimeCreated(new Timestamp(timestamp));
	    wcmEvent.setContent(is);
	    wcmEventService.addWcmEvent(wcmEvent);
	}
	
	@Transactional
	public void addNewItemEvent(
			RestNode restNode, 
			String repositoryName,
			String workspace,
			String path,
			WcmEvent.WcmItemType itemType) {
		
		WcmEvent event = this.createNewItemEvent(
				restNode, 
				repositoryName, 
				workspace, 
				path, 
				itemType);
		wcmEventService.addWcmEvent(event);
	}
	
	@Transactional
	public int[] addNewItemEvents(List<WcmEvent> events) {
		return wcmEventService.batchInsert(events);
	}
	
	@Transactional
	public int[] addUpdateItemEvents(List<WcmEvent> events) {
		return wcmEventService.batchUpdate(events);
	}
	
	@Transactional
	public int[] addDeleteItemEvents(List<WcmEvent> events) {
		return wcmEventService.batchUpdate(events);
	}
	
	@Transactional
	public void addUpdateItemEvent(
			RestNode restNode, 
			String repositoryName,
			String workspace,
			String path,
			WcmEvent.WcmItemType itemType,
			Set<String> previousDescendants) {
		
		WcmEvent event = this.createUpdateItemEvent(
				restNode, 
				repositoryName, 
				workspace, path, 
				itemType, 
				previousDescendants);
		wcmEventService.updateWcmEvent(event);	}
	
	@Transactional
	public void addDeleteItemEvent(
			String nodeId, 
			String repositoryName,
			String workspace,
			String path,
			WcmEvent.WcmItemType itemType,
			Set<String> previousDescendants) {
		
		WcmEvent event = this.createDeleteItemEvent(
				nodeId, 
				repositoryName, 
				workspace, 
				path, 
				itemType, 
				previousDescendants);
		wcmEventService.updateWcmEvent(event);
	}
	
	public WcmEvent createNewItemEvent(
			RestNode restNode, 
			String repositoryName,
			String workspace,
			String nodePath,
			WcmEvent.WcmItemType itemType) {
		
		WcmEvent event = new WcmEvent();
		event.setId(restNode.getId());
		event.setRepository(repositoryName);
		event.setWorkspace(workspace);
		event.setLibrary(WcmUtils.library(nodePath));
		event.setNodePath(nodePath);
		event.setOperation(WcmEvent.Operation.create);
		event.setItemType(itemType);
		
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
	
	public WcmEvent createUpdateItemEvent(
			RestNode restNode, 
			String repositoryName,
			String workspace,
			String nodePath,
			WcmEvent.WcmItemType itemType,
			Set<String> previousDescendants) {
		
		WcmEvent event = new WcmEvent();
		event.setId(restNode.getId());
		event.setRepository(repositoryName);
		event.setWorkspace(workspace);
		event.setLibrary(WcmUtils.library(nodePath));
		event.setNodePath(nodePath);
		event.setOperation(WcmEvent.Operation.update);
		event.setItemType(itemType);
		
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
	
	public WcmEvent createDeleteItemEvent(
			String nodeId, 
			String repositoryName,
			String workspace,
			String nodePath,
			WcmEvent.WcmItemType itemType,
			Set<String> previousDescendants) {
		
		WcmEvent event = new WcmEvent();
		
		event.setRepository(repositoryName);
		event.setWorkspace(workspace);
		event.setLibrary(WcmUtils.library(nodePath));
		event.setNodePath(nodePath);
		event.setOperation(WcmEvent.Operation.delete);
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
