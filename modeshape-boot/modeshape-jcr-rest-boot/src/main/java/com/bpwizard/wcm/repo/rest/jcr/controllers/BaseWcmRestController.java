package com.bpwizard.wcm.repo.rest.jcr.controllers;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.jcr.Node;
import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.servlet.http.HttpServletRequest;

import org.modeshape.web.jcr.RepositoryManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;

import com.bpwizard.wcm.repo.rest.JsonUtils;
import com.bpwizard.wcm.repo.rest.RestHelper;
import com.bpwizard.wcm.repo.rest.WcmUtils;
import com.bpwizard.wcm.repo.rest.handler.RestItemHandler;
import com.bpwizard.wcm.repo.rest.handler.RestNodeTypeHandler;
import com.bpwizard.wcm.repo.rest.handler.RestRepositoryHandler;
import com.bpwizard.wcm.repo.rest.handler.RestServerHandler;
import com.bpwizard.wcm.repo.rest.jcr.exception.WcmRepositoryException;
import com.bpwizard.wcm.repo.rest.jcr.model.AuthoringTemplate;
import com.bpwizard.wcm.repo.rest.jcr.model.BaseFormGroup;
import com.bpwizard.wcm.repo.rest.jcr.model.ContentAreaLayout;
import com.bpwizard.wcm.repo.rest.jcr.model.ControlField;
import com.bpwizard.wcm.repo.rest.jcr.model.ControlFieldMetadata;
import com.bpwizard.wcm.repo.rest.jcr.model.FieldLayout;
import com.bpwizard.wcm.repo.rest.jcr.model.Footer;
import com.bpwizard.wcm.repo.rest.jcr.model.FormColumn;
import com.bpwizard.wcm.repo.rest.jcr.model.FormControl;
import com.bpwizard.wcm.repo.rest.jcr.model.FormRow;
import com.bpwizard.wcm.repo.rest.jcr.model.FormRows;
import com.bpwizard.wcm.repo.rest.jcr.model.FormStep;
import com.bpwizard.wcm.repo.rest.jcr.model.FormSteps;
import com.bpwizard.wcm.repo.rest.jcr.model.FormTab;
import com.bpwizard.wcm.repo.rest.jcr.model.FormTabs;
import com.bpwizard.wcm.repo.rest.jcr.model.JsonForm;
import com.bpwizard.wcm.repo.rest.jcr.model.WcmProperty;
import com.bpwizard.wcm.repo.rest.jcr.model.LayoutColumn;
import com.bpwizard.wcm.repo.rest.jcr.model.LayoutRow;
import com.bpwizard.wcm.repo.rest.jcr.model.Library;
import com.bpwizard.wcm.repo.rest.jcr.model.NavBar;
import com.bpwizard.wcm.repo.rest.jcr.model.Navigation;
import com.bpwizard.wcm.repo.rest.jcr.model.NavigationBadge;
import com.bpwizard.wcm.repo.rest.jcr.model.NavigationItem;
import com.bpwizard.wcm.repo.rest.jcr.model.NavigationType;
import com.bpwizard.wcm.repo.rest.jcr.model.PageLayout;
import com.bpwizard.wcm.repo.rest.jcr.model.RenderTemplate;
import com.bpwizard.wcm.repo.rest.jcr.model.RenderTemplateLayoutColumn;
import com.bpwizard.wcm.repo.rest.jcr.model.RenderTemplateLayoutRow;
import com.bpwizard.wcm.repo.rest.jcr.model.ResourceElementRender;
import com.bpwizard.wcm.repo.rest.jcr.model.ResourceViewer;
import com.bpwizard.wcm.repo.rest.jcr.model.SearchData;
import com.bpwizard.wcm.repo.rest.jcr.model.SidePane;
import com.bpwizard.wcm.repo.rest.jcr.model.SidePanel;
import com.bpwizard.wcm.repo.rest.jcr.model.SiteArea;
import com.bpwizard.wcm.repo.rest.jcr.model.SiteAreaLayout;
import com.bpwizard.wcm.repo.rest.jcr.model.SiteConfig;
import com.bpwizard.wcm.repo.rest.jcr.model.Toolbar;
import com.bpwizard.wcm.repo.rest.jcr.model.WcmProperties;
import com.bpwizard.wcm.repo.rest.modeshape.model.RestChildType;
import com.bpwizard.wcm.repo.rest.modeshape.model.RestNode;
import com.bpwizard.wcm.repo.rest.modeshape.model.RestNodeType;
import com.bpwizard.wcm.repo.rest.modeshape.model.RestProperty;
import com.bpwizard.wcm.repo.rest.modeshape.model.RestPropertyType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

public abstract class BaseWcmRestController {
	
	protected static final String WCM_ROOT_PATH = "/bpwizard/library";
	protected static final String WCM_ROOT_PATH_PATTERN = "/bpwizard/library/%s";
	protected static final String WCM_AT_PATH_PATTERN = "/bpwizard/library/%s/authoringTemplate/%s";
	protected static final String WCM_AT_ROOT_PATH_PATTERN = "/bpwizard/library/%s/authoringTemplate";
	protected static final String WCM_RT_PATH_PATTERN = "/bpwizard/library/%s/renderTemplate/%s";
	protected static final String WCM_CONTENT_LAYOUT_PATH_PATTERN = "/bpwizard/library/%s/contentAreaLayout/%s";
	protected static final String WCM_SITECONFIG_PATH_PATTERN = "/bpwizard/library/%s/siteConfig/%s";
	protected static final String WCM_CATEGORY_PATH_PATTERN = "/bpwizard/library/%s/category%s";
	protected static final String WCM_VALIDATTOR_PATH_PATTERN = "/bpwizard/library/%s/validationRule/%s";
	protected static final String WCM_WORKFLOW_PATH_PATTERN = "/bpwizard/library/%s/workflow/%s";
	protected static final String WCM_QUERY_PATH_PATTERN = "/bpwizard/library/%s/query/%s";
	protected static final String DEFAULT_WS = "default";
	protected static final String DRAFT_WS = "draft";
	protected static final String EXPIRED_WS = "expired";
	
	@Value("${bpw.modeshape.authoring.enabled:true}")
	protected boolean authoringEnabled = true;
	
	@Autowired
	protected RestItemHandler itemHandler;

	@Autowired
	protected RepositoryManager repositoryManager;
	
	@Autowired
	protected RestRepositoryHandler repositoryHandler;

	@Autowired
	protected RestNodeTypeHandler nodeTypeHandler;
	
	@Autowired
	protected RestServerHandler serverHandler;

	@Autowired
	protected WcmUtils wcmUtils;
	
	protected ObjectMapper objectMapper = new ObjectMapper();
	
	protected boolean isControlField(RestNode node) {
		return this.wcmUtils.checkNodeType(node, "bpw:controlField");
	}

	protected boolean isSiteArea(RestNode node) {
		return this.wcmUtils.checkNodeType(node, "bpw:siteArea");
	}
	
	protected boolean isControlFieldMetaData(RestNode node) {
		return this.wcmUtils.checkNodeType(node, "bpw:controlFieldMetaData");
	}

	protected boolean isLibrary(RestNode node) {
		return this.wcmUtils.checkNodeType(node, "bpw:library");
	}

	protected boolean notSystemLibrary(RestNode node) {
		return !"system".equalsIgnoreCase(node.getName());
	}
	
	protected boolean isTheme(RestNode node) {
		return this.wcmUtils.checkNodeType(node, "bpw:themeType");
	}

	protected boolean isRenderTemplate(RestNode node) {
		return this.wcmUtils.checkNodeType(node, "bpw:renderTemplate");
	}

	protected boolean isContentAreaLayout(RestNode node) {
		return this.wcmUtils.checkNodeType(node, "bpw:contentAreaLayout");
	}

	protected boolean isAuthortingTemplate(RestNode node) {
		return this.wcmUtils.checkNodeType(node, "bpw:authoringTemplate");
	}
	
  	protected void doPurgeWcmItem(
  			String repository,
		    String workspace,
  			String absPath) { 
  		
  		absPath = (absPath.startsWith("/")) ? absPath : String.format("/%s", absPath);
  		try {
  			if (this.authoringEnabled) {
  				try {
		  			Session draftSession = this.repositoryManager.getSession(repository, "draft");
		  			Node draftNode = draftSession.getNode(absPath);
		  			// String workflowState = node.getProperty("bpw:currentLifecycleState").getValue().getString();
		            // if ("Expired".equals(workflowState)) {
		  			    draftNode.remove();
		            	draftSession.save();
		            // }
  				} catch (Exception e) {
  					//TODO: 
  					e.printStackTrace();
  				}
  				try {
	            	Session expiredSession = this.repositoryManager.getSession(repository, "expired");
		  			Node expiredNode = expiredSession.getNode(absPath);
		  			// String workflowState = node.getProperty("bpw:currentLifecycleState").getValue().getString();
		            // if ("Expired".equals(workflowState)) {
		  			expiredNode.remove();
		  			expiredSession.save();
		            // }
  				} catch (Exception e) {
  					//TODO: 
  					e.printStackTrace();
  				}
  			}
  			
  			try {
  				Session session = this.repositoryManager.getSession(repository, "default");
	  			Node node = session.getNode(absPath);
	            node.remove();
	            session.save();
  			} catch (PathNotFoundException ex) {
  				//logger.warn(String.format("Content item %s does not exist", absPath));
  			}
		} catch (WcmRepositoryException e ) {
			throw e;
		} catch (RepositoryException re) { 
			throw new WcmRepositoryException(re);
	    } catch (Throwable t) {
			throw new WcmRepositoryException(t);
		}
  	};
  	
	protected boolean filterLibrary(Library library, String filter) {
		return StringUtils.hasText(filter) ? library.getName().startsWith(filter) : true;
	}
	
	protected Map<String, JsonForm[]> doGetAuthoringTemplateAsJsonForm(String repository,
			String workspace, HttpServletRequest request) 
			throws WcmRepositoryException {

		String baseUrl = RestHelper.repositoryUrl(request);
		Map<String, JsonForm[]> jsonForms =  this.getAuthoringTemplateLibraries(repository, workspace, baseUrl)
				.flatMap(at -> this.getAuthoringTemplates(at, baseUrl))
				.map(at -> this.toJsonForms(request, at)).collect(Collectors.toMap(
						jfs -> String.format(WCM_AT_PATH_PATTERN, jfs[0].getLibrary(), jfs[0].getResourceType()), 
						Function.identity()));

		
		return jsonForms;
	}
	
	protected Map<String, JsonForm[]> doGetApplicationJsonForm(String repository,
			String workspace, String library, HttpServletRequest request) 
			throws WcmRepositoryException {

		String baseUrl = RestHelper.repositoryUrl(request);
		AuthoringTemplate libraryAt = new AuthoringTemplate();
		libraryAt.setRepository(repository);
		libraryAt.setWorkspace(workspace);
		libraryAt.setLibrary(library);
		Map<String, JsonForm[]> jsonForms = this.getAuthoringTemplates(libraryAt, baseUrl)
				.map(at -> this.toJsonForms(request, at))
				.collect(Collectors.toMap(
					jfs -> String.format(WCM_AT_PATH_PATTERN, jfs[0].getLibrary(), jfs[0].getResourceType()), 
					Function.identity()));

		return jsonForms;
	}
	
	protected Stream<AuthoringTemplate> getAuthoringTemplateLibraries(String repository, String workspace,
			String baseUrl) throws WcmRepositoryException {
		try {
			RestNode bpwizardNode = (RestNode) this.itemHandler.item(baseUrl, repository, workspace,
					WCM_ROOT_PATH, 1);
			return bpwizardNode.getChildren().stream().filter(this::isLibrary)
					.map(node -> toAuthoringTemplateWithLibrary(node, repository, workspace));
		} catch (RepositoryException e) {
			throw new WcmRepositoryException(e);
		}
	}
	
	protected AuthoringTemplate toAuthoringTemplateWithLibrary(RestNode node, String repository, String workspace) {
		AuthoringTemplate authoringTemplateWithLibrary = new AuthoringTemplate();
		authoringTemplateWithLibrary.setRepository(repository);
		authoringTemplateWithLibrary.setWorkspace(workspace);
		authoringTemplateWithLibrary.setLibrary(node.getName());
		return authoringTemplateWithLibrary;
	}
	
	protected  JsonForm[] toJsonForms(HttpServletRequest request, AuthoringTemplate at) throws WcmRepositoryException {
	    return new JsonForm[] {
            this.toJsonForm(request, at, false),
            this.toJsonForm(request, at, true)
	    };
	}
	
	protected  JsonForm toJsonForm(HttpServletRequest request, AuthoringTemplate at, boolean editMode) throws WcmRepositoryException {
		try {
			//JsonForm jsonFormCreate = new JsonForm();
			//JsonForm jsonFormEdit = new JsonForm();
			JsonForm jsonForm = new JsonForm();
			jsonForm.setRepository(at.getRepository());
			jsonForm.setWorkspace(at.getWorkspace());
			jsonForm.setLibrary(at.getLibrary());
			jsonForm.setResourceType(at.getName());
			ObjectNode jsonNode = JsonNodeFactory.instance.objectNode();
			ObjectNode schemaNode = JsonNodeFactory.instance.objectNode();
			jsonNode.set("schema", schemaNode);
			schemaNode.put("type", "object");

			//TODO: 
			if (StringUtils.hasText(at.getName())) {
				schemaNode.put("name", at.getName());
			}
			
			if (StringUtils.hasText(at.getTitle())) {
				schemaNode.put("title", at.getTitle());
			}
	
			if (StringUtils.hasText(at.getDescription())) {
				schemaNode.put("description", at.getDescription());
			}
			
			ObjectNode properties = this.objectMapper.createObjectNode();	
			ObjectNode definitions = this.objectMapper.createObjectNode();
			if (at.getProperties() != null && at.getProperties().size() > 0) {
				ObjectNode propertyPropertiesNode = this.objectMapper.createObjectNode();
				propertyPropertiesNode.put("type", "object");
				ObjectNode propertyProperties = this.objectMapper.createObjectNode();
				propertyPropertiesNode.set("properties", propertyProperties);
				properties.set("properties", propertyPropertiesNode);
				this.popluateFormControls(
						request, 
						at.getRepository(), 
						at.getWorkspace(),
						properties,
						propertyProperties,
						definitions,
						at.getProperties(),
						editMode);
				String requiredProperties[] = at.getProperties().entrySet().stream().map(entry -> entry.getValue())
						.filter(formControl -> formControl.isMandatory()).map(formControl -> formControl.getName())
						.toArray(String[]::new);				
				if (requiredProperties != null && requiredProperties.length > 0) {
					ArrayNode reqiuriedNode = this.objectMapper.createArrayNode();
					for (String name : requiredProperties) {
						reqiuriedNode.add(name);
					}
					propertyPropertiesNode.set("required", reqiuriedNode);
				}
			}
						
			if (at.getElements() != null && at.getElements().size() > 0) {
				ObjectNode elementPropertiesNode = this.objectMapper.createObjectNode();
				elementPropertiesNode.put("type", "object");
				ObjectNode elementProperties = this.objectMapper.createObjectNode();
				elementPropertiesNode.set("properties", elementProperties);
				properties.set("elements", elementPropertiesNode);
				this.popluateFormControls(
						request, 
						at.getRepository(), 
						at.getWorkspace(),
						properties,
						elementProperties,
						definitions,
						at.getElements(),
						editMode);
				String requiredElements[] = at.getElements().entrySet().stream().map(entry -> entry.getValue())
						.filter(formControl -> formControl.isMandatory()).map(formControl -> formControl.getName())
						.toArray(String[]::new);		
				if (requiredElements != null && requiredElements.length > 0) {
					ArrayNode reqiuriedNode = this.objectMapper.createArrayNode();
					for (String name : requiredElements) {
						reqiuriedNode.add(name);
					}
					elementPropertiesNode.set("required", reqiuriedNode);
				}		
			}
			
			// properties.addAll(propertyNodes);
			schemaNode.set("properties", properties);
			//TODO: add definitions node only when needed
			schemaNode.set("definitions", definitions);
					
			jsonNode.set("layout", this.toFormLayoutNode(at));
			// "params" node
			// "validate": false
	
			// data: object to populate the form with default or previously submitted values
			// options: object to set any global options for the form
			// widgets: object to add custom widgets
			// language: string to set the error message language (currently supports 'en'
			// and 'fr')
			// framework: string or object to set which framework to use
			jsonForm.setFormSchema(jsonNode);
	
			// return new JsonForm[] {jsonFormCreate, jsonFormEdit};
			return jsonForm;
		} catch (RepositoryException e) {
			throw new WcmRepositoryException(e);
		}
	}

	protected void popluateFormControls(
			HttpServletRequest request, 
			String repository, 
			String workspace,
			ObjectNode properties,
			ObjectNode simpleProperties,
			ObjectNode definitions,
			Map<String, FormControl> formControls,
			boolean editMode) throws RepositoryException {
		
		for (String key : formControls.keySet()) {
			FormControl formControl = formControls.get(key);
//			if ("properties".equals(formControl.getControlType())) {
//				ObjectNode definition = this.toTypeDefinition(request, repository, workspace, "bpw:properties", false);
//				definitions.set("properties", definition);
//				ObjectNode objectNode = this.toPropertyNode(formControl, definitions);
//				properties.set(key, objectNode);
//			} else 
			if ("association".equals(formControl.getControlType())) {
				ObjectNode definition = this.toTypeDefinition(request, repository, workspace, formControl.getJcrDataType(),
						false);
				String fieldName = this.fieldNameFromNodeTypeName(formControl.getJcrDataType());
				definitions.set(fieldName, definition);
				ObjectNode objectNode = this.toPropertyNode(formControl, definitions, editMode);
				properties.set(key, objectNode);
			} else {
			  ObjectNode objectNode = this.toPropertyNode(formControl, definitions, editMode);
			  simpleProperties.set(key, objectNode);
			}
		}
	}
	
	protected ObjectNode toJsonFormField(RestPropertyType restProperty) {
		ObjectNode objectNode = this.objectMapper.createObjectNode();
		if (restProperty.isMultiple()) {
			objectNode.put("type", "array");
			ObjectNode itemNode = this.objectMapper.createObjectNode();
			itemNode.put("type", "string");//restProperty.getRequiredType());
			objectNode.set("items", itemNode);
		} else {
			objectNode.put("type", "string");//restProperty.getRequiredType());
		}
		return objectNode;
	}
	
	protected ArrayNode toFormLayoutNode(AuthoringTemplate at) {
		ArrayNode formNode = this.objectMapper.createArrayNode();
		if (at.getPropertyRow() != null && at.getPropertyRow().getColumns().length > 0) {
			formNode.add(this.getRowNode(at, at.getPropertyRow()));
		}
		if (at.getElementGroups() != null && at.getElementGroups().length > 0) {
			for (BaseFormGroup formGroup : at.getElementGroups()) {
				if (formGroup instanceof FormRow) {
					ObjectNode rowNode = this.getRowNode(at, (FormRow) formGroup);
					formNode.add(rowNode);
				}
	
				if (formGroup instanceof FormRows) {
					for (FormRow formRow : ((FormRows) formGroup).getRows()) {
						ObjectNode rowNode = this.getRowNode(at, (FormRow) formRow);
						formNode.add(rowNode);
					}
				}
	
				if (formGroup instanceof FormTabs) {
					ObjectNode tabsNode = this.objectMapper.createObjectNode();
					tabsNode.put("type", "tabs");
					ArrayNode tabArrayNode = this.objectMapper.createArrayNode();
					for (FormTab formTab : ((FormTabs) formGroup).getTabs()) {
						ObjectNode tabNode = this.objectMapper.createObjectNode();
						tabNode.put("title", formTab.getTabTitle());
						ArrayNode tabItemNodes = this.objectMapper.createArrayNode();
						for (BaseFormGroup formRow : formTab.getFormGroups()) {
							ObjectNode rowNode = this.getRowNode(at, (FormRow) formRow);
							tabItemNodes.add(rowNode);
						}
						tabNode.set("items", tabItemNodes);
						tabArrayNode.add(tabNode);
					}
					tabsNode.set("tabs", tabArrayNode);
					formNode.add(tabsNode);
				}
	
				if (formGroup instanceof FormSteps) {
					ObjectNode stepsNode = this.objectMapper.createObjectNode();
					stepsNode.put("type", "stepper");
					ArrayNode stepArrayNode = this.objectMapper.createArrayNode();
					for (FormStep formStep : ((FormSteps) formGroup).getSteps()) {
						ObjectNode stepNode = this.objectMapper.createObjectNode();
						stepNode.put("title", formStep.getStepTitle());
						ArrayNode stepItemNodes = this.objectMapper.createArrayNode();
						for (BaseFormGroup formRow : formStep.getFormGroups()) {
							ObjectNode rowNode = this.getRowNode(at, (FormRow) formRow);
							stepItemNodes.add(rowNode);
						}
						stepNode.set("items", stepItemNodes);
						stepArrayNode.add(stepNode);
					}
					stepsNode.set("steps", stepArrayNode);
					formNode.add(stepsNode);
				}
				// at.getFormControls()
				// type ace, enum
	
			}
		}
		return formNode;
	}
	
	protected ObjectNode toTypeDefinition(HttpServletRequest request, String repository, String workspace,
			RestChildType restChildType) throws RepositoryException {
		return this.toTypeDefinition(request, repository, workspace, restChildType.getRequiredPrimaryTypeNames()[0],
				"*".equals(restChildType.getName()));
	}
	
	protected ObjectNode toTypeDefinition(HttpServletRequest request, String repository, String workspace,
			String jcrDataType, boolean multiple) throws RepositoryException {
		String baseUrl = RestHelper.repositoryUrl(request);
		RestNodeType restNodeType = nodeTypeHandler.getNodeType(baseUrl, repository, workspace, jcrDataType);

		ObjectNode definition = this.objectMapper.createObjectNode();
		ObjectNode itemsNode;

		if (multiple) {
			definition.put("type", "array");
			itemsNode = this.objectMapper.createObjectNode();
			itemsNode.put("type", "object");
			definition.set("items", itemsNode);
		} else {
			definition.put("type", "object");
			itemsNode = definition;
		}

		ObjectNode properties = this.objectMapper.createObjectNode();
		itemsNode.set("properties", properties);
		for (RestPropertyType restProperty : restNodeType.getPropertyTypes()) {
			ObjectNode fieldNode = this.toJsonFormField(restProperty);
			String fieldName = this.fieldNameFromNodeTypeName(restProperty.getName());
			properties.set(fieldName, fieldNode);
		}
		List<RestChildType> childTypes = restNodeType.getChildTypes();
		if (childTypes != null && childTypes.size() > 0) {
			for (RestChildType restChildType : childTypes) {
				String fieldName = "*".equals(restChildType.getName())
						? this.fieldNameFromNodeTypeName(restChildType.getDeclaringNodeTypeName())
						: restChildType.getName();
				ObjectNode childType = this.toTypeDefinition(request, repository, workspace, restChildType);
				properties.set(fieldName, childType);
			}
		}
		return definition;
		
	}
	
	protected String fieldNameFromNodeTypeName(String typeName) {
		return typeName.startsWith("bpw:") ? typeName.substring("bpw:".length()) : typeName;
	}
	
	protected ObjectNode getRowNode(AuthoringTemplate at, FormRow formRow) {
		ObjectNode rowNode = this.objectMapper.createObjectNode();
		rowNode.put("type", "flex");
		rowNode.put("flex-flow", "row wrap");
		ArrayNode columnNodes = this.objectMapper.createArrayNode();
		for (FormColumn column : formRow.getColumns()) {
			ObjectNode columnNode = this.getColumnNode(at, column);
			columnNodes.add(columnNode);
		}
		// { , , "items": [ "first_name", "last_name" ] },
		rowNode.set("items", columnNodes);
		return rowNode;
	}
	
	private ObjectNode toPropertyNode(FormControl formControl, ObjectNode definitions, boolean editMode) {
		ObjectNode propertyNode = JsonNodeFactory.instance.objectNode();
		// propertyNode.put("name", formControl.getName());
		// propertyNode.put("type", formControl.getDataType());
//		if ("properties".equals(formControl.getControlName())) {
//			propertyNode.put("$ref", "#/definitions/properties");
//		} else 
		if ("association".equals(formControl.getControlType())) {
			propertyNode.put("$ref",
					String.format("#/definitions/%s", this.fieldNameFromNodeTypeName(formControl.getJcrDataType())));
		} else {
			propertyNode.put("type", formControl.getDataType());
		}
		
		if (formControl.getEnumeration() != null && formControl.getEnumeration().length > 0) {
			propertyNode.set("enum", this.toArrayNode(formControl.getEnumeration()));
		}
		// type boolean, number
		// range: integer:
		// "minimum": 7,
		/// "maximum": 77,
		// "format": "color"

		// emum: "enum": [ "male", "female", "alien" ]

		// array
		if (StringUtils.hasText(formControl.getHint())) {
			propertyNode.put("description", formControl.getHint());
		}
		
		if (editMode && !formControl.isEditable()) {
			propertyNode.put("readonly", Boolean.TRUE);
		}
		
		if (StringUtils.hasText(formControl.getDefaultValue())) {
			propertyNode.put(DEFAULT_WS, formControl.getDefaultValue());
		}
		return propertyNode;
	}

	protected ArrayNode toArrayNode(String values[]) {
    	
		ArrayNode valueArray = JsonUtils.creatArrayNode();
		for (String value : values) {
			valueArray.add(value);
		}
		return valueArray;
    }
	
	private ObjectNode getColumnNode(AuthoringTemplate at, FormColumn formColumn) {
		ObjectNode columnNode = this.objectMapper.createObjectNode();
		columnNode.put("type", "div");
		columnNode.put("displayFlex", true);
		columnNode.put("flex-direction", "column");
		columnNode.put("fxFlex", formColumn.getFxFlex());

		ArrayNode fieldNodes = this.objectMapper.createArrayNode();
		for (String fieldName : formColumn.getFormControls()) {
			String names[] = this.getNameAndPrefix(fieldName);
			String name = names[1];
			String prefix = names[0];
			FormControl formControl = null;
			if (at.getElements() != null && at.getElements().size() > 0) {
				formControl = at.getElements().get(name);
			}
			formControl = (formControl == null) ? at.getProperties().get(name) : formControl;
			Optional<FieldLayout> associationLayout = this.getAssociationLayout(
					formColumn,
					name);
			if (associationLayout.isPresent()) {
				ArrayNode layoutNodes = null;
				if (associationLayout.get().isMultiple()) {
					ObjectNode fieldNode = this.objectMapper.createObjectNode();
					fieldNode.put("type", "array");
					fieldNode.put("title", name);
					fieldNodes.add(fieldNode);					
					layoutNodes = this.objectMapper.createArrayNode();
					fieldNode.set("items", layoutNodes);
				} else {
					layoutNodes = fieldNodes;
				}
				if (associationLayout.get().getFieldLayouts() == null || associationLayout.get().getFieldLayouts().length == 0) {
					FieldLayout fieldLayout = associationLayout.get();
					ObjectNode fieldNode = this.objectMapper.createObjectNode();
					if (StringUtils.hasText(fieldLayout.getTitle())) {
						fieldNode.put("title", fieldLayout.getTitle());
					} else {
						fieldNode.put("notitle", "true");
					}
					fieldNode.put("key", this.getLayoutFieldKey(fieldLayout.getKey(), prefix));
					if (fieldLayout.isMultiple()) {
						fieldNode.put("type", "array");
						ArrayNode itemsNode = this.objectMapper.createArrayNode();
						fieldNode.set("items", itemsNode);
						itemsNode.add(fieldLayout.getItems());
					} 
					fieldNodes.add(fieldNode);
				} else {
					for (FieldLayout fieldLayout: associationLayout.get().getFieldLayouts()) {
						ObjectNode fieldNode = this.objectMapper.createObjectNode();
						if (StringUtils.hasText(fieldLayout.getTitle())) {
							fieldNode.put("title", fieldLayout.getTitle());
						} else {
							fieldNode.put("notitle", "true");
						}
						fieldNode.put("key", this.getLayoutFieldKey(fieldLayout.getName(), prefix));
						if (fieldLayout.isMultiple()) {
							fieldNode.put("type", "array");
							ArrayNode itemsNode = this.objectMapper.createArrayNode();
							fieldNode.set("items", itemsNode);
							itemsNode.add(fieldLayout.getItems());
						} 
						layoutNodes.add(fieldNode);
					}
				}
			} else {
				ObjectNode fieldNode = this.objectMapper.createObjectNode();
				fieldNodes.add(fieldNode);
				fieldNode.put("key", this.getLayoutFieldKey(formControl.getName(), prefix));
				if (!"text".equals(formControl.getControlType())) {
					fieldNode.put("type", formControl.getControlType());
				}
				if (StringUtils.hasText(formControl.getTitle())) {
					fieldNode.put("title", formControl.getTitle());
				} else {
					fieldNode.put("notitle", true);
				}
				if (StringUtils.hasText(formControl.getFlex())) {
					fieldNode.put("flex", formControl.getFlex());
				}
				if (StringUtils.hasText(formControl.getPlaceholder())) {
					fieldNode.put("placeholder", formControl.getPlaceholder());
				}
				// TODO: others
			}
		}
		columnNode.set("items", fieldNodes);
		return columnNode;
	}

	protected String[] getNameAndPrefix(String fieldName) {
		String names[] = fieldName.split("\\.", 2);
		if (names.length == 1) {
			names = new String[] { "", names[0]};
		}
		return names;
	}
	
	protected Optional<FieldLayout> getAssociationLayout(FormColumn formColumn, String controlName) {
		FieldLayout associationLayout = null;
		for (FieldLayout layout : formColumn.getFieldLayouts()) {
			if (controlName.equals(layout.getName())) {
				associationLayout = layout;
				break;
			}
		}
		return associationLayout == null ? Optional.empty() : Optional.of(associationLayout);
	}

	protected String getLayoutFieldKey(String key, String prefix) {
		return (prefix.length() > 0) ? String.format("%s.%s", prefix, key) : key;
	}

	protected String getJcrType(RestNode node) {
		String jcrType = "";
		for (RestProperty property: node.getJcrProperties()) {
			if ("bpw:jcrType".equals(property.getName())) {
				jcrType = property.getValues().get(0);
				break;
			}
		}
		return jcrType;
	}
	
	protected Stream<RenderTemplate> getRenderTemplates(RenderTemplate rt, HttpServletRequest request)
			throws WcmRepositoryException {
		try {
			String baseUrl = RestHelper.repositoryUrl(request);
			RestNode themeNode = (RestNode) this.itemHandler.item(baseUrl, rt.getRepository(), rt.getWorkspace(),
					"/bpwizard/library/" + rt.getLibrary() + "/renderTemplate", 5);
			return themeNode.getChildren().stream().filter(this::isRenderTemplate)
					.map(node -> this.toRenderTemplate(node, rt.getRepository(), rt.getWorkspace(), rt.getLibrary()));
		} catch (RepositoryException e) {
			throw new WcmRepositoryException(e);
		}
	}
	
	protected RenderTemplate toRenderTemplate(RestNode node, String repository, String workspace, String library) {
		RenderTemplate rt = new RenderTemplate();
		rt.setRepository(repository);
		rt.setWorkspace(workspace);
		rt.setLibrary(library);
		rt.setName(node.getName());
		this.wcmUtils.resolveResourceNode(rt, node);
		for (RestProperty property : node.getJcrProperties()) {
			if ("bpw:code".equals(property.getName())) {
				rt.setCode(property.getValues().get(0));
			} else if ("bpw:preloop".equals(property.getName())) {
				rt.setPreloop(property.getValues().get(0));
			} else if ("bpw:postloop".equals(property.getName())) {
				rt.setPostloop(property.getValues().get(0));
			} else if ("bpw:maxEntries".equals(property.getName())) {
				rt.setMaxEntries(Integer.parseInt(property.getValues().get(0)));
			} else if ("bpw:note".equals(property.getName())) {
				rt.setNote(property.getValues().get(0));
			} else if ("bpw:resourceName".equals(property.getName())) {
				rt.setResourceName(property.getValues().get(0));
			} else if ("bpw:isQuery".equals(property.getName())) {
				rt.setQuery(Boolean.parseBoolean(property.getValues().get(0)));
			} 
		}
		
		List<RenderTemplateLayoutRow> rows = new ArrayList<>();
		for (RestNode rowNode: node.getChildren()) {
			if (this.wcmUtils.checkNodeType(rowNode, "bpw:RenderTemplateLayoutRow")) {
				RenderTemplateLayoutRow row = new RenderTemplateLayoutRow();
				rows.add(row);
				List<RenderTemplateLayoutColumn> columns = new ArrayList<>();
				for (RestNode columnNode: rowNode.getChildren()) {
					if (this.wcmUtils.checkNodeType(columnNode, "bpw:RenderTemplateLayoutColumn")) {
						RenderTemplateLayoutColumn column = new RenderTemplateLayoutColumn();
						columns.add(column);	
						for (RestProperty property : columnNode.getJcrProperties()) {
							if ("bpw:id".equals(property.getName())) {
								column.setId(property.getValues().get(0));
							} else if ("bpw:widtd".equals(property.getName())) {
								column.setWidth(Integer.parseInt(property.getValues().get(0)));
							} 
						}
						List<ResourceElementRender> elements = new ArrayList<>();
						for (RestNode elementNode: columnNode.getChildren()) {
							if (this.wcmUtils.checkNodeType(elementNode, "bpw:ResourceElementRender")) {
								ResourceElementRender element = new ResourceElementRender();
								elements.add(element);
								element.setName(elementNode.getName());
							}
						}
						column.setElements(elements.toArray(new ResourceElementRender[elements.size()]));
						
					}
				}
				row.setColumns(columns.toArray(new RenderTemplateLayoutColumn[columns.size()]));
			}
		}
		
		if (rows.size() > 0) {
			rt.setRows(rows.toArray(new RenderTemplateLayoutRow[rows.size()]));
		}
		return rt;
	}
	
	protected RenderTemplate toRenderTemplateWithLibrary(RestNode node, String repository, String workspace) {
		RenderTemplate rtWithLibrary = new RenderTemplate();
		rtWithLibrary.setRepository(repository);
		rtWithLibrary.setWorkspace(workspace);
		rtWithLibrary.setLibrary(node.getName());
		return rtWithLibrary;
	}
	
	protected Map<String, RenderTemplate> doGetRenderTemplates(String repository,
			String workspace, HttpServletRequest request) 
			throws WcmRepositoryException {
		Map<String, RenderTemplate> renderTemplates = this.getRenderTemplateLibraries(repository, workspace, request)
				.flatMap(rt -> this.getRenderTemplates(rt, request)).collect(Collectors.toMap(
						rt -> String.format(WCM_RT_PATH_PATTERN, rt.getLibrary(), rt.getName()), 
						Function.identity()));

		return renderTemplates;

	}
	
	protected Stream<RenderTemplate> getRenderTemplateLibraries(String repository, String workspace,
			HttpServletRequest request) throws WcmRepositoryException {
		try {
			String baseUrl = RestHelper.repositoryUrl(request);
			RestNode bpwizardNode = (RestNode) this.itemHandler.item(baseUrl, repository, workspace,
					"/bpwizard/library", 1);
			return bpwizardNode.getChildren().stream().filter(this::isLibrary).filter(this::notSystemLibrary)
					.map(node -> toRenderTemplateWithLibrary(node, repository, workspace));
		} catch (RepositoryException e) {
			throw new WcmRepositoryException(e);
		}
	}
	
	protected Map<String, ContentAreaLayout> doGetContentAreaLayouts(
			String repository,
			String workspace, 
			HttpServletRequest request) 
			throws WcmRepositoryException {
		String baseUrl = RestHelper.repositoryUrl(request);
		Map<String, ContentAreaLayout> contentAreaLayouts = this.getContentAreaLayoutLibraries(repository, workspace, baseUrl)
				.flatMap(layout -> this.getContentArealayouts(layout, baseUrl))
				.collect(Collectors.toMap(
						layout -> String.format( WCM_CONTENT_LAYOUT_PATH_PATTERN, layout.getLibrary(), layout.getName()), 
						Function.identity()));

		return contentAreaLayouts;
	}
	
	protected Stream<ContentAreaLayout> getContentAreaLayoutLibraries(String repository, String workspace, String baseUrl)
			throws WcmRepositoryException {
		try {
			RestNode bpwizardNode = (RestNode) this.itemHandler.item(baseUrl, repository, workspace,
					"/bpwizard/library", 1);
			return bpwizardNode.getChildren().stream().filter(this::isLibrary).filter(this::notSystemLibrary)
					.map(node -> toContentAreaLayoutWithLibrary(node, repository, workspace));
		} catch (RepositoryException e) {
			throw new WcmRepositoryException(e);
		}
	}
	
	private Stream<ContentAreaLayout> getContentArealayouts(ContentAreaLayout contentAreaLayout, String baseUrl)
			throws WcmRepositoryException {
		try {
			RestNode contentAreaLayoutNode = (RestNode) this.itemHandler.item(baseUrl, contentAreaLayout.getRepository(),
					contentAreaLayout.getWorkspace(), "/bpwizard/library/" + contentAreaLayout.getLibrary() + "/contentAreaLayout", 4);
			return contentAreaLayoutNode.getChildren().stream().filter(this::isContentAreaLayout)
					.map(node -> this.toContentAreaLayout(node, contentAreaLayout));
		} catch (RepositoryException e) {
			throw new WcmRepositoryException(e);
		}
	}
	
	private ContentAreaLayout toContentAreaLayoutWithLibrary(RestNode node, String repository, String workspace) {
		ContentAreaLayout contentAreaLayoutWithLibrary = new ContentAreaLayout();
		contentAreaLayoutWithLibrary.setRepository(repository);
		contentAreaLayoutWithLibrary.setWorkspace(workspace);
		contentAreaLayoutWithLibrary.setLibrary(node.getName());
		return contentAreaLayoutWithLibrary;
	}
	
	protected ContentAreaLayout toContentAreaLayout(RestNode node, ContentAreaLayout layout) {
		ContentAreaLayout contentAreaLayout = new ContentAreaLayout();
		contentAreaLayout.setRepository(layout.getRepository());
		contentAreaLayout.setWorkspace(layout.getWorkspace());
		contentAreaLayout.setLibrary(layout.getLibrary());
		contentAreaLayout.setName(node.getName());
		this.wcmUtils.resolveResourceNode(contentAreaLayout, node);
		for (RestProperty property : node.getJcrProperties()) {
			if (" bpw:contentWidth".equals(property.getName())) {
				contentAreaLayout.setContentWidth(Integer.parseInt(property.getValues().get(0)));
				break;
			} 
		}

		List<LayoutRow> rows = new ArrayList<>();

		node.getChildren().forEach(childNode -> {
			if (this.wcmUtils.checkNodeType(childNode, "bpw:contentAreaSidePanel")) {
				SidePane sidepane = new SidePane();
				for (RestProperty property : childNode.getJcrProperties()) {
					if ("bpw:isLeft".equals(property.getName())) {
						sidepane.setLeft(Boolean.parseBoolean(property.getValues().get(0)));
					} else if ("bpw:width".equals(property.getName())) {
						sidepane.setWidth(Integer.parseInt(property.getValues().get(0)));
					}
				}
				sidepane.setViewers(this.resolveResourceViewer(childNode));
				contentAreaLayout.setSidePane(sidepane);
			} else if (this.wcmUtils.checkNodeType(childNode, "bpw:layoutRow")) {
				LayoutRow row = this.resolveLayoutRow(childNode);
				rows.add(row);
			}
		});
		contentAreaLayout.setRows(rows.toArray(new LayoutRow[rows.size()]));
		return contentAreaLayout;
	}
	
	protected ResourceViewer[] resolveResourceViewer(RestNode restNode) {
		List<ResourceViewer> viewers = new ArrayList<>();
		
		restNode.getChildren().forEach(viewerNode -> {
			if (this.wcmUtils.checkNodeType(viewerNode, "bpw:resourceViewer")) {
				ResourceViewer viewer = new ResourceViewer();
				for (RestProperty property : viewerNode.getJcrProperties()) {
					if ("bpw:title".equals(property.getName())) {
						viewer.setTitle(property.getValues().get(0));
					} else if ("bpw:renderTemplateName".equals(property.getName())) {
						viewer.setRenderTemplate(property.getValues().get(0));
					} else if ("bpw:contentPath".equals(property.getName())) {
						viewer.setContentPath(property.getValues().toArray(new String[property.getValues().size()]));
					}
				}
				viewers.add(viewer);
			}
		});
		return viewers.toArray(new ResourceViewer[viewers.size()]);
	}
	
	protected LayoutRow resolveLayoutRow(RestNode restNode) {
		LayoutRow row = new LayoutRow();
		LayoutColumn columns[] = restNode.getChildren().stream()
				.filter(node -> this.wcmUtils.checkNodeType(node, "bpw:layoutColumn")).map(this::toLayoutColumn)
				.toArray(LayoutColumn[]::new);
		row.setColumns(columns);
		return row;
	}

	private LayoutColumn toLayoutColumn(RestNode node) {
		LayoutColumn column = new LayoutColumn();
		for (RestProperty property : node.getJcrProperties()) {
			if ("bpw:width".equals(property.getName())) {
				column.setWidth(Integer.parseInt(property.getValues().get(0)));
			}
		}
		column.setViewers(this.resolveResourceViewer(node));
		return column;
	}

	protected Map<String, AuthoringTemplate> doGetAuthoringTemplates(String repository,
			String workspace, HttpServletRequest request) 
			throws WcmRepositoryException {
		String baseUrl = RestHelper.repositoryUrl(request);
		Map<String, AuthoringTemplate> authoringTemplates = this.getAuthoringTemplateLibraries(repository, workspace, baseUrl)
				.flatMap(at -> this.getAuthoringTemplates(at, baseUrl))
				.collect(Collectors.toMap(
						at -> String.format(WCM_AT_PATH_PATTERN, at.getLibrary(), at.getName()), 
						Function.identity()));

		return authoringTemplates;
	}
	
	protected Stream<AuthoringTemplate> getAuthoringTemplates(AuthoringTemplate at, String baseUrl)
			throws WcmRepositoryException {
		try {
			RestNode atNode = (RestNode) this.itemHandler.item(baseUrl, at.getRepository(), at.getWorkspace(),
					String.format(WCM_AT_ROOT_PATH_PATTERN, at.getLibrary()), 8);
			
			return atNode.getChildren().stream().filter(this::isAuthortingTemplate)
					.map(node -> this.wcmUtils.toAuthoringTemplate(node, at.getRepository(), at.getWorkspace(), at.getLibrary()));
		} catch (RepositoryException e) {
			e.printStackTrace();
			throw new WcmRepositoryException(e);
		}
	}
	
	protected SiteConfig doGetSiteConfig(
			HttpServletRequest request,
			String repository,
			String workspace,
			String library, 
			String siteConfigName) throws RepositoryException {
		
		String baseUrl = RestHelper.repositoryUrl(request);
		String absPath = String.format(WCM_SITECONFIG_PATH_PATTERN, library, siteConfigName);
		RestNode siteConfigNode = (RestNode) this.itemHandler.item(baseUrl, repository, workspace, absPath, 2);
		SiteConfig siteConfig = this.getSiteConfig(siteConfigNode);
		siteConfig.setRepository(repository);
		siteConfig.setWorkspace(workspace);
		siteConfig.setLibrary(library);
	
		return siteConfig;
			
	}
	
	protected SiteConfig getSiteConfig(RestNode siteConfigNode) throws RepositoryException {
		SiteConfig siteConfig = new SiteConfig();
		this.wcmUtils.resolveResourceNode(siteConfig, siteConfigNode);
		for (RestProperty property : siteConfigNode.getJcrProperties()) {
			if ("bpw:name".equals(property.getName())) {
				siteConfig.setName(property.getValues().get(0));
			} else if ("bpw:colorTheme".equals(property.getName())) {
				siteConfig.setColorTheme(property.getValues().get(0));
			} else if ("bpw:customScrollbars".equals(property.getName())) {
				siteConfig.setCustomScrollbars(Boolean.parseBoolean(property.getValues().get(0)));
			} else if ("bpw:rootSiteArea".equals(property.getName())) {
				siteConfig.setRootSiteArea(property.getValues().get(0));
			} 
		}
		
		for (RestNode layoutNode: siteConfigNode.getChildren()) {
			if ("layout".equals(layoutNode.getName())) {
				PageLayout layout = new PageLayout();
				siteConfig.setLayout(layout);
				for (RestProperty property : layoutNode.getJcrProperties()) {
					if ("bpw:style".equals(property.getName())) {
						layout.setStyle(property.getValues().get(0));
					} else if ("bpw:width".equals(property.getName())) {
						layout.setWidth(property.getValues().get(0));
					} 
				}
				for (RestNode node: layoutNode.getChildren()) {
					if ("navbar".equals(node.getName())) {
						NavBar navbar = new NavBar();
						layout.setNavbar(navbar);						
						for (RestProperty property : node.getJcrProperties()) {
							if ("folded".equals(property.getName())) {
								navbar.setFolded(Boolean.getBoolean(property.getValues().get(0)));
							} else if ("primaryBackground".equals(property.getName())) {
								navbar.setPrimaryBackground(property.getValues().get(0));
							} else if ("secondaryBackground".equals(property.getName())) {
								navbar.setSecondaryBackground(property.getValues().get(0));
							} else if ("variant".equals(property.getName())) {
								navbar.setVariant(property.getValues().get(0));
							} else if ("position".equals(property.getName())) {
								navbar.setPosition(property.getValues().get(0));
							} else if ("hidden".equals(property.getName())) {
								navbar.setHidden(Boolean.getBoolean(property.getValues().get(0)));
							} 
						}
					} else if ("toolbar".equals(node.getName())) {
						Toolbar toolbar = new Toolbar();
						layout.setToolbar(toolbar);						
						for (RestProperty property : node.getJcrProperties()) {
							if ("customBackgroundColor".equals(property.getName())) {
								toolbar.setCustomBackgroundColor(Boolean.getBoolean(property.getValues().get(0)));
							} else if ("background".equals(property.getName())) {
								toolbar.setBackground(property.getValues().get(0));
							} else if ("position".equals(property.getName())) {
								toolbar.setPosition(property.getValues().get(0));
							} else if ("hidden".equals(property.getName())) {
								toolbar.setHidden(Boolean.getBoolean(property.getValues().get(0)));
							} 
						}
					} else if ("footer".equals(node.getName())) {
						Footer footer = new Footer();
						layout.setFooter(footer);				
						for (RestProperty property : node.getJcrProperties()) {
							if ("customBackgroundColor".equals(property.getName())) {
								footer.setCustomBackgroundColor(Boolean.getBoolean(property.getValues().get(0)));
							} else if ("background".equals(property.getName())) {
								footer.setBackground(property.getValues().get(0));
							} else if ("position".equals(property.getName())) {
								footer.setPosition(property.getValues().get(0));
							} else if ("hidden".equals(property.getName())) {
								footer.setHidden(Boolean.getBoolean(property.getValues().get(0)));
							} 
						}
					} else if ("sidePanel".equals(node.getName())) {
						SidePanel sidePanel = new SidePanel();
						layout.setSidePanel(sidePanel);				
						for (RestProperty property : node.getJcrProperties()) {
							if ("position".equals(property.getName())) {
								sidePanel.setPosition(property.getValues().get(0));
							} else if ("hidden".equals(property.getName())) {
								sidePanel.setHidden(Boolean.getBoolean(property.getValues().get(0)));
							} 
						}
					}
				}
				break;
			} 
		}
		return siteConfig;
	}
	
	protected Map<String, SiteArea> getSiteAreas(
			String repository,			
			String workspace, 
			String library,
			String rootSiteArea,
			String baseUrl
			) throws RepositoryException {
		RestNode saNode = (RestNode) this.itemHandler.item(baseUrl, repository, workspace,
				String.format("/bpwizard/library/%s/%s", library, rootSiteArea), 5);
		Map<String, SiteArea> siteAreas = new HashMap<>();
		for (RestNode node :saNode.getChildren()) {
			if (this.isSiteArea(node)) {
				this.toSiteArea(node, siteAreas);
			}
		}
		return siteAreas;
	}
	
	private SiteArea toSiteArea(RestNode saNode, Map<String, SiteArea> siteAreas) {
		SiteArea sa = new SiteArea();
		this.loadSiteArea(saNode, sa);

		for (RestNode node :saNode.getChildren()) {
			if (this.isSiteArea(node)) {
				this.toSiteArea(node, siteAreas);
			}
		}
		String url = sa.getProperties().get("url");
		siteAreas.put(url.replace("/", "~"), sa);
		return sa;
	}
	
	protected void loadSiteArea(RestNode saNode, SiteArea sa) {
		Map<String, String> elements = new HashMap<>();
		Map<String, String> properties = new HashMap<>();
		sa.setProperties(properties);
		sa.setElements(elements);
		for (RestProperty property: saNode.getJcrProperties()) {
			if ("bpw:body".equals(property.getName())) {
				elements.put("body", property.getValues().get(0));
			} else if (property.getName().startsWith("bpw:")){
				properties.put(this.getSiteAreaPropertyName(property.getName()), property.getValues().get(0));
			}
		}
		
		for (RestNode node: saNode.getChildren()) {
		   if (this.wcmUtils.checkNodeType(node, "bpw:properties") && ("bpw:metaData".equals(node.getName()))) {
			   WcmProperties metadata = new WcmProperties();
			   List<WcmProperty> propertyList = new ArrayList<>();
			   for (RestNode kvNode: node.getChildren()) {
				   if (this.wcmUtils.checkNodeType(kvNode, "bpw:property")) {
					   WcmProperty wcmProperty = new WcmProperty();
					   for (RestProperty property: kvNode.getJcrProperties()) {
						   if ("bpw:name".equals(property.getName())) {
							   wcmProperty.setName(property.getValues().get(0));
						   } else if ("bpw:value".equals(property.getName())) {
							   wcmProperty.setValue(property.getValues().get(0));
						   }
					   }
					   propertyList.add(wcmProperty);
				   }
			   }
			   metadata.setProperties(propertyList.toArray(new WcmProperty[propertyList.size()]));
			   sa.setMetadata(metadata);
		   } else if (this.wcmUtils.checkNodeType(node, "bpw:pageSearchData") && ("bpw:searchData".equals(node.getName()))) {
			   SearchData searchData = new SearchData();
			   for (RestProperty property: node.getJcrProperties()) {
				   if ("description".equals(property.getName())) {
					   searchData.setDescription(property.getValues().get(0));
				   } else if ("keywords".equals(property.getName())) {
					   searchData.setKeywords(property.getValues().toArray(new String[property.getValues().size()]));
				   }
			   }
			   sa.setSearchData(searchData);
		   } else if (this.wcmUtils.checkNodeType(node, "bpw:navigationBadge") && ("bpw:badge".equals(node.getName()))) {
			   NavigationBadge badge = new NavigationBadge();
			   for (RestProperty property: node.getJcrProperties()) {
				   if ("bpw:title".equals(property.getName())) {
					   badge.setTitle(property.getValues().get(0));
				   } else if ("bpw:translate".equals(property.getName())) {
					   badge.setTranslate(property.getValues().get(0));
				   } else if ("bpw:bg".equals(property.getName())) {
					   badge.setBg(property.getValues().get(0));
				   } else if ("bpw:fg".equals(property.getName())) {
					   badge.setFg(property.getValues().get(0));
				   }
			   }
			   sa.setBadge(badge);
		   } else if (this.wcmUtils.checkNodeType(node, "bpw:siteAreaLayout") && ("siteAreaLayout".equals(node.getName()))) {
			   SiteAreaLayout siteAreaLayout = new SiteAreaLayout();
			   List<LayoutRow> rows = new ArrayList<>();
			   for (RestProperty property : node.getJcrProperties()) {
					if ("bpw:contentWidth".equals(property.getName())) {
						siteAreaLayout.setContentWidth(Integer.parseInt(property.getValues().get(0)));
						break;
					} 
			   }
			   for (RestNode childNode: node.getChildren()) {
				   if (this.wcmUtils.checkNodeType(childNode, "bpw:contentAreaSidePanel") && "sidePane".equals(childNode.getName())) {
					   SidePane sidePane = new SidePane();
					   for (RestProperty property : childNode.getJcrProperties()) {
							if ("bpw:isLeft".equals(property.getName())) {
								sidePane.setLeft(Boolean.parseBoolean(property.getValues().get(0)));
							} else if ("bpw:width".equals(property.getName())) {
								sidePane.setWidth(Integer.parseInt(property.getValues().get(0)));
							}
						}
					   sidePane.setViewers(this.resolveResourceViewer(childNode));
					   siteAreaLayout.setSidePane(sidePane);
				   } else if (this.wcmUtils.checkNodeType(childNode, "bpw:layoutRow")) {
						LayoutRow row = this.resolveLayoutRow(childNode);
						rows.add(row);
				   }
			   }
			   siteAreaLayout.setRows(rows.toArray(new LayoutRow[rows.size()]));
			   sa.setSiteAreaLayout(siteAreaLayout);
		   }
		}
	}
	
	private String getSiteAreaPropertyName(String propertyName) {
		return propertyName.split(":", 2)[1];
	}
	
	protected Navigation[] getNavigations(
			String baseUrl,
			String repository,			
			String workspace, 
			String library,
			String rootSiteArea
			) throws RepositoryException {
		RestNode siteArea = (RestNode) this.itemHandler.item(baseUrl, repository, workspace,
				String.format("/bpwizard/library/%s/%s", library, rootSiteArea), 3);
		
		Navigation[] navigation = siteArea.getChildren().stream().filter(this::isSiteArea)
				.map(node -> this.toNavigation(node)).toArray(Navigation[]::new);
		return navigation;
	}
	
	protected ControlField[] doGetControlField(String repository,
			String workspace, HttpServletRequest request) 
			throws RepositoryException {
			String baseUrl = RestHelper.repositoryUrl(request);
			RestNode controlFieldFolder = (RestNode) this.itemHandler.item(baseUrl, repository, workspace,
					String.format(WCM_ROOT_PATH_PATTERN, "system/controlField"), 2);
			ControlField[] ControlFileds = controlFieldFolder.getChildren().stream().filter(this::isControlField)
					.map(this::toControlField).toArray(ControlField[]::new);
	
			return ControlFileds;
	}
	
	private ControlFieldMetadata toControlFieldMetaData(RestNode node) {
		ControlFieldMetadata metadata = new ControlFieldMetadata();
		metadata.setName(node.getName());
		for (RestProperty restProperty : node.getJcrProperties()) {
			if ("bpw:title".equals(restProperty.getName())) {
				metadata.setTitle(restProperty.getValues().get(0));
			} else if ("bpw:controlType".equals(restProperty.getName())) {
				metadata.setControlType(restProperty.getValues().get(0));
			} else if ("bpw:hintText".equals(restProperty.getName())) {
				metadata.setHintText(restProperty.getValues().get(0));
			} else if ("bpw:required".equals(restProperty.getName())) {
				metadata.setRequired(Boolean.parseBoolean(restProperty.getValues().get(0)));
			} else if ("bpw:readOnly".equals(restProperty.getName())) {
				metadata.setReadonly(Boolean.parseBoolean(restProperty.getValues().get(0)));
			} else if ("bpw:selectOptions".equals(restProperty.getName())) {
				metadata.setSelectOptions(
						restProperty.getValues().toArray(new String[restProperty.getValues().size()]));
			}
		}
		return metadata;
	}

	private ControlField toControlField(RestNode node) {
		ControlField controlField = new ControlField();
		controlField.setName(node.getName());
		for (RestProperty restProperty : node.getJcrProperties()) {
			if ("bpw:title".equals(restProperty.getName())) {
				controlField.setTitle(restProperty.getValues().get(0));
			} else if ("bpw:icon".equals(restProperty.getName())) {
				controlField.setIcon(restProperty.getValues().get(0));
			} else if ("bpw:hint".equals(restProperty.getName())) {
				controlField.setHint(restProperty.getValues().get(0));
			} 
			
		}
//		ControlFieldMetadata[] controlFieldMetadata = node.getChildren().stream().filter(this::isControlFieldMetaData)
//				.map(this::toControlFieldMetaData).toArray(ControlFieldMetadata[]::new);
//		controlField.setControlFieldMetaData(controlFieldMetadata);
		return controlField;
	}
	
	private NavigationItem toNavigation(RestNode siteArea) {
		Navigation navigation = new Navigation();
		for (RestProperty property : siteArea.getJcrProperties()) {
			if ("bpw:navigationId".equals(property.getName())) {
				navigation.setId(property.getValues().get(0));
			} else if ("bpw:navigationType".equals(property.getName())) {
				navigation.setType(NavigationType.valueOf(property.getValues().get(0)));
			} else if ("bpw:title".equals(property.getName())) {
				navigation.setTitle(property.getValues().get(0));
			} else if ("bpw:translate".equals(property.getName())) {
				navigation.setTranslate(property.getValues().get(0));
			} else if ("bpw:icon".equals(property.getName())) {
				navigation.setIcon(property.getValues().get(0));
			} else if ("bpw:url".equals(property.getName())) {
				navigation.setUrl(property.getValues().get(0));
			}
		}
		for (RestNode node: siteArea.getChildren()) {
			if (this.wcmUtils.checkNodeType(node, "bpw:navigationBadge")) {
				NavigationBadge badge = new NavigationBadge();
				for (RestProperty property : node.getJcrProperties()) {
					if ("bpw:title".equals(property.getName())) {
						badge.setTitle(property.getValues().get(0));
					} else if ("bpw:translate".equals(property.getName())) {
						badge.setTranslate(property.getValues().get(0));
					} else if ("bpw:bg".equals(property.getName())) {
						badge.setBg(property.getValues().get(0));
					} else if ("bpw:fg".equals(property.getName())) {
						badge.setFg(property.getValues().get(0));
					} 
				}
				navigation.setBadge(badge);
			}
		}
		NavigationItem[] navigationItems = siteArea.getChildren().stream().filter(this::isSiteArea)
				.map(node -> this.toNavigationItem(node, 1)).toArray(NavigationItem[]::new);
		navigation.setChildren(navigationItems);
		
		return navigation;
	}
	
	private NavigationItem toNavigationItem(RestNode siteArea, int level) {
		NavigationItem navigation = new NavigationItem();
		for (RestProperty property : siteArea.getJcrProperties()) {
			if ("bpw:navigationId".equals(property.getName())) {
				navigation.setId(property.getValues().get(0));
			} else if ("bpw:navigationType".equals(property.getName())) {
				navigation.setType(NavigationType.valueOf(property.getValues().get(0)));
			} else if ("bpw:title".equals(property.getName())) {
				navigation.setTitle(property.getValues().get(0));
			} else if ("bpw:translate".equals(property.getName())) {
				navigation.setTranslate(property.getValues().get(0));
			} else if ("bpw:icon".equals(property.getName())) {
				navigation.setIcon(property.getValues().get(0));
			} else if ("bpw:url".equals(property.getName())) {
				navigation.setUrl(property.getValues().get(0));
			}
		}
		if (level <= 2) {
			NavigationItem[] navigationItems = siteArea.getChildren().stream().filter(this::isSiteArea)
					.map(node -> this.toNavigationItem(node, level + 1)).toArray(NavigationItem[]::new);
			navigation.setChildren(navigationItems);
		}
		return navigation;
	}
}
