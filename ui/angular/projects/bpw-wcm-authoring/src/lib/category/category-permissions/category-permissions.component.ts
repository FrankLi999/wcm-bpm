import { Component, OnInit, OnDestroy, ViewEncapsulation } from "@angular/core";
import { ActivatedRoute, Router } from "@angular/router";
import { Store } from "@ngrx/store";
import { of, BehaviorSubject } from "rxjs";
import { catchError, filter } from "rxjs/operators";

import { wcmAnimations, UiService } from "bpw-common";
import * as fromStore from "bpw-wcm-service";
import {
  Category,
  AclService,
  Grant,
  PrincipalPermission,
  WcmConstants,
  WcmUtils,
} from "bpw-wcm-service";

@Component({
  selector: "category-permissions",
  templateUrl: "./category-permissions.component.html",
  styleUrls: ["./category-permissions.component.scss"],
  encapsulation: ViewEncapsulation.None,
  animations: wcmAnimations,
})
export class CategoryPermissionsComponent implements OnInit, OnDestroy {
  repository: string;
  workspace: string;
  item: Category;
  itemName: string;
  library: string;
  error: string;
  itemPermission: PrincipalPermission[] = [];
  itemPermissionSubject = new BehaviorSubject<PrincipalPermission[]>(
    this.itemPermission
  );

  constructor(
    private aclService: AclService,
    private store: Store<fromStore.WcmAppState>,
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
    return WcmUtils.itemPath(
      this.library,
      WcmConstants.ROOTNODE_CATEGORY,
      this.itemName
    );
  }

  backItems() {
    this.router.navigate([WcmConstants.NAV_CATEGORY_ITEM]);
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
        viewer: WcmUtils.isViewer(role.wcmRoles),
        editor: WcmUtils.isEditor(role.wcmRoles),
        administrator: WcmUtils.isAdministrator(role.wcmRoles),
      };
      this.itemPermission.push(permission);
    });
    this.itemPermissionSubject.next(this.itemPermission);
  }
}
