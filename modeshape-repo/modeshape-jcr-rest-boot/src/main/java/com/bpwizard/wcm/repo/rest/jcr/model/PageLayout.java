package com.bpwizard.wcm.repo.rest.jcr.model;

import java.util.Arrays;

import com.bpwizard.wcm.repo.rest.modeshape.model.HasName;

public class PageLayout implements HasName {
	
	String name;
	boolean headerEnabled = true;
	boolean footerEnabled = true;
	String theme;
	SideNav sidenav;
	int contentWidth;
	LayoutRow rows[];
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isHeaderEnabled() {
		return headerEnabled;
	}
	public void setHeaderEnabled(boolean headerEnabled) {
		this.headerEnabled = headerEnabled;
	}
	public boolean isFooterEnabled() {
		return footerEnabled;
	}
	public void setFooterEnabled(boolean footerEnabled) {
		this.footerEnabled = footerEnabled;
	}
	public String getTheme() {
		return theme;
	}
	public void setTheme(String theme) {
		this.theme = theme;
	}
	public boolean isSidenavEnabled() {
		return sidenavEnabled;
	}
	public void setSidenavEnabled(boolean sidenavEnabled) {
		this.sidenavEnabled = sidenavEnabled;
	}
	public SideNav getSidenav() {
		return sidenav;
	}
	public void setSidenav(SideNav sidenav) {
		this.sidenav = sidenav;
	}
	public LayoutRow[] getRows() {
		return rows;
	}
	public void setRows(LayoutRow[] rows) {
		this.rows = rows;
	}
	public int getContentWidth() {
		return contentWidth;
	}
	public void setContentWidth(int contentWidth) {
		this.contentWidth = contentWidth;
	}
	
	@Override
	public String toString() {
		return "PageLayout [name=" + name + ", headerEnabled=" + headerEnabled + ", footerEnabled=" + footerEnabled
				+ ", theme=" + theme + ", sidenavEnabled=" + sidenavEnabled + ", sidenav=" + sidenav + ", contentWidth="
				+ contentWidth + ", rows=" + Arrays.toString(rows) + "]";
	}
}
