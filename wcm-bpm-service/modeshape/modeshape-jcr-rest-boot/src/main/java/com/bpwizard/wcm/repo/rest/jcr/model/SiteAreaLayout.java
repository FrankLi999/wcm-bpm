package com.bpwizard.wcm.repo.rest.jcr.model;

import java.util.Arrays;

public class SiteAreaLayout {
	int contentWidth;
	SidePane sidePane;
	LayoutRow rows[];
	
	public int getContentWidth() {
		return contentWidth;
	}
	public void setContentWidth(int contentWidth) {
		this.contentWidth = contentWidth;
	}
	public SidePane getSidePane() {
		return sidePane;
	}
	public void setSidePane(SidePane sidePane) {
		this.sidePane = sidePane;
	}
	public LayoutRow[] getRows() {
		return rows;
	}
	public void setRows(LayoutRow[] rows) {
		this.rows = rows;
	}
	@Override
	public String toString() {
		return "SiteAreaLayout [contentWidth=" + contentWidth + ", sidePane=" + sidePane + ", rows="
				+ Arrays.toString(rows) + "]";
	}
}
