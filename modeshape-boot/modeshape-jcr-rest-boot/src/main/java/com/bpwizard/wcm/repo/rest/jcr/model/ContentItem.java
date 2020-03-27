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
	
	private static final long serialVersionUID = -6271150516003474875L;
	private String id;
	private String[] categories;
	private String repository;
	private String workspace;
	private String wcmPath;
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
		return this.properties.get("name");
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

	public String getWcmPath() {
		return wcmPath;
	}

	public void setWcmPath(String wcmPath) {
		this.wcmPath = wcmPath;
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
		ObjectNode children = JsonUtils.createObjectNode();
		
		jsonNode.set("children", children);
		jsonNode.put(JcrConstants.JCR_PRIMARY_TYPE, "bpw:content");
		jsonNode.put("bpw:authoringTemplate", this.getAuthoringTemplate());

		String name = this.properties.get("name");
//		if (StringUtils.hasText(name)) {
//			jsonNode.put("bpw:name", name);
//			// this.properties.remove("name");			
//		}
		String title = this.properties.get("title");
		title = StringUtils.hasText(title) ? title : name;
		if (StringUtils.hasText(title)) {
			this.setTitle(title);
			// this.properties.remove("title");
		}
		if (StringUtils.hasText(this.properties.get("description"))) {
			this.setDescription(this.properties.get("description"));
			// this.properties.remove("description");
		}
		if (StringUtils.hasText(this.properties.get("workflow"))) {
			this.setWorkflow(this.properties.get("workflow")); 
			// this.properties.remove("workflow");
		}
		if (StringUtils.hasText(this.properties.get("publishDate"))) {
			this.setPublishDate(this.properties.get("publishDate"));
			// this.properties.remove("publishDate");
		}
		if (StringUtils.hasText(this.properties.get("expireDate"))) {
			this.setExpireDate(this.properties.get("expireDate"));
			// this.properties.remove("expireDate");
		}
		if (StringUtils.hasText(this.properties.get("categories"))) {
			String categories[] = (this.properties.get("categories")).split(",");
			if (categories != null && categories.length > 0) {
				ArrayNode valueArray = JsonUtils.creatArrayNode();
				for (String value : categories) {
					valueArray.add(value);
				}
				jsonNode.set("bpw:categories", valueArray);
			}
			// this.properties.remove("categories");
		}
		
		
		ObjectNode comementFolderNode = JsonUtils.createObjectNode();
		ObjectNode comementFolderChildren = JsonUtils.createObjectNode();
		comementFolderNode.set("children", comementFolderChildren);
		comementFolderNode.put(JcrConstants.JCR_PRIMARY_TYPE,  "bpw:commentFolder");
		children.set("comments", comementFolderNode);
		
		ObjectNode elementsNode = JsonUtils.createObjectNode();
		ObjectNode elementsNodeChildren = JsonUtils.createObjectNode();
		elementsNode.set("children", elementsNodeChildren);
		elementsNode.put(JcrConstants.JCR_PRIMARY_TYPE,  "bpw:contentElementFolder");
		children.set("elements", elementsNode);
		
		Map<String, String> contentElements = this.getElements();
		for (String key: contentElements.keySet()) {
			ObjectNode elementNode = JsonUtils.createObjectNode();
			elementsNodeChildren.set(key, elementNode);
			elementNode.put(JcrConstants.JCR_PRIMARY_TYPE, "bpw:contentElement");
			elementNode.put("bpw:multiple", false);
			elementNode.put("bpw:value", contentElements.get(key));
		}
		
		ObjectNode propertiesNode = JsonUtils.createObjectNode();
		ObjectNode propertiesNodeChildren = JsonUtils.createObjectNode();
		propertiesNode.set("children", propertiesNodeChildren);
		propertiesNode.put(JcrConstants.JCR_PRIMARY_TYPE,  "bpw:propertyElementFolder");
		children.set("properties", propertiesNode);
		
		Map<String, String> propertyElements = this.getElements();
		for (String key: propertyElements.keySet()) {
			ObjectNode propertyNode = JsonUtils.createObjectNode();
			propertiesNodeChildren.set(key, propertyNode);
			propertyNode.put(JcrConstants.JCR_PRIMARY_TYPE, "bpw:contentElement");
			propertyNode.put("bpw:multiple", false);
			propertyNode.put("bpw:value", contentElements.get(key));
		}
		
		return jsonNode;
	}

	@Override
	public String toString() {
		return "ContentItem [id=" + id + ", name=" + this.getName() + ", categories=" + Arrays.toString(categories)
				+ ", repository=" + repository + ", workspace=" + workspace + ", wcmPath=" + wcmPath
				+ ", authoringTemplate=" + authoringTemplate + ", lifeCycleStage=" + lifeCycleStage + ", elements="
				+ elements + ", properties=" + properties + ", toString()=" + super.toString() + "]";
	}
}
