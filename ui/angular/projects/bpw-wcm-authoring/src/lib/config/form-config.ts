import { CategoryFormWidgetComponent } from "../wcm-form-widget/category-form-widget/category-form-widget.component";
import { SiteConfigFormWidgetComponent } from "../wcm-form-widget/site-config-form-widget/site-config-form-widget.component";
import { ContentAreaLayoutFormWidgetComponent } from "../wcm-form-widget/content-area-layout-form-widget/content-area-layout-form-widget.component";
import { SiteareaFormWidgetComponent } from "../wcm-form-widget/sitearea-form-widget/sitearea-form-widget.component";
import { WorkflowFormWidgetComponent } from "../wcm-form-widget/workflow-form-widget/workflow-form-widget.component";
import { WcmFileComponent } from "../wcm-form-widget/wcm-file/wcm-file.component";
export const FormConfig = {
  jsonFormOptions: {
    addSubmit: true, // Add a submit button if layout does not have one
    debug: false, // Don't show inline debugging information
    loadExternalAssets: true, // Load external css and JavaScript for frameworks
    returnEmptyFields: false, // Don't return values for empty input fields
    setSchemaDefaults: true, // Always use schema defaults for empty fields
    defautWidgetOptions: { feedback: true }, // Show inline feedback icons
  },
  selectedFramework: "material-design",
  selectedLanguage: "en",
  wcmWidgets: {
    "wcm-categories": CategoryFormWidgetComponent,
    "site-config": SiteConfigFormWidgetComponent,
    "content-area-layout": ContentAreaLayoutFormWidgetComponent,
    "wcm-sitearea": SiteareaFormWidgetComponent,
    "wcm-workflow": WorkflowFormWidgetComponent,
    "wcm-file": WcmFileComponent,
  },
};

export const form_input_controls: string[] = [
  "text",
  "checkbox",
  "color",
  "email",
  "hidden",
  "month",
  "number",
  "password",
  "radio",
  "search",
  "url",
  "week",
];
export const custom_field_controls: string[] = ["customField"];
export const binary_field_controls: string[] = ["image", "file"];
export const object_controls: string[] = ["object"];
export const formated_input_controls: string[] = [
  "date",
  "time",
  "datetime-local",
  "tel",
];
export const textarea_controls: string[] = ["textarea"];
