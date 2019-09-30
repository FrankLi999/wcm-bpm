import { Component, OnInit, OnDestroy, ViewEncapsulation } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { select, Store } from '@ngrx/store';
import {
  RenderTemplate,
  AuthoringTemplate,
  Query
} from '../../model';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';

export interface Code {
  name: string;
  code: string;
  preloop: string;
  postloop: string;
  isQuery: boolean;
}
import * as fromStore from '../../store';

@Component({
  selector: 'render-template',
  templateUrl: './render-template.component.html',
  styleUrls: ['./render-template.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class RenderTemplateComponent implements OnInit, OnDestroy {
  
  renderTemplateForm: FormGroup;
  code: Code = {
    name: '',
    code: '',
    preloop: '',
    postloop: '',
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
  private unsubscribeAll: Subject<any>;
  error: string;
  constructor(
    // private wcmService: WcmService,
    private store: Store<fromStore.WcmAppState>,
    private formBuilder: FormBuilder) { 
      this.unsubscribeAll = new Subject();
    }

  ngOnInit() {
    this.store.pipe(
        takeUntil(this.unsubscribeAll),
        select(fromStore.getCreateRenderTemplateError)).subscribe(
      (error: string) => {
         this.error = error;
      }
    )
    // this.wcmService.getAuthoringTemplate('bpwizard', 'default').subscribe(
      this.store.pipe(
        takeUntil(this.unsubscribeAll),
        select(fromStore.getAuthoringTemplates)).subscribe(
      (authoringTemplates: {[key: string]: AuthoringTemplate}) => {
        if (authoringTemplates) {
          Object.entries(authoringTemplates).forEach(([key, at]) => {
            this.contentTypes.push(at.name);
            let formControls: string[] = [...Object.keys(at.formControls)];
            // for (let property in at.formControls) {
            //   formControls.push(property)
            // }
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
        // preloop: ['<div>preLoop</div>', Validators.required],
        // postloop: ['<div>postLoop</div>', Validators.required],
        selectedContentType: [''],
        selectedQuery: [''],
        selectedContentElement: [''],
        note: ['', Validators.required],
    });
  }

  /**
  * On destroy
  */
  ngOnDestroy(): void {
    this.unsubscribeAll.next();
    this.unsubscribeAll.complete();
    this.error && this.store.dispatch(new fromStore.RenderTemplateClearError);
  }

  hasContentItems():boolean {
    return (this.renderTemplateForm.get('maxEntries').value > 0)
  }

  selectContentType() {
    let selectedContentType = this.renderTemplateForm.get('selectedContentType').value;
    this.code = {
      name: selectedContentType,
      code: '',
      preloop: '',
      postloop: '',
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
      preloop: '',
      postloop: '',
      isQuery: true
    };
    this.contentElements = this.queryElementsMap.get(selectedQuery);
    console.log(this.contentElements);
    this.renderTemplateForm.get('selectedContentType').setValue("");
    return false;
  }

  addContentElement() {
    let selectedContentElement = this.renderTemplateForm.get('selectedContentElement').value;
    this.code.code += `<render-template [elementName]='\"${selectedContentElement}\"'></render-template>`
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
      preloop: this.code.preloop,
      postloop: this.code.postloop,
      maxEntries: formValue.maxEntries,
      note:  formValue.note,
      isQuery: this.code.isQuery,
      resourceName: this.code.name
    }

    this.store.dispatch(new fromStore.CreateRenderTemplate(renderTemplate));
  }
}
