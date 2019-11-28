package com.bpwizard.wcm.repo.rest.jcr.model;

public class NavBar {
	
//	public enum Position {
//		left,
//		right,
//		top
//	}
	private String primaryBackground;
	private String secondaryBackground;
	private boolean hidden;
	private boolean folded;
    private String position;
    private String variant;
	public String getPrimaryBackground() {
		return primaryBackground;
	}
	public void setPrimaryBackground(String primaryBackground) {
		this.primaryBackground = primaryBackground;
	}
	public String getSecondaryBackground() {
		return secondaryBackground;
	}
	public void setSecondaryBackground(String secondaryBackground) {
		this.secondaryBackground = secondaryBackground;
	}
	public boolean isHidden() {
		return hidden;
	}
	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}
	public boolean isFolded() {
		return folded;
	}
	public void setFolded(boolean folded) {
		this.folded = folded;
	}
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	public String getVariant() {
		return variant;
	}
	public void setVariant(String variant) {
		this.variant = variant;
	}
	
	@Override
	public String toString() {
		return "NavBar [primaryBackground=" + primaryBackground + ", secondaryBackground=" + secondaryBackground
				+ ", hidden=" + hidden + ", folded=" + folded + ", position=" + position + ", variant=" + variant + "]";
	}
}
