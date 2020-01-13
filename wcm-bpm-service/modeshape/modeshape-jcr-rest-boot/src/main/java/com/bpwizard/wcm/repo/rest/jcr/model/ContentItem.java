package com.bpwizard.wcm.repo.rest.jcr.model;

import java.util.Arrays;
import java.util.Map;

import org.modeshape.jcr.api.JcrConstants;
import org.springframework.util.StringUtils;

import com.bpwizard.wcm.repo.rest.JsonUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class ContentItem extends WorkflowNode {
	
	private String id;
	private String name;
	private String[] categories;
	private String repository;
	private String workspace;
	private String nodePath;
	private String authoringTemplate;
	private String lifeCycleStage;

	private Map<String, String> elements;
	private Map<String, String> properties;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String[] getCategories() {
		return categories;
	}

	public void setCategories(String[] categories) {
		this.categories = categories;
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

	//TODO: super.toJson()
	public JsonNode toJson() {
		ObjectNode jsonNode = JsonUtils.createObjectNode();
		ObjectNode properties = JsonUtils.createObjectNode();
		ObjectNode children = JsonUtils.createObjectNode();
		
		jsonNode.set("children", children);
		jsonNode.set("properties", properties);
		properties.put(JcrConstants.JCR_PRIMARY_TYPE, "bpw:content");
		properties.put("bpw:authoringTemplate", this.getAuthoringTemplate());
		
		Map<String, String> contentProperties = this.getProperties();
		
		this.name = contentProperties.get("name"); 
		if (StringUtils.hasText(name)) {
			properties.put("bpw:name", name);
			contentProperties.remove("name");			
		}
		String title = contentProperties.get("title");
		title = StringUtils.hasText(title) ? title : name;
		if (StringUtils.hasText(title)) {
			this.setTitle(title);
			contentProperties.remove("title");
		}
		if (StringUtils.hasText(contentProperties.get("description"))) {
			this.setDescription(contentProperties.get("description"));
			contentProperties.remove("description");
		}
		if (StringUtils.hasText(contentProperties.get("workflow"))) {
			this.setWorkflow(contentProperties.get("workflow")); 
			contentProperties.remove("workflow");
		}
		if (StringUtils.hasText(contentProperties.get("publishDate"))) {
			this.setPublishDate(contentProperties.get("publishDate"));
			contentProperties.remove("publishDate");
		}
		if (StringUtils.hasText(contentProperties.get("expireDate"))) {
			this.setExpireDate(contentProperties.get("expireDate"));
			contentProperties.remove("expireDate");
		}
		if (StringUtils.hasText(contentProperties.get("categories"))) {
			String categories[] = contentProperties.get("categories").split(",");
			if (categories != null && categories.length > 0) {
				ArrayNode valueArray = JsonUtils.creatArrayNode();
				for (String value : categories) {
					valueArray.add(value);
				}
				properties.set("bpw:categories", valueArray);
			}
			contentProperties.remove("categories");
		}
		
		
		ObjectNode comementFolderNode = JsonUtils.createObjectNode();
		ObjectNode comementFolderNodeProperties = JsonUtils.createObjectNode();
		comementFolderNode.set("children", comementFolderNode);
		comementFolderNode.set("properties", comementFolderNodeProperties);
		comementFolderNodeProperties.put(JcrConstants.JCR_PRIMARY_TYPE,  "bpw:commentFolder");
		children.set("comments", comementFolderNode);
		
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
		return "ContentItem [id=" + id + ", name=" + name + ", categories=" + Arrays.toString(categories)
				+ ", repository=" + repository + ", workspace=" + workspace + ", nodePath=" + nodePath
				+ ", authoringTemplate=" + authoringTemplate + ", lifeCycleStage=" + lifeCycleStage + ", elements="
				+ elements + ", properties=" + properties + ", toString()=" + super.toString() + "]";
	}
}
