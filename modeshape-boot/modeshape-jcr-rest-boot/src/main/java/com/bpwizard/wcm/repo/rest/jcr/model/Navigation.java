package com.bpwizard.wcm.repo.rest.jcr.model;

public class Navigation extends NavigationItem {
	private NavigationItem children [];

	public NavigationItem[] getChildren() {
		return children;
	}

	public void setChildren(NavigationItem[] children) {
		this.children = children;
	}
}
