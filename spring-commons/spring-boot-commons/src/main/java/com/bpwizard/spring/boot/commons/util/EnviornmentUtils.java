package com.bpwizard.spring.boot.commons.util;

import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;

public class EnviornmentUtils {
	public static long getLong(Environment env, String property, long defaultValue) {
		String stringValue = env.getProperty(property);
		return StringUtils.hasText(stringValue)? Long.parseLong(stringValue) : defaultValue;
	}
	
	public static int getInt(Environment env, String property, int defaultValue) {
		String stringValue = env.getProperty(property);
		return StringUtils.hasText(stringValue)? Integer.parseInt(stringValue) : defaultValue;
	}
}
