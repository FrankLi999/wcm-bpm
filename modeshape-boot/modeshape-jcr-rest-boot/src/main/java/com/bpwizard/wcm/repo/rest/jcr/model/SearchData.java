package com.bpwizard.wcm.repo.rest.jcr.model;

import java.util.Arrays;

public class SearchData {
	private String description;
	private String[] keywords;
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String[] getKeywords() {
		return keywords;
	}
	public void setKeywords(String[] keywords) {
		this.keywords = keywords;
	}
	
	@Override
	public String toString() {
		return "SearchData [description=" + description + ", keywords=" + Arrays.toString(keywords) + "]";
	}
}
