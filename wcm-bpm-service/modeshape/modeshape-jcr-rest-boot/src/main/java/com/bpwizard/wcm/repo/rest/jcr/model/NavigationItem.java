package com.bpwizard.wcm.repo.rest.jcr.model;

import java.util.Arrays;

public class NavigationItem {
	private String id;
	private String title;
	private NavigationType type;
	private String translate;
	private String icon;
	private boolean hidden;
	private String url;
	private String classes;
	private boolean exactMatch;
	private boolean externalUrl;
	private boolean openInNewTab;
	private String function;
	private NavigationBadge badge;
	private NavigationItem children[];
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public NavigationType getType() {
		return type;
	}
	public void setType(NavigationType type) {
		this.type = type;
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
	public boolean isHidden() {
		return hidden;
	}
	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
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
	public String getFunction() {
		return function;
	}
	public void setFunction(String function) {
		this.function = function;
	}
	public NavigationBadge getBadge() {
		return badge;
	}
	public void setBadge(NavigationBadge badge) {
		this.badge = badge;
	}
	public NavigationItem[] getChildren() {
		return children;
	}
	public void setChildren(NavigationItem[] children) {
		this.children = children;
	}
	@Override
	public String toString() {
		return "NavigationItem [id=" + id + ", title=" + title + ", type=" + type + ", translate=" + translate
				+ ", icon=" + icon + ", hidden=" + hidden + ", url=" + url + ", classes=" + classes + ", exactMatch="
				+ exactMatch + ", externalUrl=" + externalUrl + ", openInNewTab=" + openInNewTab + ", function="
				+ function + ", badge=" + badge + ", children=" + Arrays.toString(children) + "]";
	}
}
