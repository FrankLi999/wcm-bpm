package com.bpwizard.wcm.repo.rest.jcr.model;

public class StringConstraint {
	private Integer minLength;
	private Integer maxLength;
	private String pattern;
	private String format;
	private String formatMaximum;
	private String formatMinimum;
	private Boolean formatExclusiveMaximum;
	private Boolean formatExclusiveMinimum;
	private String contentMediaType;
	private String contentEncoding;
	private String contentSchema;
	
	public Integer getMinLength() {
		return minLength;
	}
	public void setMinLength(Integer minLength) {
		this.minLength = minLength;
	}
	public Integer getMaxLength() {
		return maxLength;
	}
	public void setMaxLength(Integer maxLength) {
		this.maxLength = maxLength;
	}
	public String getPattern() {
		return pattern;
	}
	public void setPattern(String pattern) {
		this.pattern = pattern;
	}
	public String getFormat() {
		return format;
	}
	public void setFormat(String format) {
		this.format = format;
	}
	public String getFormatMaximum() {
		return formatMaximum;
	}
	public void setFormatMaximum(String formatMaximum) {
		this.formatMaximum = formatMaximum;
	}
	public String getFormatMinimum() {
		return formatMinimum;
	}
	public void setFormatMinimum(String formatMinimum) {
		this.formatMinimum = formatMinimum;
	}
	public Boolean getFormatExclusiveMaximum() {
		return formatExclusiveMaximum;
	}
	public void setFormatExclusiveMaximum(Boolean formatExclusiveMaximum) {
		this.formatExclusiveMaximum = formatExclusiveMaximum;
	}
	public Boolean getFormatExclusiveMinimum() {
		return formatExclusiveMinimum;
	}
	public void setFormatExclusiveMinimum(Boolean formatExclusiveMinimum) {
		this.formatExclusiveMinimum = formatExclusiveMinimum;
	}
	public String getContentMediaType() {
		return contentMediaType;
	}
	public void setContentMediaType(String contentMediaType) {
		this.contentMediaType = contentMediaType;
	}
	public String getContentEncoding() {
		return contentEncoding;
	}
	public void setContentEncoding(String contentEncoding) {
		this.contentEncoding = contentEncoding;
	}
	public String getContentSchema() {
		return contentSchema;
	}
	public void setContentSchema(String contentSchema) {
		this.contentSchema = contentSchema;
	}
	@Override
	public String toString() {
		return "StringConstraint [minLength=" + minLength + ", maxLength=" + maxLength + ", pattern=" + pattern
				+ ", format=" + format + ", formatMaximum=" + formatMaximum + ", formatMinimum=" + formatMinimum
				+ ", formatExclusiveMaximum=" + formatExclusiveMaximum + ", formatExclusiveMinimum="
				+ formatExclusiveMinimum + ", contentMediaType=" + contentMediaType + ", contentEncoding="
				+ contentEncoding + ", contentSchema=" + contentSchema + "]";
	}
}
