package com.bpwizard.wcm.repo.rest.jcr.model;

import org.modeshape.jcr.api.JcrConstants;

import com.bpwizard.wcm.repo.rest.JsonUtils;
import com.bpwizard.wcm.repo.rest.modeshape.model.HasName;
import com.bpwizard.wcm.repo.rest.utils.WcmConstants;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class Category implements HasName {
	private String repository;
	private String workspace;
	private String library;
	
	private String name;
	private String parent;

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

	public String getLibrary() {
		return library;
	}

	public void setLibrary(String library) {
		this.library = library;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getParent() {
		return parent;
	}
	
	public void setParent(String parent) {
		this.parent = parent;
	}

	public JsonNode toJson() {
		
		ObjectNode jsonNode = JsonUtils.createObjectNode();
		jsonNode.put(JcrConstants.JCR_PRIMARY_TYPE, "bpw:system_categoryType");

		ObjectNode children = JsonUtils.createObjectNode();
		jsonNode.set(WcmConstants.JCR_JSON_NODE_CHILDREN, children);
		
		ObjectNode propertiesNode = JsonUtils.createObjectNode();
		children.set(WcmConstants.WCM_NODE_PROPERTIES, propertiesNode);
		propertiesNode.put(JcrConstants.JCR_PRIMARY_TYPE, "bpw:ContentItemproperties");
		propertiesNode.put("bpw:name", this.getName());
		
		ObjectNode elementsNode = JsonUtils.createObjectNode();
		children.set(WcmConstants.WCM_NODE_ELEMENTS, elementsNode);
		elementsNode.put(JcrConstants.JCR_PRIMARY_TYPE, "bpw:system_categoryType_ElementFolder");
		return jsonNode;
	}
	
	@Override
	public String toString() {
		return "Category [repository=" + repository + ", workspace=" + workspace + ", library=" + library + ", name="
				+ name + ", parent=" + parent + "]";
	}
}
