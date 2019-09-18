package com.bpwizard.wcm.repo.rest.jcr.model;

import java.util.Arrays;

public class SidePane {

    private boolean left = true;
    private int width;
	private ResourceViewer viewers[];
	
	public boolean isLeft() {
		return left;
	}
	public void setLeft(boolean left) {
		this.left = left;
	}
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public ResourceViewer[] getViewers() {
		return viewers;
	}
	public void setViewers(ResourceViewer[] viewers) {
		this.viewers = viewers;
	}
	@Override
	public String toString() {
		return "SideNav [isLeft=" + left + ", width=" + width + ", viewers=" + Arrays.toString(viewers)
				+ ", toString()=" + super.toString() + "]";
	}
}
