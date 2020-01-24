package com.bpwizard.wcm.repo.rest.jcr.model;
import java.util.Arrays;

import com.bpwizard.wcm.repo.rest.modeshape.model.HasName;
public class FormControl implements HasName {
	private String name;
	private String title;
	private String fieldPath;
	private String controlName;
	private String[] values;
	private String[] options;
	private String defaultValue;
	private String hint;
	private String dataType = "text";
	private boolean multiple;
	private String relationshipType;
	private String relationshipCardinality;
	private String valditionRegEx;
	private boolean mandatory = false;
	private boolean userSearchable = false;
	private boolean systemIndexed = false;
	private boolean showInList = false;
	private boolean unique = false;
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
	public String getControlName() {
		return controlName;
	}
	public void setControlName(String controlName) {
		this.controlName = controlName;
	}
	public String[] getValues() {
		return values;
	}
	public void setValues(String[] values) {
		this.values = values;
	}
	public String[] getOptions() {
		return options;
	}
	public void setOptions(String[] options) {
		this.options = options;
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
	@Override
	public String toString() {
		return "FormControl [name=" + name + ", title=" + title + ", fieldPath=" + fieldPath + ", controlName="
				+ controlName + ", values=" + Arrays.toString(values) + ", options=" + Arrays.toString(options)
				+ ", defaultValue=" + defaultValue + ", hint=" + hint + ", dataType=" + dataType + ", multiple="
				+ multiple + ", relationshipType=" + relationshipType + ", relationshipCardinality="
				+ relationshipCardinality + ", valditionRegEx=" + valditionRegEx + ", mandatory=" + mandatory
				+ ", userSearchable=" + userSearchable + ", systemIndexed=" + systemIndexed + ", showInList="
				+ showInList + ", unique=" + unique + "]";
	}
	
}
