import { Component, OnInit, OnDestroy, ViewEncapsulation } from "@angular/core";
import { ActivatedRoute, Router } from "@angular/router";
import { select, Store } from "@ngrx/store";
import { of, BehaviorSubject } from "rxjs";
import { catchError, filter } from "rxjs/operators";

import { wcmAnimations, UiService } from "bpw-common";
import * as fromStore from "bpw-wcm-service";
import {
  Library,
  AclService,
  Grant,
  PrincipalPermission,
  WcmConstants,
} from "bpw-wcm-service";

@Component({
  selector: "library-permissions",
  templateUrl: "./library-permissions.component.html",
  styleUrls: ["./library-permissions.component.scss"],
  encapsulation: ViewEncapsulation.None,
  animations: wcmAnimations,
})
export class LibraryPermissionsComponent implements OnInit, OnDestroy {
  repository: string = WcmConstants.REPO_BPWIZARD;
  workspace: string = WcmConstants.WS_DEFAULT;
  library: Library;
  libraryIndex: number;
  error: string;
  libraryPermission: PrincipalPermission[] = [];
  libraryPermissionSubject = new BehaviorSubject<PrincipalPermission[]>(
    this.libraryPermission
  );
  authoringTemplatePermission: PrincipalPermission[] = [];
  authoringTemplatePermissionSubject = new BehaviorSubject<
    PrincipalPermission[]
  >(this.authoringTemplatePermission);
  renderTemplatePermission: PrincipalPermission[] = [];
  renderTemplatePermissionSubject = new BehaviorSubject<PrincipalPermission[]>(
    this.renderTemplatePermission
  );
  contentAreaLayoutPermission: PrincipalPermission[] = [];
  contentAreaLayoutPermissionSubject = new BehaviorSubject<
    PrincipalPermission[]
  >(this.renderTemplatePermission);
  contentItemsPermission: PrincipalPermission[] = [];
  contentItemsPermissionSubject = new BehaviorSubject<PrincipalPermission[]>(
    this.contentItemsPermission
  );
  categoryPermission: PrincipalPermission[] = [];
  categoryPermissionSubject = new BehaviorSubject<PrincipalPermission[]>(
    this.categoryPermission
  );
  workflowPermission: PrincipalPermission[] = [];
  workflowPermissionSubject = new BehaviorSubject<PrincipalPermission[]>(
    this.workflowPermission
  );
  constructor(
    private aclService: AclService,
    private store: Store<fromStore.WcmAppState>,
    private route: ActivatedRoute,
    protected router: Router,
    private uiService: UiService
  ) {}

  ngOnInit(): void {
    this.route.queryParams.subscribe((param) => this._getParameters(param));
  }
  ngOnDestroy(): void {
    this.libraryPermissionSubject.complete();
    this.authoringTemplatePermissionSubject.complete();
    this.renderTemplatePermissionSubject.complete();
    this.contentAreaLayoutPermissionSubject.complete();
    this.contentItemsPermissionSubject.complete();
    this.categoryPermissionSubject.complete();
    this.workflowPermissionSubject.complete();
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

  private _isViewer(roles: string[]): boolean {
    return roles.some(
      (role) =>
        role === "viewer" || role === "editor" || role === "administrator"
    );
  }

  private _isEditor(roles: string[]): boolean {
    return roles.some((role) => role === "editor" || role === "administrator");
  }

  private _isAdministrator(roles: string[]): boolean {
    return roles.some((role) => role === "administrator");
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
    this.aclService
      .getLibraryGrants(
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
      .subscribe((grants: { [key: string]: Grant }) => {
        this._resolveLibraryPermissions(grants);
      });
  }

  private _resolveLibraryPermissions(grants: { [key: string]: Grant }) {
    grants = grants || {};
    this._resolvePrincipalPermissions(
      grants["library"],
      this.libraryPermission
    );
    this._resolvePrincipalPermissions(
      grants["renderTemplate"],
      this.renderTemplatePermission
    );
    this._resolvePrincipalPermissions(
      grants["contentAreaLayout"],
      this.contentAreaLayoutPermission
    );
    this._resolvePrincipalPermissions(
      grants["contentItem"],
      this.contentItemsPermission
    );
    this._resolvePrincipalPermissions(
      grants["authoringTemplate"],
      this.authoringTemplatePermission
    );
    this._resolvePrincipalPermissions(
      grants["category"],
      this.categoryPermission
    );
    this._resolvePrincipalPermissions(
      grants["workflow"],
      this.workflowPermission
    );
    this.libraryPermissionSubject.next(this.libraryPermission);
    this.renderTemplatePermissionSubject.next(this.renderTemplatePermission);
    this.contentAreaLayoutPermissionSubject.next(
      this.contentAreaLayoutPermission
    );
    this.contentItemsPermissionSubject.next(this.contentItemsPermission);
    this.authoringTemplatePermissionSubject.next(
      this.authoringTemplatePermission
    );
    this.categoryPermissionSubject.next(this.categoryPermission);
    this.workflowPermissionSubject.next(this.workflowPermission);
  }

  private _resolvePrincipalPermissions(
    grant: Grant,
    permissions: PrincipalPermission[]
  ) {
    grant.permissions.forEach((role) => {
      const permission: PrincipalPermission = {
        principalName: role.principalId,
        principalType: role.principalType,
        viewer: this._isViewer(role.wcmRoles),
        editor: this._isEditor(role.wcmRoles),
        administrator: this._isAdministrator(role.wcmRoles),
      };
      permissions.push(permission);
    });
  }
}
