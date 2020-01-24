package com.bpwizard.spring.boot.cluster.i18n;

import com.bpwizard.spring.boot.cluster.common.i18n.I18n;

public class I18nMessages {
	 public static I18n cannotStartJournal;
	 public static I18n cannotStopJournal;
	 private I18nMessages() {
	    }

	    static {
	        try {
	            I18n.initialize(I18nMessages.class);
	        } catch (final Exception err) {
	            // CHECKSTYLE IGNORE check FOR NEXT 1 LINES
	            System.err.println(err);
	        }
	    }
}
