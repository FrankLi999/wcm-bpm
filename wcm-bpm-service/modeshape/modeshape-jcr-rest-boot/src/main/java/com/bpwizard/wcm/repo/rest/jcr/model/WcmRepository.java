package com.bpwizard.wcm.repo.rest.jcr.model;

import java.util.Arrays;

public class WcmRepository {
	private String name;
	private WcmWorkspace workspaces[];
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public WcmWorkspace[] getWorkspaces() {
		return workspaces;
	}
	public void setWorkspaces(WcmWorkspace[] workspaces) {
		this.workspaces = workspaces;
	}
	@Override
	public String toString() {
		return "WcmRepository [name=" + name + ", workspaces=" + Arrays.toString(workspaces) + "]";
	}
	
	
}
