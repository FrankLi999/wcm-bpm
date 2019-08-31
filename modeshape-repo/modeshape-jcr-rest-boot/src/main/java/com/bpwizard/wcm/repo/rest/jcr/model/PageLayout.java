package com.bpwizard.wcm.repo.rest.jcr.model;

public class PageLayout {
    private String colorTheme;
    private boolean customScrollbars;
	private NavBar navbar;
	private Toolbar toolbar;
	private Footer Footer;
	private SidePanel sidePanel;
	public String getColorTheme() {
		return colorTheme;
	}
	public void setColorTheme(String colorTheme) {
		this.colorTheme = colorTheme;
	}
	public boolean isCustomScrollbars() {
		return customScrollbars;
	}
	public void setCustomScrollbars(boolean customScrollbars) {
		this.customScrollbars = customScrollbars;
	}
	public NavBar getNavbar() {
		return navbar;
	}
	public void setNavbar(NavBar navbar) {
		this.navbar = navbar;
	}
	public Toolbar getToolbar() {
		return toolbar;
	}
	public void setToolbar(Toolbar toolbar) {
		this.toolbar = toolbar;
	}
	public Footer getFooter() {
		return Footer;
	}
	public void setFooter(Footer footer) {
		Footer = footer;
	}
	public SidePanel getSidePanel() {
		return sidePanel;
	}
	public void setSidePanel(SidePanel sidePanel) {
		this.sidePanel = sidePanel;
	}
	@Override
	public String toString() {
		return "PageLayout [colorTheme=" + colorTheme + ", customScrollbars=" + customScrollbars + ", navbar=" + navbar
				+ ", toolbar=" + toolbar + ", Footer=" + Footer + ", sidePanel=" + sidePanel + "]";
	}
}
