package com.bpwizard.wcm.repo.rest.jcr.model;
import java.util.Map;

import com.bpwizard.wcm.repo.rest.modeshape.model.HasName;
public class FormControl implements HasName {
	private String name;
	
	private String fieldName; //For simple/array type with out number, use number starting from 1, such as "1", "1[]". 
	private String controlType; //HTML control type
	private String dataType = "string"; //Json schema type
	private String format = ""; //Json schema type
	private String jcrDataType = "STRING"; // JCR data type
	private boolean multiple = false;
	private boolean useReference = true;
	private boolean autoCreate = false;
	private boolean mandatory = false;
	private boolean userSearchable = false;
	private boolean systemIndexed = false;
	private boolean showInList = false;
	private FormControlLayout formControlLayout;
	private CommonConstraint constraint;
	private ObjectConstraint objectConstraint;
	private StringConstraint stringConstraint;
	private NumberConstraint numberConstraint;
	private ArrayConstraint arrayConstraint;
	private CustomConstraint customConstraint;
	private ConditionalConstraint conditionalConstraint;
	private Map<String, FormControl> notConstraint;
	private Map<String, FormControl> oneOfConstraint;
	private Map<String, FormControl> anyOfConstraint;
	private Map<String, FormControl> allOfConstraint;
	private Map<String, FormControl> formControls;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getFieldName() {
		return fieldName;
	}
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	public String getFormat() {
		return format;
	}
	public void setFormat(String format) {
		this.format = format;
	}
	public String getControlType() {
		return controlType;
	}
	public void setControlType(String controlType) {
		this.controlType = controlType;
	}
	public String getDataType() {
		return dataType;
	}
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	public String getJcrDataType() {
		return jcrDataType;
	}
	public void setJcrDataType(String jcrDataType) {
		this.jcrDataType = jcrDataType;
	}
	public boolean isMultiple() {
		return multiple;
	}
	public void setMultiple(boolean multiple) {
		this.multiple = multiple;
	}
	public boolean isAutoCreate() {
		return autoCreate;
	}
	public void setAutoCreate(boolean autoCreate) {
		this.autoCreate = autoCreate;
	}
	public boolean isMandatory() {
		return mandatory;
	}
	public void setMandatory(boolean mandatory) {
		this.mandatory = mandatory;
	}
	public boolean isUserSearchable() {
		return userSearchable;
	}
	public void setUserSearchable(boolean userSearchable) {
		this.userSearchable = userSearchable;
	}
	public boolean isSystemIndexed() {
		return systemIndexed;
	}
	public void setSystemIndexed(boolean systemIndexed) {
		this.systemIndexed = systemIndexed;
	}
	public boolean isShowInList() {
		return showInList;
	}
	public void setShowInList(boolean showInList) {
		this.showInList = showInList;
	}
	public FormControlLayout getFormControlLayout() {
		return formControlLayout;
	}
	public void setFormControlLayout(FormControlLayout formControlLayout) {
		this.formControlLayout = formControlLayout;
	}
	public CommonConstraint getConstraint() {
		return constraint;
	}
	public void setConstraint(CommonConstraint constraint) {
		this.constraint = constraint;
	}
	public ObjectConstraint getObjectConstraint() {
		return objectConstraint;
	}
	public void setObjectConstraint(ObjectConstraint objectConstraint) {
		this.objectConstraint = objectConstraint;
	}
	public StringConstraint getStringConstraint() {
		return stringConstraint;
	}
	public void setStringConstraint(StringConstraint stringConstraint) {
		this.stringConstraint = stringConstraint;
	}
	public NumberConstraint getNumberConstraint() {
		return numberConstraint;
	}
	public void setNumberConstraint(NumberConstraint numberConstraint) {
		this.numberConstraint = numberConstraint;
	}
	public ArrayConstraint getArrayConstraint() {
		return arrayConstraint;
	}
	public void setArrayConstraint(ArrayConstraint arrayConstraint) {
		this.arrayConstraint = arrayConstraint;
	}
	public CustomConstraint getCustomConstraint() {
		return customConstraint;
	}
	public void setCustomConstraint(CustomConstraint customConstraint) {
		this.customConstraint = customConstraint;
	}
	public ConditionalConstraint getConditionalConstraint() {
		return conditionalConstraint;
	}
	public void setConditionalConstraint(ConditionalConstraint conditionalConstraint) {
		this.conditionalConstraint = conditionalConstraint;
	}
	public Map<String, FormControl> getNotConstraint() {
		return notConstraint;
	}
	public void setNotConstraint(Map<String, FormControl> notConstraint) {
		this.notConstraint = notConstraint;
	}
	public Map<String, FormControl> getOneOfConstraint() {
		return oneOfConstraint;
	}
	public void setOneOfConstraint(Map<String, FormControl> oneOfConstraint) {
		this.oneOfConstraint = oneOfConstraint;
	}
	public Map<String, FormControl> getAnyOfConstraint() {
		return anyOfConstraint;
	}
	public void setAnyOfConstraint(Map<String, FormControl> anyOfConstraint) {
		this.anyOfConstraint = anyOfConstraint;
	}
	public Map<String, FormControl> getAllOfConstraint() {
		return allOfConstraint;
	}
	public void setAllOfConstraint(Map<String, FormControl> allOfConstraint) {
		this.allOfConstraint = allOfConstraint;
	}
	public Map<String, FormControl> getFormControls() {
		return formControls;
	}
	public void setFormControls(Map<String, FormControl> formControls) {
		this.formControls = formControls;
	}
	public boolean isUseReference() {
		return useReference;
	}
	public void setUseReference(boolean useReference) {
		this.useReference = useReference;
	}
	@Override
	public String toString() {
		return "FormControl [name=" + name + ", fieldName=" + fieldName + ", controlType=" + controlType + ", dataType="
				+ dataType + ", format=" + format + ", jcrDataType=" + jcrDataType + ", multiple=" + multiple
				+ ", autoCreate=" + autoCreate + ", mandatory=" + mandatory + ", userSearchable=" + userSearchable
				+ ", systemIndexed=" + systemIndexed + ", showInList=" + showInList + ", formControlLayout="
				+ formControlLayout + ", constraint=" + constraint + ", objectConstraint=" + objectConstraint
				+ ", useReference=" + useReference + ", stringConstraint=" + stringConstraint + ", numberConstraint=" + numberConstraint
				+ ", arrayConstraint=" + arrayConstraint + ", customConstraint=" + customConstraint
				+ ", conditionalConstraint=" + conditionalConstraint + ", notConstraint=" + notConstraint
				+ ", oneOfConstraint=" + oneOfConstraint + ", anyOfConstraint=" + anyOfConstraint + ", allOfConstraint="
				+ allOfConstraint + ", formControls=" + formControls + "]";
	}
}
