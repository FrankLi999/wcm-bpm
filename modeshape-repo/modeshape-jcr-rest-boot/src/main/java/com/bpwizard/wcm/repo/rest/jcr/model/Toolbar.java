package com.bpwizard.wcm.repo.rest.jcr.model;

public class Toolbar {
	public enum Position {
		above("above"),
		above_static("above-static"),
		above_fixed("above-fixed"),
		below("below"),
		below_static("below-static"),
		below_fixed("below-fixed");
		
		private final String position;
		private Position(String position) {
			this.position = position;
		}
		
		public String getPosition() {
	        return this.position;
	    }
	}
	private boolean customBackgroundColor;
    private String background;
    private boolean hidden;
    private Position position;
	public boolean isCustomBackgroundColor() {
		return customBackgroundColor;
	}
	public void setCustomBackgroundColor(boolean customBackgroundColor) {
		this.customBackgroundColor = customBackgroundColor;
	}
	public String getBackground() {
		return background;
	}
	public void setBackground(String background) {
		this.background = background;
	}
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
		return "Toolbar [customBackgroundColor=" + customBackgroundColor + ", background=" + background + ", hidden="
				+ hidden + ", position=" + position + "]";
	}
}
