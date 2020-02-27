package com.bpwizard.wcm.repo.rest.jcr.model;

import org.modeshape.jcr.api.JcrConstants;
import org.springframework.util.StringUtils;

import com.bpwizard.wcm.repo.rest.JsonUtils;
import com.bpwizard.wcm.repo.rest.modeshape.model.HasName;
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
		jsonNode.put(JcrConstants.JCR_PRIMARY_TYPE, "bpw:category");
		jsonNode.put("bpw:name", this.getName());
		if (StringUtils.hasText(this.getParent())) {
			jsonNode.put("bpw:parent", this.getParent());
		}
		
        return jsonNode;
	}
	
	@Override
	public String toString() {
		return "Category [repository=" + repository + ", workspace=" + workspace + ", library=" + library + ", name="
				+ name + ", parent=" + parent + "]";
	}
}
