import {
  Component,
  OnInit,
  OnDestroy,
  ViewChild,
  ViewContainerRef,
} from "@angular/core";
import { ActivatedRoute, Router } from "@angular/router";
import { select, Store } from "@ngrx/store";
import { Observable, of } from "rxjs";
import { Subject } from "rxjs";
import { takeUntil, switchMap, filter, map, tap } from "rxjs/operators";

import { BlockUIService } from "bpw-common";
import * as fromStore from "bpw-wcm-service";
import {
  WcmConfigService,
  ValidationRule,
  JsonForm,
  WcmConstants,
  WcmUtils,
  WCM_ACTION_SUCCESSFUL,
} from "bpw-wcm-service";
import { FormConfig } from "../../config/form-config";

@Component({
  selector: "validation-rule",
  templateUrl: "./validation-rule.component.html",
  styleUrls: ["./validation-rule.component.scss"],
})
export class ValidationRuleComponent implements OnInit, OnDestroy {
  repository: string;
  workspace: string;
  wcmPath: string;
  library: string;
  editing: boolean = false;

  item: ValidationRule;

  itemForm: JsonForm;
  formConfig = FormConfig;
  formData: any;

  status$: Observable<any>;

  private blocking: boolean = false;
  @ViewChild("blockUIContainer", { read: ViewContainerRef })
  private blockui: ViewContainerRef;
  componentRef: any;
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
        filter((item) => item != null),
        switchMap((item) => this._getAuthoringTemplateForm(item))
      )
      .subscribe(
        (authoringTemplateForm) => (this.itemForm = authoringTemplateForm)
      );
    this.status$ = this.store.pipe(
      select(fromStore.getValidationRuleStatus),
      takeUntil(this.unsubscribeAll),
      tap((status) => this._handleWcmActionStatus(status))
    );
  }

  /**
   * On destroy
   */
  ngOnDestroy(): void {
    this.unsubscribeAll.next();
    this.unsubscribeAll.complete();
  }

  get successMessage(): string {
    return this.editing
      ? "Successfully updated validation rule"
      : "Successfully created validation rule";
  }

  backToItems() {
    this.router.navigate([WcmConstants.NAV_VALIDATION_RULE_LIST]);
  }

  upsertItem(formData: any) {
    this.item.name = formData.properties.name;
    this.item.title = formData.properties.title;
    this.item.description = formData.properties.description;

    this.item.type = formData.elements.type;
    this.item.rule = formData.elements.rule;
    this._createBlockUIComponent(
      this.editing ? "Updating workflow" : "Creating workflow"
    );
    if (this.editing) {
      this.store.dispatch(new fromStore.UpdateValidationRule(this.item));
    } else {
      this.store.dispatch(new fromStore.CreateValidationRule(this.item));
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

  private _getAuthoringTemplateForm(
    item: ValidationRule
  ): Observable<JsonForm> {
    this.item = item;
    this.formData = {
      properties: {
        name: item.name,
        title: item.title,
        description: item.description,
      },
      elements: {
        type: item.type,
        rule: item.rule,
      },
    };
    return this.store.pipe(
      select(fromStore.getAuthoringTemplateForms),
      map((authoringTemplateForms) => {
        return this.editing
          ? authoringTemplateForms[WcmConstants.WCM_VALIDATION_RULE_TYPE][1]
          : authoringTemplateForms[WcmConstants.WCM_VALIDATION_RULE_TYPE][0];
      })
    );
  }

  private _getItem(param: any): Observable<ValidationRule> {
    this.wcmPath = param.wcmPath;
    this.workspace = param.workspace;
    this.repository = param.repository;
    this.editing = param.editing === "true";
    this.library = param.library;
    if (this.editing) {
      return this.store.pipe(
        select(fromStore.getValidationRuleByLibraryAndName, {
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
        name: "ValidationRule",
        title: "Validatio Rule Title",
        description: "Validatio Rule Description",
        resourceName: "",
        type: "",
        rule: "",
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
