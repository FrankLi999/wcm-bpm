import { FormConfig } from "../config/form-config";
import { Directive } from "@angular/core";
@Directive()
export class BaseMewResourceDialog {
  authoringTemplateForm: any;
  jsonFormOptions = FormConfig.jsonFormOptions;
  selectedFramework = FormConfig.selectedFramework;
  selectedLanguage = FormConfig.selectedLanguage;
  wcmWidgets = FormConfig.wcmWidgets;
  constructor(protected data: any) {}

  ngOnInit() {
    this.authoringTemplateForm = this.data.authoringTemplateForm;
  }
}
