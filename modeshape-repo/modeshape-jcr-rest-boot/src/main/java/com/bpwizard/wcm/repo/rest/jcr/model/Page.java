package com.bpwizard.wcm.repo.rest.jcr.model;

import com.bpwizard.wcm.repo.rest.modeshape.model.HasName;

public class Page implements HasName {
	private String repository;
	private String workspace;
	private String nodePath;
	private String name;
	private String title;
	private String description;
	private String pageLayout;

	private int cacheTTL;
	private String url;
	private String friendlyURL;

	private int sortOrder;


	private boolean securePage;
	private boolean showOnMenu;
	
	private keyValues metadata;
	private SearchData searchData;
	
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
	public String getPageLayout() {
		return pageLayout;
	}
	public void setPageLayout(String pageLayout) {
		this.pageLayout = pageLayout;
	}
	public int getCacheTTL() {
		return cacheTTL;
	}
	public void setCacheTTL(int cacheTTL) {
		this.cacheTTL = cacheTTL;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getFriendlyURL() {
		return friendlyURL;
	}
	public void setFriendlyURL(String friendlyURL) {
		this.friendlyURL = friendlyURL;
	}
	public int getSortOrder() {
		return sortOrder;
	}
	public void setSortOrder(int sortOrder) {
		this.sortOrder = sortOrder;
	}
	public boolean isSecurePage() {
		return securePage;
	}
	public void setSecurePage(boolean securePage) {
		this.securePage = securePage;
	}
	public boolean isShowOnMenu() {
		return showOnMenu;
	}
	public void setShowOnMenu(boolean showOnMenu) {
		this.showOnMenu = showOnMenu;
	}
	public keyValues getMetadata() {
		return metadata;
	}
	public void setMetadata(keyValues metadata) {
		this.metadata = metadata;
	}
	public SearchData getSearchData() {
		return searchData;
	}
	public void setSearchData(SearchData searchData) {
		this.searchData = searchData;
	}
	@Override
	public String toString() {
		return "Page [repository=" + repository + ", workspace=" + workspace + ", nodePath=" + nodePath + ", name="
				+ name + ", title=" + title + ", description=" + description + ", pageLayout=" + pageLayout
				+ ", cacheTTL=" + cacheTTL + ", url=" + url + ", friendlyURL=" + friendlyURL + ", sortOrder="
				+ sortOrder + ", securePage=" + securePage + ", showOnMenu=" + showOnMenu + ", metadata=" + metadata
				+ ", searchData=" + searchData + "]";
	}
	
	
}
