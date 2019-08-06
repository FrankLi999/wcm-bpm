package com.bpwizard.wcm.repo.rest.jcr.model;

import java.util.Arrays;

public class LayoutColumn {
    private int width;
    private ResourceViewer viewers[];
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
		return "LayoutColumn [width=" + width + ", viewers=" + Arrays.toString(viewers) + "]";
	}
}
