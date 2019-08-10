package com.bpwizard.wcm.repo.rest.jcr.model;

public class FieldLayout {
	
	private String name;
	private String title;
	private boolean multiple;
	private String items;
	
	public String getItems() {
		return items;
	}
	public void setItems(String items) {
		this.items = items;
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
	public boolean isMultiple() {
		return multiple;
	}
	public void setMultiple(boolean multiple) {
		this.multiple = multiple;
	}
	@Override
	public String toString() {
		return "FieldLayout [name=" + name + ", title=" + title + ", multiple=" + multiple + ", items=" + items + "]";
	}
}
