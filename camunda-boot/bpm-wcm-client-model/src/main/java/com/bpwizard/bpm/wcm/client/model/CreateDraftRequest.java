package com.bpwizard.bpm.wcm.client.model;

public class CreateDraftRequest {

	private String author;
	private String repository; 
	private String workspace; 
	private String itemId;
	private String itemName;
	private String itemPath;
	
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
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
	public String getItemId() {
		return itemId;
	}
	public void setItemId(String itemId) {
		this.itemId = itemId;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public String getItemPath() {
		return itemPath;
	}
	public void setItemPath(String itemPath) {
		this.itemPath = itemPath;
	}
	@Override
	public String toString() {
		return "CreateDraftRequest [author=" + author + ", repository=" + repository + ", workspace=" + workspace
				+ ", itemId=" + itemId + ", itemName=" + itemName + ", itemPath=" + itemPath + "]";
	}
}
