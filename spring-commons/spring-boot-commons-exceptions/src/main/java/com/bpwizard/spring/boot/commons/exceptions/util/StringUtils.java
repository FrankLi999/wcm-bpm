package com.bpwizard.spring.boot.commons.exceptions.util;

public class StringUtils {
	public static String substringAfter(final String str, final String separator) {
        if (!org.springframework.util.StringUtils.hasText(str)) {
            return str;
        }
        final int pos = str.indexOf(separator);
        if (pos == -1) {
            return "";
        }
        return str.substring(pos + 1);
    }
}
