package com.bpwizard.wcm.repo.rest.jcr.model;

import java.util.Arrays;
import java.util.Map;

import com.bpwizard.wcm.repo.rest.modeshape.model.HasName;

public class AuthoringTemplate extends ResourceNode implements HasName {
	private String name;
	private String repository;
	private String workspace;
	private String library;
	private String baseResourceType;
	
	private BaseFormGroup[] formGroups;
	private Map<String, FormControl> formControls;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public BaseFormGroup[] getFormGroups() {
		return formGroups;
	}
	public void setFormGroups(BaseFormGroup[] formGroups) {
		this.formGroups = formGroups;
	}
	
	public String getBaseResourceType() {
		return baseResourceType;
	}
	public void setBaseResourceType(String baseResourceType) {
		this.baseResourceType = baseResourceType;
	}
	public Map<String, FormControl> getFormControls() {
		return formControls;
	}
	public void setFormControls(Map<String, FormControl> formControls) {
		this.formControls = formControls;
	}
	public String getRepository() {
		return repository;
	}
	public void setRepository(String repository) {
		this.repository = repository;
	}
	public String getWorkspace() {
		return workspace;
	}
	public void setWorkspace(String workspace) {
		this.workspace = workspace;
	}
	public String getLibrary() {
		return library;
	}
	public void setLibrary(String library) {
		this.library = library;
	}
	@Override
	public String toString() {
		return "AuthoringTemplate [name=" + name + ", repository=" + repository + ", workspace=" + workspace
				+ ", library=" + library + ", baseResourceType=" + baseResourceType + ", formGroups="
				+ Arrays.toString(formGroups) + ", formControls=" + formControls + ", toString()=" + super.toString()
				+ "]";
	}
}
