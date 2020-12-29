import {
  Component,
  OnInit,
  OnDestroy,
  ViewChild,
  ViewContainerRef,
} from "@angular/core";
import { ActivatedRoute, Router } from "@angular/router";
import { Store, select } from "@ngrx/store";
import { Subscription, of } from "rxjs";
import { catchError, filter, tap } from "rxjs/operators";
import { BlockUIService } from "bpw-common";
import { ContentItemService, JsonForm, WcmConstants } from "bpw-wcm-service";
import * as fromStore from "bpw-wcm-service";
import { FormConfig } from "../../config/form-config";

@Component({
  selector: "reject-draft",
  templateUrl: "./reject-draft.component.html",
  styleUrls: ["./reject-draft.component.scss"],
})
export class RejectDraftComponent implements OnInit, OnDestroy {
  contentId: string;
  formConfig = FormConfig;
  processInstanceId: string;
  repository: string;
  reviewTaskForm: JsonForm;
  reviewer: string;
  wcmPath: string;
  workspace: string;

  private blocking: boolean = false;
  @ViewChild("blockUIContainer", { read: ViewContainerRef })
  private blockui: ViewContainerRef;
  private componentRef: any;
  private sub: Subscription;

  constructor(
    private blockUIService: BlockUIService,
    private contentItemService: ContentItemService,
    private route: ActivatedRoute,
    private router: Router,
    private store: Store<fromStore.WcmAppState>
  ) {}

  ngOnInit() {
    this.sub = this.route.queryParams
      .pipe(tap((param) => this._handleParams(param)))
      .subscribe();
    this.store
      .pipe(
        select(fromStore.getForms),
        filter((jsonForms) => !!jsonForms)
      )
      .subscribe((jsonForms) => this._getControlForms(jsonForms));
  }

  /**
   * On destroy
   */
  ngOnDestroy(): void {
    this.sub.unsubscribe();
  }

  rejectDraft(form) {
    this._createBlockUIComponent("Rejecting draft item");
    this.contentItemService
      .rejectDraft({
        repository: this.repository,
        wcmPath: this.wcmPath,
        contentId: this.contentId,
        processInstanceId: this.processInstanceId,
        reviewer: this.reviewer,
        comment: form.comment,
        approved: false,
      })
      .pipe(
        filter((resp) => resp !== undefined),
        tap((resp) => {
          this._destroyBlockUIComponent();
        }),
        catchError((err) => {
          this._destroyBlockUIComponent();
          return of(err);
        })
      )
      .subscribe();
  }

  backToItems() {
    this.router.navigate(["/wcm-authoring/review-tasks"]);
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

  private _getControlForms(forms: { [key: string]: JsonForm[] }) {
    this.reviewTaskForm = forms[WcmConstants.WCM_REIVEW_TASK_FORM][0];
  }

  private _handleParams(param: any) {
    this.wcmPath = param.wcmPath;
    this.workspace = param.workspace;
    this.repository = param.repository;
    this.processInstanceId = param.processInstanceId;
    this.reviewer = param.reviewer;
    this.contentId = param.contentId;
  }
}
