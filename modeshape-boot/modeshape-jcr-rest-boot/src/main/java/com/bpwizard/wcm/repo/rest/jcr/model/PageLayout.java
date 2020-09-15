package com.bpwizard.wcm.repo.rest.jcr.model;

public class PageLayout {
    private String title = "vertical-layout-1";
    // private String width = "fullwidth";
    private String mode = "fullwidth"; //or container or boxed
    private String scroll = "content";
	private NavBar navbar;
	private Toolbar toolbar;
	private Footer footer;
	private SidePanel leftSidePanel;
	private SidePanel rightSidePanel;
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getMode() {
		return mode;
	}
	public void setMode(String mode) {
		this.mode = mode;
	}
	public String getScroll() {
		return scroll;
	}
	public void setScroll(String scroll) {
		this.scroll = scroll;
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
	public SidePanel getLeftSidePanel() {
		return leftSidePanel;
	}
	public void setLeftSidePanel(SidePanel leftSidePanel) {
		this.leftSidePanel = leftSidePanel;
	}
	public SidePanel getRightSidePanel() {
		return rightSidePanel;
	}
	public void setRightSidePanel(SidePanel rightSidePanel) {
		this.rightSidePanel = rightSidePanel;
	}
	@Override
	public String toString() {
		return "PageLayout [title=" + title + ", mode=" + mode+ ", scroll=" + scroll + ", navbar=" + navbar + ", toolbar=" + toolbar
				+ ", Footer=" + footer + ", leftSidePanel=" + leftSidePanel + ", rightSidePanel=" + rightSidePanel + "]";
	}
}
