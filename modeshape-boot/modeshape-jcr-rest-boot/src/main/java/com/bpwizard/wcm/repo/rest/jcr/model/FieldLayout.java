package com.bpwizard.wcm.repo.rest.jcr.model;

import java.util.Arrays;

public class FieldLayout {
	
	private String name;
	private String key;
	private String title;
	private boolean multiple;
	private String items;
	
	private String placeHolder;
	private String flex;
	private boolean displayFlex;
	private int listItems;
	private String flexDirection;
	
	private boolean expandable = false;
	private boolean expanded = true;
	
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
	public String getPlaceHolder() {
		return placeHolder;
	}
	public void setPlaceHolder(String placeHolder) {
		this.placeHolder = placeHolder;
	}
	public String getFlex() {
		return flex;
	}
	public void setFlex(String flex) {
		this.flex = flex;
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
	public boolean isExpandable() {
		return expandable;
	}
	public void setExpandable(boolean expandable) {
		this.expandable = expandable;
	}
	public boolean isExpanded() {
		return expanded;
	}
	public void setExpanded(boolean expanded) {
		this.expanded = expanded;
	}
	@Override
	public String toString() {
		return "FieldLayout [name=" + name + ", key=" + key + ", title=" + title + ", multiple=" + multiple + ", items="
				+ items + ", placeHolder=" + placeHolder + ", flex=" + flex + ", displayFlex=" + displayFlex
				+ ", listItems=" + listItems + ", flexDirection=" + flexDirection + ", expandable=" + expandable
				+ ", expanded=" + expanded + ", fieldLayouts=" + Arrays.toString(fieldLayouts) + ", toString()="
				+ super.toString() + "]";
	}
}
