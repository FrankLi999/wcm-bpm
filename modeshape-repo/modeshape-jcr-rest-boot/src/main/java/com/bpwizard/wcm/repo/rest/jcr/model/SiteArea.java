package com.bpwizard.wcm.repo.rest.jcr.model;

import com.bpwizard.wcm.repo.rest.modeshape.model.HasName;

public class SiteArea implements HasName {

	private String repository;
	private String workspace;
	private String nodePath;
	private String name;
	private String title;
	private String description;
	private String url;
	private int sorderOrder;
	private String friendlyURL;
	private boolean showOnMenu;
	private String defaultContent;
	private String allowedFileExtension;
	private String allowedArtifactTypes;
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
	public String getNodePath() {
		return nodePath;
	}
	public void setNodePath(String nodePath) {
		this.nodePath = nodePath;
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
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public int getSorderOrder() {
		return sorderOrder;
	}
	public void setSorderOrder(int sorderOrder) {
		this.sorderOrder = sorderOrder;
	}
	public String getFriendlyURL() {
		return friendlyURL;
	}
	public void setFriendlyURL(String friendlyURL) {
		this.friendlyURL = friendlyURL;
	}
	public boolean isShowOnMenu() {
		return showOnMenu;
	}
	public void setShowOnMenu(boolean showOnMenu) {
		this.showOnMenu = showOnMenu;
	}
	public String getDefaultContent() {
		return defaultContent;
	}
	public void setDefaultContent(String defaultContent) {
		this.defaultContent = defaultContent;
	}
	public String getAllowedFileExtension() {
		return allowedFileExtension;
	}
	public void setAllowedFileExtension(String allowedFileExtension) {
		this.allowedFileExtension = allowedFileExtension;
	}
	public String getAllowedArtifactTypes() {
		return allowedArtifactTypes;
	}
	public void setAllowedArtifactTypes(String allowedArtifactTypes) {
		this.allowedArtifactTypes = allowedArtifactTypes;
	}
	@Override
	public String toString() {
		return "SiteArea [repository=" + repository + ", workspace=" + workspace + ", nodePath=" + nodePath + ", name="
				+ name + ", title=" + title + ", description=" + description + ", url=" + url + ", sorderOrder="
				+ sorderOrder + ", friendlyURL=" + friendlyURL + ", showOnMenu=" + showOnMenu + ", defaultContent="
				+ defaultContent + ", allowedFileExtension=" + allowedFileExtension + ", allowedArtifactTypes="
				+ allowedArtifactTypes + "]";
	}
}
