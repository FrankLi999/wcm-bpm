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
	private static final long serialVersionUID = 1L;
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
		ObjectNode children = JsonUtils.createObjectNode();
		
		jsonNode.set("children", children);
		jsonNode.put(JcrConstants.JCR_PRIMARY_TYPE, "bpw:authoringTemplate");
		
		if (StringUtils.hasText(this.getBaseResourceType())) {
			jsonNode.put("bpw:baseResourceType", this.getBaseResourceType());
		}
		super.toJson(jsonNode, children);
		
		if (StringUtils.hasText(this.getContentItemWorkflow())) {
			jsonNode.put("bpw:contentWorkflow", this.getContentItemWorkflow());
		}

		if (this.getContentItemAcl() != null) {
			ObjectNode contentItemAclNode = JsonUtils.createObjectNode();
			children.set("contentItemAcl", contentItemAclNode);
			ObjectNode contentItemAclNodeChildren = JsonUtils.createObjectNode();
			contentItemAclNode.set("children", contentItemAclNodeChildren);
			contentItemAclNode.put(JcrConstants.JCR_PRIMARY_TYPE, "bpw:contentItemAcl");
			
			this.addAccessControlEntry(contentItemAclNodeChildren, "onSaveDraft", this.getContentItemAcl().getOnSaveDraftPermissions());
			this.addAccessControlEntry(contentItemAclNodeChildren, "onReivedDraft", this.getContentItemAcl().getOnReviewedDraftPermissions());
			this.addAccessControlEntry(contentItemAclNodeChildren, "onPublish", this.getContentItemAcl().getOnPublishPermissions());
		}
		if (this.propertyRow != null && this.propertyRow.getColumns().length > 0) {
			this.addRow(children, this.getPropertyRow(), "propertyGroup");
		}
		
		if (this.elementGroups != null && this.elementGroups.length > 0) {
			ObjectNode elementGroupNode = JsonUtils.createObjectNode();
			ObjectNode elementGroupNodeChildren = JsonUtils.createObjectNode();
			children.set("elementGroups", elementGroupNode);
			elementGroupNode.set("children", elementGroupNodeChildren);
			elementGroupNode.put(JcrConstants.JCR_PRIMARY_TYPE, "bpw:formGroupFoler");
			
			int groupCount = 0;
			for (BaseFormGroup g : this.getElementGroups()) {
				groupCount++;
				String groupName = StringUtils.hasText(g.getGroupTitle()) ? g.getGroupTitle() : 
					("group" + groupCount);
				String primaryFormGroupType = (g instanceof FormSteps) ? "bpw:formSteps"
						: (g instanceof FormTabs) ? "bpw:formTabs"
								: (g instanceof FormRows) ? "bpw:formRows" : "bpw:formRow";
				
				ObjectNode formGroupNode = JsonUtils.createObjectNode();
				ObjectNode formGroupNodeChildren = JsonUtils.createObjectNode();
		
				formGroupNode.set("children", formGroupNodeChildren);
				
				elementGroupNodeChildren.set(groupName, formGroupNode);
				formGroupNode.put(JcrConstants.JCR_PRIMARY_TYPE, primaryFormGroupType);
	
				if (g instanceof FormSteps) {
					this.addSteps(formGroupNodeChildren, ((FormSteps) g).getSteps());
				} else if (g instanceof FormTabs) {
					this.addTabs(formGroupNodeChildren, ((FormTabs) g).getTabs());
				} else if (g instanceof FormRows) {
					this.addRows(formGroupNodeChildren, ((FormRows) g).getRows());
				}
			}
		}
		
		if (this.elements != null && this.elements.size() > 0 ) {
			ObjectNode elementFolderNode = JsonUtils.createObjectNode();
			ObjectNode elementFolderNodeChildren = JsonUtils.createObjectNode();
	
			elementFolderNode.set("children", elementFolderNodeChildren);
			children.set("elements", elementFolderNode);
			elementFolderNode.put(JcrConstants.JCR_PRIMARY_TYPE, "bpw:elementFolder");
			for (String controlName : this.getElements().keySet()) {
				this.addControl(elementFolderNodeChildren, this.getElements().get(controlName));
			}		
		}
		
		if (this.properties != null && this.properties.size() > 0 ) {
			ObjectNode propertiesFolder = JsonUtils.createObjectNode();
			ObjectNode propertiesFolderChildren = JsonUtils.createObjectNode();
	
			propertiesFolder.set("children", propertiesFolderChildren);
			children.set("properties", propertiesFolder);
			propertiesFolder.put(JcrConstants.JCR_PRIMARY_TYPE, "bpw:propertyFolder");
			for (String controlName : this.getProperties().keySet()) {
				this.addControl(propertiesFolderChildren, this.getProperties().get(controlName));
			}
		}
		return jsonNode;
	}
	
	private void addControl(ObjectNode elementFolder, FormControl control) {
		
		ObjectNode controlNode = JsonUtils.createObjectNode();
		elementFolder.set(control.getName(), controlNode);

		controlNode.put(JcrConstants.JCR_PRIMARY_TYPE, "bpw:formControl");
		if (StringUtils.hasText(control.getTitle())) {
			controlNode.put("bpw:title", control.getTitle());
		}

		if (StringUtils.hasText(control.getJsonPath())) {
			controlNode.put("bpw:jsonPath", control.getJsonPath());
		}
		
		if (StringUtils.hasText(control.getControlType())) {
			controlNode.put("bpw:controlType", control.getControlType());
		}

		if (StringUtils.hasText(control.getFormat())) {
			controlNode.put("bpw:format", control.getFormat());
		}
		
		if ((control.getEnumeration() != null) && (control.getEnumeration().length > 0)) {
			ArrayNode optionArray = JsonUtils.creatArrayNode();
			for (String option : control.getEnumeration()) {
				optionArray.add(option);
			}
			controlNode.set("bpw:enum", optionArray);
		}

		if (StringUtils.hasText(control.getDefaultValue())) {
			controlNode.put("bpw:defaultValue", control.getDefaultValue());
		}

		if (StringUtils.hasText(control.getHint())) {
			controlNode.put("bpw:hint", control.getHint());
		}

		if (StringUtils.hasText(control.getDataType())) {
			controlNode.put("bpw:dataType", control.getDataType());
		}

		if (StringUtils.hasText(control.getRelationshipType())) {
			controlNode.put("bpw:relationshipType", control.getRelationshipType());
		}

		if (StringUtils.hasText(control.getRelationshipCardinality())) {
			controlNode.put("bpw:relationshipCardinality", control.getRelationshipCardinality());
		}

		if (StringUtils.hasText(control.getValdition())) {
			controlNode.put("bpw:valdition", control.getValdition());
		}

		controlNode.put("bpw:mandatory", control.isMandatory());
		controlNode.put("bpw:userSearchable", control.isUserSearchable());
		controlNode.put("bpw:systemIndexed", control.isSystemIndexed());
		controlNode.put("bpw:showInList", control.isShowInList());
		controlNode.put("bpw:unique", control.isUnique());
		controlNode.put("bpw:editable", control.isEditable());
		controlNode.put("bpw:expandable", control.isEditable());
		
		controlNode.put("bpw:multiple", control.isMultiple());
		controlNode.put("bpw:rows", control.getRows());
		controlNode.put("bpw:jcrDataType", control.getJcrDataType());
		controlNode.put("bpw:richText", control.isRichText());
		controlNode.put("bpw:flex", control.getFlex());
		controlNode.put("bpw:placeholder", control.getPlaceholder());		
	}
	
	private void addSteps(ObjectNode stepsNode, FormStep[] steps) {
		for (FormStep step : steps) {			
			ObjectNode stepNode = JsonUtils.createObjectNode();
			ObjectNode stepNodeChildren = JsonUtils.createObjectNode();
			stepsNode.set(step.getStepName(), stepNode);
			stepNode.set("children", stepNodeChildren);
			stepNode.put(JcrConstants.JCR_PRIMARY_TYPE, "bpw:formStep");

			String stepTitle = StringUtils.hasText(step.getStepTitle()) ? step.getStepTitle() : step.getStepName();
			if (StringUtils.hasText(stepTitle)) {
				stepNode.put("bpw:stepName", stepTitle);
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
			ObjectNode tabNodeChildren = JsonUtils.createObjectNode();
			tabsNode.set(tab.getTabName(), tabNode);
			tabNode.set("children", tabNodeChildren);
			tabNode.put(JcrConstants.JCR_PRIMARY_TYPE, "bpw:formTab");

			String tabTitle = StringUtils.hasText(tab.getTabTitle()) ? tab.getTabTitle() : tab.getTabName();
			if (StringUtils.hasText(tabTitle)) {
				tabNode.put("bpw:tabName", tabTitle);
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
		ObjectNode rowNodeChildren = JsonUtils.createObjectNode();
		groupNode.set(rowName, rowNode);
		rowNode.set("children", rowNodeChildren);
		rowNode.put(JcrConstants.JCR_PRIMARY_TYPE, "bpw:formRow");
		
		String rowTitle = StringUtils.hasText(row.getRowTitle()) ? row.getRowTitle() : row.getRowName();
		if (StringUtils.hasText(rowTitle)) {
			rowNode.put("bpw:rowName", rowTitle);
		}
		int colCount = 0;
		for (FormColumn col : row.getColumns()) {
			String columnName = StringUtils.hasText(col.getId()) ? col.getId() : "column" + colCount;
			this.addColumn(rowNodeChildren, col, columnName);
		}
	}
	
	private void addColumn(ObjectNode rowNode, FormColumn column, String columnNameName) {
		
		ObjectNode columnNode = JsonUtils.createObjectNode();
		rowNode.set(columnNameName, columnNode);
		columnNode.put(JcrConstants.JCR_PRIMARY_TYPE, "bpw:formColumn");
		
		columnNode.put("bpw:fxFlex", column.getFxFlex());
		columnNode.put("bpw:id", columnNameName);
		
		if (column.getFormControls() != null && column.getFormControls().length > 0) {
			ArrayNode controlArray = JsonUtils.creatArrayNode();
			for (String cn : column.getFormControls()) {
				controlArray.add(cn);
			}
			columnNode.set("bpw:fieldNames", controlArray);
		}
		if (column.getFieldLayouts() != null) {
			ObjectNode columnNodeChildren = JsonUtils.createObjectNode();
			columnNode.set("children", columnNodeChildren);
			
			for (FieldLayout fieldLayout: column.getFieldLayouts()) {
				this.addFieldLayout(columnNodeChildren, fieldLayout);
			}
		}
	}

	private void addFieldLayout(ObjectNode containerNode, FieldLayout fieldLayout) {
		ObjectNode fieldLayoutNode = JsonUtils.createObjectNode();
		containerNode.set(fieldLayout.getName(), fieldLayoutNode);
		fieldLayoutNode.put(JcrConstants.JCR_PRIMARY_TYPE, "bpw:fieldLayout");
		fieldLayoutNode.put("bpw:multiple", fieldLayout.isMultiple());
		fieldLayoutNode.put("bpw:name", fieldLayout.getName());
		fieldLayoutNode.put("bpw:key", fieldLayout.getKey());
		fieldLayoutNode.put("bpw:title", fieldLayout.getTitle());
		fieldLayoutNode.put("bpw:items", fieldLayout.getItems());
		fieldLayoutNode.put("bpw:displayFlex", fieldLayout.isDisplayFlex());
		fieldLayoutNode.put("bpw:listItems", fieldLayout.getListItems());
		fieldLayoutNode.put("bpw:flexDirection", fieldLayout.getFlexDirection());
		fieldLayoutNode.put("bpw:flex", fieldLayout.getFlex());
		fieldLayoutNode.put("bpw:placeHolder", fieldLayout.getPlaceHolder());
		if (fieldLayout.getFieldLayouts() != null) {
			ObjectNode fieldLayoutNodeChildren = JsonUtils.createObjectNode();
			fieldLayoutNode.set("children", fieldLayoutNodeChildren);			
			for (FieldLayout childFieldLayout: fieldLayout.getFieldLayouts()) {
				this.addFieldLayout(fieldLayoutNodeChildren, childFieldLayout);
			}
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
