import { Component, OnInit, OnDestroy, ViewEncapsulation } from "@angular/core";
import { ActivatedRoute, Router } from "@angular/router";
import { of, BehaviorSubject } from "rxjs";
import { catchError, filter } from "rxjs/operators";

import { wcmAnimations, UiService } from "bpw-common";
import {
  AclService,
  Grant,
  PrincipalPermission,
  WcmConstants,
} from "bpw-wcm-service";

@Component({
  selector: "item-permissions",
  templateUrl: "./item-permissions.component.html",
  styleUrls: ["./item-permissions.component.scss"],
  encapsulation: ViewEncapsulation.None,
  animations: wcmAnimations,
})
export class ItemPermissionsComponent implements OnInit, OnDestroy {
  repository: string = WcmConstants.REPO_BPWIZARD;
  workspace: string = WcmConstants.WS_DEFAULT;
  wcmPath: string;
  error: string;
  itemPermission: PrincipalPermission[] = [];
  itemPermissionSubject = new BehaviorSubject<PrincipalPermission[]>(
    this.itemPermission
  );

  constructor(
    private aclService: AclService,
    private route: ActivatedRoute,
    private router: Router,
    private uiService: UiService
  ) {}

  ngOnInit(): void {
    this.route.queryParams.subscribe((param) => this._getParameters(param));
  }
  ngOnDestroy(): void {
    this.itemPermissionSubject.complete();
  }

  get itemPath(): string {
    return this.wcmPath;
  }

  backToItems() {
    this.router.navigate(["/wcm-authoring/site-explorer/navigator"]);
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
    this.wcmPath = param.wcmPath;
    this.aclService
      .getGrants(this.repository, this.workspace, this.wcmPath)
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
