package com.bpwizard.wcm.repo.rest.jcr.model;

import java.util.Arrays;

public class SideNav {

    private boolean isLeft = true;
    private int width;
	private ResourceViewer viewers[];
	
	public boolean isLeft() {
		return isLeft;
	}
	public void setLeft(boolean isLeft) {
		this.isLeft = isLeft;
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
		return "SideNav [isLeft=" + isLeft + ", width=" + width + ", viewers=" + Arrays.toString(viewers) + "]";
	}
}
