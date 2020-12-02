package com.bpwizard.wcm.repo.rest.service;


import java.io.InputStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bpwizard.wcm.repo.rest.handler.WcmEventHandler;
import com.bpwizard.wcm.repo.rest.jcr.model.WcmEvent;
import com.bpwizard.wcm.repo.rest.modeshape.model.RestNode;
import com.bpwizard.wcm.repo.rest.modeshape.model.RestProperty;

@Service
public class WcmEventService {
	private static final Logger logger = LogManager.getLogger(SyndicationService.class);
	

	@Autowired
	protected WcmEventHandler wcmEventHandler;
	
	public void populateDescendantIds(RestNode restNode, List<String> nodeIds) {
		if (logger.isDebugEnabled()) {
			logger.traceEntry();
		}
		for (RestNode child: restNode.getChildren()) {
			nodeIds.add(child.getId());
			populateDescendantIds(child, nodeIds);
		}
		if (logger.isDebugEnabled()) {
			logger.traceExit();
		}
	}
	
	@Transactional
	public void addCNDEvent(
			String repositoryName,
			String workspace,
			InputStream is,
			WcmEvent.WcmItemType itemType) {
		
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		Long timestamp = System.currentTimeMillis();
		parameters.addValue("ID", String.format("cnd_%s", timestamp));
		parameters.addValue("REPOSITORY", repositoryName);
		parameters.addValue("WORKSPACE", workspace);
	    parameters.addValue("ITEMTYPE", itemType);
	    parameters.addValue("OPERATION", WcmEvent.Operation.create.name());
	    parameters.addValue("timeCreated", new Timestamp(timestamp));
	    parameters.addValue("CONTENT", is);
	    wcmEventHandler.insertWcmEvent(parameters);
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
		wcmEventHandler.addWcmEvent(event);
	}
	
	@Transactional
	public int[] addNewItemEvents(List<WcmEvent> events) {
		return wcmEventHandler.batchInsert(events);
	}
	
	@Transactional
	public int[] addUpdateItemEvents(List<WcmEvent> events) {
		return wcmEventHandler.batchInsert(events);
	}
	
	@Transactional
	public int[] addDeleteItemEvents(List<WcmEvent> events) {
		return wcmEventHandler.batchInsert(events);
	}
	
	@Transactional
	public void addUpdateItemEvent(
			RestNode restNode, 
			String repositoryName,
			String workspace,
			String path,
			WcmEvent.WcmItemType itemType,
			List<String> previousDescendants) {
		
		WcmEvent event = this.createUpdateItemEvent(
				restNode, 
				repositoryName, 
				workspace, path, 
				itemType, 
				previousDescendants);
		wcmEventHandler.updateWcmEvent(event);
	}
	
	@Transactional
	public void addDeleteItemEvent(
			String nodeId, 
			String repositoryName,
			String workspace,
			String path,
			WcmEvent.WcmItemType itemType,
			List<String> previousDescendants) {
		
		WcmEvent event = this.createDeleteItemEvent(
				nodeId, 
				repositoryName, 
				workspace, 
				path, 
				itemType, 
				previousDescendants);
		wcmEventHandler.updateWcmEvent(event);
	}
	
	public WcmEvent createNewItemEvent(
			RestNode restNode, 
			String repositoryName,
			String workspace,
			String path,
			WcmEvent.WcmItemType itemType) {
		
		WcmEvent event = new WcmEvent();
		event.setId(restNode.getId());
		event.setRepository(repositoryName);
		event.setWorkspace(workspace);
		event.setWcmPath(path);
		event.setOperation(WcmEvent.Operation.create);
		event.setItemType(itemType);
		

		for (RestProperty prop: restNode.getJcrProperties()) {
		    if ("jcr:lastModified".equals(prop.getName())) {
		    	event.setTimeCreated(Timestamp.valueOf(prop.getValues().get(0)));
		    }
		}
		
		List<String> descendants = new ArrayList<String>();
		this.populateDescendantIds(restNode, descendants);
		event.setDescendants(descendants);
		return event;
	}
	
	public WcmEvent createUpdateItemEvent(
			RestNode restNode, 
			String repositoryName,
			String workspace,
			String path,
			WcmEvent.WcmItemType itemType,
			List<String> previousDescendants) {
		
		WcmEvent event = new WcmEvent();
		event.setId(restNode.getId());
		event.setRepository(repositoryName);
		event.setWorkspace(workspace);
		event.setWcmPath(path);
		event.setOperation(WcmEvent.Operation.update);
		event.setItemType(itemType);
		
		for (RestProperty prop: restNode.getJcrProperties()) {
		    if ("jcr:lastModified".equals(prop.getName())) {
		    	event.setTimeCreated(Timestamp.valueOf(prop.getValues().get(0)));
		    }
		}
		
		List<String> descendants = new ArrayList<String>();
		this.populateDescendantIds(restNode, descendants);
		event.setDescendants(descendants);
		
		List<String> removedDescendants = new ArrayList<String>();
		
		for (String previousDescendant: previousDescendants) {
			if (!descendants.contains(previousDescendant)) {
				removedDescendants.add(previousDescendant);
			}
		}
		event.setRemovedDescendants(removedDescendants);
		return event;
	}
	
	public WcmEvent createDeleteItemEvent(
			String nodeId, 
			String repositoryName,
			String workspace,
			String path,
			WcmEvent.WcmItemType itemType,
			List<String> previousDescendants) {
		
		WcmEvent event = new WcmEvent();
		
		event.setRepository(repositoryName);
		event.setWorkspace(workspace);
		event.setWcmPath(path);
		event.setOperation(WcmEvent.Operation.delete);
		event.setItemType(itemType);
		event.setId(nodeId);
		event.setTimeCreated(new Timestamp(System.currentTimeMillis()));
		event.setDescendants(null);
		event.setRemovedDescendants(previousDescendants);
		return event;
	}
}
