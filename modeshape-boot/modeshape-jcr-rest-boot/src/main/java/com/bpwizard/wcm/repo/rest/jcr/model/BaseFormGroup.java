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
	protected boolean isArray = false;
	protected String filedPath;
	private VisbleCondition condition;
	
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
	public boolean isArray() {
		return isArray;
	}
	public void setArray(boolean isArray) {
		this.isArray = isArray;
	}
	public String getFiledPath() {
		return filedPath;
	}
	public void setFiledPath(String filedPath) {
		this.filedPath = filedPath;
	}
	public VisbleCondition getCondition() {
		return condition;
	}
	public void setCondition(VisbleCondition condition) {
		this.condition = condition;
	}
	@Override
	public String toString() {
		return "BaseFormGroup [groupName=" + groupName + ", groupTitle=" + groupTitle + ", isArray=" + isArray
				+ ", filedPath=" + filedPath + ", condition=" + condition + "]";
	}
}
