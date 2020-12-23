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
import { WcmConfigService } from "bpw-wcm-service";

import { BlockUIService } from "bpw-common";
import * as fromStore from "bpw-wcm-service";
import {
  QueryStatement,
  JsonForm,
  WcmConstants,
  WCM_ACTION_SUCCESSFUL,
} from "bpw-wcm-service";
import { FormConfig } from "../../config/form-config";

@Component({
  selector: "query-statement",
  templateUrl: "./query-statement.component.html",
  styleUrls: ["./query-statement.component.scss"],
})
export class QueryStatementComponent implements OnInit, OnDestroy {
  editing: boolean = false;
  formConfig = FormConfig;
  formData: any;
  item: QueryStatement;
  itemForm: JsonForm;
  library: string;
  repository: string;
  status$: Observable<any>;
  wcmPath: string;
  workspace: string;

  private blocking: boolean = false;
  @ViewChild("blockUIContainer", { read: ViewContainerRef })
  private blockui: ViewContainerRef;
  private componentRef: any;
  private unsubscribeAll: Subject<any>;

  constructor(
    private blockUIService: BlockUIService,
    private route: ActivatedRoute,
    protected router: Router,
    private store: Store<fromStore.WcmAppState>,
    protected wcmConfigService: WcmConfigService
  ) {
    this.unsubscribeAll = new Subject();
  }

  ngOnInit() {
    this.route.queryParams
      .pipe(
        takeUntil(this.unsubscribeAll),
        switchMap((param) => this._getItem(param)),
        filter((item) => !!item),
        switchMap((item) => this._getAuthoringTemplateForm(item))
      )
      .subscribe(
        (authoringTemplateForm) => (this.itemForm = authoringTemplateForm)
      );

    this.status$ = this.store.pipe(
      select(fromStore.getQueryStatus),
      takeUntil(this.unsubscribeAll),
      tap((status) => this._handleWcmActionStatus(status))
      // map((status) => (status && status["error"] ? status["error"] : status))
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
      ? "Successfully updated workflow"
      : "Successfully created workflow";
  }

  backToItems() {
    this.router.navigate(["/wcm-authoring/query-builder/list"]);
  }

  upsertItem(formData: any) {
    this.item.name = formData.properties.name;
    this.item.title = formData.properties.title;
    this.item.query = formData.elements.query;
    this._createBlockUIComponent(
      this.editing ? "Updating query" : "Creating query"
    );
    if (this.editing) {
      this.store.dispatch(new fromStore.UpdateQuery(this.item));
    } else {
      this.store.dispatch(new fromStore.CreateQuery(this.item));
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
    item: QueryStatement
  ): Observable<JsonForm> {
    this.item = item;
    this.formData = {
      properties: {
        name: item.name,
        title: item.title,
      },
      elements: {
        query: item.query,
      },
    };
    return this.store.pipe(
      select(fromStore.getAuthoringTemplateForms),
      map((authoringTemplateForms) => {
        return this.editing
          ? authoringTemplateForms[WcmConstants.WCM_QUERY_TYPE][1]
          : authoringTemplateForms[WcmConstants.WCM_QUERY_TYPE][0];
      })
    );
  }

  private _getItem(param: any): Observable<QueryStatement> {
    this.wcmPath = param.wcmPath;
    this.workspace = param.workspace;
    this.repository = param.repository;
    this.editing = param.editing === "true";
    this.library = param.library;
    if (this.editing) {
      const path: string[] = this.wcmPath.split("/");
      return this.store.pipe(
        select(fromStore.getQueryByLibraryAndName, {
          library: this.library,
          name: path[path.length - 1],
        })
        //map(items => items[this._id(this.library, path[path.length - 1])])
      );
    } else {
      return of({
        repository: param.repository,
        workspace: param.workspace,
        library: this.library,
        name: "Query Statement",
        title: "Title",
        query: "Query",
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

  private _id(library: string, name: string) {
    return `${library}_${name}`;
  }
}
