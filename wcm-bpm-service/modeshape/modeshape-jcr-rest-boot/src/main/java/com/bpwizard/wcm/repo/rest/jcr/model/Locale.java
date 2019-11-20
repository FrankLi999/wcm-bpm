package com.bpwizard.wcm.repo.rest.jcr.model;

public class Locale {
    private String lang;
	private String data;
	
	public String getLang() {
		return lang;
	}
	public void setLang(String lang) {
		this.lang = lang;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	
	@Override
	public String toString() {
		return "Locale [lang=" + lang + ", data=" + data + "]";
	}
}
