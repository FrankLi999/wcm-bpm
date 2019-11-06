package com.bpwizard.wcm.repo.rest.jcr.model;

public class WcmNode {
    private String wcmPath;
    private String name;
    private String nodeType;
	public String getWcmPath() {
		return wcmPath;
	}
	public void setWcmPath(String wcmPath) {
		this.wcmPath = wcmPath;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getNodeType() {
		return nodeType;
	}
	public void setNodeType(String nodeType) {
		this.nodeType = nodeType;
	}
	@Override
	public String toString() {
		return "WcmNode [wcmPath=" + wcmPath + ", name=" + name + ", nodeType=" + nodeType + "]";
	}
}
