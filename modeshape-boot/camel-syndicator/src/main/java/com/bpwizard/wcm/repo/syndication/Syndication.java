package com.bpwizard.wcm.repo.syndication;

import java.sql.Timestamp;

public class Syndication {
	
	private String id;	
	private String name;
	
	private String authoringDBUrl;
	private String authoringDBUser;
	private String authoringDBPassword;
	
	private String renderingDBUrl;
	private String renderingDBUser;
	private String renderingDBPassword;
	
	private Timestamp lastSyndication;
	
	private String repository;
	private String workspace;
	private String library;
	private String importServiceUrl;
	
	private String importUser;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAuthoringDBUrl() {
		return authoringDBUrl;
	}

	public void setAuthoringDBUrl(String authoringDBUrl) {
		this.authoringDBUrl = authoringDBUrl;
	}

	public String getAuthoringDBUser() {
		return authoringDBUser;
	}

	public void setAuthoringDBUser(String authoringDBUser) {
		this.authoringDBUser = authoringDBUser;
	}

	public String getAuthoringDBPassword() {
		return authoringDBPassword;
	}

	public void setAuthoringDBPassword(String authoringDBPassword) {
		this.authoringDBPassword = authoringDBPassword;
	}

	public String getRenderingDBUrl() {
		return renderingDBUrl;
	}

	public void setRenderingDBUrl(String renderingDBUrl) {
		this.renderingDBUrl = renderingDBUrl;
	}

	public String getRenderingDBUser() {
		return renderingDBUser;
	}

	public void setRenderingDBUser(String renderingDBUser) {
		this.renderingDBUser = renderingDBUser;
	}

	public String getRenderingDBPassword() {
		return renderingDBPassword;
	}

	public void setRenderingDBPassword(String renderingDBPassword) {
		this.renderingDBPassword = renderingDBPassword;
	}

	public Timestamp getLastSyndication() {
		return lastSyndication;
	}

	public void setLastSyndication(Timestamp lastSyndication) {
		this.lastSyndication = lastSyndication;
	}

	public String getRepository() {
		return repository;
	}

	public void setRepository(String repository) {
		this.repository = repository;
	}

	public String getWorkspace() {
		return workspace;
	}

	public void setWorkspace(String workspace) {
		this.workspace = workspace;
	}

	public String getImportServiceUrl() {
		return importServiceUrl;
	}
	
	public String getLibrary() {
		return library;
	}

	public void setLibrary(String library) {
		this.library = library;
	}

	public void setImportServiceUrl(String importServiceUrl) {
		this.importServiceUrl = importServiceUrl;
	}

	public String getImportUser() {
		return importUser;
	}

	public void setImportUser(String importUser) {
		this.importUser = importUser;
	}

	@Override
	public String toString() {
		return "Syndication [id=" + id + ", name=" + name + ", authoringDBUrl=" + authoringDBUrl + ", authoringDBUser="
				+ authoringDBUser + ", authoringDBPassword=" + authoringDBPassword + ", renderingDBUrl="
				+ renderingDBUrl + ", renderingDBUser=" + renderingDBUser + ", renderingDBPassword="
				+ renderingDBPassword + ", lastSyndication=" + lastSyndication + ", repository=" + repository
				+ ", workspace=" + workspace + ", library=" + library + ", importServiceUrl=" + importServiceUrl
				+ ", importUser=" + importUser + "]";
	}
}
