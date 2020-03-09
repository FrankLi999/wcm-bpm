package com.bpwizard.wcm.repo.rest.jcr.model;
import java.util.Arrays;

import com.bpwizard.wcm.repo.rest.modeshape.model.HasName;
public class FormControl implements HasName {
	private String name;
	private String title;
	private String fieldPath;
	private String controlType;
	private String[] values;
	private String[] enumeration;
	private String defaultValue;
	private String hint;
	private String dataType = "text";
	private String jcrDataType = "STRING";
	private boolean multiple;
	private String relationshipType;
	private String relationshipCardinality;
	private String valditionRegEx;
	private boolean mandatory = false;
	private boolean userSearchable = false;
	private boolean systemIndexed = false;
	private boolean showInList = false;
	private boolean unique = false;
	private boolean editable = false;
	private boolean expandable = false;
	private boolean richText = false;
	private int rows = 3;
	private String flex;
	private String placeholder;
	
	public String[] getEnumeration() {
		return enumeration;
	}
	public void setEnumeration(String[] enumeration) {
		this.enumeration = enumeration;
	}
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
	public String getControlType() {
		return controlType;
	}
	public void setControlType(String controlName) {
		this.controlType = controlName;
	}
	public String[] getValues() {
		return values;
	}
	public void setValues(String[] values) {
		this.values = values;
	}

	public String getDefaultValue() {
		return defaultValue;
	}
	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}
	public String getHint() {
		return hint;
	}
	public void setHint(String hint) {
		this.hint = hint;
	}
	public String getDataType() {
		return dataType;
	}
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	public boolean isMultiple() {
		return multiple;
	}
	public void setMultiple(boolean multiple) {
		this.multiple = multiple;
	}
	public String getRelationshipType() {
		return relationshipType;
	}
	public void setRelationshipType(String relationshipType) {
		this.relationshipType = relationshipType;
	}
	public String getRelationshipCardinality() {
		return relationshipCardinality;
	}
	public void setRelationshipCardinality(String relationshipCardinality) {
		this.relationshipCardinality = relationshipCardinality;
	}
	public String getValditionRegEx() {
		return valditionRegEx;
	}
	public void setValditionRegEx(String valditionRegEx) {
		this.valditionRegEx = valditionRegEx;
	}
	public boolean isMandatory() {
		return mandatory;
	}
	public void setMandatory(boolean mandatory) {
		this.mandatory = mandatory;
	}
	public boolean isUserSearchable() {
		return userSearchable;
	}
	public void setUserSearchable(boolean userSearchable) {
		this.userSearchable = userSearchable;
	}
	public boolean isSystemIndexed() {
		return systemIndexed;
	}
	public void setSystemIndexed(boolean systemIndexed) {
		this.systemIndexed = systemIndexed;
	}
	public boolean isShowInList() {
		return showInList;
	}
	public void setShowInList(boolean showInList) {
		this.showInList = showInList;
	}
	public boolean isUnique() {
		return unique;
	}
	public void setUnique(boolean unique) {
		this.unique = unique;
	}
	
	public String getFieldPath() {
		return fieldPath;
	}
	public void setFieldPath(String fieldPath) {
		this.fieldPath = fieldPath;
	}
	
	public String getJcrDataType() {
		return jcrDataType;
	}
	public void setJcrDataType(String jcrDataType) {
		this.jcrDataType = jcrDataType;
	}
	
	public boolean isRichText() {
		return richText;
	}
	public void setRichText(boolean richText) {
		this.richText = richText;
	}
	
	public int getRows() {
		return rows;
	}
	public void setRows(int rows) {
		this.rows = rows;
	}
	public String getFlex() {
		return flex;
	}
	public void setFlex(String flex) {
		this.flex = flex;
	}
	public String getPlaceholder() {
		return placeholder;
	}
	
	public void setPlaceholder(String placeholder) {
		this.placeholder = placeholder;
	}
	
	@Override
	public String toString() {
		return "FormControl [name=" + name + ", title=" + title + ", fieldPath=" + fieldPath + ", controlType="
				+ controlType + ", values=" + Arrays.toString(values) + ", enumeration=" + Arrays.toString(enumeration)
				+ ", defaultValue=" + defaultValue + ", hint=" + hint + ", dataType=" + dataType + ", jcrDataType="
				+ jcrDataType + ", multiple=" + multiple + ", relationshipType=" + relationshipType
				+ ", relationshipCardinality=" + relationshipCardinality + ", valditionRegEx=" + valditionRegEx
				+ ", mandatory=" + mandatory + ", userSearchable=" + userSearchable + ", systemIndexed=" + systemIndexed
				+ ", showInList=" + showInList + ", unique=" + unique + ", editable=" + editable + ", expandable="
				+ expandable + ", richText=" + richText + ", rows=" + rows + ", flex=" + flex + ", placeholder="
				+ placeholder + "]";
	}
}
