package com.bpwizard.wcm.repo.rest.jcr.model;

import com.bpwizard.wcm.repo.rest.modeshape.model.HasName;

public class Theme extends ResourceNode implements HasName {
	private static final long serialVersionUID = 4129668983360076043L;
	private String name;
	private String library;
	private String repositoryName;
	private String workspace;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getRepositoryName() {
		return repositoryName;
	}
	public void setRepositoryName(String repositoryName) {
		this.repositoryName = repositoryName;
	}
	public String getWorkspace() {
		return workspace;
	}
	public void setWorkspace(String workspace) {
		this.workspace = workspace;
	}
	public String getLibrary() {
		return library;
	}
	public void setLibrary(String library) {
		this.library = library;
	}
	@Override
	public String toString() {
		return "Theme [name=" + name + ", library=" + library + ", repositoryName=" + repositoryName + ", workspace="
				+ workspace + ", toString()=" + super.toString() + "]";
	}
}
