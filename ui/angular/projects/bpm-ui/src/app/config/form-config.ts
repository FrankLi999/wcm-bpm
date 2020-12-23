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
