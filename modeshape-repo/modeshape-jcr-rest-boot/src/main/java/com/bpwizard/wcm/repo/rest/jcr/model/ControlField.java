package com.bpwizard.wcm.repo.rest.jcr.model;

import com.bpwizard.wcm.repo.rest.modeshape.model.HasName;

public class ControlField implements HasName {
	private String name;
	private String icon;
	private String title;
	private ControlFieldMetadata controlFieldMetaData[];
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public ControlFieldMetadata[] getControlFieldMetaData() {
		return controlFieldMetaData;
	}

	public void setControlFieldMetaData(ControlFieldMetadata[] controlFieldMetaData) {
		this.controlFieldMetaData = controlFieldMetaData;
	}
}
