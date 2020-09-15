package com.bpwizard.wcm.repo.rest.jcr.model;

public class ThemeColors {
	private String main;
	private String navbar;
	private String toolbar;
	private String footer;
	public String getMain() {
		return main;
	}
	public void setMain(String main) {
		this.main = main;
	}
	public String getNavbar() {
		return navbar;
	}
	public void setNavbar(String navbar) {
		this.navbar = navbar;
	}
	public String getToolbar() {
		return toolbar;
	}
	public void setToolbar(String toolbar) {
		this.toolbar = toolbar;
	}
	public String getFooter() {
		return footer;
	}
	public void setFooter(String footer) {
		this.footer = footer;
	}
	@Override
	public String toString() {
		return "ThemeColors [main=" + main + ", navbar=" + navbar + ", toolbar=" + toolbar + ", footer=" + footer + "]";
	}
}
