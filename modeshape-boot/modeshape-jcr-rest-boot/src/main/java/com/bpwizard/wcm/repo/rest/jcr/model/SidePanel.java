package com.bpwizard.wcm.repo.rest.jcr.model;

public class SidePanel {

	private boolean display;
	public boolean isDisplay() {
		return this.display;
	}
	public void setDisplay(boolean display) {
		this.display = display;
	}

	@Override
	public String toString() {
		return "SidePanel [display=" + display + "]";
	}
}
