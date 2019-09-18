package com.bpwizard.wcm.repo.rest.jcr.model;

public class SidePanel {
	public enum Position {
		left,
		right
	}
	private boolean hidden;
    private Position position;
	public boolean isHidden() {
		return hidden;
	}
	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}
	public Position getPosition() {
		return position;
	}
	public void setPosition(Position position) {
		this.position = position;
	}
	@Override
	public String toString() {
		return "SidePanel [hidden=" + hidden + ", position=" + position + "]";
	}
}
