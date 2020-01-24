package com.bpwizard.wcm.repo.rest.jcr.model;

import java.util.Arrays;

import com.bpwizard.wcm.repo.rest.modeshape.model.HasName;

public class WcmWorkspace implements HasName { //implements JSONAble {
    private String name;
    private WcmLibrary libraries[];
	public String getName() {
		return name;
	}
	
	public WcmLibrary[] getLibraries() {
		return libraries;
	}
	public void setLibraries(WcmLibrary[] libraries) {
		this.libraries = libraries;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Override
	public String toString() {
		return "WcmWorkspace [name=" + name + ", libraries="
				+ Arrays.toString(libraries) + "]";
	}
}
