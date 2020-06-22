package com.bpwizard.spring.boot.commons.exceptions.util;

import java.util.Locale;

import org.springframework.context.i18n.LocaleContext;
import org.springframework.context.i18n.SimpleLocaleContext;
import org.springframework.core.NamedInheritableThreadLocal;
import org.springframework.lang.Nullable;

public class SpringLocaleHolder {
	private static final ThreadLocal<LocaleContext> localeContextHolder =
			new NamedInheritableThreadLocal<>("SpringLocaleContext");
	
	public static void setLocale(@Nullable Locale locale) {
		LocaleContext localeContext = new SimpleLocaleContext(locale);
		setLocaleContext(localeContext);
	}
	
	
	
	public static Locale getLocale() {
		return getLocale(getLocaleContext());
	}
	
	private static void setLocaleContext(@Nullable LocaleContext localeContext) {
		localeContextHolder.set(localeContext);
	}
	
	@Nullable
	private static LocaleContext getLocaleContext() {		
		return localeContextHolder.get();		
	}
	
	private static Locale getLocale(@Nullable LocaleContext localeContext) {
		if (localeContext!= null) {
			Locale locale = localeContext.getLocale();
			if (locale != null) {
				return locale;
			}
		}
		return Locale.getDefault();
	}
}
