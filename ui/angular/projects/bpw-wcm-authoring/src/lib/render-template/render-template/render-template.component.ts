import {
  Component,
  OnInit,
  OnDestroy,
  ViewEncapsulation,
  ViewChild,
  ViewContainerRef,
} from "@angular/core";
import { FormBuilder, FormGroup, Validators } from "@angular/forms";
import { ActivatedRoute, Router } from "@angular/router";
import { select, Store } from "@ngrx/store";
import { Observable, of, Subject } from "rxjs";
import { map, tap, takeUntil, switchMap, filter } from "rxjs/operators";

import {
  RenderTemplate,
  AuthoringTemplate,
  WcmConfigService,
  QueryStatement,
  WcmUtils,
} from "bpw-wcm-service";
import { BlockUIService } from "bpw-common";

export interface Code {
  name: string;
  code: string;
  preloop: string;
  postloop: string;
  query: boolean;
}
import * as fromStore from "bpw-wcm-service";
class ContentType {
  name: string;
  value: string;
}
@Component({
  selector: "render-template",
  templateUrl: "./render-template.component.html",
  styleUrls: ["./render-template.component.scss"],
  encapsulation: ViewEncapsulation.None,
})
export class RenderTemplateComponent implements OnInit, OnDestroy {
  repository: string;
  workspace: string;
  wcmPath: string;
  library: string;
  editing: boolean = false;

  item: RenderTemplate;
  status$: Observable<any>;
  @ViewChild("blockUIContainer", { read: ViewContainerRef })
  blockui: ViewContainerRef;
  componentRef: any;
  blocking: boolean = false;
  private unsubscribeAll: Subject<any>;

  contentElementsMap = new Map<String, string[]>();
  contentPropertiesMap = new Map<String, string[]>();
  queryElementsMap = new Map<String, string[]>();

  contentTypes: ContentType[] = [];
  queries: ContentType[] = [];
  renderTemplateForm: FormGroup;
  code: Code;

  constructor(
    protected wcmConfigService: WcmConfigService,
    private route: ActivatedRoute,
    protected router: Router,
    private store: Store<fromStore.WcmAppState>,
    private formBuilder: FormBuilder,
    private blockUIService: BlockUIService
  ) {
    this.unsubscribeAll = new Subject();
  }

  ngOnInit() {
    this.code = {
      name: "",
      code: "",
      preloop: "",
      postloop: "",
      query: false,
    };
    this.renderTemplateForm = this.formBuilder.group({
      name: ["My Template", Validators.required],
      title: ["", Validators.required],
      description: ["", Validators.required],
      maxEntries: [1, Validators.required],
      selectedContentType: [""],
      selectedQuery: [""],
      selectedContentElement: [""],
      selectedContentProperty: [""],
      note: ["", Validators.required],
    });

    this.route.queryParams
      .pipe(
        takeUntil(this.unsubscribeAll),
        switchMap((param) => this._getRenderTemplate(param)),
        filter((renderTemplate) => renderTemplate != null)
      )
      .subscribe((renderTemplate) =>
        this._subscribeRenderTemplate(renderTemplate)
      );

    this.store
      .pipe(
        takeUntil(this.unsubscribeAll),
        select(fromStore.getAuthoringTemplates)
      )
      .subscribe((authoringTemplates: { [key: string]: AuthoringTemplate }) => {
        if (authoringTemplates) {
          Object.entries(authoringTemplates)
            .filter(([key, at]) => {
              return this._isNotSystemLibrary(key);
            })
            .forEach(([key, at]) => {
              this.contentTypes.push({
                name: this._key(at.library, at.name),
                value: WcmUtils.itemPath(
                  at.library,
                  "authoringTemplate",
                  at.name
                ),
              });
              if (at.elements) {
                let formControls: string[] = [...Object.keys(at.elements)];
                this.contentElementsMap.set(
                  WcmUtils.itemPath(at.library, "authoringTemplate", at.name),
                  formControls
                );
              }
              if (at.properties) {
                let formControls: string[] = [...Object.keys(at.properties)];
                this.contentPropertiesMap.set(
                  WcmUtils.itemPath(at.library, "authoringTemplate", at.name),
                  formControls
                );
              }
            });
        }
      });
    this.store
      .pipe(takeUntil(this.unsubscribeAll), select(fromStore.getQueries))
      .subscribe((queryStatements: QueryStatement[]) => {
        if (queryStatements) {
          queryStatements
            .filter((query) => {
              return this._isNotSystemLibrary(query.library);
            })
            .forEach((query) => {
              this.queries.push({
                name: this._key(query.library, query.name),
                value: WcmUtils.itemPath(query.library, "query", query.name),
              });
              if (query.columns) {
                this.queryElementsMap.set(
                  WcmUtils.itemPath(query.library, "query", query.name),
                  query.columns
                );
              }
            });
        }
      });

    this.status$ = this.store.pipe(
      select(fromStore.getRenderTemplateError),
      tap((status) => this._handleWcmActionStatus(status))
    );
  }

  /**
   * On destroy
   */
  ngOnDestroy(): void {
    this.unsubscribeAll.next();
    this.unsubscribeAll.complete();
    this.store.dispatch(new fromStore.RenderTemplateClearError());
  }

  get successMessage(): string {
    return this.editing
      ? "Successfully updated render template"
      : "Successfully created render template";
  }

  backToItems() {
    this.router.navigate(["/wcm-authoring/render-template/list"]);
  }

  layoutTabIndex(): number {
    return this.item && this.item.code ? 1 : 0;
  }

  hasContentItems(): boolean {
    return this.item && this.renderTemplateForm.get("maxEntries").value > 0;
  }

  selectContentType() {
    let selectedContentType = this.renderTemplateForm.get("selectedContentType")
      .value;
    this.code = {
      name: selectedContentType,
      code: "",
      preloop: "",
      postloop: "",
      query: false,
    };
    this.renderTemplateForm.get("selectedQuery").setValue("");
    return false;
  }

  selectQuery() {
    let selectedQuery = this.renderTemplateForm.get("selectedQuery").value;
    this.code = {
      name: selectedQuery,
      code: "",
      preloop: "",
      postloop: "",
      query: true,
    };
    this.renderTemplateForm.get("selectedContentType").setValue("");
    return false;
  }

  get selectedContentType(): string {
    return this.code.query
      ? this.renderTemplateForm.get("selectedQuery").value
      : this.renderTemplateForm.get("selectedContentType").value;
  }

  get disableSelectContentTypeButton(): boolean {
    return !this.renderTemplateForm.get("selectedContentType").value;
  }

  get disableSelectQueryButton(): boolean {
    return !this.renderTemplateForm.get("selectedQuery").value;
  }

  get disableAddElementButton(): boolean {
    return !this.renderTemplateForm.get("selectedContentElement").value;
  }

  get disableAddPropertyButton(): boolean {
    return !this.renderTemplateForm.get("selectedContentProperty").value;
  }

  get contentElements(): string[] {
    return this.code.query
      ? this.queryElementsMap.get(
          this.renderTemplateForm.get("selectedQuery").value
        )
      : this.contentElementsMap.get(
          this.renderTemplateForm.get("selectedContentType").value
        );
  }

  get contentProperties(): string[] {
    return this.code.query
      ? []
      : this.contentPropertiesMap.get(
          this.renderTemplateForm.get("selectedContentType").value
        );
  }

  addContentElement() {
    let selectedContentElement = this.renderTemplateForm.get(
      "selectedContentElement"
    ).value;
    if (this.code.query) {
      this.code.code += `<query-result-column column='${selectedContentElement}'></query-result-column>`;
    } else {
      this.code.code += `<render-element element='${selectedContentElement}'></render-template>`;
    }
    return false;
  }

  addContentProperty() {
    let selectedContentProperty = this.renderTemplateForm.get(
      "selectedContentProperty"
    ).value;
    this.code.code += `<render-property property='${selectedContentProperty}'></render-property>`;
    return false;
  }

  saveRenderTemplate() {
    const formValue = this.renderTemplateForm.value;
    const renderTemplate: RenderTemplate = {
      repository: this.repository,
      workspace: this.workspace,
      library: this.library,
      name: formValue.name,
      title: formValue.title,
      description: formValue.description,
      code: this.code.code,
      preloop: this.code.preloop,
      postloop: this.code.postloop,
      maxEntries: formValue.maxEntries,
      note: formValue.note,
      query: this.code.query,
      resourceName: this.selectedContentType,
      rows: this.item.rows,
    };
    this._createBlockUIComponent(
      this.editing ? "Updating workflow" : "Creating workflow"
    );
    if (this.editing) {
      this.store.dispatch(new fromStore.UpdateRenderTemplate(renderTemplate));
    } else {
      this.store.dispatch(new fromStore.CreateRenderTemplate(renderTemplate));
    }
  }
  private _getRenderTemplate(param: any): Observable<RenderTemplate> {
    this.wcmPath = param.wcmPath;
    this.workspace = param.workspace;
    this.repository = param.repository;
    this.editing = param.editing === "true";
    this.library = WcmUtils.library(param.wcmPath);

    if (this.editing) {
      const path: string[] = this.wcmPath.split("/");
      return this.store.pipe(
        select(fromStore.getRenderTemplateByLibraryAndName, {
          library: this.library,
          name: path[path.length - 1],
        }),
        map((item: RenderTemplate) => {
          return {
            ...item,
          };
        })
      );
    } else {
      return of({
        repository: param.repository,
        workspace: param.workspace,
        library: this.library,
        name: "Render Template Name",
        resourceName: "",
        code: "",
        preloop: "",
        postloop: "",
        query: false,
        rows: [],
      });
    }
  }

  _subscribeRenderTemplate(item: RenderTemplate) {
    this.item = item;
    this.item.rows = this.item.rows ? [...this.item.rows] : [];

    this.code = {
      name: this.item.resourceName,
      code: this.item.code,
      preloop: this.item.preloop,
      postloop: this.item.postloop,
      query: this.item.query,
    };

    this.renderTemplateForm.patchValue({
      name: this.item.name,
      title: this.item.title,
      description: this.item.description,
      maxEntries: this.item.maxEntries,
      selectedContentType: this.item.query ? "" : this.item.resourceName,
      selectedQuery: this.item.query ? this.item.resourceName : "",
      selectedContentElement: "",
      selectedContentProperty: "",
      note: this.item.note,
    });
  }
  private _handleWcmActionStatus(status: any) {
    if (status != null && this.blocking) {
      this._destroyBlockUIComponent();
    }
  }

  private _createBlockUIComponent(message: string) {
    this.componentRef = this.blockUIService.createBlockUIComponent(
      message,
      this.blockui
    );
    this.blocking = true;
  }

  private _destroyBlockUIComponent() {
    this.blockUIService.destroyBlockUIComponent(
      this.blockui,
      this.componentRef
    );
    this.blocking = false;
  }

  private _isNotSystemLibrary(wcmPath: string) {
    return "system" !== WcmUtils.library(wcmPath);
  }

  private _key(library: string, itemName: string) {
    return `${itemName} - ${library}`;
  }
}
