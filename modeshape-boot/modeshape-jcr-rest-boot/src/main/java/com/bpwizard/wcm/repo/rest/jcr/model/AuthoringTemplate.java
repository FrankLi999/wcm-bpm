package com.bpwizard.wcm.repo.rest.jcr.model;

import java.util.Arrays;
import java.util.Map;

import org.modeshape.jcr.api.JcrConstants;
import org.springframework.util.StringUtils;

import com.bpwizard.wcm.repo.rest.JsonUtils;
import com.bpwizard.wcm.repo.rest.modeshape.model.HasName;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class AuthoringTemplate extends ResourceNode implements HasName {
	private String name;
	private String repository;
	private String workspace;
	private String library;
	private String baseResourceType;
	private FormRow propertyRow;
	private AccessControlList contentItemAcl;
	private BaseFormGroup[] elementGroups;
	private String contentItemWorkflow;
	private Map<String, FormControl> elements;
	private Map<String, FormControl> properties;
	
	public AuthoringTemplate() {
	}
	
	public String getContentItemWorkflow() {
		return contentItemWorkflow;
	}

	public void setContentItemWorkflow(String contentItemWorkflow) {
		this.contentItemWorkflow = contentItemWorkflow;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getBaseResourceType() {
		return baseResourceType;
	}
	public void setBaseResourceType(String baseResourceType) {
		this.baseResourceType = baseResourceType;
	}
	public Map<String, FormControl> getElements() {
		return elements;
	}
	public void setElements(Map<String, FormControl> elements) {
		this.elements = elements;
	}
	public Map<String, FormControl> getProperties() {
		return properties;
	}
	public void setProperties(Map<String, FormControl> properties) {
		this.properties = properties;
	}
	public String getRepository() {
		return repository;
	}
	public void setRepository(String repository) {
		this.repository = repository;
	}
	public String getWorkspace() {
		return workspace;
	}
	public void setWorkspace(String workspace) {
		this.workspace = workspace;
	}
	public String getLibrary() {
		return library;
	}
	public void setLibrary(String library) {
		this.library = library;
	}
	public FormRow getPropertyRow() {
		return propertyRow;
	}
	public void setPropertyRow(FormRow propertyRow) {
		this.propertyRow = propertyRow;
	}

	public AccessControlList getContentItemAcl() {
		return contentItemAcl;
	}

	public void setContentItemAcl(AccessControlList contentItemAcl) {
		this.contentItemAcl = contentItemAcl;
	}

	public BaseFormGroup[] getElementGroups() {
		return elementGroups;
	}
	public void setElementGroups(BaseFormGroup[] elementGroups) {
		this.elementGroups = elementGroups;
	}
	
	public JsonNode toJson() {
		ObjectNode jsonNode = JsonUtils.createObjectNode();
		ObjectNode properties = JsonUtils.createObjectNode();
		ObjectNode children = JsonUtils.createObjectNode();
		
		jsonNode.set("children", children);
		jsonNode.set("properties", properties);
		properties.put(JcrConstants.JCR_PRIMARY_TYPE, "bpw:authoringTemplate");
		
		if (StringUtils.hasText(this.getBaseResourceType())) {
			properties.put("bpw:baseResourceType", this.getBaseResourceType());
		}
		super.toJson(properties, children);
		
		if (StringUtils.hasText(this.getContentItemWorkflow())) {
			properties.put("bpw:contentWorkflow", this.getContentItemWorkflow());
		}

		if (this.getContentItemAcl() != null) {
			ObjectNode contentItemAclNode = JsonUtils.createObjectNode();
			children.set("contentItemAcl", contentItemAclNode);
			ObjectNode contentItemAclNodeProperties = JsonUtils.createObjectNode();
			ObjectNode contentItemAclNodeChildren = JsonUtils.createObjectNode();
			contentItemAclNode.set("properties", contentItemAclNodeProperties);
			contentItemAclNode.set("children", contentItemAclNodeChildren);
			contentItemAclNodeProperties.put(JcrConstants.JCR_PRIMARY_TYPE, "bpw:contentItemAcl");
			
			this.addAccessControlEntry(contentItemAclNodeChildren, "onSaveDraft", this.getContentItemAcl().getOnSaveDraftPermissions());
			this.addAccessControlEntry(contentItemAclNodeChildren, "onReivedDraft", this.getContentItemAcl().getOnReviewedDraftPermissions());
			this.addAccessControlEntry(contentItemAclNodeChildren, "onPublish", this.getContentItemAcl().getOnPublishPermissions());
		}
		this.addRow(children, this.getPropertyRow(), "property-group");
		
		ObjectNode elementGroupNode = JsonUtils.createObjectNode();
		ObjectNode elementGroupNodeProperties = JsonUtils.createObjectNode();
		ObjectNode elementGroupNodeChildren = JsonUtils.createObjectNode();
		children.set("element-group", elementGroupNodeProperties);
		elementGroupNode.set("properties", elementGroupNodeProperties);
		elementGroupNode.set("children", elementGroupNodeChildren);
		elementGroupNodeProperties.put(JcrConstants.JCR_PRIMARY_TYPE, "bpw:formGroupFoler");
		
		int groupCount = 0;
		for (BaseFormGroup g : this.getElementGroups()) {
			groupCount++;
			String groupName = StringUtils.hasText(g.getGroupTitle()) ? g.getGroupTitle() : 
				("group" + groupCount);
			String primaryFormGroupType = (g instanceof FormSteps) ? "bpw:formSteps"
					: (g instanceof FormTabs) ? "bpw:formTabs"
							: (g instanceof FormRows) ? "bpw:formRows" : "bpw:formRow";
			
			ObjectNode formGroupNode = JsonUtils.createObjectNode();
			ObjectNode formGroupNodeProperties = JsonUtils.createObjectNode();
			ObjectNode formGroupNodeChildren = JsonUtils.createObjectNode();
	
			formGroupNode.set("properties", formGroupNodeProperties);
			formGroupNode.set("children", formGroupNodeChildren);
			
			formGroupNodeChildren.set(groupName, formGroupNode);
			formGroupNodeProperties.put(JcrConstants.JCR_PRIMARY_TYPE, primaryFormGroupType);

			if (g instanceof FormSteps) {
				this.addSteps(formGroupNodeChildren, ((FormSteps) g).getSteps());
			} else if (g instanceof FormTabs) {
				this.addTabs(formGroupNodeChildren, ((FormTabs) g).getTabs());
			} else if (g instanceof FormRows) {
				this.addRows(formGroupNodeChildren, ((FormRows) g).getRows());
			}
		}
		
		ObjectNode elementFolderNode = JsonUtils.createObjectNode();
		ObjectNode elementFolderNodeProperties = JsonUtils.createObjectNode();
		ObjectNode elementFolderNodeChildren = JsonUtils.createObjectNode();

		elementFolderNode.set("properties", elementFolderNodeProperties);
		elementFolderNode.set("children", elementFolderNodeChildren);
		children.set("elements", elementFolderNode);
		elementFolderNodeProperties.put(JcrConstants.JCR_PRIMARY_TYPE, "bpw:elementFolder");
		for (String controlName : this.getElements().keySet()) {
			this.addControl(elementFolderNodeChildren, this.getElements().get(controlName));
		}
		
		
		ObjectNode propertiesFolder = JsonUtils.createObjectNode();
		ObjectNode propertiesFolderProperties = JsonUtils.createObjectNode();
		ObjectNode propertiesFolderChildren = JsonUtils.createObjectNode();

		propertiesFolder.set("properties", propertiesFolderProperties);
		propertiesFolder.set("children", propertiesFolderChildren);
		children.set("properties", propertiesFolder);
		propertiesFolderProperties.put(JcrConstants.JCR_PRIMARY_TYPE, "bpw:propertyFolder");
		for (String controlName : this.getProperties().keySet()) {
			this.addControl(propertiesFolderChildren, this.getElements().get(controlName));
		}
		
		return jsonNode;
	}
	
	private void addControl(ObjectNode elementFolder, FormControl control) {
		
		ObjectNode controlNode = JsonUtils.createObjectNode();
		ObjectNode controlNodeProperties = JsonUtils.createObjectNode();


		controlNode.set("properties", controlNodeProperties);
		controlNodeProperties.put(JcrConstants.JCR_PRIMARY_TYPE, "bpw:formControl");
		

		if (StringUtils.hasText(control.getTitle())) {
			controlNodeProperties.put("bpw:title", control.getTitle());
		}

		if (StringUtils.hasText(control.getFieldPath())) {
			controlNodeProperties.put("bpw:fieldPath", control.getFieldPath());
		}
		
		if (StringUtils.hasText(control.getControlName())) {
			controlNodeProperties.put("bpw:controlName", control.getControlName());
		}

		if ((control.getValues() != null) && (control.getValues().length > 0)) {
			ArrayNode valueArray = JsonUtils.creatArrayNode();
			for (String value : control.getValues()) {
				valueArray.add(value);
			}
			controlNodeProperties.set("bpw:value", valueArray);
		}

		if ((control.getOptions() != null) && (control.getOptions().length > 0)) {
			ArrayNode optionArray = JsonUtils.creatArrayNode();
			for (String option : control.getOptions()) {
				optionArray.add( option);
			}
			controlNodeProperties.set("bpw:options", optionArray);
		}

		if (StringUtils.hasText(control.getDefaultValue())) {
			controlNodeProperties.put("bpw:defaultValue", control.getDefaultValue());
		}

		if (StringUtils.hasText(control.getHint())) {
			controlNodeProperties.put("bpw:hint", control.getHint());
		}

		if (StringUtils.hasText(control.getDataType())) {
			controlNodeProperties.put("bpw:dataType", control.getDataType());
		}

		if (StringUtils.hasText(control.getRelationshipType())) {
			controlNodeProperties.put("bpw:relationshipType", control.getRelationshipType());
		}

		if (StringUtils.hasText(control.getRelationshipCardinality())) {
			controlNodeProperties.put("bpw:relationshipCardinality", control.getRelationshipCardinality());
		}

		if (StringUtils.hasText(control.getValditionRegEx())) {
			controlNodeProperties.put("bpw:valditionRegEx", control.getValditionRegEx());
		}

		controlNodeProperties.put("bpw:mandatory", control.isMandatory());
		controlNodeProperties.put("bpw:userSearchable", control.isUserSearchable());
		controlNodeProperties.put("bpw:systemIndexed", control.isSystemIndexed());
		controlNodeProperties.put("bpw:showInList", control.isShowInList());
		controlNodeProperties.put("bpw:unique", control.isUnique());
	}
	
	private void addSteps(ObjectNode stepsNode, FormStep[] steps) {
		for (FormStep step : steps) {			
			ObjectNode stepNode = JsonUtils.createObjectNode();
			ObjectNode stepNodeProperties = JsonUtils.createObjectNode();
			ObjectNode stepNodeChildren = JsonUtils.createObjectNode();
			stepsNode.set(step.getStepName(), stepNode);
			stepNode.set("properties", stepNodeProperties);
			stepNode.set("children", stepNodeChildren);
			stepNodeProperties.put(JcrConstants.JCR_PRIMARY_TYPE, "bpw:formStep");

			String stepTitle = StringUtils.hasText(step.getStepTitle()) ? step.getStepTitle() : step.getStepName();
			if (StringUtils.hasText(stepTitle)) {
				stepNodeProperties.put("bpw:stepName", stepTitle);
			}
			int rowCount = 0;
			for (BaseFormGroup row : step.getFormGroups()) {
				this.addRow(stepNodeChildren, (FormRow) row, "row" + rowCount++);
			}
		}
	}

	private void addTabs(ObjectNode tabsNode, FormTab[] tabs) {
		for (FormTab tab : tabs) {
			ObjectNode tabNode = JsonUtils.createObjectNode();
			ObjectNode tabNodeProperties = JsonUtils.createObjectNode();
			ObjectNode tabNodeChildren = JsonUtils.createObjectNode();
			tabsNode.set(tab.getTabName(), tabNode);
			tabNode.set("properties", tabNodeProperties);
			tabNode.set("children", tabNodeChildren);
			tabNodeProperties.put(JcrConstants.JCR_PRIMARY_TYPE, "bpw:formTab");

			String tabTitle = StringUtils.hasText(tab.getTabTitle()) ? tab.getTabTitle() : tab.getTabName();
			if (StringUtils.hasText(tabTitle)) {
				tabNodeProperties.put("bpw:tabName", tabTitle);
			}
			int rowCount = 0;
			for (BaseFormGroup row : tab.getFormGroups()) {
				this.addRow(tabNodeChildren, (FormRow) row, "row" + rowCount++);
			}
		}
	}

	private void addRows(ObjectNode rowsNode, FormRow[] rows) {
		int rowCount = 0;
		for (FormRow row : rows) {
			this.addRow(rowsNode, (FormRow) row, "row" + rowCount++);
		}
	}
	
	private void addRow(ObjectNode groupNode, FormRow row, String rowName) {
		ObjectNode rowNode = JsonUtils.createObjectNode();
		ObjectNode rowNodeProperties = JsonUtils.createObjectNode();
		ObjectNode rowNodeChildren = JsonUtils.createObjectNode();
		groupNode.set(rowName, rowNode);
		rowNode.set("properties", rowNodeProperties);
		rowNode.set("children", rowNodeChildren);
		rowNodeProperties.put(JcrConstants.JCR_PRIMARY_TYPE, "bpw:formRow");
		
		String rowTitle = StringUtils.hasText(row.getRowTitle()) ? row.getRowTitle() : row.getRowName();
		if (StringUtils.hasText(rowTitle)) {
			rowNodeProperties.put("bpw:rowName", rowTitle);
		}
		int colCount = 0;
		for (FormColumn col : row.getColumns()) {
			String columnName = StringUtils.hasText(col.getId()) ? col.getId() : "column" + colCount;
			this.addColumn(rowNodeChildren, col, columnName);
		}
	}
	
	private void addColumn(ObjectNode rowNode, FormColumn column, String columnNameName) {
		
		ObjectNode columnNode = JsonUtils.createObjectNode();
		ObjectNode columnNodeProperties = JsonUtils.createObjectNode();
		//ObjectNode rowNodeChildren = JsonUtils.createObjectNode();
		rowNode.set(columnNameName, columnNode);
		columnNode.set("properties", columnNodeProperties);
		columnNodeProperties.put(JcrConstants.JCR_PRIMARY_TYPE, "bpw:formColumn");
		
		columnNodeProperties.put("bpw:fxFlex", column.getFxFlex());
		columnNodeProperties.put("bpw:id", columnNameName);
		
		if (column.getFormControls() != null && column.getFormControls().length > 0) {
			ArrayNode controlArray = JsonUtils.creatArrayNode();
			for (String cn : column.getFormControls()) {
				controlArray.add(cn);
			}
			columnNodeProperties.set("bpw:fieldNames", controlArray);
		}
	}

	@Override
	public String toString() {
		return "AuthoringTemplate [name=" + name + ", repository=" + repository + ", workspace=" + workspace
				+ ", library=" + library + ", baseResourceType=" + baseResourceType + ", propertyRow=" + propertyRow
				+ ", contentItemAcl=" + contentItemAcl + ", elementGroups=" + Arrays.toString(elementGroups)
				+ ", elements=" + elements + ", properties=" + properties + "]";
	}
}
