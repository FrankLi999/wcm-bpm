package com.bpwizard.wcm.repo.rest.jcr.model;

import java.util.Arrays;

public class RenderTemplateLayoutColumn {
	private String id;
    private int width;
    private ResourceElementRender elements[];
	
    public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getWidth() {
		return width;
	}
	
	public void setWidth(int width) {
		this.width = width;
	}
	
	public ResourceElementRender[] getElements() {
		return elements;
	}
	
	public void setElements(ResourceElementRender[] elements) {
		this.elements = elements;
	}

	@Override
	public String toString() {
		return "RenderTemplateLayoutColumn [id=" + id + ", width=" + width + ", elements=" + Arrays.toString(elements)
				+ "]";
	}
}
