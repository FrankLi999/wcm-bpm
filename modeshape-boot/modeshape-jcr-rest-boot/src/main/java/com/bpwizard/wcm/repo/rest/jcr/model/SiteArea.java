package com.bpwizard.wcm.repo.rest.jcr.model;

import java.util.Map;

import org.modeshape.jcr.api.JcrConstants;
import org.springframework.util.StringUtils;

import com.bpwizard.wcm.repo.rest.JsonUtils;
import com.bpwizard.wcm.repo.rest.modeshape.model.HasName;
import com.bpwizard.wcm.repo.rest.utils.WcmConstants;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class SiteArea implements HasName, HasWcmAuthority {

	private String repository;
	private String workspace;
	private String wcmPath;
	// private String name;
	private SiteAreaLayout siteAreaLayout;
	private String lockOwner;
	
    private Map<String, Object> elements;
//	private Map<String, String> properties;
	private ResourceMixin properties;
//	private ContentItemElements elements;
	private WcmProperties metadata;
	private SearchData searchData;
	private WcmAuthority wcmAuthority;

	
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
		return this.properties.getName();
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
	
	public Map<String, Object> getElements() {
		return elements;
	}
	
	public void setElements(Map<String, Object> elements) {
		this.elements = elements;
	}
	
	public ResourceMixin getProperties() {
		return properties;
	}
	
	public void setProperties(ResourceMixin properties) {
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
		return (NavigationBadge) this.getElements().get("badge");
	}
	
	public void setBadge(NavigationBadge badge) {
		this.getElements().put("badge", badge);
	}
	public WcmAuthority getWcmAuthority() {
		return wcmAuthority;
	}
	public void setWcmAuthority(WcmAuthority wcmAuthority) {
		this.wcmAuthority = wcmAuthority;
	}
	public JsonNode toJson() {
		ObjectNode jsonNode = JsonUtils.createObjectNode();
		ObjectNode children = JsonUtils.createObjectNode();
		
		jsonNode.set(WcmConstants.JCR_JSON_NODE_CHILDREN, children);
		jsonNode.put(JcrConstants.JCR_PRIMARY_TYPE, "bpw:system_siteAreaType");
		
		ObjectNode propertiesNode = JsonUtils.createObjectNode();
		children.set(WcmConstants.WCM_ITEM_PROPERTIES, propertiesNode);
		propertiesNode.put(JcrConstants.JCR_PRIMARY_TYPE, WcmConstants.JCR_TYPE_PROPERTY_FOLDER);
//		propertiesNode.put("bpw:description", this.getDescription());
		propertiesNode.put("bpw:name", this.getName());
		String title = StringUtils.hasText(this.getProperties().getTitle()) ? this.getProperties().getTitle() : this.getName();
		propertiesNode.put("title", title);

		ObjectNode elementsNode = JsonUtils.createObjectNode();
		children.set(WcmConstants.WCM_ITEM_ELEMENTS, elementsNode);
		elementsNode.put(JcrConstants.JCR_PRIMARY_TYPE, "bpw:system_siteAreaType_ElementFolder");
		//elementsNode.put("language", this.getLanguage());
		
		String url = StringUtils.hasText((String)(this.getElements().get("url"))) ? (String)this.getElements().get("url") : this.getName();
		elementsNode.put("url", url);
		if (this.getElements() != null) {
			for (String key : this.getElements().keySet()) {
				// jsonNode.put(String.format("bpw:%s", key), (String)this.getElements().get(key));
				if ( this.getElements().get(key) instanceof String) {
					if ("url".equals(key)) {
						continue;
					} else if ("allowedArtifactTypes".equals(key)) {
						String allowedArtifactTypes = (String) this.getElements().get("allowedArtifactTypes");
						String[] artifacts = allowedArtifactTypes.split(",");
						ArrayNode artifactArray = JsonUtils.creatArrayNode();
						for (String value : artifacts) {
							artifactArray.add(value);
						}
						jsonNode.set("allowedArtifactTypes", artifactArray);
					} else if ("allowedFileExtension".equals(key)) {
						String allowedFileExtension = (String) this.getElements().get("allowedFileExtension");
						String[] artifacts = allowedFileExtension.split(",");
						ArrayNode artifactArray = JsonUtils.creatArrayNode();
						for (String value : artifacts) {
							artifactArray.add(value);
						}
						jsonNode.set("allowedFileExtension", artifactArray);
					} else {
					
						//bpw:description, bpw:friendlyURL, bpw:sorderOrder, bpw:showOnMenu, bpw:defaultContent
						// bpw:contentAreaLayout, bpw:siteConfig, bpw:securePage, bpw:cacheTTL
						// bpw:navigationId, bpw:navigationType, bpw:translate, bpw:function, bpw:icon
						// bpw:classes, bpw:exactMatch, bpw:externalUrl, bpw:openInNewTab
						// jsonNode.put(String.format("bpw:%s", prop), (String) this.getElements().get(prop));
						elementsNode.put(key, (String)this.getElements().get(key));
					}
					
				} else if ( this.getElements().get(key) instanceof NavigationBadge) {
					NavigationBadge badge = this.getBadge();
					ObjectNode badgeNode = JsonUtils.createObjectNode();
					elementsNode.set("badge", badgeNode);
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
			}
			
		}
		

		
		if (this.getMetadata() != null) {
			ObjectNode metaDataNode = JsonUtils.createObjectNode();
			ObjectNode metaDataNodeChildren = JsonUtils.createObjectNode();
			children.set("bpw:metaData", metaDataNode);
			metaDataNode.set(WcmConstants.JCR_JSON_NODE_CHILDREN, metaDataNodeChildren);
			metaDataNode.put(JcrConstants.JCR_PRIMARY_TYPE, "bpw:properties");
			WcmProperties metadata = (WcmProperties)this.getMetadata();
			for (WcmProperty property: metadata.getProperties()) {
				ObjectNode kvNode = JsonUtils.createObjectNode();
				metaDataNodeChildren.set(property.getName(), kvNode);
				kvNode.put(JcrConstants.JCR_PRIMARY_TYPE, "bpw:property");
				kvNode.put("bpw:name", property.getName());
				kvNode.put("bpw:value", property.getValue());
			}
		} 
		if (this.getSearchData() != null) {	
			ObjectNode searchDataNode = JsonUtils.createObjectNode();
			children.set("bpw:searchData", searchDataNode);
			searchDataNode.put(JcrConstants.JCR_PRIMARY_TYPE, "bpw:pageSearchData");
			SearchData searchData = (SearchData) this.getSearchData();
			if (StringUtils.hasText(searchData.getDescription())) {
				searchDataNode.put("description", searchData.getDescription());
			}
			if (searchData.getKeywords() != null) {
				String[] keywords = searchData.getKeywords();
				ArrayNode keywordArray = JsonUtils.creatArrayNode();
				for (String keyword : keywords) {
					keywordArray.add(keyword);
				}				
				searchDataNode.set("keywords", keywordArray);
			}	
		}
		
		if (this.getSiteAreaLayout() != null) {
			ObjectNode siteAreaLayoutNode = JsonUtils.createObjectNode();
			ObjectNode siteAreaLayoutNodeChildren = JsonUtils.createObjectNode();
			children.set("siteAreaLayout", siteAreaLayoutNode);
			siteAreaLayoutNode.set(WcmConstants.JCR_JSON_NODE_CHILDREN, siteAreaLayoutNodeChildren);
			siteAreaLayoutNode.put(JcrConstants.JCR_PRIMARY_TYPE, "bpw:siteAreaLayout");
			
			ObjectNode sidePaneNode = JsonUtils.createObjectNode();
			ObjectNode sidePaneNodeChildren = JsonUtils.createObjectNode();
			siteAreaLayoutNodeChildren.set("sidePane", siteAreaLayoutNode);
			sidePaneNode.set(WcmConstants.JCR_JSON_NODE_CHILDREN, sidePaneNodeChildren);
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
			rowNode.set(WcmConstants.JCR_JSON_NODE_CHILDREN, rowNodeChildren);
			rowNode.put(JcrConstants.JCR_PRIMARY_TYPE, "bpw:layoutRow");
			
			int columnCount = 0;
			for (LayoutColumn column : row.getColumns()) {
				ObjectNode columnNode = JsonUtils.createObjectNode();
				ObjectNode columnNodeChildren = JsonUtils.createObjectNode();
				pageLayoutNode.set("column" + columnCount++, rowNode);
				columnNode.set(WcmConstants.JCR_JSON_NODE_CHILDREN, columnNodeChildren);
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
		return "SiteArea [repository=" + repository + ", workspace=" + workspace + ", wcmPath=" + wcmPath
				+ ", siteAreaLayout=" + siteAreaLayout + ", lockOwner=" + lockOwner + ", elements=" + elements
				+ ", properties=" + properties + ", metadata=" + metadata + ", searchData=" + searchData 
				 + ", wcmAuthority=" + wcmAuthority + "]";
	}
}
