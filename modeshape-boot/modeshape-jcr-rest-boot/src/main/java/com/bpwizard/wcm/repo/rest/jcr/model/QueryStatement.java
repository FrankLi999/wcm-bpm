package com.bpwizard.wcm.repo.rest.jcr.model;

import java.io.Serializable;

import org.modeshape.jcr.api.JcrConstants;
import org.springframework.util.StringUtils;

import com.bpwizard.wcm.repo.rest.JsonUtils;
import com.bpwizard.wcm.repo.rest.modeshape.model.HasName;
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

	public JsonNode toJson() {
		ObjectNode jsonNode = JsonUtils.createObjectNode();
		jsonNode.put(JcrConstants.JCR_PRIMARY_TYPE, "bpw:queryStatement");
		
		jsonNode.put("bpw:name", this.getName());
		if (StringUtils.hasText(this.getTitle())) {
			jsonNode.put("bpw:title", this.getTitle());
		} else {
			jsonNode.put("bpw:title", this.getName());
		}
		
		jsonNode.put("bpw:query", this.getQuery());
				
		return jsonNode;
	}

	@Override
	public String toString() {
		return "QueryStatement [repository=" + repository + ", workspace=" + workspace + ", library=" + library
				+ ", name=" + name + ", title=" + title + ", query=" + query + "]";
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
