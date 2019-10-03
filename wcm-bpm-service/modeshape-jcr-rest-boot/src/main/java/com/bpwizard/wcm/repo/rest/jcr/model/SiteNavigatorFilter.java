package com.bpwizard.wcm.repo.rest.jcr.model;

import java.util.Arrays;
import java.util.Map;

public class SiteNavigatorFilter {
	private String nodePath;
	private String[] nodeTypes;
	private Map<String, Map<String, String>> filters;
	
	public String getNodePath() {
		return nodePath;
	}

	public void setNodePath(String nodePath) {
		this.nodePath = nodePath;
	}

	public String[] getNodeTypes() {
		return nodeTypes;
	}
	
	public void setNodeTypes(String[] nodeTypes) {
		this.nodeTypes = nodeTypes;
	}
	
	public Map<String, Map<String, String>> getFilters() {
		return filters;
	}
	
	public void setFilters(Map<String, Map<String, String>> filters) {
		this.filters = filters;
	}

	@Override
	public String toString() {
		return "SiteNavigatorFilter [nodePath=" + nodePath + ", nodeTypes=" + Arrays.toString(nodeTypes) + ", filters="
				+ filters + "]";
	}
}
