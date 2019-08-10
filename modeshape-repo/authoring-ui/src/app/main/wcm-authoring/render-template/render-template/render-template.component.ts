import { Component, OnInit, ViewEncapsulation } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

import {
  RenderTemplate,
  AuthoringTemplate,
  Query
} from '../../model';
import { WcmService } from '../../service/wcm.service';

export interface Code {
  name: string;
  code: string;
  isQuery: boolean;
}

@Component({
  selector: 'render-template',
  templateUrl: './render-template.component.html',
  styleUrls: ['./render-template.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class RenderTemplateComponent implements OnInit {
  // renderTemplateModel: RenderTemplate = {
  //   name: '',
  //   title: '',
  //   description: '',
  //   maxEntries: 0,
  //   preloop: '',
  //   postloop: '',
  //   note: '',
  //   isQuery: false,
  //   code: '',
  //   resourceName: ''
  // }

  renderTemplateForm: FormGroup;
  code: Code = {
    name: '',
    code: '',
    isQuery: false
  };
  
  contentTypes: string[] = [];
  contentElementsMap = new Map<String, string[]>();
  queryElementsMap = new Map<String, string[]>();
  contentTypeMap = new Map<String, AuthoringTemplate>();
  queryMap = new Map<String, Query>();
  queries: string[] = [
  ];

  contentElements: string[] = [];

  constructor(
    private wcmService: WcmService,
    private formBuilder: FormBuilder) { }

  ngOnInit() {
    this.wcmService.getAuthoringTemplate('bpwizard', 'default').subscribe(
      (authoringTemplates: AuthoringTemplate[]) => {
        if (authoringTemplates) {
          authoringTemplates.forEach(at => {
            this.contentTypes.push(at.name);
            let formControls: string[] = [];
            for (let property in at.formControls) {
              formControls.push(property)
            }
            this.contentElementsMap.set(at.name, formControls);
            this.contentTypeMap.set(at.name, at);
          });
          console.log(this.contentTypes);
        }
      }

    );
    this.renderTemplateForm = this.formBuilder.group({
        name: ['My Template', Validators.required],
        title: ['', Validators.required],
        description: ['', Validators.required],
        maxEntries: [1, Validators.required],
        preloop: ['<div>preLoop</div>', Validators.required],
        postloop: ['<div>postLoop</div>', Validators.required],
        selectedContentType: [''],
        selectedQuery: [''],
        selectedContentElement: [''],
        note: ['', Validators.required],
    });
  }

  hasContentItems():boolean {
    return (this.renderTemplateForm.get('maxEntries').value > 0)
  }

  selectContentType() {
    let selectedContentType = this.renderTemplateForm.get('selectedContentType').value;
    this.code = {
      name: selectedContentType,
      code: '',
      isQuery: false
    };
    this.contentElements = this.contentElementsMap.get(selectedContentType);
    this.renderTemplateForm.get('selectedQuery').setValue("");
    return false;
  }

  selectQuery() {
    let selectedQuery = this.renderTemplateForm.get('selectedQuery').value;
    this.code = {
      name: selectedQuery,
      code: '',
      isQuery: true
    };
    this.contentElements = this.queryElementsMap.get(selectedQuery);
    console.log(this.contentElements);
    this.renderTemplateForm.get('selectedContentType').setValue("");
    return false;
  }

  addContentElement() {
    let selectedContentElement = this.renderTemplateForm.get('selectedContentElement').value;
    this.code.code += `<render-template element_name=\"${selectedContentElement}\"></render-template>`
    return false;
  }
  
  saveRenderTemplate() {
    const formValue = this.renderTemplateForm.value;
    const renderTemplate: RenderTemplate = {
      repository: 'bpwizard',
      workspace: 'default',
      library: 'design',
      name: formValue.name,
      title: formValue.title,
      description: formValue.description,
      code: this.code.code,
      preloop: formValue.preloop,
      postloop: formValue.postloop,
      maxEntries: formValue.maxEntries,
      note:  formValue.note,
      isQuery: this.code.isQuery,
      resourceName: this.code.name
    }
    this.wcmService.createRenderTemplate(renderTemplate).subscribe(
      (event: any) => {
        console.log(event);
      },
      response => {
        console.log("GET call in error", response);
        console.log(response);
      },
      () => {
        console.log("The GET observable is now completed.");
      }
    );
    console.log(renderTemplate)
  }
}
