package com.bpwizard.wcm.repo.rest.jcr.model;

import com.bpwizard.wcm.repo.rest.modeshape.model.HasName;

public class AuthoringTemplate implements HasName {
	private String name;
	private String title;
	private String description;
	private String baseResourceType;
	private String[] workflow;
	private String[] categories;
	private String publishDate;
	private BaseFormGroup[] formGroups;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public BaseFormGroup[] getFormGroups() {
		return formGroups;
	}
	public void setFormGroups(BaseFormGroup[] formGroups) {
		this.formGroups = formGroups;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String[] getWorkflow() {
		return workflow;
	}
	public void setWorkflow(String[] workflow) {
		this.workflow = workflow;
	}
	public String[] getCategories() {
		return categories;
	}
	public void setCategories(String[] categories) {
		this.categories = categories;
	}
	public String getBaseResourceType() {
		return baseResourceType;
	}
	public void setBaseResourceType(String baseResourceType) {
		this.baseResourceType = baseResourceType;
	}
	public String getPublishDate() {
		return publishDate;
	}
	public void setPublishDate(String publishDate) {
		this.publishDate = publishDate;
	}
}
