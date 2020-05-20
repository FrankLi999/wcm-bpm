package com.bpwizard.wcm.repo.rest.jcr.model;

import java.io.Serializable;
import java.util.Arrays;

import org.modeshape.jcr.api.JcrConstants;
import org.springframework.util.StringUtils;

import com.bpwizard.wcm.repo.rest.JsonUtils;
import com.bpwizard.wcm.repo.rest.WcmUtils;
import com.bpwizard.wcm.repo.rest.modeshape.model.HasName;
import com.bpwizard.wcm.repo.rest.utils.WcmConstants;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class QueryStatement implements HasName, Serializable, Comparable<QueryStatement> {

	private static final long serialVersionUID = 1L;

	private String repository;
	private String workspace;
	private String library;
	
	private String name;
	private String title;
	private String query;
	private String[] columns;
	
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
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public String[] getColumns() {
		return columns;
	}

	public void setColumns(String[] columns) {
		this.columns = columns;
	}

	public JsonNode toJson() {
		ObjectNode jsonNode = JsonUtils.createObjectNode();
		jsonNode.put(JcrConstants.JCR_PRIMARY_TYPE, "bpw:system_queryStatementType");
		
		ObjectNode children = JsonUtils.createObjectNode();
		jsonNode.set(WcmConstants.JCR_JSON_NODE_CHILDREN, children);
		
		ObjectNode propertiesNode = JsonUtils.createObjectNode();
		children.set(WcmConstants.WCM_ITEM_PROPERTIES, propertiesNode);
		propertiesNode.put(JcrConstants.JCR_PRIMARY_TYPE, WcmConstants.JCR_TYPE_PROPERTY_FOLDER);
		propertiesNode.put("bpw:name", this.getName());
		if (StringUtils.hasText(this.getTitle())) {
			propertiesNode.put("bpw:title", this.getTitle());
		} else {
			propertiesNode.put("bpw:title", this.getName());
		}
		
		ObjectNode elementsNode = JsonUtils.createObjectNode();
		children.set(WcmConstants.WCM_ITEM_ELEMENTS, elementsNode);
		elementsNode.put(JcrConstants.JCR_PRIMARY_TYPE, "bpw:system_queryStatementType_ElementFolder");
		elementsNode.put("query", this.getQuery());
		elementsNode.set("columns", WcmUtils.toArrayNode(this.getColumns()));
		return jsonNode;
	}

	@Override
	public String toString() {
		return "QueryStatement [repository=" + repository + ", workspace=" + workspace + ", library=" + library
				+ ", name=" + name + ", title=" + title + ", query=" + query + ", columns=" + Arrays.toString(columns)
				+ "]";
	}

	@Override
	public int compareTo(QueryStatement o) {
		int result = 1;
		if (o != null) {
		    result = this.name.compareTo(o.getName());
		    if (result == 0) {
		    	result = this.library.compareTo(o.getLibrary());
		    }
		}
		return result;
	}
}
