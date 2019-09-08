package com.bpwizard.wcm.repo.rest.jcr.model;

import java.util.Arrays;
import java.util.Map;

public class ApplicationConfig {
	private SiteConfig siteConfig;
	private Navigation[] navigations;
	private Map<String, JsonForm> jsonForms;
	private Map<String, RenderTemplate> renderTemplates;
	//Navigation id to SiteArea map
	private Map<String, SiteArea> siteAreas;
	private Map<String, ContentAreaLayout> contentAreaLayouts;
	public SiteConfig getSiteConfig() {
		return siteConfig;
	}
	public void setSiteConfig(SiteConfig siteConfig) {
		this.siteConfig = siteConfig;
	}
	public Navigation[] getNavigations() {
		return navigations;
	}
	public void setNavigations(Navigation[] navigations) {
		this.navigations = navigations;
	}
	public Map<String, JsonForm> getJsonForms() {
		return jsonForms;
	}
	public void setJsonForms(Map<String, JsonForm> jsonForms) {
		this.jsonForms = jsonForms;
	}
	public Map<String, RenderTemplate> getRenderTemplates() {
		return renderTemplates;
	}
	public void setRenderTemplates(Map<String, RenderTemplate> renderTemplates) {
		this.renderTemplates = renderTemplates;
	}
	public Map<String, SiteArea> getSiteAreas() {
		return siteAreas;
	}
	public void setSiteAreas(Map<String, SiteArea> siteAreas) {
		this.siteAreas = siteAreas;
	}
	public Map<String, ContentAreaLayout> getContentAreaLayouts() {
		return contentAreaLayouts;
	}
	public void setContentAreaLayouts(Map<String, ContentAreaLayout> contentAreaLayouts) {
		this.contentAreaLayouts = contentAreaLayouts;
	}
	@Override
	public String toString() {
		return "ApplicationConfig [siteConfig=" + siteConfig + ", navigations=" + Arrays.toString(navigations)
				+ ", jsonForms=" + jsonForms + ", renderTemplates=" + renderTemplates + ", siteAreas=" + siteAreas
				+ ", contentAreaLayouts=" + contentAreaLayouts + "]";
	}
}
