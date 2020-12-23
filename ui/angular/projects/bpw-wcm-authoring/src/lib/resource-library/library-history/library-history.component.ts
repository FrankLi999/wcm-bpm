import { Component, OnInit, OnDestroy, ViewEncapsulation } from "@angular/core";
import { ActivatedRoute, Router } from "@angular/router";
import { select, Store } from "@ngrx/store";
import { of, BehaviorSubject } from "rxjs";
import { catchError, filter } from "rxjs/operators";

import { wcmAnimations, UiService } from "bpw-common";
import * as fromStore from "bpw-wcm-service";
import {
  WcmHistory,
  WcmVersion,
  HistoryService,
  Library,
  WcmConstants,
} from "bpw-wcm-service";

@Component({
  selector: "library-history",
  templateUrl: "./library-history.component.html",
  styleUrls: ["./library-history.component.scss"],
  encapsulation: ViewEncapsulation.None,
  animations: wcmAnimations,
})
export class LibraryHistoryComponent implements OnInit, OnDestroy {
  repository: string = WcmConstants.REPO_BPWIZARD;
  workspace: string = WcmConstants.WS_DEFAULT;
  library: Library;
  libraryIndex: number;
  error: string;

  libraryHistory: WcmVersion[] = [];
  libraryHistorySubject = new BehaviorSubject<WcmVersion[]>(
    this.libraryHistory
  );
  authoringTemplateHistory: WcmVersion[] = [];
  authoringTemplateHistorySubject = new BehaviorSubject<WcmVersion[]>(
    this.authoringTemplateHistory
  );
  renderTemplateHistory: WcmVersion[] = [];
  renderTemplateHistorySubject = new BehaviorSubject<WcmVersion[]>(
    this.renderTemplateHistory
  );
  contentAreaLayoutHistory: WcmVersion[] = [];
  contentAreaLayoutHistorySubject = new BehaviorSubject<WcmVersion[]>(
    this.contentAreaLayoutHistory
  );
  contentItemsHistory: WcmVersion[] = [];
  contentItemsHistorySubject = new BehaviorSubject<WcmVersion[]>(
    this.contentItemsHistory
  );
  categoryHistory: WcmVersion[] = [];
  categoryHistorySubject = new BehaviorSubject<WcmVersion[]>(
    this.categoryHistory
  );
  workflowHistory: WcmVersion[] = [];
  workflowHistorySubject = new BehaviorSubject<WcmVersion[]>(
    this.workflowHistory
  );
  constructor(
    private historyService: HistoryService,
    private store: Store<fromStore.WcmAppState>,
    private route: ActivatedRoute,
    protected router: Router,
    private uiService: UiService
  ) {}

  ngOnInit(): void {
    this.route.queryParams.subscribe((param) => this._getParameters(param));
  }
  ngOnDestroy(): void {
    this.libraryHistorySubject.complete();
    this.authoringTemplateHistorySubject.complete();
    this.renderTemplateHistorySubject.complete();
    this.contentAreaLayoutHistorySubject.complete();
    this.contentItemsHistorySubject.complete();
    this.categoryHistorySubject.complete();
    this.workflowHistorySubject.complete();
  }

  get authoringTemplatePath(): string {
    return `${this.library.name}/authoringTemplate`;
  }

  get contentItemsPath(): string {
    return `${this.library.name}/rootSiteArea`;
  }

  get renderTemplatePath(): string {
    return `${this.library.name}/renderTemplate`;
  }

  get contentAreaLayoutPath(): string {
    return `${this.library.name}/contentAreaLayout`;
  }

  get categoryPath(): string {
    return `${this.library.name}/category`;
  }

  get workflowPath(): string {
    return `${this.library.name}/workflow`;
  }

  backToLibraries() {
    this.router.navigate(["/wcm-authoring/resource-library/list"]);
  }

  private _getParameters(param: any) {
    this.repository = param.repository;
    this.workspace = param.workspace;
    this.store
      .pipe(select(fromStore.getWcmLibraries))
      .subscribe((libraries) => this._initializeData(param, libraries));
  }

  private _initializeData(param: any, libraries: Library[]) {
    this.library = libraries[param.libraryIndex];
    this.libraryIndex = param.libraryIndex;
    this.historyService
      .getLibraryHistory(
        this.repository,
        this.workspace,
        `/${this.library.name}`
      )
      .pipe(
        catchError((err) => {
          this.error = this.uiService.getErrorMessage(err);
          return of({});
        }),
        filter((grant) => !!grant)
      )
      .subscribe((histories: { [key: string]: WcmHistory }) => {
        this._resolveLibraryHistories(histories);
      });
  }

  private _resolveLibraryHistories(histories: { [key: string]: WcmHistory }) {
    histories = histories || {};
    this._resolveHistory(histories["library"], this.libraryHistory);
    this._resolveHistory(
      histories["renderTemplate"],
      this.renderTemplateHistory
    );
    this._resolveHistory(
      histories["contentAreaLayout"],
      this.contentAreaLayoutHistory
    );
    this._resolveHistory(histories["contentItem"], this.contentItemsHistory);
    this._resolveHistory(
      histories["authoringTemplate"],
      this.authoringTemplateHistory
    );
    this._resolveHistory(histories["category"], this.categoryHistory);
    this._resolveHistory(histories["workflow"], this.workflowHistory);
    this.libraryHistorySubject.next(this.libraryHistory);
    this.renderTemplateHistorySubject.next(this.renderTemplateHistory);
    this.contentAreaLayoutHistorySubject.next(this.contentAreaLayoutHistory);
    this.contentItemsHistorySubject.next(this.contentItemsHistory);
    this.authoringTemplateHistorySubject.next(this.authoringTemplateHistory);
    this.categoryHistorySubject.next(this.categoryHistory);
    this.workflowHistorySubject.next(this.workflowHistory);
  }

  private _resolveHistory(history: WcmHistory, permissions: WcmVersion[]) {
    history && history.versions && permissions.concat(history.versions);
  }
}
