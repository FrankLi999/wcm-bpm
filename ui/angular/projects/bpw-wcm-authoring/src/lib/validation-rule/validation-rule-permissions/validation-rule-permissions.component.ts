import { Component, OnInit, OnDestroy, ViewEncapsulation } from "@angular/core";
import { ActivatedRoute, Router } from "@angular/router";
import { of, BehaviorSubject } from "rxjs";
import { catchError, filter } from "rxjs/operators";

import { wcmAnimations, UiService } from "bpw-common";
import {
  ValidationRule,
  AclService,
  Grant,
  PrincipalPermission,
  WcmConstants,
} from "bpw-wcm-service";

@Component({
  selector: "validation-rule-permissions",
  templateUrl: "./validation-rule-permissions.component.html",
  styleUrls: ["./validation-rule-permissions.component.scss"],
  encapsulation: ViewEncapsulation.None,
  animations: wcmAnimations,
})
export class ValidationRulePermissionsComponent implements OnInit, OnDestroy {
  repository: string = WcmConstants.REPO_BPWIZARD;
  workspace: string = WcmConstants.WS_DEFAULT;
  item: ValidationRule;
  itemName: string;
  library: string;
  error: string;
  itemPermission: PrincipalPermission[] = [];
  itemPermissionSubject = new BehaviorSubject<PrincipalPermission[]>(
    this.itemPermission
  );

  constructor(
    private aclService: AclService,
    private route: ActivatedRoute,
    protected router: Router,
    private uiService: UiService
  ) {}

  ngOnInit(): void {
    this.route.queryParams.subscribe((param) => this._getParameters(param));
  }
  ngOnDestroy(): void {
    this.itemPermissionSubject.complete();
  }

  get itemPath(): string {
    return `${this.library}/validationRule/${this.itemName}`;
  }

  backToItems() {
    this.router.navigate(["/wcm-authoring/validation-rule/list"]);
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
