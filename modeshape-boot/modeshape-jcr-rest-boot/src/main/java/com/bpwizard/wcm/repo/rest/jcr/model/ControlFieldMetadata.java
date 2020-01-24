package com.bpwizard.wcm.repo.rest.jcr.model;

import com.bpwizard.wcm.repo.rest.modeshape.model.HasName;

public class ControlFieldMetadata implements HasName {
	private String name;
	private String title;
	private String controlType;
	private String hintText;
	private boolean required;
	private boolean readonly;
	private String[] selectOptions;

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

	public void setControlType(String controlType) {
		this.controlType = controlType;
	}

	public String getHintText() {
		return hintText;
	}

	public void setHintText(String hintText) {
		this.hintText = hintText;
	}

	public boolean isRequired() {
		return required;
	}

	public void setRequired(boolean required) {
		this.required = required;
	}

	public boolean isReadonly() {
		return readonly;
	}

	public void setReadonly(boolean readonly) {
		this.readonly = readonly;
	}

	public String[] getSelectOptions() {
		return selectOptions;
	}

	public void setSelectOptions(String[] selectOptions) {
		this.selectOptions = selectOptions;
	}
}
