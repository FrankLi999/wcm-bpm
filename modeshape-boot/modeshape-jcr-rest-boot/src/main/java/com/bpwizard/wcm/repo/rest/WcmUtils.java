package com.bpwizard.wcm.repo.rest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.jcr.PropertyType;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.Value;
import javax.jcr.nodetype.NodeDefinitionTemplate;
import javax.jcr.nodetype.NodeTypeDefinition;
import javax.jcr.nodetype.NodeTypeManager;
import javax.jcr.nodetype.NodeTypeTemplate;
import javax.jcr.nodetype.PropertyDefinitionTemplate;
import javax.jcr.version.OnParentVersionAction;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modeshape.web.jcr.RepositoryManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.bpwizard.wcm.repo.rest.handler.RestItemHandler;
import com.bpwizard.wcm.repo.rest.jcr.exception.WcmRepositoryException;
import com.bpwizard.wcm.repo.rest.jcr.model.ArrayConstraint;
import com.bpwizard.wcm.repo.rest.jcr.model.AuthoringTemplate;
import com.bpwizard.wcm.repo.rest.jcr.model.AuthoringTemplateProperties;
import com.bpwizard.wcm.repo.rest.jcr.model.BaseFormGroup;
import com.bpwizard.wcm.repo.rest.jcr.model.CommonConstraint;
import com.bpwizard.wcm.repo.rest.jcr.model.ConditionalConstraint;
import com.bpwizard.wcm.repo.rest.jcr.model.CustomConstraint;
import com.bpwizard.wcm.repo.rest.jcr.model.Form;
import com.bpwizard.wcm.repo.rest.jcr.model.FormColumn;
import com.bpwizard.wcm.repo.rest.jcr.model.FormControl;
import com.bpwizard.wcm.repo.rest.jcr.model.FormControlLayout;
import com.bpwizard.wcm.repo.rest.jcr.model.FormRow;
import com.bpwizard.wcm.repo.rest.jcr.model.FormRows;
import com.bpwizard.wcm.repo.rest.jcr.model.FormStep;
import com.bpwizard.wcm.repo.rest.jcr.model.FormSteps;
import com.bpwizard.wcm.repo.rest.jcr.model.FormTab;
import com.bpwizard.wcm.repo.rest.jcr.model.FormTabs;
import com.bpwizard.wcm.repo.rest.jcr.model.JavascriptFunction;
import com.bpwizard.wcm.repo.rest.jcr.model.NumberConstraint;
import com.bpwizard.wcm.repo.rest.jcr.model.ObjectConstraint;
import com.bpwizard.wcm.repo.rest.jcr.model.ResourceMixin;
import com.bpwizard.wcm.repo.rest.jcr.model.ResourceNode;
import com.bpwizard.wcm.repo.rest.jcr.model.StringConstraint;
import com.bpwizard.wcm.repo.rest.jcr.model.VisbleCondition;
import com.bpwizard.wcm.repo.rest.modeshape.model.RestNode;
import com.bpwizard.wcm.repo.rest.modeshape.model.RestProperty;
import com.bpwizard.wcm.repo.rest.utils.WcmConstants;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.node.ArrayNode;

@Component
public class WcmUtils {
	private static final Logger logger = LogManager.getLogger(WcmUtils.class);
	
	@Autowired
	private RestItemHandler itemHandler;
	
	@Autowired
	protected RepositoryManager repositoryManager;
	
	public static String nodePath(String wcmPath) {
		return String.format(wcmPath.startsWith("/") ? WcmConstants.NODE_ROOT_PATH_PATTERN : WcmConstants.NODE_ROOT_REL_PATH_PATTERN, 
				wcmPath);
	}
	
	public static String nodePath(String wcmPath, String name) {
		return String.format(wcmPath.startsWith("/") ? WcmConstants.NODE_PATH_PATTERN : WcmConstants.NODE_REL_PATH_PATTERN, 
				wcmPath, name);
	}

	public static String library(String wcmPath) {
		return wcmPath.startsWith("/") ? wcmPath.split("/", 3)[1] : wcmPath.split("/", 2)[0];	
	}
	
	public static String at(String atPath) {
		String path[] = atPath.split("/");
		return path[path.length - 1];
	}
	
	public static String getElementFolderType(String library, String atName) {
		return String.format(WcmConstants.JCR_TYPE_ELEMENTS_FOLDER_PATTERN, library, atName);
	}
	
	public static String getContentType(String library, String at) {
		return String.format(WcmConstants.CONTENT_TYPE, library, at);
	}
	
	public static ArrayNode toArrayNode(List<? extends Object> values) {
		ArrayNode valueArray = JsonUtils.creatArrayNode();
		for (Object value : values) {
			valueArray.add(value.toString());
		}
		return valueArray;
	}
	
	public static ArrayNode toArrayNode(String values[]) {
    	
		ArrayNode valueArray = JsonUtils.creatArrayNode();
		for (String value : values) {
			valueArray.add(value);
		}
		return valueArray;
    }
	
	public static String definitionUrl(String fieldType) {
		return String.format(WcmConstants.DEFINITION_PATH_PATTERN, fieldType);
	}
	
	public static String jsonSchemaPattern(String pattern) {
		return String.format(WcmConstants.JSON_STRING_PATTERN, pattern);
	}
	
	public static FormControl getFormControl(Form form, String fieldName) {
		String jsonPath[] = fieldName.split("\\.");
		FormControl formControl = null;
		Map<String, FormControl> sourceControls = form.getFormControls(); 
		if (jsonPath.length > 0) {
			formControl = sourceControls.get(jsonPath[0]);
		}
		for (int i = 1; (i < jsonPath.length) && (formControl != null) && (formControl.getFormControls() != null); i++) {
			formControl = formControl.getFormControls().get(jsonPath[i]);
		}
		
		return formControl;
	}
	
	public static FormControl getFormControl(AuthoringTemplate at, String fieldName) {
		String jsonPath[] = fieldName.split("\\.");
		FormControl formControl = null;
		
		if ("properties".equals(jsonPath[0])) {
			formControl = getAtProperty(at, jsonPath[1]);
		} else {
			Map<String, FormControl> sourceControls = "elements".equals(jsonPath[0]) ? at.getElements() 
				 : Collections.<String, FormControl>emptyMap();
			if (jsonPath.length > 1) {
				formControl = sourceControls.get(jsonPath[1]);
			}
			for (int i = 2; (i < jsonPath.length) && (formControl != null) && (formControl.getFormControls() != null); i++) {
				formControl = formControl.getFormControls().get(jsonPath[i]);
			}
		}
		return formControl;
	}
	
	private static FormControl getAtProperty(AuthoringTemplate at, String property) {
		FormControl formControl = null;
//		if ("authoringTemplate".equals(property)) {
//			formControl = at.getProperties().getAuthoringTemplate();
//		} else if ("nodeType".equals(property)) {
//			formControl = at.getProperties().getNodeType();
//		} else 
		if ("workflow".equals(property)) {
			formControl = at.getProperties().getWorkflow();
		} else if ("categories".equals(property)) {
			formControl = at.getProperties().getCategories();
		} else if ("name".equals(property)) {
			formControl = at.getProperties().getName();
		} else if ("title".equals(property)) {
			formControl = at.getProperties().getTitle();
		} else if ("description".equals(property)) {
			formControl = at.getProperties().getDescription();
		}
		return formControl;
	}
	
	public static String layoutPath(String fieldPath, Form form) {
		String jsonPath[] = fieldPath.split("\\.");
		String result = fieldPath;
		FormControl formControl = null;
		
			
		// StringBuilder layoutPath = new StringBuilder(jsonPath[0]);
		StringBuilder layoutPath = new StringBuilder("");	
		Map<String, FormControl> formControls = form.getFormControls();				
		
		// boolean isParentNodeArray = false;
		
		int i = 0;
		for (;(i < jsonPath.length) && (formControls != null); i++) {
			formControl = formControls.get(jsonPath[i]);
			formControls = formControl.getFormControls();
			String controlFieldName = StringUtils.hasText(formControl.getName()) ? 
			    formControl.getName() : "1";
			WcmUtils.appendControlField(i, controlFieldName, layoutPath, formControl.isMultiple());
		}
		result = (i == jsonPath.length) ? layoutPath.toString() : fieldPath;
		return result;
	}
	
	public static String layoutPath(String fieldPath, AuthoringTemplate at) {
		String jsonPath[] = fieldPath.split("\\.");
		String result = fieldPath;
		FormControl formControl = null;
		if (!"properties".equals(jsonPath[0])) {
			
			StringBuilder layoutPath = new StringBuilder(jsonPath[0]);
			Map<String, FormControl> formControls = "elements".equals(jsonPath[0]) ? at.getElements() 
					 : Collections.<String, FormControl>emptyMap();				
			
			// boolean isParentNodeArray = false;
			
			int i = 1;
			for (;(i < jsonPath.length - 1) && (formControls != null); i++) {
				formControl = formControls.get(jsonPath[i]);
				formControls = formControl.getFormControls();
				String controlFieldName = StringUtils.hasText(formControl.getName()) ? 
						formControl.getName() : "1";
				WcmUtils.appendControlField(i, controlFieldName, layoutPath, formControl.isMultiple());
//				if (formControl.isMultiple()) {
//					if (isParentNodeArray) { 
//						layoutPath.append("[]");
//					} else {
//						WcmUtils.appendControlField(i, controlFieldName, layoutPath, true);
//					}
//					isParentNodeArray = true;
//				} else {
//					WcmUtils.appendControlField(i, controlFieldName, layoutPath, false);
//					isParentNodeArray = false;
//				}
			}
			result = (i == jsonPath.length) ? layoutPath.toString() : fieldPath;
		}
		return result;
	}
	
	private static void appendControlField(int position, String controlFieldName, StringBuilder layoutPath, boolean isArray) {
		if (position > 0) {
			layoutPath.append(".");
		}
		layoutPath.append(controlFieldName);
		if (isArray) {
			layoutPath.append("[]");
		}
	}
	
	public static String fieldPath(String parentFieldPath, String controlName) {
		return String.format("%s.%s", parentFieldPath, controlName);
	}
	
	public static String[] getValues(ArrayNode jsonNode) throws JsonProcessingException {
		String[] values = new String[jsonNode.size()];
		for (int i = 0; i < jsonNode.size(); i++) {
			values[i] = JsonUtils.writeValueAsString(jsonNode.get(i));
		}
		return values;
	}
	
	@SuppressWarnings("unchecked")
	public void registerNodeType(String worksoace, AuthoringTemplate at) throws RepositoryException {
		Session session = this.repositoryManager.getSession(at.getRepository(), worksoace);
        NodeTypeManager nodeTypeManager = session.getWorkspace().getNodeTypeManager();
        String elementFolderType = String.format(WcmConstants.ELEMENT_FOLDER_TYPE, at.getLibrary(), at.getName());
        NodeTypeTemplate elementFolderNodeType = this.createElementFolderNodeType(
        		elementFolderType, 
        		new String[] {WcmConstants.FOLDER_NODE_TYPE},
        		nodeTypeManager,
        		true);
        for (String element: at.getElements().keySet()) {
        	elementFolderNodeType.getPropertyDefinitionTemplates().add(this.createPropertyDefinitionTemplate(
        			element, 
            		at.getElements().get(element),
            		session,
            		nodeTypeManager));
        }
        elementFolderNodeType.getPropertyDefinitionTemplates().add(this.createPropertyDefinitionDefault(
        		"*", nodeTypeManager));
        
        String propertyFolderType = WcmConstants.JCR_TYPE_PROPERTY_FOLDER;
        NodeTypeTemplate propertyFolderNodeType = this.createElementFolderNodeType(
        		propertyFolderType, 
        		new String[] {WcmConstants.FOLDER_NODE_TYPE},
        		nodeTypeManager,
        		true);
        //AuthoringTemplate properties
//    	propertyFolderNodeType.getPropertyDefinitionTemplates().add(this.createPropertyDefinitionTemplate(
//        		"authoringTemplate", 
//        		at.getProperties().getAuthoringTemplate(),
//        		session,
//        		nodeTypeManager));
    	propertyFolderNodeType.getPropertyDefinitionTemplates().add(this.createPropertyDefinitionTemplate(
        		"categories", 
        		at.getProperties().getCategories(),
        		session,
        		nodeTypeManager));
//    	propertyFolderNodeType.getPropertyDefinitionTemplates().add(this.createPropertyDefinitionTemplate(
//        		"nodeType", 
//        		at.getProperties().getNodeType(),
//        		session,
//        		nodeTypeManager));
    	
    	propertyFolderNodeType.getPropertyDefinitionTemplates().add(this.createPropertyDefinitionTemplate(
        		"workflow", 
        		at.getProperties().getWorkflow(),
        		session,
        		nodeTypeManager));
    	propertyFolderNodeType.getPropertyDefinitionTemplates().add(this.createPropertyDefinitionTemplate(
        		"name", 
        		at.getProperties().getName(),
        		session,
        		nodeTypeManager));
    	
    	propertyFolderNodeType.getPropertyDefinitionTemplates().add(this.createPropertyDefinitionTemplate(
        		"title", 
        		at.getProperties().getTitle(),
        		session,
        		nodeTypeManager));
    	
    	propertyFolderNodeType.getPropertyDefinitionTemplates().add(this.createPropertyDefinitionTemplate(
        		"description", 
        		at.getProperties().getDescription(),
        		session,
        		nodeTypeManager));
        propertyFolderNodeType.getPropertyDefinitionTemplates().add(this.createPropertyDefinitionDefault(
        		"*", nodeTypeManager));
        
        
        NodeTypeTemplate contentNodeType = this.createElementFolderNodeType(
        		getContentType(at.getLibrary(), at.getName()),
        		new String[] {WcmConstants.WORKFLOW_NODE_TYPE},
        		nodeTypeManager,
        		true);
        
        NodeDefinitionTemplate elementFolder = this.createNodeDefinitionTemplate(
        		WcmConstants.WCM_ITEM_ELEMENTS,
        		new String[] { elementFolderType },
        		nodeTypeManager);
        
        NodeDefinitionTemplate propertyFolder = this.createNodeDefinitionTemplate(
        		WcmConstants.WCM_ITEM_PROPERTIES,
        		new String[] { propertyFolderType },
        		nodeTypeManager);		
        		
        contentNodeType.getNodeDefinitionTemplates().add(elementFolder);
        contentNodeType.getNodeDefinitionTemplates().add(propertyFolder);
        
        nodeTypeManager.registerNodeTypes(new NodeTypeDefinition[] {
        		elementFolderNodeType, 
        		propertyFolderNodeType, 
        		contentNodeType
    		}, true);
	}
	
	public AuthoringTemplate getAuthoringTemplate(
			String repository,
			String workspace, 
			String wcmAtPath,
			String baseUrl) 
			throws WcmRepositoryException {
		if (logger.isDebugEnabled()) {
			logger.traceEntry();
		}
		try {
			String library = WcmUtils.library(wcmAtPath);
			String absPath = WcmUtils.nodePath(wcmAtPath);
			RestNode atNode = (RestNode) this.itemHandler.item(baseUrl, repository, workspace,
					absPath, WcmConstants.AT_JSON_FORM_DEPATH);
			
			AuthoringTemplate at = this.toAuthoringTemplate(atNode, repository, workspace, library);
			if (logger.isDebugEnabled()) {
				logger.traceExit();
			}
			return at;
		} catch (WcmRepositoryException e ) {
			throw e;
		} catch (Throwable t) {
			throw new WcmRepositoryException(t);
		}	
	}
	
	public Form getForm(
			String repository,
			String workspace, 
			String wcmAtPath,
			String baseUrl) 
			throws WcmRepositoryException {
		if (logger.isDebugEnabled()) {
			logger.traceEntry();
		}
		try {
			String library = WcmUtils.library(wcmAtPath);
			String absPath = WcmUtils.nodePath(wcmAtPath);
			RestNode atNode = (RestNode) this.itemHandler.item(baseUrl, repository, workspace,
					absPath, WcmConstants.FORM_JSON_FORM_DEPATH);
			
			Form form = this.toForm(atNode, repository, workspace, library);
			if (logger.isDebugEnabled()) {
				logger.traceExit();
			}
			return form;
		} catch (WcmRepositoryException e ) {
			throw e;
		} catch (Throwable t) {
			throw new WcmRepositoryException(t);
		}	
	}
	
	public AuthoringTemplate toAuthoringTemplate(RestNode node, String repository, String workspace, String library) {
		AuthoringTemplate at = new AuthoringTemplate();
		at.setRepository(repository);
		at.setWorkspace(workspace);
		at.setLibrary(library);
		at.setName(node.getName());
		this.resolveResourceNode(at, node);
		for (RestProperty property : node.getJcrProperties()) {
			if ("bpw:baseResourceType".equals(property.getName())) {
				at.setBaseResourceType(property.getValues().get(0));
			} else if ("bpw:contentWorkflow".equals(property.getName())) {
				at.setContentItemWorkflow(property.getValues().get(0));
			} else if ("bpw:nodeType".equals(property.getName())) {
				at.setNodeType(property.getValues().get(0));
			} 
		}
		if (StringUtils.isEmpty(at.getNodeType())) {
			at.setNodeType(WcmUtils.getContentType(at.getLibrary(), at.getName()));
		}
		this.populateAuthoringTemplate(at, node);
		return at;
	}
	
	public Form toForm(RestNode node, String repository, String workspace, String library) {
		Form form = new Form();
		form.setRepository(repository);
		form.setWorkspace(workspace);
		form.setLibrary(library);
		form.setName(node.getName());
		this.resolveResourceMixin(form, node);
		this.populateForm(form, node);
		return form;
	}
	
	public void resolveResourceNode(ResourceNode resourceNode, RestNode restNode) {
		for (RestProperty property : restNode.getJcrProperties()) {
			if ("bpw:name".equals(property.getName())) {
				resourceNode.setName(property.getValues().get(0));
			} else if ("bpw:title".equals(property.getName())) {
				resourceNode.setTitle(property.getValues().get(0));
			} else if ("bpw:description".equals(property.getName())) {
				resourceNode.setDescription(property.getValues().get(0));
			} else if ("bpw:author".equals(property.getName())) {
				resourceNode.setAuthor(property.getValues().get(0));
			} else if ("jcr:lockOwner".equals(property.getName())) {
				resourceNode.setLockOwner(property.getValues().get(0));
			}				
		}
	}
	
	public void resolveResourceMixin(ResourceMixin resourceMixin, RestNode restNode) {
		resourceMixin.setName(restNode.getName());
		for (RestProperty property : restNode.getJcrProperties()) {
			if ("bpw:name".equals(property.getName())) {
				resourceMixin.setName(property.getValues().get(0));
			} else if ("bpw:title".equals(property.getName())) {
				resourceMixin.setTitle(property.getValues().get(0));
			} else if ("bpw:description".equals(property.getName())) {
				resourceMixin.setDescription(property.getValues().get(0));
			} else if ("bpw:author".equals(property.getName())) {
				resourceMixin.setAuthor(property.getValues().get(0));
			}				
		}
	}
	
	public static boolean checkNodeType(RestNode node, String nodeType) {
		return node.getJcrProperties().stream().anyMatch(
				property -> "jcr:primaryType".equals(property.getName()) && property.getValues().contains(nodeType));
	}
	
	public static boolean showOnMenu(RestNode siteArea) {
		boolean result = true;
		for (RestNode childNode : siteArea.getChildren()) {
			if (WcmConstants.WCM_ITEM_ELEMENTS.equals(childNode.getName())) {
				for (RestProperty property : childNode.getJcrProperties()) {
					if ("showOnMenu".equals(property.getName())) {
						result = Boolean.valueOf(property.getValues().get(0));
						break;
					} 
				}
				break;
			}
		}
		return result;
	}
	
	public void unlock(
			String repository,
			String workspace, 
			String absPath) throws RepositoryException {
		
		javax.jcr.lock.LockManager lm = this.repositoryManager.getSession(repository, workspace).getWorkspace().getLockManager();
		
		if (lm.isLocked(absPath)) {
			lm.unlock(absPath);
		}
	}
	
	public static Value[] getValues(String[] strings, Session session) throws RepositoryException {
		org.modeshape.jcr.api.ValueFactory valueFactory = (org.modeshape.jcr.api.ValueFactory)session.getValueFactory();
		Value values[] = new Value[strings.length];
		for (int i = 0; i < strings.length; i++) {
			values[i] = valueFactory.createValue(strings[i]);
        }
        return values;
    }

	public static boolean isFormControl(RestNode node) {
		return checkNodeType(node, "bpw:FormControl");
	}
	
	public static boolean isFormControlLayout(RestNode node) {
		return checkNodeType(node, "bpw:FormControlLayout");
	}
	
	private void populateAuthoringTemplate(AuthoringTemplate at, RestNode restNode) {
		
		restNode.getChildren().forEach(node -> {
			if (isElementFolderNode(node)) {
				this.populateElementControls(at, node);
			} else if (isPropertyFolderNode(node)) {
				this.populatePropertyControls(at, node);
			} else if (isElementGroupNode(node)) {
				List<BaseFormGroup> formGroups = this.populateFormGroups(node);
				at.setElementGroups(formGroups.toArray(new BaseFormGroup[formGroups.size()]));
			}
		});
		
	}
	
	private void populateForm(Form form, RestNode restNode) {
		
		restNode.getChildren().forEach(node -> {
			if (isElementFolderNode(node)) {
				this.populateFormControls(form, node);
			} else if (isElementGroupNode(node)) {
				List<BaseFormGroup> formGroups = this.populateFormGroups(node);
				form.setFormLayout(formGroups.toArray(new BaseFormGroup[formGroups.size()]));
			}
		});
	}

	private List<BaseFormGroup> populateFormGroups(RestNode node) {
		List<BaseFormGroup> formGroups = new ArrayList<>();
		node.getChildren().forEach(groupNode -> {
			Optional<BaseFormGroup> group = this.populateFormGroup(groupNode);
			if (group.isPresent()) {
				formGroups.add(group.get());
			}
		});
		
		return formGroups;
	}
	public static boolean isElementFolderNode(RestNode node) {
		return checkNodeType(node, "bpw:elementFolder");
	}

	public static boolean isPropertyFolderNode(RestNode node) {
		return checkNodeType(node, "bpw:propertyFolder");
	}
	
	public static boolean isElementGroupNode(RestNode node) {
		return checkNodeType(node, "bpw:formGroupFoler");
	}
	
	private void populateFormControls(Form form, RestNode restNode) {
		Map<String, FormControl> formControls = restNode.getChildren().stream()
				.filter(node -> checkNodeType(node, "bpw:FormControl")).map(this::toFormControl)
				.collect(Collectors.toMap(FormControl::getName, Function.identity()));
		form.setFormControls(formControls);
	}
	
	private void populateElementControls(AuthoringTemplate at, RestNode restNode) {
		Map<String, FormControl> formControls = restNode.getChildren().stream()
				.filter(node -> checkNodeType(node, "bpw:FormControl")).map(this::toFormControl)
				.collect(Collectors.toMap(FormControl::getName, Function.identity()));
		at.setElements(formControls);
	}

	private void populatePropertyControls(AuthoringTemplate at, RestNode restNode) {
		Map<String, FormControl> formControls = restNode.getChildren().stream()
				.filter(node -> checkNodeType(node, "bpw:FormControl")).map(this::toFormControl)
				.collect(Collectors.toMap(FormControl::getName, Function.identity()));
		AuthoringTemplateProperties atProperties = new AuthoringTemplateProperties();
		// atProperties.setAuthoringTemplate(formControls.get("authoringTemplate"));
		// atProperties.setNodeType(formControls.get("nodeType"));
		atProperties.setCategories(formControls.get("categories"));
		atProperties.setWorkflow(formControls.get("workflow"));

		atProperties.setName(formControls.get("name"));
		atProperties.setTitle(formControls.get("title"));
		atProperties.setDescription(formControls.get("description"));
		at.setProperties(atProperties);
	}
	
	private Optional<BaseFormGroup> populateFormGroup(RestNode node) {
		BaseFormGroup group = null;
		if (checkNodeType(node, "bpw:formTabs")) {
			group = populateFormTabs(node);
		} else if (checkNodeType(node, "bpw:formSteps")) {
			group = populateFormSteps(node);
		} else if (checkNodeType(node, "bpw:formRows")) {
			group = this.populateFormRows(node);
		} else if (checkNodeType(node, "bpw:formRow")) {
			group = this.populateFormRow(node);
		}
		return group == null ? Optional.empty() : Optional.of(group);
		// return Optional.of(group).orElse(Optional.<BaseFormGroup>empty());
	}
	
	private FormControl toFormControl(RestNode node) {
		FormControl formControl = new FormControl();
		formControl.setName(node.getName());
		
		for (RestProperty property : node.getJcrProperties()) {
			if ("bpw:controlType".equals(property.getName())) {
				formControl.setControlType(property.getValues().get(0));
			} else if ("bpw:fieldName".equals(property.getName())) {
				formControl.setFieldName(property.getValues().get(0));
			} else if ("bpw:dataType".equals(property.getName())) {
				formControl.setDataType(property.getValues().get(0));
			} else if ("bpw:format".equals(property.getName())) {
				formControl.setFormat(property.getValues().get(0));
			} else if ("bpw:jcrDataType".equals(property.getName())) {
				formControl.setJcrDataType(property.getValues().get(0));
			} else if ("bpw:multiple".equals(property.getName())) {
				formControl.setMultiple(Boolean.parseBoolean(property.getValues().get(0)));
			} else if ("bpw:useReference".equals(property.getName())) {
				formControl.setUseReference(Boolean.parseBoolean(property.getValues().get(0)));
			} else if ("bpw:mandatory".equals(property.getName())) {
				formControl.setMandatory(Boolean.parseBoolean(property.getValues().get(0)));
			} else if ("bpw:systemIndexed".equals(property.getName())) {
				formControl.setSystemIndexed(Boolean.parseBoolean(property.getValues().get(0)));
			} else if ("bpw:userSearchable".equals(property.getName())) {
				formControl.setUserSearchable(Boolean.parseBoolean(property.getValues().get(0)));
			} else if ("bpw:showInList".equals(property.getName())) {
				formControl.setShowInList(Boolean.parseBoolean(property.getValues().get(0)));
			} 
		}

		if (node.getChildren() != null) {
			Map<String, FormControl> fromControls = new HashMap<>();
			for (RestNode childNode: node.getChildren()) {
				if (isFormControl(childNode)) {
					fromControls.put(childNode.getName(), this.toFormControl(childNode));
				} else if (isFormControlLayout(childNode)) {
					formControl.setFormControlLayout(this.toFormControlLayout(childNode));
				} else if (checkNodeType(childNode, "bpw:CommonConstraint")) {
					formControl.setConstraint(this.toCommonConstraint(childNode));					
				} else if (checkNodeType(childNode, "bpw:ObjectConstraint")) {
					formControl.setObjectConstraint(this.toObjectConstraint(childNode));
				} else if (checkNodeType(childNode, "bpw:StringConstraint")) {
					formControl.setStringConstraint(this.toStringConstraint(childNode));
				} else if (checkNodeType(childNode, "bpw:NumberConstraint")) {
					formControl.setNumberConstraint(this.toNumberConstraint(childNode));
				} else if (checkNodeType(childNode, "bpw:ArrayConstraint")) {
					formControl.setArrayConstraint(this.toArrayConstraint(childNode));
				} else if (checkNodeType(childNode, "bpw:ConditionalConstraint")) {
					formControl.setConditionalConstraint(this.toConditionalConstraint(childNode));
				} else if (checkNodeType(childNode, "bpw:FormControls") && 
						"notConstraint".equals(childNode.getName())) {
					formControl.setNotConstraint(this.toFormControls(childNode));					
				} else if (checkNodeType(childNode, "bpw:FormControls") && 
						"allOf".equals(childNode.getName())) {
					formControl.setAllOfConstraint(this.toFormControls(childNode));
				} else if (checkNodeType(childNode, "bpw:FormControls") && 
						"oneOf".equals(childNode.getName())) {
					formControl.setOneOfConstraint(this.toFormControls(childNode));
				} else if (checkNodeType(childNode, "bpw:FormControls") && 
						"anyOf".equals(childNode.getName())) {
					formControl.setAnyOfConstraint(this.toFormControls(childNode));
				} else if (checkNodeType(childNode, "bpw:CustomConstraint")) {
					formControl.setCustomConstraint(this.toCustomConstraint(childNode));
				}
			}
			
			if (fromControls.size() > 0) {
				formControl.setFormControls(fromControls);
			}
		}
		
		return formControl;
	}

	private CommonConstraint toCommonConstraint(RestNode node) {
		CommonConstraint commonConstraint = new CommonConstraint();
		for (RestProperty property : node.getJcrProperties()) {
			if ("bpw:enum".equals(property.getName())) {
				commonConstraint.setEnumeration(property.getValues().toArray(new String[property.getValues().size()]));
			} else if ("bpw:defaultValue".equals(property.getName())) {
				commonConstraint.setDefaultValues(property.getValues().toArray(new String[property.getValues().size()]));
			} else if ("bpw:constant".equals(property.getName())) {
				commonConstraint.setConstant(property.getValues().get(0));
			} 
		}
		return commonConstraint;
	}
	
	private ObjectConstraint toObjectConstraint(RestNode  node) {
		ObjectConstraint objectConstraint = new ObjectConstraint();
		
		for (RestProperty property : node.getJcrProperties()) {
			if ("bpw:minProperties".equals(property.getName())) {
				objectConstraint.setMinProperties(Integer.parseInt(property.getValues().get(0)));
			} else if ("bpw:maxProperties".equals(property.getName())) {
				objectConstraint.setMaxProperties(Integer.parseInt(property.getValues().get(0)));
			} else if ("bpw:propertyNamePattern".equals(property.getName())) {
				objectConstraint.setPropertyNamePattern(property.getValues().get(0));
			} else if ("bpw:allowAdditionalProperties".equals(property.getName())) {
				objectConstraint.setAllowAdditionalProperties(Boolean.valueOf(property.getValues().get(0)));
			} else if ("bpw:required".equals(property.getName())) {
				objectConstraint.setRequired(property.getValues().toArray(new String[property.getValues().size()]));
			} 
		}
		
		for (RestNode childNode: node.getChildren()) {
			if (checkNodeType(childNode, "bpw:FormControls") && 
					"additionalProperties".equals(childNode.getName())) {
				objectConstraint.setAdditionalProperties(this.toFormControls(childNode));
			} else if (checkNodeType(childNode, "bpw:FormControls") && 
					"patternProperties".equals(childNode.getName())) {
				objectConstraint.setPatternProperties(this.toFormControls(childNode));
			} else if (checkNodeType(childNode, "bpw:FormControls") && 
					"schemaDependencies".equals(childNode.getName())) {
				objectConstraint.setSchemaDependencies(this.toFormControls(childNode));
			} else if (checkNodeType(childNode, "bpw:Dependencies") && 
					"dependencies".equals(childNode.getName())) {
				Map<String, String[]> dependencies = new HashMap<>();
				for (RestNode dependencyNode: childNode.getChildren()) {
					for (RestProperty dependencyProperty : dependencyNode.getJcrProperties()) {
						dependencies.put(dependencyNode.getName(), dependencyProperty.getValues()
								.toArray(new String[dependencyProperty.getValues().size()]));
						break;
					}
				}
				if (dependencies.size() > 0) {
					objectConstraint.setDependencies(dependencies);
				}
			}
		}
		return objectConstraint;
	}
	
	private StringConstraint toStringConstraint(RestNode node) {
		StringConstraint stringConstraint = new StringConstraint();
		
		for (RestProperty property : node.getJcrProperties()) {
			if ("bpw:format".equals(property.getName())) {
				stringConstraint.setFormat(property.getValues().get(0));
			} else if ("bpw:minLength".equals(property.getName())) {
				stringConstraint.setMinLength(Integer.parseInt(property.getValues().get(0)));
			} else if ("bpw:maxLength".equals(property.getName())) {
				stringConstraint.setMaxLength(Integer.parseInt(property.getValues().get(0)));
			} else if ("bpw:pattern".equals(property.getName())) {
				stringConstraint.setPattern(property.getValues().get(0));
			} else if ("bpw:contentMediaType".equals(property.getName())) {
				stringConstraint.setContentMediaType(property.getValues().get(0));
			} else if ("bpw:contentEncoding".equals(property.getName())) {
				stringConstraint.setContentEncoding(property.getValues().get(0));
			} else if ("bpw:contentSchema".equals(property.getName())) {
				stringConstraint.setContentSchema(property.getValues().get(0));
			} 
		}
		return stringConstraint;
	}
	
	private NumberConstraint toNumberConstraint(RestNode node) {
		NumberConstraint numberConstraint = new NumberConstraint();
		for (RestProperty property : node.getJcrProperties()) {
			if ("bpw:multipleOf".equals(property.getName())) {
				numberConstraint.setMultipleOf(Integer.parseInt(property.getValues().get(0)));
			} else if ("bpw:maximum".equals(property.getName())) {
				numberConstraint.setMaximum(Integer.parseInt(property.getValues().get(0)));
			} else if ("bpw:minimum".equals(property.getName())) {
				numberConstraint.setMinimum(Integer.parseInt(property.getValues().get(0)));
			} else if ("bpw:exclusiveMinimum".equals(property.getName())) {
				numberConstraint.setExclusiveMinimum(Boolean.parseBoolean(property.getValues().get(0)));
			} else if ("bpw:exclusiveMinimum".equals(property.getName())) {
				numberConstraint.setExclusiveMaximum(Boolean.parseBoolean(property.getValues().get(0)));
			} 
		}

		return numberConstraint;
	}
	
	private ArrayConstraint toArrayConstraint(RestNode node) {
		ArrayConstraint arrayConstraint = new ArrayConstraint();
		for (RestProperty property : node.getJcrProperties()) {
			if ("bpw:minItems".equals(property.getName())) {
				arrayConstraint.setMinItems(Integer.parseInt(property.getValues().get(0)));
			} else if ("bpw:maxItems".equals(property.getName())) {
				arrayConstraint.setMaxItems(Integer.parseInt(property.getValues().get(0)));
			} else if ("bpw:uniqueItems".equals(property.getName())) {
				arrayConstraint.setUniqueItems(Boolean.valueOf(property.getValues().get(0)));
			} else if ("bpw:contains".equals(property.getName())) {
				arrayConstraint.setContains(property.getValues().get(0));
			} 
		}

		for (RestNode childNode: node.getChildren()) {
			if (checkNodeType(childNode, "bpw:FormControls") && 
					"additionalItems".equals(childNode.getName())) {
				arrayConstraint.setAdditionalItems(this.toFormControls(childNode));
			}
		}
		return arrayConstraint;
	}
	
	private ConditionalConstraint toConditionalConstraint(RestNode node) {
		ConditionalConstraint conditionalConstraint = new ConditionalConstraint();
		for (RestNode childNode: node.getChildren()) {
			if (checkNodeType(childNode, "bpw:FormControls") && 
					"ifCondition".equals(childNode.getName())) {
				conditionalConstraint.setIfCondition(this.toFormControls(childNode));
			} else if (checkNodeType(childNode, "bpw:FormControls") && 
					"thenRule".equals(childNode.getName())) {
				conditionalConstraint.setThenRule(this.toFormControls(childNode));
				
			} else if (checkNodeType(childNode, "bpw:FormControls") && 
					"elseRule".equals(childNode.getName())) {
				conditionalConstraint.setElseRule(this.toFormControls(childNode));
			}
		}
	    return conditionalConstraint;	
	}
	
	private CustomConstraint toCustomConstraint(RestNode node) {
		CustomConstraint customConstraint = new CustomConstraint();
		List<JavascriptFunction> functions = new ArrayList<>();
		for (RestNode childNode: node.getChildren()) {
			if (checkNodeType(childNode, "bpw:JavascriptFunction")) {
				JavascriptFunction function = new JavascriptFunction();
				for (RestProperty property : node.getJcrProperties()) {
					if ("bpw:params".equals(property.getName())) {
						function.setParams(property.getValues().toArray(new String[property.getValues().size()]));
					} else if ("bpw:functionBody".equals(property.getName())) {
						function.setFunctionBody(property.getValues().get(0));
					}
				}
				functions.add(function);				
			}
		}
		
		if (functions.size() > 0) {
			customConstraint.setJavascriptFunction(functions.toArray(new JavascriptFunction[functions.size()]));
		}
		return customConstraint;
	}
	
	private Map<String, FormControl> toFormControls(RestNode node) {
		Map<String, FormControl> formControls = new HashMap<>();
		for (RestNode childNode: node.getChildren()) {
			if (isFormControl(childNode)) {
				formControls.put(childNode.getName(), this.toFormControl(childNode));
			}
		}
		return null;
	}
	
	private void populateBaseFormGroup(BaseFormGroup group, RestNode restNode) {
		for (RestProperty property : restNode.getJcrProperties()) {
			if ("bpw:groupTitle".equals(property.getName())) {
				group.setGroupTitle(property.getValues().get(0));
			} else if ("bpw:groupName".equals(property.getName())) {
				group.setGroupName(property.getValues().get(0));
			}
		}
		
		for (RestNode node: restNode.getChildren()) {
			if (checkNodeType(node, "bpw:VisbleCondition")) {
				group.setCondition(toVisibleCondition(node));
				break;
			}
		}
		
	}

	private FormTabs populateFormTabs(RestNode restNode) {
		FormTabs formTabs = new FormTabs();
		populateBaseFormGroup(formTabs, restNode);
		FormTab tabs[] = restNode.getChildren().stream().filter(node -> checkNodeType(node, "bpw:formTab"))
				.map(this::populateFormTab).toArray(FormTab[]::new);
		formTabs.setTabs(tabs);
		return formTabs;
	}

	private FormRows populateFormRows(RestNode restNode) {
		FormRows formRows = new FormRows();
		populateBaseFormGroup(formRows, restNode);
		FormRow rows[] = restNode.getChildren().stream().filter(node -> checkNodeType(node, "bpw:formRow"))
				.map(this::populateFormRow).toArray(FormRow[]::new);
		formRows.setRows(rows);
		return formRows;
	}

	private FormStep populateFormStep(RestNode restNode) {
		FormStep formStep = new FormStep();
		formStep.setStepName(restNode.getName());
		for (RestProperty property : restNode.getJcrProperties()) {
			if ("bpw:stepName".equals(property.getName())) {
				formStep.setStepTitle(property.getValues().get(0));
				break;
			}
		}
		List<BaseFormGroup> formGroups = new ArrayList<>();
		restNode.getChildren().forEach(node -> {
			if (checkNodeType(node, "bpw:VisbleCondition")) {
				formStep.setCondition(this.toVisibleCondition(node));
			} else {
				Optional<BaseFormGroup> group = this.populateFormGroup(node);
				if (group.isPresent()) {
					formGroups.add(group.get());
				}
			}
		});
		formStep.setFormGroups(formGroups.toArray(new BaseFormGroup[formGroups.size()]));
		return formStep;
	}

	private FormTab populateFormTab(RestNode restNode) {
		FormTab formTab = new FormTab();
		formTab.setTabName(restNode.getName());
		for (RestProperty property : restNode.getJcrProperties()) {
			if ("bpw:tabName".equals(property.getName())) {
				formTab.setTabTitle(property.getValues().get(0));
				break;
			}
		}
		List<BaseFormGroup> formGroups = new ArrayList<>();
		restNode.getChildren().forEach(node -> {
			if (checkNodeType(node, "bpw:VisbleCondition")) {
				formTab.setCondition(this.toVisibleCondition(node));
			} else {
				Optional<BaseFormGroup> group = this.populateFormGroup(node);
				if (group.isPresent()) {
					formGroups.add(group.get());
				}
			}
		});
		formTab.setFormGroups(formGroups.toArray(new BaseFormGroup[formGroups.size()]));

		return formTab;
	}

	private FormRow populateFormRow(RestNode restNode) {
		FormRow formRow = new FormRow();
		formRow.setRowName(restNode.getName());
		for (RestProperty property : restNode.getJcrProperties()) {
			if ("bpw:rowName".equals(property.getName())) {
				formRow.setRowTitle(property.getValues().get(0));
				break;
			}
		}
		FormColumn columns[] = restNode.getChildren().stream()
				.filter(node -> checkNodeType(node, "bpw:formColumn")).map(this::populateFormColumn)
				.toArray(FormColumn[]::new);
		formRow.setColumns(columns);
		return formRow;
	}

	private FormSteps populateFormSteps(RestNode restNode) {
		FormSteps formSteps = new FormSteps();
		populateBaseFormGroup(formSteps, restNode);
		FormStep steps[] = restNode.getChildren().stream().filter(node -> checkNodeType(node, "bpw:formStep"))
				.map(this::populateFormStep).toArray(FormStep[]::new);
		formSteps.setSteps(steps);
		return formSteps;
	}
	
	private FormColumn populateFormColumn(RestNode columnNode) {
		FormColumn column = new FormColumn();
		for (RestProperty property : columnNode.getJcrProperties()) {
			if ("bpw:id".equals(property.getName())) {
				column.setId(property.getValues().get(0));
			} else if ("equals".equals(property.getName())) {
				column.setFxFlex(Integer.parseInt(property.getValues().get(0)));
			} else if ("bpw:fieldNames".equals(property.getName())) {
				column.setFormControls(property.getValues().toArray(new String[property.getValues().size()]));
			}
		}

		if (columnNode.getChildren() != null && columnNode.getChildren().size() > 0) {
			List<BaseFormGroup> formGroups = new ArrayList<>();
			for (RestNode node: columnNode.getChildren()) {
				if (checkNodeType(node, "bpw:VisbleCondition")) {
					column.setCondition(toVisibleCondition(node));
				} else {
					Optional<BaseFormGroup> formGroup = this.populateFormGroup(node);
					if (formGroup.isPresent()) {
						formGroups.add(formGroup.get());
					}
				}				
			}
			if (formGroups.size() > 0) {
				column.setFormGroups(formGroups.toArray(new BaseFormGroup[formGroups.size()]));
			}
		}
		
		return column;
	}

	private VisbleCondition toVisibleCondition(RestNode node) {
		VisbleCondition visbleCondition = new VisbleCondition();
		for (RestProperty property : node.getJcrProperties()) {
			if ("bpw:functionBody".equals(property.getName())) {
				visbleCondition.setFunctionBody(property.getValues().get(0));
			} else if ("bpw:condition".equals(property.getName())) {
				visbleCondition.setCondition(property.getValues().get(0));
			} 
		}
		return visbleCondition;
	}
	
	private FormControlLayout toFormControlLayout(RestNode node) {

		FormControlLayout fieldLayout = new FormControlLayout();
		for (RestProperty property : node.getJcrProperties()) {
			if ("bpw:title".equals(property.getName())) {
				fieldLayout.setTitle(property.getValues().get(0));
			} else if ("bpw:hint".equals(property.getName())) {
				fieldLayout.setHint(property.getValues().get(0));
			} else if ("bpw:editable".equals(property.getName())) {
				fieldLayout.setEditable(Boolean.parseBoolean(property.getValues().get(0)));
			} else if ("bpw:expandable".equals(property.getName())) {
				fieldLayout.setExpandable(Boolean.parseBoolean(property.getValues().get(0)));
			} else if ("bpw:expanded".equals(property.getName())) {
				fieldLayout.setExpanded(Boolean.parseBoolean(property.getValues().get(0)));
			} else if ("bpw:rows".equals(property.getName())) {
				fieldLayout.setRows(Integer.parseInt(property.getValues().get(0)));
			} else if ("bpw:cols".equals(property.getName())) {
				fieldLayout.setCols(Integer.parseInt(property.getValues().get(0)));
			} else if ("bpw:displayFlex".equals(property.getName())) {
				fieldLayout.setDisplayFlex(Boolean.parseBoolean(property.getValues().get(0)));
			} else if ("bpw:listItems".equals(property.getName())) {
				fieldLayout.setListItems(Integer.parseInt(property.getValues().get(0)));
			} else if ("bpw:flexDirection".equals(property.getName())) {
				fieldLayout.setFlexDirection(property.getValues().get(0));
			} else if ("bpw:placeHolder".equals(property.getName())) {
				fieldLayout.setPlaceHolder(property.getValues().get(0));
			} else if ("bpw:flex".equals(property.getName())) {
				fieldLayout.setFlex(property.getValues().get(0));
			} 
		}
		return fieldLayout;
	}
	
	private NodeTypeTemplate createElementFolderNodeType(
			String name, 
			String[] superTypes, 
			NodeTypeManager nodeTypeManager,
			boolean orderable) throws RepositoryException {
		NodeTypeTemplate nodeTypeTemplate = nodeTypeManager.createNodeTypeTemplate();
		nodeTypeTemplate.setName(name);
		nodeTypeTemplate.setDeclaredSuperTypeNames(superTypes);
		nodeTypeTemplate.setAbstract(false);
		nodeTypeTemplate.setMixin(false);
		nodeTypeTemplate.setOrderableChildNodes(orderable);
		nodeTypeTemplate.setQueryable(true);
        
        return nodeTypeTemplate;
	}
	
	private PropertyDefinitionTemplate createPropertyDefinitionDefault(String name, NodeTypeManager nodeTypeManager)
			throws RepositoryException {
		PropertyDefinitionTemplate propertyDef = nodeTypeManager.createPropertyDefinitionTemplate();
		propertyDef.setName(name);
		propertyDef.setAutoCreated(false);
		propertyDef.setMandatory(false);
		propertyDef.setProtected(false);
		propertyDef.setOnParentVersion(OnParentVersionAction.COPY);
		propertyDef.setMultiple(false);
		propertyDef.setFullTextSearchable(true);
		propertyDef.setQueryOrderable(true);
		propertyDef.setRequiredType(PropertyType.STRING);
        return propertyDef;
	}
	
	private NodeDefinitionTemplate createNodeDefinitionTemplate(
			String name, String[] nodeTypes, NodeTypeManager nodeTypeManager) throws RepositoryException {
		NodeDefinitionTemplate nodeDefinition = nodeTypeManager.createNodeDefinitionTemplate();
		nodeDefinition.setName(name);
	    
	    nodeDefinition.setRequiredPrimaryTypeNames(nodeTypes);
	    nodeDefinition.setMandatory(true);
	    nodeDefinition.setProtected(false);
	    nodeDefinition.setOnParentVersion(javax.jcr.version.OnParentVersionAction.COPY);
	    nodeDefinition.setSameNameSiblings(false);
	    
	    return nodeDefinition;
	}
	
    
	private PropertyDefinitionTemplate createPropertyDefinitionTemplate(
			String name, 
			FormControl formControl, 
			Session session, 
			NodeTypeManager nodeTypeManager) throws RepositoryException {
		
		PropertyDefinitionTemplate propertydef = nodeTypeManager.createPropertyDefinitionTemplate();
		propertydef.setName(name);
    	propertydef.setAutoCreated(false);
    	propertydef.setMandatory(formControl.isMandatory());
    	propertydef.setProtected(false);
    	propertydef.setOnParentVersion(OnParentVersionAction.COPY);
    	propertydef.setMultiple(formControl.isMandatory());
    	propertydef.setFullTextSearchable(formControl.isUserSearchable());
    	propertydef.setQueryOrderable(true);
    	if (StringUtils.hasText(formControl.getJcrDataType())) {
    		propertydef.setRequiredType(PropertyType.valueFromName(StringUtils.capitalize(formControl.getJcrDataType().toLowerCase())));
    	} else {
    		propertydef.setRequiredType(PropertyType.STRING);
    	}
    	if (formControl.getConstraint() != null) {
	    	if (formControl.getConstraint().getDefaultValues() != null) {
	    		propertydef.setDefaultValues(getValues(formControl.getConstraint().getDefaultValues(), session));
	    	}
	
	        if (formControl.getConstraint().getEnumeration() != null) {
	        	propertydef.setValueConstraints(formControl.getConstraint().getEnumeration());
	        } 
    	}
        return propertydef;
	}
}
