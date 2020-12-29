import { Component, OnInit, OnDestroy, ViewEncapsulation } from "@angular/core";
import { ActivatedRoute, Router } from "@angular/router";
import { of, BehaviorSubject, Observable } from "rxjs";
import { catchError, filter, switchMap, tap } from "rxjs/operators";
import { wcmAnimations, UiService } from "bpw-common";
import {
  WcmHistory,
  WcmVersion,
  HistoryService,
  AuthoringTemplate,
  WcmConstants,
} from "bpw-wcm-service";

@Component({
  selector: "authoring-template-history",
  templateUrl: "./authoring-template-history.component.html",
  styleUrls: ["./authoring-template-history.component.scss"],
  encapsulation: ViewEncapsulation.None,
  animations: wcmAnimations,
})
export class AuthoringTemplateHistoryComponent implements OnInit, OnDestroy {
  error: string;
  item: AuthoringTemplate;
  itemName: string;
  itemHistory: WcmVersion[] = [];
  itemHistorySubject = new BehaviorSubject<WcmVersion[]>(this.itemHistory);
  library: string;
  repository: string = WcmConstants.REPO_BPWIZARD;
  workspace: string = WcmConstants.WS_DEFAULT;

  constructor(
    private historyService: HistoryService,
    private route: ActivatedRoute,
    private router: Router,
    private uiService: UiService
  ) {}

  ngOnInit(): void {
    this.route.queryParams
      .pipe(
        switchMap((param) => this._getParameters(param)),
        catchError((err) => {
          this.error = this.uiService.getErrorMessage(err);
          return of({});
        }),
        filter((grant) => !!grant),
        tap((history: WcmHistory) => {
          this._resolveItemHistories(history);
        })
      )
      .subscribe();
  }
  ngOnDestroy(): void {
    this.itemHistorySubject.complete();
  }

  get itemPath(): string {
    return `${this.library}/authoringTemplate/${this.itemName}`;
  }

  backToItems() {
    this.router.navigate(["/wcm-authoring/authoring-template/list"]);
  }

  private _getParameters(param: any): Observable<WcmHistory> {
    this.repository = param.repository;
    this.workspace = param.workspace;
    this.itemName = param.itemName;
    this.library = param.library;
    return this.historyService.getHistory(
      this.repository,
      this.workspace,
      this.itemPath
    );
  }

  private _resolveItemHistories(history: WcmHistory) {
    this.itemHistory = history.versions;
    this.itemHistorySubject.next(this.itemHistory);
  }
}
