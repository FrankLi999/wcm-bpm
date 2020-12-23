import { Component, OnInit, OnDestroy, ViewEncapsulation } from "@angular/core";
import { ActivatedRoute, Router } from "@angular/router";
import { of, BehaviorSubject } from "rxjs";
import { catchError, filter, tap } from "rxjs/operators";

import { wcmAnimations, UiService } from "bpw-common";
import {
  AuthoringTemplate,
  AclService,
  Grant,
  PrincipalPermission,
  WcmConstants,
} from "bpw-wcm-service";

@Component({
  selector: "authoring-template-permission",
  templateUrl: "./authoring-template-permission.component.html",
  styleUrls: ["./authoring-template-permission.component.scss"],
  encapsulation: ViewEncapsulation.None,
  animations: wcmAnimations,
})
export class AuthoringTemplatePermissionComponent implements OnInit, OnDestroy {
  error: string;
  item: AuthoringTemplate;
  itemName: string;
  itemPermission: PrincipalPermission[] = [];
  itemPermissionSubject = new BehaviorSubject<PrincipalPermission[]>(
    this.itemPermission
  );
  library: string;
  repository: string = WcmConstants.REPO_BPWIZARD;
  workspace: string = WcmConstants.WS_DEFAULT;

  constructor(
    private aclService: AclService,
    private route: ActivatedRoute,
    private router: Router,
    private uiService: UiService
  ) {}

  ngOnInit(): void {
    this.route.queryParams
      .pipe(tap((param) => this._getParameters(param)))
      .subscribe();
  }

  ngOnDestroy(): void {
    this.itemPermissionSubject.complete();
  }

  get itemPath(): string {
    return `${this.library}/authoringTemplate/${this.itemName}`;
  }

  backToItems() {
    this.router.navigate(["/wcm-authoring/authoring-template/list"]);
  }

  private _getParameters(param: any) {
    this.repository = param.repository;
    this.workspace = param.workspace;
    this.library = param.library;
    this.itemName = param.itemName;
    this.aclService
      .getGrants(this.repository, this.workspace, this.itemPath)
      .pipe(
        catchError((err) => {
          this.error = this.uiService.getErrorMessage(err);
          return of({});
        }),
        filter((grant) => !!grant)
      )
      .subscribe((grant: Grant) => {
        this._resolveItemPermissions(grant);
      });
  }

  private _isAdministrator(roles: string[]): boolean {
    return roles.some((role) => role === "administrator");
  }

  private _isEditor(roles: string[]): boolean {
    return roles.some((role) => role === "editor" || role === "administrator");
  }

  private _isViewer(roles: string[]): boolean {
    return roles.some(
      (role) =>
        role === "viewer" || role === "editor" || role === "administrator"
    );
  }

  private _resolveItemPermissions(grant: Grant) {
    grant.permissions.forEach((role) => {
      const permission: PrincipalPermission = {
        principalName: role.principalId,
        principalType: role.principalType,
        viewer: this._isViewer(role.wcmRoles),
        editor: this._isEditor(role.wcmRoles),
        administrator: this._isAdministrator(role.wcmRoles),
      };
      this.itemPermission.push(permission);
    });
    this.itemPermissionSubject.next(this.itemPermission);
  }
}
