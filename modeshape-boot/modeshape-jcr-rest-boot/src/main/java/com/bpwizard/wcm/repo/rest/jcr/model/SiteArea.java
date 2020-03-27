package com.bpwizard.wcm.repo.rest.jcr.model;

import java.util.Map;

import org.modeshape.jcr.api.JcrConstants;
import org.springframework.util.StringUtils;

import com.bpwizard.wcm.repo.rest.JsonUtils;
import com.bpwizard.wcm.repo.rest.modeshape.model.HasName;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class SiteArea implements HasName {

	private String repository;
	private String workspace;
	private String wcmPath;
	// private String name;
	private SiteAreaLayout siteAreaLayout;
	private String lockOwner;
	
	private Map<String, String> elements;
	private Map<String, String> properties;
	
	private WcmProperties metadata;

	private SearchData searchData;
	private NavigationBadge badge;

	
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
	public String getWcmPath() {
		return wcmPath;
	}
	public void setWcmPath(String wcmPath) {
		this.wcmPath = wcmPath;
	}
	public String getName() {
		return this.properties.get("name");
	}
//	public void setName(String name) {
//		this.name = name;
//	}
	
	public SiteAreaLayout getSiteAreaLayout() {
		return siteAreaLayout;
	}
	public void setSiteAreaLayout(SiteAreaLayout siteAreaLayout) {
		this.siteAreaLayout = siteAreaLayout;
	}
	
	public String getLockOwner() {
		return lockOwner;
	}
	public void setLockOwner(String lockOwner) {
		this.lockOwner = lockOwner;
	}	
	
	public Map<String, String> getElements() {
		return elements;
	}
	
	public void setElements(Map<String, String> elements) {
		this.elements = elements;
	}
	
	public Map<String, String> getProperties() {
		return properties;
	}
	
	public void setProperties(Map<String, String> properties) {
		this.properties = properties;
	}

	public WcmProperties getMetadata() {
		return metadata;
	}
	public void setMetadata(WcmProperties metadata) {
		this.metadata = metadata;
	}
	public SearchData getSearchData() {
		return searchData;
	}
	public void setSearchData(SearchData searchData) {
		this.searchData = searchData;
	}
	public NavigationBadge getBadge() {
		return badge;
	}
	public void setBadge(NavigationBadge badge) {
		this.badge = badge;
	}
	
	public JsonNode toJson() {
		ObjectNode jsonNode = JsonUtils.createObjectNode();
		ObjectNode children = JsonUtils.createObjectNode();
		
		jsonNode.set("children", children);
		jsonNode.put(JcrConstants.JCR_PRIMARY_TYPE, "bpw:siteArea");
		
		jsonNode.put("bpw:name", this.getName());
		if (this.getElements() != null) {
			for (String key : this.getElements().keySet()) {
				jsonNode.put(String.format("bpw:%s", key), this.getElements().get(key));
			}
		}
		String title = StringUtils.hasText(this.getProperties().get("title")) ? this.getProperties().get("title") : this.getName();
		jsonNode.put("bpw:title", title);
		String url = StringUtils.hasText(this.getProperties().get("url")) ? this.getProperties().get("url") : this.getName();
		jsonNode.put("bpw:url", url);
		for (String prop : this.getProperties().keySet()) {
			if ("url".equals(prop) || "title".equals(prop)) {
				continue;
			} else if ("allowedArtifactTypes".equals(prop)) {
				String allowedArtifactTypes = this.getProperties().get("allowedArtifactTypes");
				String[] artifacts = allowedArtifactTypes.split(",");
				ArrayNode artifactArray = JsonUtils.creatArrayNode();
				for (String value : artifacts) {
					artifactArray.add(value);
				}
				jsonNode.set("bpw:allowedArtifactTypes", artifactArray);
			} else if ("allowedFileExtension".equals(prop)) {
				String allowedFileExtension = this.getProperties().get("allowedFileExtension");
				String[] artifacts = allowedFileExtension.split(",");
				ArrayNode artifactArray = JsonUtils.creatArrayNode();
				for (String value : artifacts) {
					artifactArray.add(value);
				}
				jsonNode.set("bpw:allowedFileExtension", artifactArray);
			} else {
			
				//bpw:description, bpw:friendlyURL, bpw:sorderOrder, bpw:showOnMenu, bpw:defaultContent
				// bpw:contentAreaLayout, bpw:siteConfig, bpw:securePage, bpw:cacheTTL
				// bpw:navigationId, bpw:navigationType, bpw:translate, bpw:function, bpw:icon
				// bpw:classes, bpw:exactMatch, bpw:externalUrl, bpw:openInNewTab
				jsonNode.put(String.format("bpw:%s", prop), this.getProperties().get(prop));
			}
		}
		
		if (this.getBadge() != null) {
			NavigationBadge badge = this.getBadge();
			ObjectNode badgeNode = JsonUtils.createObjectNode();
			children.set("bpw:badge", badgeNode);
			badgeNode.put(JcrConstants.JCR_PRIMARY_TYPE, "bpw:navigationBadge");
				
			if (StringUtils.hasText(badge.getTitle())) {
				badgeNode.put("bpw:title", badge.getTitle());
			}
			if (StringUtils.hasText(badge.getTranslate())) {
				badgeNode.put("bpw:translate", badge.getTranslate());
			}
			if (StringUtils.hasText(badge.getBg())) {
				badgeNode.put("bpw:bg", badge.getBg());
			}
			if (StringUtils.hasText(badge.getFg())) {
				badgeNode.put("bpw:fg", badge.getFg());
			}
		}
		
		if (this.getMetadata() != null) {
			
			ObjectNode metaDataNode = JsonUtils.createObjectNode();
			ObjectNode metaDataNodeChildren = JsonUtils.createObjectNode();
			children.set("bpw:metaData", metaDataNode);
			metaDataNode.set("children", metaDataNodeChildren);
			metaDataNode.put(JcrConstants.JCR_PRIMARY_TYPE, "bpw:properties");
			int count = 0;
			WcmProperties metadata = (WcmProperties)this.getMetadata();
			for (WcmProperty property: metadata.getProperties()) {
				
				ObjectNode kvNode = JsonUtils.createObjectNode();
				ObjectNode kvNodeChildren = JsonUtils.createObjectNode();
				metaDataNodeChildren.set("kv" + count++, kvNode);
				kvNode.set("children", kvNodeChildren);
				kvNode.put(JcrConstants.JCR_PRIMARY_TYPE, "bpw:property");
				kvNode.put("bpw:name", property.getName());
				kvNode.put("bpw:value", property.getValue());
			}
		} 
		if (this.getSearchData() != null) {	
			ObjectNode searchDataDataNode = JsonUtils.createObjectNode();
			children.set("bpw:searchData", searchDataDataNode);
			searchDataDataNode.put(JcrConstants.JCR_PRIMARY_TYPE, "bpw:pageSearchData");
			SearchData searchData = (SearchData) this.getSearchData();
			if (StringUtils.hasText(searchData.getDescription())) {
				searchDataDataNode.put("description", searchData.getDescription());
			}
			if (searchData.getKeywords() != null) {
				String[] keywords = searchData.getKeywords();
				ArrayNode keywordArray = JsonUtils.creatArrayNode();
				for (String keyword : keywords) {
					keywordArray.add(keyword);
				}				
				searchDataDataNode.set("keywords", keywordArray);
			}	
		}
		
		if (this.getSiteAreaLayout() != null) {
			ObjectNode siteAreaLayoutNode = JsonUtils.createObjectNode();
			ObjectNode siteAreaLayoutNodeChildren = JsonUtils.createObjectNode();
			children.set("siteAreaLayout", siteAreaLayoutNode);
			siteAreaLayoutNode.set("children", siteAreaLayoutNodeChildren);
			siteAreaLayoutNode.put(JcrConstants.JCR_PRIMARY_TYPE, "bpw:siteAreaLayout");
			
			
			ObjectNode sidePaneNode = JsonUtils.createObjectNode();
			ObjectNode sidePaneNodeChildren = JsonUtils.createObjectNode();
			siteAreaLayoutNodeChildren.set("sidePane", siteAreaLayoutNode);
			sidePaneNode.set("children", sidePaneNodeChildren);
			sidePaneNode.put(JcrConstants.JCR_PRIMARY_TYPE, "bpw:contentAreaSidePanel");
			
			sidePaneNode.put("bpw:contentWidth", this.getSiteAreaLayout().getContentWidth());
			
			
			this.addSidePaneNode(sidePaneNodeChildren, sidePaneNode, this.getSiteAreaLayout().getSidePane());
			this.addPageLayoutNodes(siteAreaLayoutNodeChildren, this.getSiteAreaLayout().getRows());
		}
		return jsonNode;
	}
	
	private void addPageLayoutNodes(ObjectNode pageLayoutNode, LayoutRow[] rows) {
		int rowCount = 0;
		for (LayoutRow row : rows) {
			ObjectNode rowNode = JsonUtils.createObjectNode();
			ObjectNode rowNodeChildren = JsonUtils.createObjectNode();
			pageLayoutNode.set("row" + rowCount++, rowNode);
			rowNode.set("children", rowNodeChildren);
			rowNode.put(JcrConstants.JCR_PRIMARY_TYPE, "bpw:layoutRow");
			
			int columnCount = 0;
			for (LayoutColumn column : row.getColumns()) {
				ObjectNode columnNode = JsonUtils.createObjectNode();
				ObjectNode columnNodeChildren = JsonUtils.createObjectNode();
				pageLayoutNode.set("column" + columnCount++, rowNode);
				columnNode.set("children", columnNodeChildren);
				columnNode.put(JcrConstants.JCR_PRIMARY_TYPE, "bpw:layoutColumn");
				columnNode.put("bpw:width", column.getWidth());
				this.addResourceViewers(columnNodeChildren, column.getViewers());
			}
		}
	}
	
	private void addSidePaneNode(ObjectNode sidenavNode, ObjectNode sidePaneNode, SidePane sidenav) {
		sidePaneNode.put("bpw:isLeft", sidenav.isLeft());
		sidePaneNode.put("bpw:width", sidenav.getWidth());
		this.addResourceViewers(sidenavNode, sidenav.getViewers());
	}
	
	private void addResourceViewers(ObjectNode sidenavNode, ResourceViewer viewers[]) {
		int viewerCount = 0;
		for (ResourceViewer viewer : viewers) {
			ObjectNode viewerNode = JsonUtils.createObjectNode();
			sidenavNode.set("viewer" + viewerCount++, viewerNode);
			viewerNode.put(JcrConstants.JCR_PRIMARY_TYPE, "bpw:resourceViewer");
			viewerNode.put("bpw:renderTemplateName", viewer.getRenderTemplate());
			viewerNode.put("bpw:title", viewer.getTitle());
			if (viewer.getContentPath() != null && viewer.getContentPath().length > 0) {
				ArrayNode contentPathArray = JsonUtils.creatArrayNode();
				for (String path : viewer.getContentPath()) {
					contentPathArray.add(path);
				}	
				viewerNode.set("bpw:contentPath", contentPathArray);
			}
		}
	}
	@Override
	public String toString() {
		return "SiteArea [repository=" + repository + ", workspace=" + workspace + ", wcmPath=" + wcmPath + ", name="
				+ this.getName() + ", siteAreaLayout=" + siteAreaLayout + ", lockOwner=" + lockOwner + ", elements=" + elements
				+ ", properties=" + properties + ", metadata=" + metadata + ", searchData=" + searchData + ", badge="
				+ badge + "]";
	}
}
