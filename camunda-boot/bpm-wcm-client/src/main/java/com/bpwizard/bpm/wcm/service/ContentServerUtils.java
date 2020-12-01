package com.bpwizard.bpm.wcm.service;

public class ContentServerUtils {
	public static String getBusinessKey(String workflow, String contentId) {
		return String.format("%s-%s", workflow, contentId);
	}
}
