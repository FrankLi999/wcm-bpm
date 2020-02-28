package com.bpwizard.wcm.repo.rest.jcr.model;

import java.io.Serializable;

import org.modeshape.jcr.api.JcrConstants;

import com.bpwizard.wcm.repo.rest.JsonUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class Library implements Serializable {
	private static final long serialVersionUID = 1L;
	private String repository;
	private String workspace;
	private String name;
	private String title;
	private String description;
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
		
		jsonNode.set("children", children);
		jsonNode.put(JcrConstants.JCR_PRIMARY_TYPE, "bpw:library");
		jsonNode.put("jcr:language", this.getLanguage());
		jsonNode.put("bpw:title", this.getTitle());
		jsonNode.put("bpw:description", this.getDescription());
		
		ObjectNode assetFolderNode = JsonUtils.createObjectNode();
		children.set("asset", assetFolderNode);
		assetFolderNode.put(JcrConstants.JCR_PRIMARY_TYPE, "bpw:assetFolder");
		
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
		
		ObjectNode rootSiteAreaNode = JsonUtils.createObjectNode();
		children.set("rootSiteArea", rootSiteAreaNode);
		rootSiteAreaNode.put(JcrConstants.JCR_PRIMARY_TYPE, "bpw:siteArea");
		rootSiteAreaNode.put("bpw:name", this.getName());
		rootSiteAreaNode.put("bpw:title", this.getTitle());
		rootSiteAreaNode.put("bpw:navigationId", this.getName());
		rootSiteAreaNode.put("bpw:navigationType", "group");
		rootSiteAreaNode.put("bpw:navigationId", this.getName());
		rootSiteAreaNode.put("bpw:siteConfig", this.getName());
		rootSiteAreaNode.put("bpw:url", String.format("/%s", this.getName()));
		rootSiteAreaNode.put("bpw:translate", "NAV.BPM.APPLICATIONS");
		rootSiteAreaNode.put("bpw:contentAreaLayout", "bpwizard/default/design/MyLayout");
        return jsonNode;
	}
	
	@Override
	public String toString() {
		return "Library [repository=" + repository + ", workspace=" + workspace + ", name=" + name + ", title=" + title
				+ ", description=" + description + ", language=" + language + "]";
	}
}
