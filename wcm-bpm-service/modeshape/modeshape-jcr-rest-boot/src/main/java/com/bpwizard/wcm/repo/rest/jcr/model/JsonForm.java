package com.bpwizard.wcm.repo.rest.jcr.model;

import com.fasterxml.jackson.databind.JsonNode;

public class JsonForm {
	private String repository;
	private String workspace;
	private String library;
	private String resourceType;
    private JsonNode formSchema;
	public String getResourceType() {
		return resourceType;
	}
	public void setResourceType(String resourceType) {
		this.resourceType = resourceType;
	}
	public JsonNode getFormSchema() {
		return formSchema;
	}
	public void setFormSchema(JsonNode formSchema) {
		this.formSchema = formSchema;
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
	public String getLibrary() {
		return library;
	}
	public void setLibrary(String library) {
		this.library = library;
	}
	@Override
	public String toString() {
		return "JsonForm [repository=" + repository + ", workspace=" + workspace + ", library=" + library
				+ ", resourceType=" + resourceType + ", formSchema=" + formSchema + "]";
	}
}
