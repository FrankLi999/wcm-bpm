package com.bpwizard.wcm.repo.rest.jcr.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

//import com.fasterxml.jackson.annotation.JsonSubTypes;
//import com.fasterxml.jackson.annotation.JsonTypeInfo;

//@JsonTypeInfo(use= JsonTypeInfo.Id.CLASS, include= JsonTypeInfo.As.PROPERTY, property="groupType")
//@JsonSubTypes({
//        @JsonSubTypes.Type(value=TabsFormGroup.class, name = "Tabs"),
//        @JsonSubTypes.Type(value=StepsFormGroup.class, name = "Steps"),
//        @JsonSubTypes.Type(value=RowsFormGroup.class, name = "Rows"),
//        @JsonSubTypes.Type(value=TabsFormGroup.class, name = "Row")
//})
@JsonDeserialize(using = FormGroupDeserializer.class)
public abstract class BaseFormGroup {
	protected String groupName;
	protected String groupTitle;
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public String getGroupTitle() {
		return groupTitle;
	}
	public void setGroupTitle(String groupTitle) {
		this.groupTitle = groupTitle;
	}
	@Override
	public String toString() {
		return "BaseFormGroup [groupName=" + groupName + ", groupTitle=" + groupTitle + "]";
	}
}
