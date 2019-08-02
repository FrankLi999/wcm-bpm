package com.bpwizard.wcm.repo.rest.jcr.model;

public class FormStep {
	private String stepName;
	private BaseFormGroup[] formGroups;
	public String getStepName() {
		return stepName;
	}
	public void setStepName(String stepName) {
		this.stepName = stepName;
	}
	public BaseFormGroup[] getFormGroups() {
		return formGroups;
	}
	public void setFormGroups(BaseFormGroup[] formGroups) {
		this.formGroups = formGroups;
	}
	
	
}
