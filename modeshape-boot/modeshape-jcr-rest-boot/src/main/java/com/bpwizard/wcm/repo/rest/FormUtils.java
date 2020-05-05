package com.bpwizard.wcm.repo.rest;

import java.util.Map;

import org.springframework.util.StringUtils;

import com.bpwizard.wcm.repo.rest.jcr.model.FormControl;

public class FormUtils {
	public static String layoutPath(String fieldPath, Map<String, FormControl> formControls) {
		String jsonPath[] = fieldPath.split("\\.");
		StringBuilder layoutPath = new StringBuilder();
		FormControl formControl = null;
		int i = 0;
		boolean isParentNodeArray = false;
		for (;(i < jsonPath.length -  1) && (formControls != null); i++) {
			formControl = formControls.get(jsonPath[i]);
			formControls = formControl.getFormControls();
			String controlFieldName = StringUtils.hasText(formControl.getFieldName()) ? 
					formControl.getFieldName() : "1";
			if (formControl.isMultiple()) {
				if (isParentNodeArray) { 
					//Multi dimentional array
					layoutPath.append("[]");
				} else {
					FormUtils.appendControlField(i, controlFieldName, layoutPath, true);
				}
				isParentNodeArray = true;
			} else {
				FormUtils.appendControlField(i, controlFieldName, layoutPath, false);
				isParentNodeArray = false;
			}
		}
			
		return (i == jsonPath.length) ? layoutPath.toString() : fieldPath;
	}
	
	private static void appendControlField(int position, String controlFieldName, StringBuilder layoutPath, boolean isArray) {
		if (position > 0) {
			layoutPath.append(".");
		}
		layoutPath.append(controlFieldName);
		if (isArray) {
			layoutPath.append("[]");
		}
	}
}
