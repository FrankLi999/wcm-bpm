package com.bpwizard.wcm.repo.rest.jcr.model;

import org.springframework.util.StringUtils;

import com.fasterxml.jackson.databind.node.ObjectNode;

public class ResourceMixin {
	private String name;
	private String title;
	private String description;
	private String author;
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


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}


	public String getAuthor() {
		return author;
	}


	public void setAuthor(String author) {
		this.author = author;
	}


	protected void toJson(ObjectNode jsonNode) {
		title = StringUtils.hasText(title) ? title : name;
		if (StringUtils.hasText(this.getName())) {
			jsonNode.put("bpw:name", this.getName());
		}
		if (StringUtils.hasText(this.getTitle())) {
			jsonNode.put("bpw:title", this.getTitle());
		}
		
		if (StringUtils.hasText(this.getDescription())) {
			jsonNode.put("bpw:description", this.getDescription());
		}
		
		if (StringUtils.hasText(this.getAuthor())) {
			jsonNode.put("bpw:author", this.getAuthor());
		}
	}


	@Override
	public String toString() {
		return "ResourceMixin [name=" + name + ", title=" + title + ", description=" + description + ", author="
				+ author + "]";
	}


	
}
