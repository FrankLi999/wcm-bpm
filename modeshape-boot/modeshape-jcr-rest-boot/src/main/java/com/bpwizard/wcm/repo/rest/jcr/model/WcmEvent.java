package com.bpwizard.wcm.repo.rest.jcr.model;
import java.sql.Timestamp;
import java.util.List;

public class WcmEvent {
	public enum Operation {
		create,
		update,
		delete
	}
	
	public enum WcmItemType {
		library, //done
		authoringTemplateFolder,
		categoryFolder,
		contentAreaLayoutFolder,
		formFolder,
		renderTemplateFolder,
		queryFolder,
		siteConfigFolder,
		themesFolder,
		validationRuleFolder,
		workflowFolder,
		assetFolder,
		

		
		authoringTemplate, // at
		category, // done
		contentAreaLayout, // done
		contentItem, // done
		siteArea, // done
		form, // done
		// siteAreaLayout,
		query, //done
		renderTemplate, //done
		siteConfig, //done
		theme, // done
		validationRule,
		workflow,
		
		file,
		image,
		
		jcrType,
		
	}
	
	private String id;
	private String wcmPath;
	private String repository;
	private String workspace;
	private Operation operation;
	private WcmItemType itemType;
	private Timestamp timeCreated;
	private List<String> descendants;
	private List<String> removedDescendants;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getWcmPath() {
		return wcmPath;
	}
	public void setWcmPath(String wcmPath) {
		this.wcmPath = wcmPath;
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
	public Operation getOperation() {
		return operation;
	}
	public void setOperation(Operation operation) {
		this.operation = operation;
	}
	public WcmItemType getItemType() {
		return itemType;
	}
	public void setItemType(WcmItemType itemType) {
		this.itemType = itemType;
	}
	public Timestamp getTimeCreated() {
		return timeCreated;
	}
	public void setTimeCreated(Timestamp timeCreated) {
		this.timeCreated = timeCreated;
	}
	public List<String> getDescendants() {
		return descendants;
	}
	public void setDescendants(List<String> descendants) {
		this.descendants = descendants;
	}
	public List<String> getRemovedDescendants() {
		return removedDescendants;
	}
	public void setRemovedDescendants(List<String> removedDescendants) {
		this.removedDescendants = removedDescendants;
	}
	
	@Override
	public String toString() {
		return "WcmEvent [id=" + id + ", wcmPath=" + wcmPath + ", repository=" + repository + ", workspace=" + workspace
				+ ", operation=" + operation + ", itemType=" + itemType + ", timeCreated="
				+ timeCreated + ", descendants=" + descendants + ", removedDescendants=" + removedDescendants + "]";
	}
}
