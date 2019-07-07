import { Component, OnInit, ViewEncapsulation } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

export interface Code {
  name: string;
  code: string;
  isQuery: boolean;
}

export interface RenderTemplateModel {
  site: string;
  title: string;
  description: string;
  maxContents: number;
  preLoop?: string;
  postLoop?: string;
  notes: string;
  code: Code;
}

@Component({
  selector: 'render-template',
  templateUrl: './render-template.component.html',
  styleUrls: ['./render-template.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class RenderTemplateComponent implements OnInit {
  renderTemplateModel: RenderTemplateModel = {
    site: 'my_site',
    title: '',
    description: '',
    maxContents: 0,
    preLoop: '',
    postLoop: '',
    notes: '',
    code: {
      isQuery: false,
      name: '',
      code: ''
    }
  }

  renderTemplateForm: FormGroup;
  code: Code = {
    name: '',
    code: '',
    isQuery: false
  };
  
  contentTypes: string[] = [
    "content_type1",
    "content_type2",
    "content_type3"
  ];

  queries: string[] = [
    "query1",
    "query2",
    "query3"
  ];

  contentElements: string[] = [
    "content_element_1",
    "content_element_2",
    "content_element_3",
    "content_element_4",
    "content_element_5",
    "content_element_6",
    "content_element_7"
  ];

  constructor(private formBuilder: FormBuilder) { }

  ngOnInit() {
    this.renderTemplateForm = this.formBuilder.group({
        site: ['My Site', Validators.required],
        title: ['', Validators.required],
        description: ['', Validators.required],
        maxContents: [1, Validators.required],
        preLoop: ['<div>preLoop</div>', Validators.required],
        postLoop: ['<div>postLoop</div>', Validators.required],
        selectedContentType: [''],
        selectedQuery: [''],
        selectedContentElement: [''],
        notes: ['', Validators.required],
    });
  }

  hasContentItems():boolean {
    return (this.renderTemplateForm.get('maxContents').value > 0)
  }

  selectContentType() {
      let selectedContentType = this.renderTemplateForm.get('selectedContentType').value;
      this.code = {
        name: selectedContentType,
        code: '',
        isQuery: false
      };
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
    
    this.renderTemplateForm.get('selectedContentType').setValue("");
    return false;
  }

  addContentElement() {
    let selectedContentElement = this.renderTemplateForm.get('selectedContentElement').value;
    this.code.code += `<render-template element_name=\"${selectedContentElement}\"></render-template>`
    return false;
  }
}
