export class WcmConstants {
  //AT
  static WCM_CATEGORY_TYPE: string = "/system/authoringTemplate/categoryType";
  static WCM_QUERY_TYPE: string =
    "/system/authoringTemplate/queryStatementType";
  static WCM_SA_TYPE: string = "/system/authoringTemplate/siteAreaType";
  static WCM_LIBRARY_TYPE: string = "/system/authoringTemplate/libraryType";
  static WCM_REIVEW_TASK_FORM: string = "/design/form/reviewTask";
  static WCM_FORM_INPUT_TYPE: string = "/system/form/formInputType";
  static WCM_FORM_REF_TYPE: string = "/system/form/objectType";
  static WCM_FORM_CUST_FIELD_TYPE: string = "/system/form/customFieldType";
  static WCM_FORM_BINARY_TYPE: string = "/system/form/binraryType";
  static WCM_FORM_FORMATED_TYPE: string = "/system/form/formatedInputType";
  static WCM_FORM_TEXT_AREA_TYPE: string = "/system/form/textareaType";
  static WCM_VALIDATION_RULE_TYPE: string =
    "/system/authoringTemplate/validationRuleType";
  static WCM_WORKFLOW_TYPE: string =
    "/system/authoringTemplate/bpmnWorkflowType";
  static WCM_THEME_TYPE: string = "/system/authoringTemplate/themeType";
  static WCM_SITE_CONFIG_TYPE: string =
    "/system/authoringTemplate/siteConfigType";
  static WCM_FOLDER_TYPE: string = "/system/authoringTemplate/folderType";

  // NODETYPE
  static NODETYPE_CATEGORY_FOLDER: string = "bpw:categoryFolder";
  static NODETYPE_CATEGORY: string = "bpw:system_categoryType";
  static NODETYPE_CONTENT = /bpw:(.+)_(.+)_AT/;
  static NODETYPE_SA: string = "bpw:system_siteAreaType";

  // ROOTNODE
  static ROOTNODE_CATEGORY: string = "category";
  static ROOTNODE_LAYOUT = "contentAreaLayout";
  static ROOTNODE_RT = "renderTemplate";
  static ROOTNODE_AT = "authoringTemplate";
  static ROOTNODE_FORM = "form";
  //navigation
  static NAV_LAYOUT_LIST: string = "/wcm-authoring/content-area-layout/list";
  static NAV_CATEGORY_ITEM: string = "/wcm-authoring/category/item";
  static NAV_CATEGORY_PERMISSION: string =
    "/wcm-authoring/category/edit-permissions";
  static NAV_LIBRARY_LIST: string = "/wcm-authoring/resource-library/list";
  static NAV_AT_LIST: string = "/wcm-authoring/authoring-template/list";
  static NAV_FORM_LIST: string = "/wcm-authoring/form-designer/list";
  static NAV_SA_NAVIGATOR: string = "/wcm-authoring/site-explorer/navigator";
  static NAV_SA_PERMISSION: string =
    "/wcm-authoring/site-explorer/edit-permissions";
  static NAV_SA_HISTORY: string = "/wcm-authoring/site-explorer/show-history";
  static NAV_SA_EDIT_CONTENT: string =
    "/wcm-authoring/site-explorer/edit-content";
  static NAV_SA_EDIT_SA: string = "/wcm-authoring/site-explorer/edit-sa";
  static NAV_VALIDATION_RULE_LIST: string =
    "/wcm-authoring/validation-rule/list";
  static NAV_WORKDLOW_LIST: string = "/wcm-authoring/workflow/list";
  static NAV_SA_NEW_SA: string = "/wcm-authoring/site-explorer/new-sa";
  static NAV_SITE_CONFIG_LIST: string = "/wcm-authoring/site-config/list";
  static NAV_PREVIEW: string = "/wcm-authoring/preview";
  //Permission types
  static PERMISSION_TYPE_USER: string = "user";
  static PERMISSION_TYPE_GROUP: string = "group";

  //Roles
  static ROLE_VIEWER: string = "viewer";
  static ROLE_EDITOR: string = "editor";
  static ROLE_ADMIN: string = "administrator";

  //PAGENATION
  static FILTER_NONE: string = "";
  static SORT_ASC: string = "asc";

  //Message
  static UI_MSG_DELETE_CATEGORY: string =
    "Are you sure you want to delete the category?";
  static UI_TITLE_DELETE_CATEGORY: string = "Deleting Category";

  static UI_TITLE_UPDATING_LIBRARY: string = "Updating library";
  static UI_TITLE_CREATING_LIBRARY: string = "Creating library";

  static UI_MESSAGE_UPDATE_PERMISSION_SUCCESSFUL =
    "Permissions updated successfully";

  static UI_MESSAGE_UPDATED_LIBRARY: string = "Successfully updated library";
  static UI_MESSAGE_CREATED_LIBRARY: string = "Successfully updated library";

  //Layout
  static LAYOUT_GROUP_NA: string = "n/a";

  static LAYOUT_GROUP_STEPS: string = "steps";

  static LAYOUT_GROUP_TABS: string = "tabs";

  static LAYOUT_GROUP_ROWS: string = "rows";

  static LAYOUT_GROUP_COLUMNS: string = "columns";

  //Element, Property, Query
  static CONTENT_ELEMENT_PREFIX: string = "elements.";
  static CONTENT_ELEMENT: string = "elements";

  static WS_DRAFT: string = "draft";
  static WS_DEFAULT: string = "default";
  static REPO_BPWIZARD: string = "bpwizard";
}
