package com.bpwizard.wcm.repo.rest.jcr.model;

import java.util.Map;

import org.modeshape.jcr.api.JcrConstants;
import org.springframework.util.StringUtils;

import com.bpwizard.wcm.repo.rest.JsonUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class ContentItem {
	private String id;
	private String repository;
	private String workspace;
	private String nodePath;
	private String authoringTemplate;
	private String lifeCycleStage;
	private String lockOwner;
	private Map<String, String> elements;
	private Map<String, String> properties;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLockOwner() {
		return lockOwner;
	}
	public void setLockOwner(String lockOwner) {
		this.lockOwner = lockOwner;
	}	

	public String getRepository() {
		return repository;
	}

	public void setRepository(String repository) {
		this.repository = repository;
	}

	public String getWorkspace() {
		return workspace;
	}

	public void setWorkspace(String workspace) {
		this.workspace = workspace;
	}

	public String getNodePath() {
		return nodePath;
	}

	public void setNodePath(String nodePath) {
		this.nodePath = nodePath;
	}

	public String getAuthoringTemplate() {
		return authoringTemplate;
	}

	public void setAuthoringTemplate(String authoringTemplate) {
		this.authoringTemplate = authoringTemplate;
	}


	public Map<String, String> getElements() {
		return elements;
	}

	public void setElements(Map<String, String> elements) {
		this.elements = elements;
	}

	public Map<String, String> getProperties() {
		return properties;
	}

	public void setProperties(Map<String, String> properties) {
		this.properties = properties;
	}

	public String getLifeCycleStage() {
		return lifeCycleStage;
	}

	public void setLifeCycleStage(String lifeCycleStage) {
		this.lifeCycleStage = lifeCycleStage;
	}

	public JsonNode toJson() {
		ObjectNode jsonNode = JsonUtils.createObjectNode();
		ObjectNode properties = JsonUtils.createObjectNode();
		ObjectNode children = JsonUtils.createObjectNode();
		
		jsonNode.set("children", children);
		jsonNode.set("properties", properties);
		properties.put(JcrConstants.JCR_PRIMARY_TYPE, "bpw:content");
		properties.put("bpw:authoringTemplate", this.getAuthoringTemplate());
		
		Map<String, String> contentProperties = this.getProperties();
		String name = contentProperties.get("name"); 
		String title = contentProperties.get("title");
		String description = contentProperties.get("description");
		String workflow = contentProperties.get("workflow"); 
		
		if (StringUtils.hasText(workflow)) {
			properties.put("bpw:workflow", workflow);
		}
		
		properties.put("bpw:title", StringUtils.hasText(title) ? title : name);
		if (StringUtils.hasText(description)) {
			properties.put("bpw:description", description);
		}
		
		ObjectNode elementsNode = JsonUtils.createObjectNode();
		ObjectNode elementsNodeChildren = JsonUtils.createObjectNode();
		ObjectNode elementsNodeProperties = JsonUtils.createObjectNode();
		elementsNode.set("children", elementsNodeChildren);
		elementsNode.set("properties", elementsNodeProperties);
		elementsNodeProperties.put(JcrConstants.JCR_PRIMARY_TYPE,  "bpw:contentElementFolder");
		children.set("contentElements", elementsNode);
		
		Map<String, String> contentElements = this.getElements();
		for (String key: contentElements.keySet()) {
			ObjectNode elementNode = JsonUtils.createObjectNode();
			ObjectNode elementNodeProperties = JsonUtils.createObjectNode();
			elementsNodeChildren.set(key, elementNode);
			elementNode.set("properties", elementNodeProperties);
			elementNodeProperties.put(JcrConstants.JCR_PRIMARY_TYPE, "bpw:contentElement");
			elementNodeProperties.put("bpw:multiple", false);
			elementNodeProperties.put("bpw:value", contentElements.get(key));
		}
		
		ObjectNode propertiesNode = JsonUtils.createObjectNode();
		ObjectNode propertiesNodeChildren = JsonUtils.createObjectNode();
		ObjectNode propertiesNodeProperties = JsonUtils.createObjectNode();
		propertiesNode.set("children", propertiesNodeChildren);
		propertiesNode.set("properties", propertiesNodeProperties);
		propertiesNodeProperties.put(JcrConstants.JCR_PRIMARY_TYPE,  "bpw:propertyElementFolder");
		children.set("contentProperties", propertiesNode);
		
		Map<String, String> propertyElements = this.getElements();
		for (String key: propertyElements.keySet()) {
		
			
			ObjectNode elementNode = JsonUtils.createObjectNode();
			ObjectNode elementNodeProperties = JsonUtils.createObjectNode();
			propertiesNodeChildren.set(key, elementNode);
			elementNode.set("properties", elementNodeProperties);
			elementNodeProperties.put(JcrConstants.JCR_PRIMARY_TYPE, "bpw:contentElement");
			elementNodeProperties.put("bpw:multiple", false);
			elementNodeProperties.put("bpw:value", contentElements.get(key));
		}
		
		return jsonNode;
	}
	
	@Override
	public String toString() {
		return "ContentItem [id=" + id + ", repository=" + repository + ", workspace=" + workspace + ", nodePath="
				+ nodePath + ", authoringTemplate=" + authoringTemplate + ", lifeCycleStage=" + lifeCycleStage
				+ ", locked=" + ", lockOwner=" + lockOwner + ", elements=" + elements + ", properties="
				+ properties + "]";
	}
}
