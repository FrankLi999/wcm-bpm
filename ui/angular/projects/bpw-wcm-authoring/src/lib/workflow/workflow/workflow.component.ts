import {
  Component,
  OnInit,
  OnDestroy,
  ViewChild,
  ViewContainerRef,
} from "@angular/core";
import { ActivatedRoute, Router } from "@angular/router";
import { select, Store } from "@ngrx/store";
import { Observable, of, Subject } from "rxjs";
import { takeUntil, switchMap, filter, map, tap } from "rxjs/operators";
import {
  WcmConfigService,
  BpmnWorkflow,
  JsonForm,
  WcmConstants,
  WcmUtils,
  WCM_ACTION_SUCCESSFUL,
} from "bpw-wcm-service";
import * as fromStore from "bpw-wcm-service";
import { BlockUIService } from "bpw-common";
import { FormConfig } from "../../config/form-config";

@Component({
  selector: "workflow",
  templateUrl: "./workflow.component.html",
  styleUrls: ["./workflow.component.scss"],
})
export class WorkflowComponent implements OnInit, OnDestroy {
  editing: boolean = false;
  formConfig = FormConfig;
  formData: any;
  item: BpmnWorkflow;
  itemForm: JsonForm;
  library: string;
  repository: string;
  status$: Observable<any>;
  workspace: string;
  wcmPath: string;

  private blocking: boolean = false;
  @ViewChild("blockUIContainer", { read: ViewContainerRef })
  private blockui: ViewContainerRef;
  private componentRef: any;
  private unsubscribeAll: Subject<any>;

  constructor(
    protected wcmConfigService: WcmConfigService,
    private route: ActivatedRoute,
    protected router: Router,
    private store: Store<fromStore.WcmAppState>,
    private blockUIService: BlockUIService
  ) {
    this.unsubscribeAll = new Subject();
  }

  ngOnInit() {
    this.route.queryParams
      .pipe(
        takeUntil(this.unsubscribeAll),
        switchMap((param) => this._getItem(param)),
        filter((workflow) => workflow != null),
        switchMap((workflow) => this._getAuthoringTemplateForm(workflow))
      )
      .subscribe(
        (authoringTemplateForm) => (this.itemForm = authoringTemplateForm)
      );

    this.status$ = this.store.pipe(
      select(fromStore.getWorkflowStatus),
      takeUntil(this.unsubscribeAll),
      tap((status) => this._handleWcmActionStatus(status))
    );
  }

  /**
   * On destroy
   */
  ngOnDestroy(): void {
    this.unsubscribeAll.complete();
  }

  get successMessage(): string {
    return this.editing
      ? "Successfully updated workflow"
      : "Successfully created workflow";
  }

  backToItems() {
    this.router.navigate([WcmConstants.NAV_WORKDLOW_LIST]);
  }

  upsertItem(formData: any) {
    this.item.name = formData.properties.name;
    this.item.title = formData.properties.title;
    this.item.description = formData.properties.description;

    this.item.bpmn = formData.elements.bpmn;
    this._createBlockUIComponent(
      this.editing ? "Updating workflow" : "Creating workflow"
    );
    if (this.editing) {
      this.store.dispatch(new fromStore.UpdateWorkflow(this.item));
    } else {
      this.store.dispatch(new fromStore.CreateWorkflow(this.item));
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

  private _getAuthoringTemplateForm(item: BpmnWorkflow): Observable<JsonForm> {
    this.item = item;
    this.formData = {
      properties: {
        name: item.name,
        title: item.title,
        description: item.description,
      },
      elements: {
        bpmn: item.bpmn,
      },
    };

    return this.store.pipe(
      select(fromStore.getAuthoringTemplateForms),
      map((authoringTemplateForms) => {
        return this.editing
          ? authoringTemplateForms[WcmConstants.WCM_WORKFLOW_TYPE][1]
          : authoringTemplateForms[WcmConstants.WCM_WORKFLOW_TYPE][0];
      })
    );
  }

  _getItem(param: any): Observable<BpmnWorkflow> {
    this.wcmPath = param.wcmPath;
    this.workspace = param.workspace;
    this.repository = param.repository;
    this.editing = param.editing === "true";
    this.library = param.library;

    if (this.editing) {
      return this.store.pipe(
        select(fromStore.getWorkflowByLibraryAndName, {
          library: this.library,
          name: WcmUtils.itemName(this.wcmPath),
        }),
        map((item) => {
          return { ...item };
        })
      );
    } else {
      return of({
        repository: param.repository,
        workspace: param.workspace,
        library: this.library,
        name: "BpmnWorkflow",
        title: "Workflow Title",
        description: "Workflow Description",
        bpmn: "",
      });
    }
  }

  private _handleWcmActionStatus(status: any) {
    if (status != null && this.blocking) {
      this._destroyBlockUIComponent();
    }

    if (status === WCM_ACTION_SUCCESSFUL) {
      setTimeout(() => this.backToItems(), 500);
    }
  }
}
