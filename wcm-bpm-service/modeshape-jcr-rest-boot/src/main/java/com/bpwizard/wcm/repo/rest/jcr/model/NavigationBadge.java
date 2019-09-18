package com.bpwizard.wcm.repo.rest.jcr.model;

public class NavigationBadge {
	
	private String title;
	private String translate;
	private String bg;
	private String fg;
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getTranslate() {
		return translate;
	}
	public void setTranslate(String translate) {
		this.translate = translate;
	}
	public String getBg() {
		return bg;
	}
	public void setBg(String bg) {
		this.bg = bg;
	}
	public String getFg() {
		return fg;
	}
	public void setFg(String fg) {
		this.fg = fg;
	}
	@Override
	public String toString() {
		return "NavigationBadge [title=" + title + ", translate=" + translate + ", bg=" + bg + ", fg=" + fg + "]";
	}
}
