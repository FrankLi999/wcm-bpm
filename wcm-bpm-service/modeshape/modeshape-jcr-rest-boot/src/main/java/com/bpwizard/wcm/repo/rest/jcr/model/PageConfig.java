package com.bpwizard.wcm.repo.rest.jcr.model;

import java.util.Arrays;

public class PageConfig {
	
	private SiteConfig siteConfig;
	private Navigation[] navigations;
	private String langs[];
	private Locale locales[];
	  
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
		return "PageConfig [siteConfig=" + siteConfig + ", navigations=" + Arrays.toString(navigations) + ", langs="
				+ Arrays.toString(langs) + ", locales=" + Arrays.toString(locales) + "]";
	}
}
