package com.bpwizard.wcm.repo.rest;

import java.io.InputStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bpwizard.wcm.repo.rest.handler.WcmEventHandler;
import com.bpwizard.wcm.repo.rest.jcr.model.WcmEvent;
import com.bpwizard.wcm.repo.rest.modeshape.model.RestNode;
import com.bpwizard.wcm.repo.rest.modeshape.model.RestProperty;
@Component
public class SyndicationUtils {
	private static final Logger logger = LogManager.getLogger(SyndicationUtils.class);
	

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
	
	public void addCNDEvent(
			String repositoryName,
			String workspace,
			InputStream is,
			WcmEvent.WcmItemType itemType) {
		
	}
	
	public WcmEvent createNewItemEvent(
			RestNode restNode, 
			String repositoryName,
			String workspace,
			String path,
			WcmEvent.WcmItemType itemType) {
		
		WcmEvent event = new WcmEvent();
		
		event.setRepository(repositoryName);
		event.setWorkspace(workspace);
		event.setWcmPath(path);
		event.setOperation(WcmEvent.Operation.create);
		event.setItemType(itemType);
		
		event.setId(restNode.getId());
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
	
	public void addNewItemEvents(List<WcmEvent> events) {
	}
	
	public void addUpdateItemEvents(List<WcmEvent> events) {
	}
	
	public void addDeleteItemEvents(List<WcmEvent> events) {
	}

	public WcmEvent createUpdateItemEvent(
			RestNode restNode, 
			String repositoryName,
			String workspace,
			String path,
			WcmEvent.WcmItemType itemType,
			List<String> previousDescendants) {
		
		WcmEvent event = new WcmEvent();
		
		event.setRepository(repositoryName);
		event.setWorkspace(workspace);
		event.setWcmPath(path);
		event.setOperation(WcmEvent.Operation.update);
		event.setItemType(itemType);
		
		event.setId(restNode.getId());
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
}
