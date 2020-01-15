package com.bpwizard.wcm.repo.content;

public class ContentServerUtils {
	public static String getBusinessKey(String workflow, String contentId) {
		return String.format("%s-%s", workflow, contentId);
	}
}
