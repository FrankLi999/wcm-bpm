package com.bpwizard.wcm.repo.rest.jcr.model;

import java.util.Arrays;
import java.util.Map;

public class WcmSystem {
	
	private WcmRepository wcmRepositories[];
	private Map<String, WcmOperation[]> operations;
	
	private Theme jcrThemes[];
	private Map<String, JsonForm[]> jsonForms;
	private Map<String, RenderTemplate> renderTemplates;
	private Map<String, ContentAreaLayout> contentAreaLayouts;
	private Map<String, AuthoringTemplate> authoringTemplates;
	private SiteConfig siteConfig;
	private Navigation[] navigations;
	//Navigation id to SiteArea map
	private Map<String, SiteArea> siteAreas;
	private ControlField controlFiels[];
	private String langs[];
	private Locale locales[];
	
	public WcmRepository[] getWcmRepositories() {
		return wcmRepositories;
	}
	public void setWcmRepositories(WcmRepository[] wcmRepositories) {
		this.wcmRepositories = wcmRepositories;
	}
	public Theme[] getJcrThemes() {
		return jcrThemes;
	}
	public void setJcrThemes(Theme[] jcrThemes) {
		this.jcrThemes = jcrThemes;
	}
	public Map<String, WcmOperation[]> getOperations() {
		return operations;
	}
	public void setOperations(Map<String, WcmOperation[]> operations) {
		this.operations = operations;
	}
	public Map<String, JsonForm[]> getJsonForms() {
		return jsonForms;
	}
	public void setJsonForms(Map<String, JsonForm[]> jsonForms) {
		this.jsonForms = jsonForms;
	}
	public Map<String, RenderTemplate> getRenderTemplates() {
		return renderTemplates;
	}
	public void setRenderTemplates(Map<String, RenderTemplate> renderTemplates) {
		this.renderTemplates = renderTemplates;
	}
	public Map<String, ContentAreaLayout> getContentAreaLayouts() {
		return contentAreaLayouts;
	}
	public void setContentAreaLayouts(Map<String, ContentAreaLayout> contentAreaLayouts) {
		this.contentAreaLayouts = contentAreaLayouts;
	}
	public Map<String, AuthoringTemplate> getAuthoringTemplates() {
		return authoringTemplates;
	}
	public void setAuthoringTemplates(Map<String, AuthoringTemplate> authoringTemplates) {
		this.authoringTemplates = authoringTemplates;
	}
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
	public Map<String, SiteArea> getSiteAreas() {
		return siteAreas;
	}
	public void setSiteAreas(Map<String, SiteArea> siteAreas) {
		this.siteAreas = siteAreas;
	}
	public ControlField[] getControlFiels() {
		return controlFiels;
	}
	public void setControlFiels(ControlField[] controlFiels) {
		this.controlFiels = controlFiels;
	}
	public String[] getLangs() {
		return langs;
	}
	public void setLangs(String[] langs) {
		this.langs = langs;
	}
	public Locale[] getLocales() {
		return locales;
	}
	public void setLocales(Locale[] locales) {
		this.locales = locales;
	}
	@Override
	public String toString() {
		return "WcmSystem [wcmRepositories=" + Arrays.toString(wcmRepositories) + ", operations=" + operations
				+ ", jcrThemes=" + Arrays.toString(jcrThemes) + ", jsonForms=" + jsonForms + ", renderTemplates="
				+ renderTemplates + ", contentAreaLayouts=" + contentAreaLayouts + ", authoringTemplates="
				+ authoringTemplates + ", siteConfig=" + siteConfig + ", navigations=" + Arrays.toString(navigations)
				+ ", siteAreas=" + siteAreas + ", controlFiels=" + Arrays.toString(controlFiels) + ", langs="
				+ Arrays.toString(langs) + ", locales=" + Arrays.toString(locales) + "]";
	}
}
