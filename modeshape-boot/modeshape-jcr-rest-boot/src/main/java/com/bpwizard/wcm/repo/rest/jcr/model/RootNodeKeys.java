package com.bpwizard.wcm.repo.rest.jcr.model;

public class RootNodeKeys {
	private String rootNodeKey;
	private String jcrSystemKey;
	public String getRootNodeKey() {
		return rootNodeKey;
	}
	public void setRootNodeKey(String rootNodeKey) {
		this.rootNodeKey = rootNodeKey;
	}
	public String getJcrSystemKey() {
		return jcrSystemKey;
	}
	public void setJcrSystemKey(String jcrSystemKey) {
		this.jcrSystemKey = jcrSystemKey;
	}
	@Override
	public String toString() {
		return "RootNodeKeys [rootNodeKey=" + rootNodeKey + ", jcrSystemKey=" + jcrSystemKey + "]";
	}
}
