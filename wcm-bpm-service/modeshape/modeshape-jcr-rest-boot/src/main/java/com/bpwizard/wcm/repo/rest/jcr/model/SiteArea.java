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
	
	private boolean securePage;
	private String contentAreaLayout;
	private int cacheTTL;

	private String navigationId;
	private String navigationType;
	private String function;
	private String translate;
	private String icon;
	private String classes;
	private boolean exactMatch;
	private boolean externalUrl;
	private boolean openInNewTab;

	private keyValues metadata;
	private SearchData searchData;
	private NavigationBadge badge;
	private SiteAreaLayout siteAreaLayout;
	
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
	public boolean isSecurePage() {
		return securePage;
	}
	public void setSecurePage(boolean securePage) {
		this.securePage = securePage;
	}
	public String getContentAreaLayout() {
		return contentAreaLayout;
	}
	public void setContentAreaLayout(String contentAreaLayout) {
		this.contentAreaLayout = contentAreaLayout;
	}
	public int getCacheTTL() {
		return cacheTTL;
	}
	public void setCacheTTL(int cacheTTL) {
		this.cacheTTL = cacheTTL;
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
	public String getNavigationId() {
		return navigationId;
	}
	public void setNavigationId(String navigationId) {
		this.navigationId = navigationId;
	}
	public String getNavigationType() {
		return navigationType;
	}
	public void setNavigationType(String navigationType) {
		this.navigationType = navigationType;
	}
	public String getFunction() {
		return function;
	}
	public void setFunction(String function) {
		this.function = function;
	}
	public String getTranslate() {
		return translate;
	}
	public void setTranslate(String translate) {
		this.translate = translate;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public String getClasses() {
		return classes;
	}
	public void setClasses(String classes) {
		this.classes = classes;
	}
	public boolean isExactMatch() {
		return exactMatch;
	}
	public void setExactMatch(boolean exactMatch) {
		this.exactMatch = exactMatch;
	}
	public boolean isExternalUrl() {
		return externalUrl;
	}
	public void setExternalUrl(boolean externalUrl) {
		this.externalUrl = externalUrl;
	}
	public boolean isOpenInNewTab() {
		return openInNewTab;
	}
	public void setOpenInNewTab(boolean openInNewTab) {
		this.openInNewTab = openInNewTab;
	}
	public NavigationBadge getBadge() {
		return badge;
	}
	public void setBadge(NavigationBadge badge) {
		this.badge = badge;
	}
	public SiteAreaLayout getSiteAreaLayout() {
		return siteAreaLayout;
	}
	public void setSiteAreaLayout(SiteAreaLayout siteAreaLayout) {
		this.siteAreaLayout = siteAreaLayout;
	}
	@Override
	public String toString() {
		return "SiteArea [repository=" + repository + ", workspace=" + workspace + ", nodePath=" + nodePath + ", name="
				+ name + ", title=" + title + ", description=" + description + ", url=" + url + ", sorderOrder="
				+ sorderOrder + ", friendlyURL=" + friendlyURL + ", showOnMenu=" + showOnMenu + ", defaultContent="
				+ defaultContent + ", allowedFileExtension=" + allowedFileExtension + ", allowedArtifactTypes="
				+ allowedArtifactTypes + ", securePage=" + securePage + ", contentAreaLayout=" + contentAreaLayout
				+ ", cacheTTL=" + cacheTTL + ", metadata=" + metadata + ", searchData=" + searchData + ", navigationId="
				+ navigationId + ", navigationType=" + navigationType + ", function=" + function + ", translate="
				+ translate + ", icon=" + icon + ", classes=" + classes + ", exactMatch=" + exactMatch
				+ ", externalUrl=" + externalUrl + ", openInNewTab=" + openInNewTab + ", badge=" + badge
				+ ", siteAreaLayout=" + siteAreaLayout + "]";
	}
}
