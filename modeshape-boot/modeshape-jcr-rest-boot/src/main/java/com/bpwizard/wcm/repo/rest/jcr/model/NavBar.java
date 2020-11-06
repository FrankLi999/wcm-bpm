package com.bpwizard.wcm.repo.rest.jcr.model;

public class NavBar {
	
//	public enum Position {
//		left,
//		right,
//		top
//	}
	private String primaryBackground;
	private String secondaryBackground;
	private boolean display;
	private boolean folded;
	//for flutter, the value can be drawer, bottom
    private String position;
    private String variant;
    private String style;
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
	public boolean isDisplay() {
		return display;
	}
	public void setDisplay(boolean display) {
		this.display = display;
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
	
	public String getStyle() {
		return style;
	}
	public void setStyle(String style) {
		this.style = style;
	}
	@Override
	public String toString() {
		return "NavBar [primaryBackground=" + primaryBackground + ", secondaryBackground=" + secondaryBackground
				+ ", display=" + display + ", folded=" + folded + ", position=" + position + ", variant=" + variant
				+ ", style=" + style + "]";
	}
}
