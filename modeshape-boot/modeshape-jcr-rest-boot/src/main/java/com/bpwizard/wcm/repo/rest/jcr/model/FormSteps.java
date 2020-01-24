package com.bpwizard.wcm.repo.rest.jcr.model;

import java.util.Arrays;

public class FormSteps extends BaseFormGroup {
	private FormStep[] steps;

	public FormStep[] getSteps() {
		return steps;
	}

	public void setSteps(FormStep[] steps) {
		this.steps = steps;
	}

	@Override
	public String toString() {
		return "FormSteps [steps=" + Arrays.toString(steps) + "]";
	}
}
