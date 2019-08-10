package com.bpwizard.wcm.repo.rest.jcr.model;

import com.fasterxml.jackson.databind.JsonNode;

public class JsonForm {
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
}
