package com.bpwizard.wcm.repo.rest.jcr.model;

public class WcmNode {
    private String wcmPath;
    private String name;
    private String title;
    private String nodeType;    
    private String lastModified;
    private String owner;
    private String status;
    
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
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getNodeType() {
		return nodeType;
	}
	
	public void setNodeType(String nodeType) {
		this.nodeType = nodeType;
	}
	
	public String getLastModified() {
		return lastModified;
	}
	
	public void setLastModified(String lastModified) {
		this.lastModified = lastModified;
	}
	
	public String getOwner() {
		return owner;
	}
	
	public void setOwner(String owner) {
		this.owner = owner;
	}
	
	public String getStatus() {
		return status;
	}
	
	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "WcmNode [wcmPath=" + wcmPath + ", name=" + name + ", title=" + title + ", nodeType=" + nodeType
				+ ", lastModified=" + lastModified + ", owner=" + owner + ", status=" + status + "]";
	}
}
