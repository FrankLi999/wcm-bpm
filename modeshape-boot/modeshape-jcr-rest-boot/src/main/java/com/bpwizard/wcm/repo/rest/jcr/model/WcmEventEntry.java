package com.bpwizard.wcm.repo.rest.jcr.model;

import java.io.InputStream;
import java.sql.Timestamp;

public class WcmEventEntry {
	public enum Operation {
		create,
		update,
		delete
	}
	
	public enum WcmItemType {
		libraryFolder, // done,
		opertionsFolder, //done,
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
		configurationFolder,
		controlFieldFolder,
		

		
		authoringTemplate, // at
		category, // done
		contentAreaLayout, // done
		contentItem, // done
		siteArea, // done
		form, // done
		formType, // done
		query, //done
		renderTemplate, //done
		siteConfig, //done
		theme, // done
		validationRule, // done
		workflow, // done
		supportedOpertions, //done
		supportedOpertion, //done
		folder, 
		file,
		image,
		cnd,
		jcrType,
		controlField, //dome
		camunda_resources //done
		
	}
	
	private String id;
	private String repository;
	private String workspace;
	private String library;
	private String nodePath;
	private Operation operation;
	private WcmItemType itemType;
	private Timestamp timeCreated;
	private InputStream content;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getLibrary() {
		return library;
	}
	public void setLibrary(String library) {
		this.library = library;
	}
	public String getNodePath() {
		return nodePath;
	}
	public void setNodePath(String nodePath) {
		this.nodePath = nodePath;
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

	public InputStream getContent() {
		return content;
	}
	public void setContent(InputStream content) {
		this.content = content;
	}
	
	@Override
	public String toString() {
		return "WcmEvent [id=" + id + ", nodePath=" + nodePath + ", repository=" + repository
				+ ", workspace=" + workspace + ", library=" + library + ", operation=" + operation + ", itemType=" + itemType + ", timeCreated="
				+ timeCreated + "]";
	}
}
