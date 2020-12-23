import { Component, OnInit, OnDestroy } from "@angular/core";
import { ActivatedRoute, Router } from "@angular/router";
import { select, Store } from "@ngrx/store";
import { Subject, Observable, of } from "rxjs";
import { takeUntil, switchMap } from "rxjs/operators";
import cloneDeep from "lodash/cloneDeep";
import * as fromStore from "bpw-wcm-service";
import { ControlField, AuthoringTemplate, WcmUtils } from "bpw-wcm-service";

@Component({
  selector: "authoring-template-editor",
  templateUrl: "./authoring-template-editor.component.html",
  styleUrls: ["./authoring-template-editor.component.scss"],
})
export class AuthoringTemplateEditorComponent implements OnInit, OnDestroy {
  controlFields: ControlField[] = [];
  editing: boolean = false;
  library: string;
  repository: string;
  at: AuthoringTemplate;
  status$: Observable<any>;
  workspace: string;
  wcmPath: string;

  private unsubscribeAll: Subject<any>;

  constructor(
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
        switchMap((param) => this._getItem(param))
        //filter((item) => item != null)
      )
      .subscribe((item) => {
        this.at = cloneDeep(item);
      });

    this.status$ = this.store.pipe(select(fromStore.getAuthoringTemplateError));
  }

  /**
   * On destroy
   */
  ngOnDestroy(): void {
    // Unsubscribe from all subscriptions
    this.unsubscribeAll.next();
    this.unsubscribeAll.complete();
    this.store.dispatch(new fromStore.AuthoringTemplateClearError());
  }

  get successMessage(): string {
    return this.editing
      ? "Successfully updated resource type"
      : "Successfully created resource type";
  }

  backToItems() {
    this.router.navigate(["/wcm-authoring/authoring-template/list"]);
  }

  private _getItem(param: any): Observable<AuthoringTemplate> {
    this.wcmPath = param.wcmPath;
    this.workspace = param.workspace;
    this.repository = param.repository;
    this.library = param.library;
    this.editing = param.editing === "true";
    if (this.editing) {
      return this.store.pipe(
        select(fromStore.getAuthoringTemplateByLibraryAndName, {
          library: this.library,
          name: WcmUtils.itemName(this.wcmPath),
        })
      );
    } else {
      return of({
        repository: param.repository,
        workspace: param.workspace,
        library: param.library,
        name: "Authoring template name",
        title: "Title",
        description: "Content type",
        baseType: "Content",
        workflow: "System",
        categories: [],
        publishDate: new Date(2019, 0, 1),
        elementGroups: [],
        elements: {},
        properties: {
          name: {
            name: "name",
            title: "Name",
            controlType: "text",
            dataType: "Text",
            valdition: "[^(<[.\n]+>)]*",
            mandatory: true,
            systemIndexed: true,
            userSearchable: true,
            showInList: true,
          },
          title: {
            name: "title",
            title: "Title",
            controlType: "text",
            dataType: "Text",
            valdition: "[^(<[.\n]+>)]*",
            mandatory: true,
            systemIndexed: true,
            userSearchable: true,
            showInList: true,
          },
          description: {
            name: "description",
            title: "Description",
            controlType: "textarea",
            rows: 3,
            dataType: "Text",
            systemIndexed: true,
          },
          categories: {
            name: "categories",
            title: "Category",
            controlType: "category",
            userSearchable: true,
          },
          workflow: {
            name: "workflow",
            title: "Workflow",
            controlType: "text",
            dataType: "Text",
            valditionRegEx: "[^(<[.\n]+>)]*",
            mandatory: true,
            systemIndexed: true,
            userSearchable: true,
            showInList: true,
          },
        },
      });
    }
  }
}
