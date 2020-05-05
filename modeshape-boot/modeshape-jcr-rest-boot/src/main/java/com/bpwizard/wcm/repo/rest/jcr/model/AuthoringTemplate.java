package com.bpwizard.wcm.repo.rest.jcr.model;

import java.util.Arrays;
import java.util.Map;

import org.modeshape.jcr.api.JcrConstants;
import org.springframework.util.StringUtils;

import com.bpwizard.wcm.repo.rest.JsonUtils;
import com.bpwizard.wcm.repo.rest.WcmUtils;
import com.bpwizard.wcm.repo.rest.modeshape.model.HasName;
import com.bpwizard.wcm.repo.rest.utils.WcmConstants;
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
	private String nodeType;
	private AccessControlList contentItemAcl;
	private BaseFormGroup[] elementGroups;
	private String contentItemWorkflow;
	private Map<String, FormControl> elements;
	private AuthoringTemplateProperties properties;
	
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
	public AuthoringTemplateProperties getProperties() {
		return properties;
	}
	public void setProperties(AuthoringTemplateProperties properties) {
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
	
	public String getNodeType() {
		return nodeType;
	}

	public void setNodeType(String nodeType) {
		this.nodeType = nodeType;
	}

	public JsonNode toJson() {
		ObjectNode jsonNode = JsonUtils.createObjectNode();
		ObjectNode children = JsonUtils.createObjectNode();
		
		jsonNode.set(WcmConstants.JCR_JSON_NODE_CHILDREN, children);
		jsonNode.put(JcrConstants.JCR_PRIMARY_TYPE, "bpw:authoringTemplate");
		
		if (StringUtils.hasText(this.getBaseResourceType())) {
			jsonNode.put("bpw:baseResourceType", this.getBaseResourceType());
		}
		jsonNode.put("bpw:nodeType", StringUtils.hasText(this.nodeType) ? 
				this.nodeType: WcmUtils.getContentType(this.getLibrary(), this.getName()));
		
		super.toJson(jsonNode, children);
		
		if (StringUtils.hasText(this.getContentItemWorkflow())) {
			jsonNode.put("bpw:contentWorkflow", this.getContentItemWorkflow());
		}

		if (this.getContentItemAcl() != null) {
			ObjectNode contentItemAclNode = JsonUtils.createObjectNode();
			children.set("contentItemAcl", contentItemAclNode);
			ObjectNode contentItemAclNodeChildren = JsonUtils.createObjectNode();
			contentItemAclNode.set(WcmConstants.JCR_JSON_NODE_CHILDREN, contentItemAclNodeChildren);
			contentItemAclNode.put(JcrConstants.JCR_PRIMARY_TYPE, "bpw:contentItemAcl");
			
			this.addAccessControlEntry(contentItemAclNodeChildren, "onSaveDraft", this.getContentItemAcl().getOnSaveDraftPermissions());
			this.addAccessControlEntry(contentItemAclNodeChildren, "onReivedDraft", this.getContentItemAcl().getOnReviewedDraftPermissions());
			this.addAccessControlEntry(contentItemAclNodeChildren, "onPublish", this.getContentItemAcl().getOnPublishPermissions());
		}
		
		if (this.elementGroups != null && this.elementGroups.length > 0) {
			ObjectNode elementGroupNode = JsonUtils.createObjectNode();
			ObjectNode elementGroupNodeChildren = JsonUtils.createObjectNode();
			children.set("elementGroups", elementGroupNode);
			elementGroupNode.set(WcmConstants.JCR_JSON_NODE_CHILDREN, elementGroupNodeChildren);
			elementGroupNode.put(JcrConstants.JCR_PRIMARY_TYPE, "bpw:formGroupFoler");
			
			this.addFormGroups(elementGroupNodeChildren, this.getElementGroups());
			
		}
		
		if (this.elements != null && this.elements.size() > 0 ) {
			ObjectNode elementFolderNode = JsonUtils.createObjectNode();
			ObjectNode elementFolderNodeChildren = JsonUtils.createObjectNode();
	
			elementFolderNode.set(WcmConstants.JCR_JSON_NODE_CHILDREN, elementFolderNodeChildren);
			children.set("elements", elementFolderNode);
			elementFolderNode.put(JcrConstants.JCR_PRIMARY_TYPE, "bpw:elementFolder");
			for (String controlName : this.getElements().keySet()) {
				this.addControl(elementFolderNodeChildren, this.getElements().get(controlName));
			}		
		}
		
		if (this.properties != null) {
			ObjectNode propertiesFolder = JsonUtils.createObjectNode();
			ObjectNode propertiesFolderChildren = JsonUtils.createObjectNode();
	
			propertiesFolder.set(WcmConstants.JCR_JSON_NODE_CHILDREN, propertiesFolderChildren);
			children.set("properties", propertiesFolder);
			propertiesFolder.put(JcrConstants.JCR_PRIMARY_TYPE, "bpw:propertyFolder");
			// this.addControl(propertiesFolderChildren, this.getProperties().getAuthoringTemplate());
			this.addControl(propertiesFolderChildren, this.getProperties().getCategories());
			// this.addControl(propertiesFolderChildren, this.getProperties().getNodeType());
			this.addControl(propertiesFolderChildren, this.getProperties().getWorkflow());
			
			this.addControl(propertiesFolderChildren, this.getProperties().getName());
			this.addControl(propertiesFolderChildren, this.getProperties().getTitle());
			this.addControl(propertiesFolderChildren, this.getProperties().getDescription());
			
		}
		return jsonNode;
	}
	
	private void addFormGroups(ObjectNode formGroupsFolder, BaseFormGroup[] elementGroups) {
		int groupCount = 0;
		for (BaseFormGroup g : elementGroups) {
			groupCount++;
			String groupName = StringUtils.hasText(g.getGroupTitle()) ? g.getGroupTitle() : 
				("group" + groupCount);
			String primaryFormGroupType = (g instanceof FormSteps) ? "bpw:formSteps"
					: (g instanceof FormTabs) ? "bpw:formTabs"
							: (g instanceof FormRows) ? "bpw:formRows" : "bpw:formRow";
			
			ObjectNode formGroupNode = JsonUtils.createObjectNode();
			ObjectNode formGroupNodeChildren = JsonUtils.createObjectNode();
	
			formGroupNode.set(WcmConstants.JCR_JSON_NODE_CHILDREN, formGroupNodeChildren);
			
			formGroupsFolder.set(groupName, formGroupNode);
			formGroupNode.put(JcrConstants.JCR_PRIMARY_TYPE, primaryFormGroupType);

			if (g.getCondition() != null) {
				this.setVisibleCondition(formGroupNodeChildren, g.getCondition());
			}
			
			if (g instanceof FormSteps) {
				this.addSteps(formGroupNodeChildren, ((FormSteps) g).getSteps());
			} else if (g instanceof FormTabs) {
				this.addTabs(formGroupNodeChildren, ((FormTabs) g).getTabs());
			} else if (g instanceof FormRows) {
				this.addRows(formGroupNodeChildren, ((FormRows) g).getRows());
			}
		}
	}
	
	private void addControl(ObjectNode elementFolder, FormControl control) {
		
		ObjectNode controlNode = JsonUtils.createObjectNode();
		ObjectNode controlChildrenNode = JsonUtils.createObjectNode();
		elementFolder.set(control.getName(), controlNode);

		controlNode.put(JcrConstants.JCR_PRIMARY_TYPE, "bpw:FormControl");
		controlNode.set(WcmConstants.JCR_JSON_NODE_CHILDREN, controlChildrenNode);
		if (StringUtils.hasText(control.getName())) {
			controlNode.put("bpw:name", control.getName());
		}
		
		
		if (StringUtils.hasText(control.getControlType())) {
			controlNode.put("bpw:controlType", control.getControlType());
		}

		if (StringUtils.hasText(control.getFieldName())) {
			controlNode.put("bpw:fieldName", control.getFieldName());
		}
		
		if (StringUtils.hasText(control.getDataType())) {
			controlNode.put("bpw:dataType", control.getDataType());
		}
		
		if (StringUtils.hasText(control.getFormat())) {
			controlNode.put("bpw:format", control.getFormat());
		}
		
		if (StringUtils.hasText(control.getJcrDataType())) {
			controlNode.put("bpw:jcrDataType", control.getJcrDataType());
		}
		
		controlNode.put("bpw:multiple", control.isMultiple());
		controlNode.put("bpw:mandatory", control.isMandatory());
		controlNode.put("bpw:userSearchable", control.isUserSearchable());
		controlNode.put("bpw:systemIndexed", control.isSystemIndexed());
		controlNode.put("bpw:showInList", control.isShowInList());
		
		FormControlLayout layout = control.getFormControlLayout();
		if (layout != null) {
			ObjectNode layoutNode = JsonUtils.createObjectNode();
			controlChildrenNode.set("formControlLayout", layoutNode);
			layoutNode.put(JcrConstants.JCR_PRIMARY_TYPE, "bpw:FormControlLayout");
			if (StringUtils.hasText(layout.getTitle())) {
				layoutNode.put("bpw:title", layout.getTitle());
			}		
			if (StringUtils.hasText(layout.getHint())) {
				layoutNode.put("bpw:hint", layout.getHint());
			}
			layoutNode.put("bpw:editable", layout.isEditable());
			layoutNode.put("bpw:expandable", layout.isEditable());
			layoutNode.put("bpw:expanded", layout.isExpanded());
			layoutNode.put("bpw:rows", layout.getRows());
			layoutNode.put("bpw:cols", layout.getCols());
			layoutNode.put("bpw:displayFlex", layout.isDisplayFlex());
			if (StringUtils.hasText(layout.getFlexDirection())) {
				layoutNode.put("bpw:flexDirection", layout.getFlexDirection());
			}
			if (StringUtils.hasText(layout.getFlex())) {
				layoutNode.put("bpw:flex", layout.getFlex());
			}
			if (StringUtils.hasText(layout.getPlaceHolder())) {
				layoutNode.put("bpw:placeHolder", layout.getPlaceHolder());		
			}
			if (layout.getListItems() > 0) {
				layoutNode.put("bpw:listItems", layout.getListItems());		
			}
		}
		
		CommonConstraint constraint = control.getConstraint();
		if (constraint != null) {
			ObjectNode constraintNode = JsonUtils.createObjectNode();
			controlChildrenNode.set("commonConstraint", constraintNode);
			constraintNode.put(JcrConstants.JCR_PRIMARY_TYPE, "bpw:CommonConstraint");
			if ((constraint.getEnumeration() != null) && (constraint.getEnumeration().length > 0)) {
				constraintNode.set("bpw:enum", WcmUtils.toArrayNode(constraint.getEnumeration()));
			}
			if (constraint.getDefaultValues() != null && (constraint.getDefaultValues().length > 0)) {
				constraintNode.set("bpw:defaultValue", WcmUtils.toArrayNode(constraint.getDefaultValues()));
			}
			if (StringUtils.hasText(constraint.getConstant())) {
				constraintNode.put("bpw:constant", constraint.getConstant());
			}
		}
		ObjectConstraint objectConstraint = control.getObjectConstraint();
		if (objectConstraint != null) {
			ObjectNode constraintNode = JsonUtils.createObjectNode();
			controlChildrenNode.set("objectConstraint", constraintNode);
			controlChildrenNode.put(JcrConstants.JCR_PRIMARY_TYPE, "bpw:ObjectConstraint");
			
			if (StringUtils.hasText(objectConstraint.getPropertyNamePattern())) {
				constraintNode.put("bpw:propertyNamePattern", objectConstraint.getPropertyNamePattern());
			}
			if (objectConstraint.getMinProperties() != null) {
				constraintNode.put("bpw:minProperties", objectConstraint.getMinProperties());
			}
			if (objectConstraint.getMaxProperties() != null) {
				constraintNode.put("bpw:maxProperties", objectConstraint.getMaxProperties());
			}
			if (objectConstraint.getAllowAdditionalProperties() != null) {
				constraintNode.put("bpw:allowAdditionalProperties", objectConstraint.getAllowAdditionalProperties());
			}
			
			if (objectConstraint.getRequired() != null) {
				constraintNode.set("bpw:required", WcmUtils.toArrayNode(objectConstraint.getRequired()));
			}
			
			ObjectNode objectConstraintChildren = JsonUtils.createObjectNode();
			constraintNode.set(WcmConstants.JCR_JSON_NODE_CHILDREN, objectConstraintChildren);
			
			Map<String, FormControl> additionalProperties = objectConstraint.getAdditionalProperties();
			if (additionalProperties != null && additionalProperties.size() > 0) {
				ObjectNode additionalPropertiesNode = JsonUtils.createObjectNode();
				ObjectNode additionalPropertiesChildren = JsonUtils.createObjectNode();
				additionalPropertiesNode.put(JcrConstants.JCR_PRIMARY_TYPE, "bpw:FormControls");
				additionalPropertiesNode.set(WcmConstants.JCR_JSON_NODE_CHILDREN, additionalPropertiesChildren);
				objectConstraintChildren.set("additionalProperties", additionalPropertiesNode);
				for (FormControl additionalItem: additionalProperties.values()) {
					this.addControl(additionalPropertiesChildren, additionalItem);
				}
			}
			
			Map<String, FormControl> patternProperties = objectConstraint.getPatternProperties();
			if (patternProperties != null && patternProperties.size() > 0) {
				ObjectNode patternPropertiesNode = JsonUtils.createObjectNode();
				ObjectNode patternPropertiesChildren = JsonUtils.createObjectNode();
				patternPropertiesNode.put(JcrConstants.JCR_PRIMARY_TYPE, "bpw:FormControls");
				patternPropertiesNode.set(WcmConstants.JCR_JSON_NODE_CHILDREN, patternPropertiesChildren);
				objectConstraintChildren.set("patternProperties", patternPropertiesNode);
				for (FormControl patternProperty: patternProperties.values()) {
					this.addControl(patternPropertiesNode, patternProperty);
				}
			}
			
			Map<String, FormControl> schemaDependencies = objectConstraint.getSchemaDependencies();
			if (schemaDependencies != null && schemaDependencies.size() > 0) {
				ObjectNode schemaDependenciesNode = JsonUtils.createObjectNode();
				ObjectNode schemaDependenciesChildren = JsonUtils.createObjectNode();
				schemaDependenciesNode.put(JcrConstants.JCR_PRIMARY_TYPE, "bpw:FormControls");
				schemaDependenciesNode.set(WcmConstants.JCR_JSON_NODE_CHILDREN, schemaDependenciesChildren);
				objectConstraintChildren.set("schemaDependencies", schemaDependenciesNode);
				for (FormControl schemaDependency: schemaDependencies.values()) {
					this.addControl(schemaDependenciesChildren, schemaDependency);
				}
			}
			
			Map<String, String[]> dependencies = objectConstraint.getDependencies();
			if (dependencies != null && dependencies.size() > 0) {
				ObjectNode dependenciesNode = JsonUtils.createObjectNode();
				ObjectNode dependenciesChildren = JsonUtils.createObjectNode();
				dependenciesNode.put(JcrConstants.JCR_PRIMARY_TYPE, "bpw:Dependencies");
				dependenciesNode.set(WcmConstants.JCR_JSON_NODE_CHILDREN, dependenciesChildren);
				objectConstraintChildren.set("schemaDependencies", dependenciesNode);
				for (String dependency: dependencies.keySet()) {
					ObjectNode dependencyNode = JsonUtils.createObjectNode();
					objectConstraintChildren.set(dependency, dependencyNode);
					dependencyNode.put(JcrConstants.JCR_PRIMARY_TYPE, "bpw:Dependency");
					dependencyNode.put("bpw:name", dependency);
					dependencyNode.set("bpw:dependencies", WcmUtils.toArrayNode(dependencies.get(dependency)));
				}
			}
		}
		StringConstraint stringConstraint = control.getStringConstraint();
		if (stringConstraint != null) {
			ObjectNode constraintNode = JsonUtils.createObjectNode();
			controlChildrenNode.set("stringConstraint", constraintNode);
			constraintNode.put(JcrConstants.JCR_PRIMARY_TYPE, "bpw:StringConstraint");
			if (StringUtils.hasText(stringConstraint.getFormat())) {
				constraintNode.put("bpw:format", stringConstraint.getFormat());
			}
			if (StringUtils.hasText(stringConstraint.getPattern())) {
				constraintNode.put("bpw:pattern", stringConstraint.getPattern());
			}
			if (stringConstraint.getMinLength() != null) {
				constraintNode.put("bpw:minLength", stringConstraint.getMinLength());
			}
			if (stringConstraint.getMaxLength() != null) {
				constraintNode.put("bpw:maxLength", stringConstraint.getMaxLength());
			}
			if (StringUtils.hasText(stringConstraint.getContentMediaType())) {
				constraintNode.put("bpw:contentMediaType", stringConstraint.getContentMediaType());
			}
			if (StringUtils.hasText(stringConstraint.getContentEncoding())) {
				constraintNode.put("bpw:contentEncoding", stringConstraint.getContentEncoding());
			}
			if (StringUtils.hasText(stringConstraint.getContentSchema())) {
				constraintNode.put("bpw:contentSchema", stringConstraint.getContentEncoding());
			}

		}
		NumberConstraint numberConstraint = control.getNumberConstraint();
		if (numberConstraint != null) {
			ObjectNode constraintNode = JsonUtils.createObjectNode();
			controlChildrenNode.set("numberConstraint", constraintNode);
			constraintNode.put(JcrConstants.JCR_PRIMARY_TYPE, "bpw:NumberConstraint");
			if (numberConstraint.getMultipleOf() != null) {
				constraintNode.put("bpw:multipleOf", numberConstraint.getMultipleOf());
			}
			if (numberConstraint.getExclusiveMaximum() != null) {
				constraintNode.put("bpw:exclusiveMaximum", numberConstraint.getExclusiveMaximum());
			}
			if (numberConstraint.getMaximum() != null) {
				constraintNode.put("bpw:maximum", numberConstraint.getMaximum());
			}
			if (numberConstraint.getExclusiveMinimum() != null) {
				constraintNode.put("bpw:exclusiveMinimum", numberConstraint.getExclusiveMinimum());
			}
			if (numberConstraint.getMinimum() != null) {
				constraintNode.put("bpw:minimum", numberConstraint.getMinimum());
			}
		}
		ArrayConstraint  arrayConstraint = control.getArrayConstraint();
		if (arrayConstraint != null) {
			ObjectNode constraintNode = JsonUtils.createObjectNode();
			controlChildrenNode.set("arrayConstraint", constraintNode);
			constraintNode.put(JcrConstants.JCR_PRIMARY_TYPE, "bpw:ArrayConstraint");
			if (arrayConstraint.getMaxItems() != null) {
				controlNode.put("bpw:maxItems", arrayConstraint.getMaxItems());
			}
			if (arrayConstraint.getMinItems() != null) {
				controlNode.put("bpw:minItems", arrayConstraint.getMinItems());
			}
			if (arrayConstraint.getUniqueItems() != null) {
				controlNode.put("bpw:uniqueItems", arrayConstraint.getUniqueItems());
			}
			if (StringUtils.hasText(arrayConstraint.getContains())) {
				constraintNode.put("bpw:contains", arrayConstraint.getContains());
			}
			Map<String, FormControl> additionalItems = arrayConstraint.getAdditionalItems();
			if (additionalItems != null && additionalItems.size() > 0) {
				ObjectNode arrayConstraintChildren = JsonUtils.createObjectNode();
				constraintNode.set(WcmConstants.JCR_JSON_NODE_CHILDREN, arrayConstraintChildren);
				ObjectNode additionalItemsNode = JsonUtils.createObjectNode();
				ObjectNode additionalItemsChildren = JsonUtils.createObjectNode();
				additionalItemsNode.put(JcrConstants.JCR_PRIMARY_TYPE, "bpw:FormControls");
				additionalItemsNode.set(WcmConstants.JCR_JSON_NODE_CHILDREN, additionalItemsChildren);
				arrayConstraintChildren.set("additionalItems", additionalItemsNode);
				for (FormControl additionalItem: arrayConstraint.getAdditionalItems().values()) {
					this.addControl(additionalItemsChildren, additionalItem);
				}
			}
		}
		
		ConditionalConstraint  conditionalConstraint = control.getConditionalConstraint();
		if (conditionalConstraint != null) {
			ObjectNode constraintNode = JsonUtils.createObjectNode();
			controlChildrenNode.set("conditionalConstraint", constraintNode);
			constraintNode.put(JcrConstants.JCR_PRIMARY_TYPE, "bpw:ConditionalConstraint");
			
			Map<String, FormControl> ifCondition = conditionalConstraint.getIfCondition();
			ObjectNode conditionalConstraintChildren = JsonUtils.createObjectNode();
			constraintNode.set(WcmConstants.JCR_JSON_NODE_CHILDREN, conditionalConstraintChildren);
			ObjectNode ifConditionNode = JsonUtils.createObjectNode();
			conditionalConstraintChildren.set("ifCondition", ifConditionNode);
			ifConditionNode.put(JcrConstants.JCR_PRIMARY_TYPE, "bpw:IfCondition");
			ObjectNode ifConditionChildren = JsonUtils.createObjectNode();
			ifConditionNode.set(WcmConstants.JCR_JSON_NODE_CHILDREN, ifConditionChildren);
			for (FormControl formControl : ifCondition.values()) {
				addControl(ifConditionChildren, formControl);
			}
			
			Map<String, FormControl> thenRule = conditionalConstraint.getThenRule();
			if (thenRule != null ) {
				ObjectNode thenRuleNode = JsonUtils.createObjectNode();
				conditionalConstraintChildren.set("thenRule", thenRuleNode);
				thenRuleNode.put(JcrConstants.JCR_PRIMARY_TYPE, "bpw:ThenRule");
				ObjectNode thenRuleChildren = JsonUtils.createObjectNode();
				thenRuleNode.set(WcmConstants.JCR_JSON_NODE_CHILDREN, thenRuleChildren);
				for (FormControl formControl : thenRule.values()) {
					addControl(thenRuleChildren, formControl);
				}
			}
			Map<String, FormControl> elseRule = conditionalConstraint.getElseRule();
			if (elseRule != null) {
				ObjectNode elseRuleNode = JsonUtils.createObjectNode();
				conditionalConstraintChildren.set("elseRule", elseRuleNode);
				elseRuleNode.put(JcrConstants.JCR_PRIMARY_TYPE, "bpw:ElseRule");
				ObjectNode elseRuleChildren = JsonUtils.createObjectNode();
				elseRuleNode.set(WcmConstants.JCR_JSON_NODE_CHILDREN, elseRuleChildren);
				for (FormControl formControl : elseRule.values()) {
					addControl(elseRuleChildren, formControl);
				}
			}
			
		}

		Map<String, FormControl> notConstraint = control.getNotConstraint();
		if (notConstraint != null && notConstraint.size() > 0) {
			ObjectNode notConstraintNode = JsonUtils.createObjectNode();
			ObjectNode notConstraintChildren = JsonUtils.createObjectNode();
			notConstraintNode.put(JcrConstants.JCR_PRIMARY_TYPE, "bpw:FormControls");
			notConstraintNode.set(WcmConstants.JCR_JSON_NODE_CHILDREN, notConstraintChildren);
			controlChildrenNode.set("notConstraint", notConstraintNode);
			for (FormControl notConstraintChild: notConstraint.values()) {
				this.addControl(notConstraintChildren, notConstraintChild);
			}
		}
		Map<String, FormControl> oneOfConstraints = control.getAllOfConstraint();
		if (oneOfConstraints != null && oneOfConstraints.size() > 0) {
			ObjectNode oneOfNode = JsonUtils.createObjectNode();
			ObjectNode oneOfChildren = JsonUtils.createObjectNode();
			oneOfNode.put(JcrConstants.JCR_PRIMARY_TYPE, "bpw:FormControls");
			oneOfNode.set(WcmConstants.JCR_JSON_NODE_CHILDREN, oneOfChildren);
			controlChildrenNode.set("oneOf", oneOfNode);
			for (FormControl constraintChild: oneOfConstraints.values()) {
				this.addControl(oneOfChildren, constraintChild);
			}
		}
		
		Map<String, FormControl> anyOfConstraints = control.getAllOfConstraint();
		if (anyOfConstraints != null && anyOfConstraints.size() > 0) {
			ObjectNode anyOfNode = JsonUtils.createObjectNode();
			ObjectNode anyOfChildren = JsonUtils.createObjectNode();
			anyOfNode.put(JcrConstants.JCR_PRIMARY_TYPE, "bpw:FormControls");
			anyOfNode.set(WcmConstants.JCR_JSON_NODE_CHILDREN, anyOfChildren);
			controlChildrenNode.set("anyOf", anyOfNode);
			for (FormControl constraintChild: anyOfConstraints.values()) {
				this.addControl(anyOfChildren, constraintChild);
			}
		}
		
		Map<String, FormControl> allOfConstraints = control.getAllOfConstraint();
		if (allOfConstraints != null && allOfConstraints.size() > 0) {
			ObjectNode allOfNode = JsonUtils.createObjectNode();
			ObjectNode allOfChildren = JsonUtils.createObjectNode();
			allOfNode.put(JcrConstants.JCR_PRIMARY_TYPE, "bpw:FormControls");
			allOfNode.set(WcmConstants.JCR_JSON_NODE_CHILDREN, allOfChildren);
			controlChildrenNode.set("allOf", allOfNode);
			for (FormControl constraintChild: anyOfConstraints.values()) {
				this.addControl(allOfChildren, constraintChild);
			}
		}
		CustomConstraint customConstraint = control.getCustomConstraint();
		JavascriptFunction[] functions = customConstraint.getFunctions();
		if (customConstraint != null && functions != null && functions.length > 0) {
			ObjectNode constraintNode = JsonUtils.createObjectNode();
			controlChildrenNode.set("customConstraint", constraintNode);
			constraintNode.put(JcrConstants.JCR_PRIMARY_TYPE, "bpw:CustomConstraint");
			ObjectNode functionsChildrenNode = JsonUtils.createObjectNode();
			constraintNode.set(WcmConstants.JCR_JSON_NODE_CHILDREN, functionsChildrenNode);
			for (JavascriptFunction function: functions) {
				ObjectNode functionNode = JsonUtils.createObjectNode();
				functionsChildrenNode.set(function.getName(), functionNode);
				functionNode.put(JcrConstants.JCR_PRIMARY_TYPE, "bpw:JavascriptFunction");
				functionNode.set("bpw:params", WcmUtils.toArrayNode(function.getParams()));
				functionNode.put("bpw:functionBody", function.getFunctionBody());
			}
		}
	
		if (control.getFormControls() != null) {
			
			ObjectNode controlNodeChildren = JsonUtils.createObjectNode();
			controlNode.set(WcmConstants.JCR_JSON_NODE_CHILDREN, controlNodeChildren);			
			if (control.getFormControls() != null) {
				for (FormControl formControl : control.getFormControls().values()) {
					this.addControl(controlNodeChildren, formControl);
				}
			}
		}
	}
	
	private void addSteps(ObjectNode stepsNode, FormStep[] steps) {
		for (FormStep step : steps) {			
			ObjectNode stepNode = JsonUtils.createObjectNode();
			ObjectNode stepNodeChildren = JsonUtils.createObjectNode();
			stepsNode.set(step.getStepName(), stepNode);
			stepNode.set(WcmConstants.JCR_JSON_NODE_CHILDREN, stepNodeChildren);
			stepNode.put(JcrConstants.JCR_PRIMARY_TYPE, "bpw:formStep");

			if (step.getCondition() != null) {
				this.setVisibleCondition(stepNode, step.getCondition());
			}
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
			tabNode.set(WcmConstants.JCR_JSON_NODE_CHILDREN, tabNodeChildren);
			tabNode.put(JcrConstants.JCR_PRIMARY_TYPE, "bpw:formTab");
			if (tab.getCondition() != null) {
				this.setVisibleCondition(tabNode, tab.getCondition());
			}
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
		rowNode.set(WcmConstants.JCR_JSON_NODE_CHILDREN, rowNodeChildren);
		rowNode.put(JcrConstants.JCR_PRIMARY_TYPE, "bpw:formRow");
		if (row.getCondition() != null) {
			this.setVisibleCondition(rowNode, row.getCondition());
		}
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
	
	private void setVisibleCondition(ObjectNode widgetNode, VisbleCondition visbleCondition) {
		ObjectNode conditionNode = JsonUtils.createObjectNode();
		conditionNode.put(JcrConstants.JCR_PRIMARY_TYPE, "bpw:VisbleCondition");
		widgetNode.set("bpw:condition", conditionNode);
		
		if (StringUtils.hasText(visbleCondition.getCondition())) {
			conditionNode.put("bpw:condition", visbleCondition.getCondition());
		}
		
		if (StringUtils.hasText(visbleCondition.getFunctionBody())) {
			conditionNode.put("bpw:functionBody", visbleCondition.getFunctionBody());
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
		
		if (column.getFormGroups() != null && column.getFormGroups().length > 0) {
			ObjectNode formGroupsFolderNode = JsonUtils.createObjectNode();
			columnNode.set(WcmConstants.JCR_JSON_NODE_CHILDREN, formGroupsFolderNode);
			formGroupsFolderNode.put(JcrConstants.JCR_PRIMARY_TYPE, "bpw:formGroupFoler");
			ObjectNode formGroupsChildren = JsonUtils.createObjectNode();
			formGroupsFolderNode.set(WcmConstants.JCR_JSON_NODE_CHILDREN, formGroupsChildren);
			this.addFormGroups(formGroupsChildren, this.getElementGroups());
		}
		if (column.getCondition() != null) {
			this.setVisibleCondition(columnNode, column.getCondition());
		}
	}

	@Override
	public String toString() {
		return "AuthoringTemplate [name=" + name + ", repository=" + repository + ", workspace=" + workspace
				+ ", library=" + library + ", baseResourceType=" + baseResourceType + ", nodeType=" + nodeType
				+ ", contentItemAcl=" + contentItemAcl + ", elementGroups=" + Arrays.toString(elementGroups)
				+ ", contentItemWorkflow=" + contentItemWorkflow + ", elements=" + elements + ", properties="
				+ properties + "]";
	}
}
