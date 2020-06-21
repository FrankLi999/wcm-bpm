package com.bpwizard.wcm.repo.rest.jcr.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.validation.constraints.NotBlank;

import org.modeshape.jcr.api.JcrConstants;

import com.bpwizard.wcm.repo.rest.JsonUtils;
import com.bpwizard.wcm.repo.rest.utils.WcmConstants;
import com.bpwizard.wcm.repo.validation.LibraryName;
import com.bpwizard.wcm.repo.validation.RepositoryName;
import com.bpwizard.wcm.repo.validation.ValidateString;
import com.bpwizard.wcm.repo.validation.WorkspaceName;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

@LibraryName(propertyNames= {"name"})
public class Library implements Serializable, Comparable<Library> {
	private static final long serialVersionUID = 1L;
	
	@RepositoryName()
	private String repository;
	
	@WorkspaceName()
	private String workspace;
	
	@NotBlank(message = "Library Name is mandatory")
	
	private String name;
	private String title;

	private String description;
	@NotBlank(message = "Library Lanaguage is mandatory")
	@ValidateString(acceptedValues={"en", "fr", "zh"}, message="Language must be en, fr or zh")
	private String language;
	
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
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	
	public JsonNode toJson() {
		ObjectNode jsonNode = JsonUtils.createObjectNode();
		ObjectNode children = JsonUtils.createObjectNode();
		
		jsonNode.set(WcmConstants.JCR_JSON_NODE_CHILDREN, children);
		jsonNode.put(JcrConstants.JCR_PRIMARY_TYPE, "bpw:system_libraryType");
		
		ObjectNode propertiesNode = JsonUtils.createObjectNode();
		children.set(WcmConstants.WCM_ITEM_PROPERTIES, propertiesNode);
		propertiesNode.put(JcrConstants.JCR_PRIMARY_TYPE, WcmConstants.JCR_TYPE_PROPERTY_FOLDER);
		propertiesNode.put("bpw:title", this.getTitle());
		propertiesNode.put("bpw:description", this.getDescription());
		
		ObjectNode elementsNode = JsonUtils.createObjectNode();
		children.set(WcmConstants.WCM_ITEM_ELEMENTS, elementsNode);
		elementsNode.put(JcrConstants.JCR_PRIMARY_TYPE, "bpw:system_libraryType_ElementFolder");
		elementsNode.put("language", this.getLanguage());
		
		ObjectNode assetFolderNode = JsonUtils.createObjectNode();
		children.set("asset", assetFolderNode);
		assetFolderNode.put(JcrConstants.JCR_PRIMARY_TYPE, "bpw:assetFolder");
		
		ObjectNode formFolderNode = JsonUtils.createObjectNode();
		children.set("form", formFolderNode);
		formFolderNode.put(JcrConstants.JCR_PRIMARY_TYPE, "bpw:formFolder");
		
		ObjectNode themeFolderNode = JsonUtils.createObjectNode();
		children.set("theme", themeFolderNode);
		themeFolderNode.put(JcrConstants.JCR_PRIMARY_TYPE, "bpw:themesFolder");

		ObjectNode siteConfigNode = JsonUtils.createObjectNode();
		children.set("siteConfig", siteConfigNode);
		siteConfigNode.put(JcrConstants.JCR_PRIMARY_TYPE, "bpw:siteConfigFolder");
		
		ObjectNode contentAreaLayoutNode = JsonUtils.createObjectNode();
		children.set("contentAreaLayout", contentAreaLayoutNode);
		contentAreaLayoutNode.put(JcrConstants.JCR_PRIMARY_TYPE, "bpw:contentAreaLayoutFolder");
		
		ObjectNode renderTemplateNode = JsonUtils.createObjectNode();
		children.set("renderTemplate", renderTemplateNode);
		renderTemplateNode.put(JcrConstants.JCR_PRIMARY_TYPE, "bpw:renderTemplateFolder");
		
		ObjectNode queryNode = JsonUtils.createObjectNode();
		children.set("query", queryNode);
		queryNode.put(JcrConstants.JCR_PRIMARY_TYPE, "bpw:queryFolder");

		ObjectNode workflowNode = JsonUtils.createObjectNode();
		children.set("workflow", workflowNode);
		workflowNode.put(JcrConstants.JCR_PRIMARY_TYPE, "bpw:workflowFolder");
		
		ObjectNode categoryNode = JsonUtils.createObjectNode();
		children.set("category", categoryNode);
		categoryNode.put(JcrConstants.JCR_PRIMARY_TYPE, "bpw:categoryFolder");

		ObjectNode authoringTemplateNode = JsonUtils.createObjectNode();
		children.set("authoringTemplate", authoringTemplateNode);
		authoringTemplateNode.put(JcrConstants.JCR_PRIMARY_TYPE, "bpw:authoringTemplateFolder");
		
		ObjectNode validationRuleNode = JsonUtils.createObjectNode();
		children.set("validationRule", validationRuleNode);
		validationRuleNode.put(JcrConstants.JCR_PRIMARY_TYPE, "bpw:validationRuleFolder");
		
		SiteArea rootSiteArea = new SiteArea();
		ResourceMixin rootSiteAreaProperties = new ResourceMixin();
		rootSiteArea.setProperties(rootSiteAreaProperties);
		rootSiteAreaProperties.setName("rootSiteArea");
		rootSiteAreaProperties.setTitle(this.getTitle());
		rootSiteAreaProperties.setDescription(this.getDescription());
		
		Map<String, Object> rootSiteAreaElements = new HashMap<>();
		rootSiteArea.setElements(rootSiteAreaElements);
		rootSiteAreaElements.put("navigationId", this.getName());
		rootSiteAreaElements.put("navigationType", "group");
		rootSiteAreaElements.put("siteConfig", this.getName());
		rootSiteAreaElements.put("url", String.format("/%s", this.getName()));
		rootSiteAreaElements.put("translate", String.format("NAV.%s.APPLICATIONS", this.getName().toUpperCase()));
		rootSiteAreaElements.put("contentAreaLayout", WcmConstants.DEFAULT_SA_LAYOUT);
		
		children.set("rootSiteArea", rootSiteArea.toJson());
        return jsonNode;
	}
	
	@Override
	public String toString() {
		return "Library [repository=" + repository + ", workspace=" + workspace + ", name=" + name + ", title=" + title
				+ ", description=" + description + ", language=" + language + "]";
	}
	
	@Override
	public int compareTo(Library o) {
		int result = 1;
		if (o != null) {
		    result = this.getName().compareTo(o.getName());
		}
		return result;
	}
}
