package com.bpwizard.wcm.repo.dto;

import java.util.Arrays;

public class IndexModel {
	
	String name = "controlField";
	String kind = "VALUE";
	String nodeType = "bpw:controlField";
	String provider = "elasticsearch";
	String workspace = "*";
	boolean synchronous = true;
	
	IndexColumn columns[] = new IndexColumn[] {
	    new IndexColumn("", "")
	};
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getKind() {
		return kind;
	}
	public void setKind(String kind) {
		this.kind = kind;
	}
	public String getNodeType() {
		return nodeType;
	}
	public void setNodeType(String nodeType) {
		this.nodeType = nodeType;
	}
	public String getProvider() {
		return provider;
	}
	public void setProvider(String provider) {
		this.provider = provider;
	}
	public String getWorkspace() {
		return workspace;
	}
	public void setWorkspace(String workspace) {
		this.workspace = workspace;
	}
	public IndexColumn[] getColumns() {
		return columns;
	}
	public void setColumns(IndexColumn[] columns) {
		this.columns = columns;
	}
	public boolean isSynchronous() {
		return synchronous;
	}
	public void setSynchronous(boolean synchronous) {
		this.synchronous = synchronous;
	}
	@Override
	public String toString() {
		return "IndexModel [name=" + name + ", kind=" + kind + ", nodeType=" + nodeType + ", provider=" + provider
				+ ", workspace=" + workspace + ", synchronous=" + synchronous + ", columns=" + Arrays.toString(columns)
				+ "]";
	}
}
