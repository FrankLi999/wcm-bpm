package com.bpwizard.wcm.repo.rest.jcr.model;

public class FormColumn {
	private String id;
	private int fxFlex;
    private String[] formControls;

    public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getFxFlex() {
		return fxFlex;
	}

	public void setFxFlex(int fxFlex) {
		this.fxFlex = fxFlex;
	}

	public String[] getFormControls() {
		return formControls;
	}

	public void setFormControls(String[] formControls) {
		this.formControls = formControls;
	}
}
