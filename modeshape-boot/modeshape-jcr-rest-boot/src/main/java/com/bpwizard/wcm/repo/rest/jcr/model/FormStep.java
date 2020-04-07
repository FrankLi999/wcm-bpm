package com.bpwizard.wcm.repo.rest.jcr.model;

import java.util.Arrays;

public class FormStep {
	private String stepName;
	private String stepTitle;
	private int order;
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
	public int getOrder() {
		return order;
	}
	public void setOrder(int order) {
		this.order = order;
	}
	@Override
	public String toString() {
		return "FormStep [stepName=" + stepName + ", stepTitle=" + stepTitle + ", order=" + order + ", formGroups="
				+ Arrays.toString(formGroups) + ", toString()=" + super.toString() + "]";
	}
}
