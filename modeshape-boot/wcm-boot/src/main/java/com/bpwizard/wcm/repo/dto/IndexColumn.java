package com.bpwizard.wcm.repo.dto;

/*  javax.jcr.PropertyType
 *  STRING
	BINARY
	LONG
	DOUBLE
	DECIMAL
	DATE
	BOOLEAN
	NAME
	PATH
	REFERENCE
	WEAKREFERENCE
	URI
 */
public class IndexColumn {
	String propertyType;
	String columnName;
	public IndexColumn(String columnName, String propertyType) {
		this.columnName = columnName;
		this.propertyType = propertyType;
	}
	public String getPropertyType() {
		return propertyType;
	}
	public void setPropertyType(String propertyType) {
		this.propertyType = propertyType;
	}
	public String getColumnName() {
		return columnName;
	}
	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}
	@Override
	public String toString() {
		return "IndexColumn [propertyType=" + propertyType + ", columnName=" + columnName + "]";
	}
}
