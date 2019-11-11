import { Component, OnInit, OnDestroy, ViewEncapsulation, Input } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { select, Store } from '@ngrx/store';
import { Observable, of } from 'rxjs';
import {
  RenderTemplate,
  AuthoringTemplate
} from '../../model';
import { Subject } from 'rxjs';
import { takeUntil, switchMap, map, filter } from 'rxjs/operators';

export interface Code {
  name: string;
  code: string;
  preloop: string;
  postloop: string;
  isQuery: boolean;
}
import * as fromStore from '../../store';
import { WcmService } from 'app/wcm-authoring/service/wcm.service';

@Component({
  selector: 'render-template',
  templateUrl: './render-template.component.html',
  styleUrls: ['./render-template.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class RenderTemplateComponent implements OnInit, OnDestroy {
  @Input() repository: string;
  @Input() workspace: string;
  @Input() nodePath: string;
  @Input() editing: boolean = false;
  @Input() renderTemplate: RenderTemplate;

  contentTypes: string[] = [];
  queries: string[] = [
  ];
  // library: string;
  // jsonForm: JsonForm;
  renderTemplateForm: FormGroup;
  code: Code;
  
  private unsubscribeAll: Subject<any>;
  error: string;
  constructor(
    private route: ActivatedRoute,
    private wcmService: WcmService,
    private store: Store<fromStore.WcmAppState>,
    private formBuilder: FormBuilder) { 
      this.unsubscribeAll = new Subject();
    }

  ngOnInit() {
    this.code = {
      name: '',
      code: '',
      preloop: '',
      postloop: '',
      isQuery: false
    };
    this.renderTemplateForm = this.formBuilder.group({
      name: ['My Template', Validators.required],
      title: ['', Validators.required],
      description: ['', Validators.required],
      maxEntries: [1, Validators.required],
      selectedContentType: [''],
      selectedQuery: [''],
      selectedContentElement: [''],
      note: ['', Validators.required],
    });

    this.route.queryParams.pipe(
      takeUntil(this.unsubscribeAll),
      switchMap(param => this.getRenderTemplate(param)),
      filter(renderTemplate => renderTemplate != null) // ,
      // switchMap(renderTemplate => this.getJsonForms(renderTemplate))
    ).subscribe(renderTemplate => this.subscribeRenderTemplate(renderTemplate));

    this.store.pipe(
        takeUntil(this.unsubscribeAll),
        select(fromStore.getCreateRenderTemplateError)).subscribe(
      (error: string) => {
         this.error = error;
      }
    )
    
    this.store.pipe(
      takeUntil(this.unsubscribeAll),
      select(fromStore.getAuthoringTemplates)).subscribe(
        (authoringTemplates: {[key: string]: AuthoringTemplate}) => {
          if (authoringTemplates) {
            Object.entries(authoringTemplates).forEach(([key, at]) => {
              this.contentTypes.push(`${at.repository}/${at.workspace}/${at.library}/${at.name}`);
            });
          }
        }
      );  
  }

  /**
  * On destroy
  */
  ngOnDestroy(): void {
    this.unsubscribeAll.next();
    this.unsubscribeAll.complete();
    this.error && this.store.dispatch(new fromStore.RenderTemplateClearError);
  }

  getRenderTemplate(param: any): Observable<RenderTemplate> {
    this.nodePath = param.wcmPath;
    this.workspace = param.workspace;
    this.repository = param.repository;
    this.editing = param.editing === 'true';
    if (this.editing) {
      return this.wcmService.getRenderTemplate(this.repository, this.workspace, this.nodePath);
    } else {
      return of({
        repository: param.repository,
        workspace: param.workspace,
        library: 'library',
        name: 'Render Template Name',
        resourceName: '',
        code: '',
        preloop: '',
        postloop: '',
        isQuery: false,
        rows: []
      })
    }
  }
  
  subscribeRenderTemplate(renderTemplate: RenderTemplate) {
    this.renderTemplate = renderTemplate;
    this.renderTemplate.rows = this.renderTemplate.rows || [];

    this.code = {
      name: this.renderTemplate.resourceName,
      code: this.renderTemplate.code,
      preloop: this.renderTemplate.preloop,
      postloop: this.renderTemplate.postloop,
      isQuery: this.renderTemplate.isQuery
    };

    this.renderTemplateForm.patchValue({
      name: this.renderTemplate.name,
      title: this.renderTemplate.title,
      description: this.renderTemplate.description,
      maxEntries: this.renderTemplate.maxEntries,
      selectedContentType: this.renderTemplate.isQuery ? '' : this.renderTemplate.resourceName,
      selectedQuery: this.renderTemplate.isQuery ? this.renderTemplate.resourceName : '',
      electedContentElement: '',
      note: this.renderTemplate.note
    });
  }

  layoutTabIndex(): number {
    return (this.renderTemplate && this.renderTemplate.code) ? 1 : 0;
  }

  hasContentItems():boolean {
    return (this.renderTemplate && this.renderTemplateForm.get('maxEntries').value > 0)
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
    this.renderTemplateForm.get('selectedContentType').setValue("");
    return false;
  }

  selectedContentType() {
    return this.renderTemplateForm.get('selectedContentType').value;
  }

  addContentElement() {
    let selectedContentElement = this.renderTemplateForm.get('selectedContentElement').value;
    this.code.code += `<render-template element-name='${selectedContentElement}'></render-template>`
    return false;
  }
  
  saveRenderTemplate() {
    const formValue = this.renderTemplateForm.value;
    const library = this.nodePath.split('/', 4)[2];
    const renderTemplate: RenderTemplate = {
      repository: this.repository,
      workspace: this.workspace,
      library: library,
      name: formValue.name,
      title: formValue.title,
      description: formValue.description,
      code: this.code.code,
      preloop: this.code.preloop,
      postloop: this.code.postloop,
      maxEntries: formValue.maxEntries,
      note:  formValue.note,
      isQuery: this.code.isQuery,
      resourceName: this.code.name,
      rows: this.renderTemplate.rows
    }

    this.store.dispatch(new fromStore.CreateRenderTemplate(renderTemplate));
  }
}
