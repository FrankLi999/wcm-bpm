package com.bpwizard.wcm.repo.rest.jcr.model;

import java.util.Arrays;

public class ApplicationConfig {
	private SiteConfig siteConfig;
	private Navigation[] navigations;
	private JsonForm[] jsonForms;
	
	public SiteConfig getSiteConfig() {
		return siteConfig;
	}
	
	public void setSiteConfig(SiteConfig siteConfig) {
		this.siteConfig = siteConfig;
	}
	
	public Navigation[] getNavigations() {
		return navigations;
	}
	
	public void setNavigation(Navigation[] navigations) {
		this.navigations = navigations;
	}
	
	public JsonForm[] getJsonForms() {
		return jsonForms;
	}
	
	public void setJsonForms(JsonForm[] jsonForms) {
		this.jsonForms = jsonForms;
	}
	
	@Override
	public String toString() {
		return "ApplicationConfig [siteConfig=" + siteConfig + ", navigations=" + navigations + ", jsonForms="
				+ Arrays.toString(jsonForms) + "]";
	}
}
