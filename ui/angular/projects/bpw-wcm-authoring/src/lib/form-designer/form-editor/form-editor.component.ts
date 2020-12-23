import { Component, OnInit, OnDestroy } from "@angular/core";
import { ActivatedRoute, Router } from "@angular/router";
import { select, Store } from "@ngrx/store";
import { Subject, Observable, of } from "rxjs";
import { takeUntil, switchMap, filter } from "rxjs/operators";
import cloneDeep from "lodash/cloneDeep";
import { BlockUIService } from "bpw-common";
import * as fromStore from "bpw-wcm-service";
import { ControlField, Form, WcmUtils } from "bpw-wcm-service";

@Component({
  selector: "form-editor",
  templateUrl: "./form-editor.component.html",
  styleUrls: ["./form-editor.component.scss"],
})
export class FormEditorComponent implements OnInit, OnDestroy {
  controlFields: ControlField[] = [];
  editing: boolean = false;
  form: Form;
  library: string;
  repository: string;
  status$: Observable<any>;
  wcmPath: string;
  workspace: string;

  private unsubscribeAll: Subject<any>;

  constructor(
    private blockUIService: BlockUIService,
    private route: ActivatedRoute,
    private router: Router,
    private store: Store<fromStore.WcmAppState>
  ) {
    this.unsubscribeAll = new Subject();
  }

  ngOnInit() {
    this.store
      .pipe(takeUntil(this.unsubscribeAll), select(fromStore.getControlFiels))
      .subscribe((controlFiels: ControlField[]) => {
        if (controlFiels) {
          this.controlFields = controlFiels;
        }
      });

    this.route.queryParams
      .pipe(
        takeUntil(this.unsubscribeAll),
        switchMap((param) => this._getItem(param)),
        filter((form) => form != null)
      )
      .subscribe((form) => {
        this.form = cloneDeep(form);
      });

    this.status$ = this.store.pipe(
      select(fromStore.getFormError),
      takeUntil(this.unsubscribeAll)
    );
  }

  /**
   * On destroy
   */
  ngOnDestroy(): void {
    this.unsubscribeAll.complete();
    this.store.dispatch(new fromStore.AuthoringTemplateClearError());
  }

  get successMessage(): string {
    return this.editing
      ? "Successfully updated form"
      : "Successfully created form";
  }

  backToItems() {
    this.router.navigate(["/wcm-authoring/form-designer/list"]);
  }

  private _getItem(param: any): Observable<Form> {
    this.wcmPath = param.wcmPath;
    this.workspace = param.workspace;
    this.repository = param.repository;
    this.library = param.library;
    this.editing = param.editing === "true";
    if (this.editing) {
      return this.store.pipe(
        select(fromStore.getFormTemplateByLibraryAndName, {
          library: this.library,
          name: WcmUtils.itemName(this.wcmPath),
        })
      );
    } else {
      return of({
        repository: param.repository,
        workspace: param.workspace,
        library: param.library,
        name: "Form name",
        title: "Form Title",
        description: "Content type",
        author: "",
        formLayout: [],
        formControls: {},
      });
    }
  }
}
