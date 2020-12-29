import { Component, OnInit, OnDestroy, ViewEncapsulation } from "@angular/core";
import { ActivatedRoute, Router } from "@angular/router";
import { of, BehaviorSubject } from "rxjs";
import { catchError, filter } from "rxjs/operators";

import { wcmAnimations, UiService } from "bpw-common";
import {
  WcmHistory,
  WcmVersion,
  HistoryService,
  ContentAreaLayout,
  WcmConstants,
} from "bpw-wcm-service";

@Component({
  selector: "content-area-layout-history",
  templateUrl: "./content-area-layout-history.component.html",
  styleUrls: ["./content-area-layout-history.component.scss"],
  encapsulation: ViewEncapsulation.None,
  animations: wcmAnimations,
})
export class ContentAreaLayoutHistoryComponent implements OnInit, OnDestroy {
  repository: string = WcmConstants.REPO_BPWIZARD;
  workspace: string = WcmConstants.WS_DEFAULT;
  item: ContentAreaLayout;
  library: string;
  itemName: string;
  error: string;

  itemHistory: WcmVersion[] = [];
  itemHistorySubject = new BehaviorSubject<WcmVersion[]>(this.itemHistory);

  constructor(
    private historyService: HistoryService,
    private route: ActivatedRoute,
    protected router: Router,
    private uiService: UiService
  ) {}

  ngOnInit(): void {
    this.route.queryParams.subscribe((param) => this._getParameters(param));
  }
  ngOnDestroy(): void {
    this.itemHistorySubject.complete();
  }

  get itemPath(): string {
    return `${this.library}/contentAreaLayout/${this.itemName}`;
  }

  backToItems() {
    this.router.navigate(["/wcm-authoring/content-area-layout/list"]);
  }

  private _getParameters(param: any) {
    this.repository = param.repository;
    this.workspace = param.workspace;
    this.itemName = param.itemName;
    this.library = param.library;
    this.historyService
      .getHistory(this.repository, this.workspace, this.itemPath)
      .pipe(
        catchError((err) => {
          this.error = this.uiService.getErrorMessage(err);
          return of({});
        }),
        filter((grant) => !!grant)
      )
      .subscribe((history: WcmHistory) => {
        this._resolveItemHistories(history);
      });
  }

  private _resolveItemHistories(history: WcmHistory) {
    this.itemHistory = history.versions;
    this.itemHistorySubject.next(this.itemHistory);
  }
}
