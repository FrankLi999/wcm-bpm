package org.camunda.bpm.engine.rest.util;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;

public class VariantUtils {

	public static boolean accept(HttpServletRequest request, MediaType mediaType) {
		String acceptHeader = request.getHeader("Accept");
		return StringUtils.hasText(acceptHeader) && acceptHeader.startsWith(mediaType.getType());
	}
	
	public static boolean accept(HttpServletRequest request, String mediaType) {
		String acceptHeader = request.getHeader("Accept");
		return StringUtils.hasText(acceptHeader) && acceptHeader.startsWith(mediaType);
	}
}
