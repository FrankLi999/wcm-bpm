package com.bpwizard.wcm.repo.rest.jcr.model;

import java.util.Arrays;

public class FormStep {
	private String stepName;
	private String stepTitle;
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
	public String getStepTitle() {
		return stepTitle;
	}
	public void setStepTitle(String stepTitle) {
		this.stepTitle = stepTitle;
	}
	@Override
	public String toString() {
		return "FormStep [stepName=" + stepName + ", stepTitle=" + stepTitle + ", formGroups="
				+ Arrays.toString(formGroups) + "]";
	}
}
