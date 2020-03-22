package com.bpwizard.wcm.repo.rest.jcr.model;

import java.util.Arrays;
import java.util.Map;

public class WcmNavigatorFilter {
	private String wcmPath;
	private String[] nodeTypes;
	private Map<String, Map<String, String>> conditions;
	
	private String filter;
	private String sortDirection;
	private int pageIndex = 0;
	private int pageSize;
	
	public String getWcmPath() {
		return wcmPath;
	}

	public void setWcmPath(String wcmPath) {
		this.wcmPath = wcmPath;
	}

	public String[] getNodeTypes() {
		return nodeTypes;
	}
	
	public void setNodeTypes(String[] nodeTypes) {
		this.nodeTypes = nodeTypes;
	}
	
	public Map<String, Map<String, String>> getConditions() {
		return conditions;
	}
	
	public void setConditions(Map<String, Map<String, String>> conditions) {
		this.conditions = conditions;
	}

	public String getFilter() {
		return filter;
	}

	public void setFilter(String filter) {
		this.filter = filter;
	}

	public String getSortDirection() {
		return sortDirection;
	}

	public void setSortDirection(String sortDirection) {
		this.sortDirection = sortDirection;
	}

	public int getPageIndex() {
		return pageIndex;
	}

	public void setPageIndex(int pageIndex) {
		this.pageIndex = pageIndex;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	@Override
	public String toString() {
		return "WcmNavigatorFilter [wcmPath=" + wcmPath + ", nodeTypes=" + Arrays.toString(nodeTypes)
				+ ", conditions=" + conditions + ", filter=" + filter + ", sortDirection=" + sortDirection
				+ ", pageIndex=" + pageIndex + ", pageSize=" + pageSize + "]";
	}
}
