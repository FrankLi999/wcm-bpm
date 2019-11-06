package com.bpwizard.wcm.repo.rest.jcr.model;

import java.util.Arrays;

public class SiteAreaLayout {
	SidePane sidePane;
	LayoutRow rows[];
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
		return "SiteAreaLayout [sidePane=" + sidePane + ", rows=" + Arrays.toString(rows) + "]";
	}
}
