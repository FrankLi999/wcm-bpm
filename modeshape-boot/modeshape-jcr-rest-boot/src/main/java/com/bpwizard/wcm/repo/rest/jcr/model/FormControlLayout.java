package com.bpwizard.wcm.repo.rest.jcr.model;

public class FormControlLayout {
	// TODO:
	// Readonly
	// Message Section -> wcmPath
	
	private String title;
	private String hint;
//	private String key;
	private boolean editable = true;
	private boolean expandable = false;
	private boolean expanded = true;
	private boolean richText = false;
	private Integer rows = 3;
	private Integer cols = 50;
	private boolean displayFlex;
	private String flexDirection;
	private String flex;
	private String placeHolder;
	private int listItems; //Number of items of the array listed on the form
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getHint() {
		return hint;
	}
	public void setHint(String hint) {
		this.hint = hint;
	}
//	public String getKey() {
//		return key;
//	}
//	public void setKey(String key) {
//		this.key = key;
//	}
	public boolean isEditable() {
		return editable;
	}
	public void setEditable(boolean editable) {
		this.editable = editable;
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
	public boolean isRichText() {
		return richText;
	}
	public void setRichText(boolean richText) {
		this.richText = richText;
	}
	public Integer getRows() {
		return rows;
	}
	public void setRows(Integer rows) {
		this.rows = rows;
	}
	public Integer getCols() {
		return cols;
	}
	public void setCols(Integer cols) {
		this.cols = cols;
	}
	public boolean isDisplayFlex() {
		return displayFlex;
	}
	public void setDisplayFlex(boolean displayFlex) {
		this.displayFlex = displayFlex;
	}
	public String getFlexDirection() {
		return flexDirection;
	}
	public void setFlexDirection(String flexDirection) {
		this.flexDirection = flexDirection;
	}
	public String getFlex() {
		return flex;
	}
	public void setFlex(String flex) {
		this.flex = flex;
	}
	public String getPlaceHolder() {
		return placeHolder;
	}
	public void setPlaceHolder(String placeHolder) {
		this.placeHolder = placeHolder;
	}
	public int getListItems() {
		return listItems;
	}
	public void setListItems(int listItems) {
		this.listItems = listItems;
	}
	
	@Override
	public String toString() {
		return "FormControlLayout [title=" + title + ", hint=" + hint
				+ ", editable=" + editable + ", expandable=" + expandable + ", expanded=" + expanded + ", richText="
				+ richText + ", rows=" + rows + ", cols=" + cols + ", displayFlex=" + displayFlex + ", flexDirection="
				+ flexDirection + ", flex=" + flex + ", placeHolder=" + placeHolder + ", listItems=" + listItems + "]";
	}
}
