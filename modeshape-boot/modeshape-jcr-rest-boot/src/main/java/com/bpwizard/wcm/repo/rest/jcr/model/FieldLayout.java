package com.bpwizard.wcm.repo.rest.jcr.model;

import java.util.Arrays;

public class FieldLayout {
	
	private String name;
	private String key;
	private String title;
	private boolean multiple;
	private String items;
	
	private boolean displayFlex;
	private int listItems;
	private String flexDirection;
	
	private FieldLayout fieldLayouts[];
	
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
	
	public FieldLayout[] getFieldLayouts() {
		return fieldLayouts;
	}
	
	public void setFieldLayouts(FieldLayout[] fieldLayouts) {
		this.fieldLayouts = fieldLayouts;
	}
	
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	
	public boolean isDisplayFlex() {
		return displayFlex;
	}
	public void setDisplayFlex(boolean displayFlex) {
		this.displayFlex = displayFlex;
	}
	public int getListItems() {
		return listItems;
	}
	public void setListItems(int listItems) {
		this.listItems = listItems;
	}
	public String getFlexDirection() {
		return flexDirection;
	}
	public void setFlexDirection(String flexDirection) {
		this.flexDirection = flexDirection;
	}
	@Override
	public String toString() {
		return "FieldLayout [name=" + name + ", key=" + key + ", title=" + title + ", multiple=" + multiple + ", items="
				+ items + ", fieldLayouts=" + Arrays.toString(fieldLayouts) + "]";
	}
}
