package com.bpwizard.wcm.repo.rest.utils;

public class WcmConstants {

	public static final String NODE_ROOT_PATH = "/library";
	public  static final String NODE_AT_PATH_PATTERN = "/library/%s/authoringTemplate/%s";
	public  static final String NODE_FORM_PATH_PATTERN = "/library/%s/form/%s";
	public  static final String NODE_AT_ROOT_PATH_PATTERN = "/library/%s/authoringTemplate";
	public  static final String NODE_FORM_ROOT_PATH_PATTERN = "/library/%s/form";
	public  static final String NODE_RT_PATH_PATTERN = "/library/%s/renderTemplate/%s";
	public  static final String NODE_CONTENT_LAYOUT_PATH_PATTERN = "/library/%s/contentAreaLayout/%s";
	public  static final String NODE_SITECONFIG_PATH_PATTERN = "/library/%s/siteConfig/%s";
	public  static final String NODE_SITECONFIG_PATH = "/library/%s/siteConfig";
	public  static final String NODE_CATEGORY_PATH_PATTERN = "/library/%s/category/%s";
	public  static final String NODE_CATEGORY_SUB_PATH_PATTERN = "/library/%s/category/%s/%s";
	public  static final String NODE_VALIDATTOR_PATH_PATTERN = "/library/%s/validationRule/%s";
	public  static final String NODE_WORKFLOW_ROOT_PATH_PATTERN = "/library/%s/workflow";
	public  static final String NODE_WORKFLOW_PATH_PATTERN = "/library/%s/workflow/%s";
	public  static final String NODE_VALIDATION_RULE_ROOT_PATH_PATTERN = "/library/%s/validationRule";
	public  static final String NODE_QUERY_ROOT_PATH_PATTERN = "/library/%s/query";
	public  static final String NODE_QUERY_PATH_PATTERN = "/library/%s/query/%s";
	public  static final String NODE_THEME_ROOT_PATH_PATTERN = "/library/%s/theme";
	public  static final String NODE_CONTROL_FIELD_ROOT = "/library/system/controlField";
	public  static final String DEFAULT_WS = "default";	
	public  static final String DRAFT_WS = "draft";
	public  static final String EXPIRED_WS = "expired";
	public  static final String BPWIZARD_REPO = "bpwizard";
	
	public  static final String WORKFLOW_STATGE_PUBLISHED = "Published";
	public  static final String WORKFLOW_STATGE_DRAFT = "Draft";
	
	public  static final String WCM_AT_PATH_PATTERN = "/%s/authoringTemplate/%s";
	public  static final String WCM_FORM_PATH_PATTERN = "/%s/form/%s";
	
	public  static final String WCM_RT_PATH_PATTERN = "/%s/renderTemplate/%s";
	public  static final String WCM_CONTENT_LAYOUT_PATH_PATTERN = "/%s/contentAreaLayout/%s";
	public  static final String NODE_PATH_PATTERN = "/library%s/%s";
	public  static final String NODE_REL_PATH_PATTERN = "/library/%s/%s";
	public  static final String NODE_ROOT_REL_PATH_PATTERN = "/library/%s";
	public  static final String NODE_ROOT_PATH_PATTERN = "/library%s";
	public  static final String NODE_LIB_PATH_PATTERN = "/library/%s";
	public  static final String WCM_PATH_PATTERN = "/%s/%s";
	public  static final String WCM_REL_PATH_PATTERN = "%s/%s";
	
	public  static final String OPERATION_REL_PATH = "system/configuration/operations";
	
	public  static final String ELEMENT_FOLDER_TYPE = "bpw:%s_%s_ElementFolder";
	// public  static final String PROPERTY_FOLDER_TYPE = "bpw:%s_%s_PropertyFolder";
	public  static final String COMMENT_FOLDER_TYPE = "bpw:%s_%s_CommentFolder";
	public  static final String CONTENT_TYPE = "bpw:%s_%s_AT";
	
	public  static final String DEFINITION_PATH_PATTERN = "#/definitions/%s";
	
	public  static final String WORKFLOW_NODE_TYPE = "bpw:workflowNode";
	public  static final String FOLDER_NODE_TYPE = "nt:folder";
	public  static final String WCM_NODE_TYPE_PREFIX = "bpw:";
	
	public static final int FULL_SUB_DEPTH = -1;
	public static final int AT_JSON_FORM_DEPATH = -1;
	public static final int FORM_JSON_FORM_DEPATH = -1;
	public static final int CONTENT_ITEM_DEPATH = -1;
	public static final int READ_DEPTH_DEFAULT = 1;
	public static final int READ_DEPTH_TWO_LEVEL = 2;
	public static final int READ_DEPTH_THREE_LEVEL = 3;
	public static final int RENDER_TEMPLATE_DEPATH = 5;
	public static final int CONTENT_AREA_LAYOUT_DEPTH = 4;
	public static final int SITE_CONFIG_DEPTH = 3;
	public static final int SITE_AREA_DEPTH = -1;
	public static final int NAVIGATION_DEPTH = 3;
	public static final int CONTROL_FIELD_DEPTH = 2;
	public static final int QUERY_STMT_DEPTH = 3;
	public static final int VALIDATION_RULE_DEPTH = 3;
	public static final int BPMN_WORKFLOW_DEPTH = 3;
	public static final int OPERATION_DEFAULT = 2;
	
	public static final String CONTROL_TYPE_OBJECT = "object";
	
	public static final String JSON_SCHEMA_REF = "$ref";
	public static final String JSON_SCHEMA_ALL_OF = "allOf";
	
	public static final String JCR_JSON_NODE_CHILDREN = "children";
	public static final String JCR_JSON_NODE_PROPERTIES = "properties";
	
	public static final String JCR_TYPE_ELEMENTS_FOLDER_PATTERN = "bpw:%s_%s_ElementFolder";
	public static final String JCR_TYPE_PROPERTY_FOLDER = "bpw:ContentItemproperties";
	
	public static final String WCM_ITEM_ELEMENTS = "elements";
	public static final String WCM_ITEM_PROPERTIES = "itemProperties";
	public static final String WCM_ITEM_COMMENTTS = "comments";
	
	public static final String JSON_STRING_PATTERN = "^%s$";
	
	public static final String DEFAULT_SA_LAYOUT = "/design/contentAreaLayout/MyLayout";
}
