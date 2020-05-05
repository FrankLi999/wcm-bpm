package com.bpwizard.wcm.repo.rest.jcr.model;

public class AuthoringTemplateProperties {
	// private FormControl authoringTemplate;
	// private FormControl nodeType;
	private FormControl workflow;
	private FormControl categories;
	private FormControl name;
	private FormControl title;
	private FormControl description;

	
//	public FormControl getAuthoringTemplate() {
//		return authoringTemplate;
//	}
//	public void setAuthoringTemplate(FormControl authoringTemplate) {
//		this.authoringTemplate = authoringTemplate;
//	}
//	public FormControl getNodeType() {
//		return nodeType;
//	}
//	public void setNodeType(FormControl nodeType) {
//		this.nodeType = nodeType;
//	}
	public FormControl getWorkflow() {
		return workflow;
	}
	public void setWorkflow(FormControl workflow) {
		this.workflow = workflow;
	}
	public FormControl getCategories() {
		return categories;
	}
	public void setCategories(FormControl categories) {
		this.categories = categories;
	}
	public FormControl getName() {
		return name;
	}
	public void setName(FormControl name) {
		this.name = name;
	}
	public FormControl getTitle() {
		return title;
	}
	public void setTitle(FormControl title) {
		this.title = title;
	}
	public FormControl getDescription() {
		return description;
	}
	public void setDescription(FormControl description) {
		this.description = description;
	}
	@Override
	public String toString() {
		return "AuthoringTemplateProperties [workflow=" + workflow + ", categories=" + categories + ", name=" + name
				+ ", title=" + title + ", description=" + description + "]";
	}
}
