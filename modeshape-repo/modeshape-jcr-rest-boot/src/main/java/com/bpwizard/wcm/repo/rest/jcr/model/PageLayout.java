package com.bpwizard.wcm.repo.rest.jcr.model;

public class PageLayout {
    private String style = "vertical-layout-1";
    private String width = "fullwidth";
    
	private NavBar navbar;
	private Toolbar toolbar;
	private Footer footer;
	private SidePanel sidePanel;
	public String getStyle() {
		return style;
	}
	public void setStyle(String style) {
		this.style = style;
	}
	public String getWidth() {
		return width;
	}
	public void setWidth(String width) {
		this.width = width;
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
		return footer;
	}
	public void setFooter(Footer footer) {
		this.footer = footer;
	}
	public SidePanel getSidePanel() {
		return sidePanel;
	}
	public void setSidePanel(SidePanel sidePanel) {
		this.sidePanel = sidePanel;
	}
	@Override
	public String toString() {
		return "PageLayout [style=" + style + ", width=" + width + ", navbar=" + navbar + ", toolbar=" + toolbar
				+ ", Footer=" + footer + ", sidePanel=" + sidePanel + "]";
	}
}
