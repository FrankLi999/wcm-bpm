package com.bpwizard.wcm.repo.rest.jcr.model;

import java.util.Arrays;

import com.bpwizard.wcm.repo.rest.modeshape.model.HasName;

public class Category implements HasName {
	private String name;
	private Category categories[];
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Category[] getCategories() {
		return categories;
	}
	public void setCategories(Category[] categories) {
		this.categories = categories;
	}
	@Override
	public String toString() {
		return "Category [name=" + name + ", categories=" + Arrays.toString(categories) + "]";
	}
}
