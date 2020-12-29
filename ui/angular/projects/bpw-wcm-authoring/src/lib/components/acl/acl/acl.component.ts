import {
  Component,
  OnInit,
  OnDestroy,
  ViewEncapsulation,
  ViewChild,
  Input
} from "@angular/core";
import { ActivatedRoute, Router } from "@angular/router";
import { MatTable, MatTableDataSource } from "@angular/material/table";
import { Store } from "@ngrx/store";
import { BehaviorSubject, Subscription } from "rxjs";
import * as fromStore from "bpw-wcm-service";
import {
  AclService,
  Grant,
  PrincipalPermission,
  WcmPermission,
  WcmConstants
} from "bpw-wcm-service";

@Component({
  selector: "wcm-acl",
  templateUrl: "./acl.component.html",
  styleUrls: ["./acl.component.scss"],
  encapsulation: ViewEncapsulation.None
})
export class AclComponent implements OnInit, OnDestroy {
  @Input() repository: string;
  @Input() workspace: string;
  @Input() wcmPath: string;
  @Input() wcmPermission: PrincipalPermission[] = [];
  @Input() permissionSubject: BehaviorSubject<PrincipalPermission[]>;
  updateStatus: string;
  sub: Subscription;
  dataSource: MatTableDataSource<PrincipalPermission>;
  @ViewChild("permissionTable", { static: true }) permissionTable: MatTable<
    PrincipalPermission
  >;
  displayedColumns = ["principal", "viewer", "editor", "admin", "actions"];

  constructor(
    private aclService: AclService,
    private store: Store<fromStore.WcmAppState>,
    private route: ActivatedRoute,
    protected router: Router
  ) {}

  ngOnInit(): void {
    this.dataSource = new MatTableDataSource(
      this.wcmPermission.length ? this.wcmPermission : [<PrincipalPermission>{}]
    );
    this.sub = this.permissionSubject.subscribe(permissions => {
      if (permissions) {
        this.wcmPermission = permissions;
        this.dataSource.data = this.wcmPermission;
      }
    });
  }

  /**
   * On destroy
   */
  ngOnDestroy(): void {
    this.sub.unsubscribe();
  }

  saveWcmPermission() {
    let wcmPermissions: WcmPermission[] = [];
    this.wcmPermission.forEach(perm => {
      let wcmPermission: WcmPermission = this.converToWcmPermission(perm);
      wcmPermissions.push(wcmPermission);
    });
    let grant: Grant = {
      wcmPath: this.wcmPath,
      permissions: wcmPermissions
    };
    this.aclService
      .updateGrants(this.repository, this.workspace, grant)
      .subscribe(
        (resp: any) => {
          this.updateStatus =
            WcmConstants.UI_MESSAGE_UPDATE_PERMISSION_SUCCESSFUL;
        },
        response => {
          this.updateStatus = response.error;
        }
      );
  }

  groupSelected(groupName: string) {
    if (
      this.wcmPermission.some(perm => {
        return perm.principalName === groupName;
      })
    ) {
      alert(`Group ${groupName} has been selected`);
    } else {
      let permission: PrincipalPermission = {
        principalName: groupName,
        principalType: WcmConstants.PERMISSION_TYPE_GROUP,
        viewer: false,
        editor: false,
        administrator: false
      };
      this.wcmPermission.push(permission);
      this.permissionTable.renderRows();
    }
  }

  userSelected(userName: string) {
    if (
      this.wcmPermission.some(perm => {
        return perm.principalName === userName;
      })
    ) {
      alert(`User ${userName} has been selected`);
    } else {
      let permission: PrincipalPermission = {
        principalName: userName,
        principalType: WcmConstants.PERMISSION_TYPE_USER,
        viewer: false,
        editor: false,
        administrator: false
      };
      this.wcmPermission.push(permission);
      this.permissionTable.renderRows();
    }
  }
  removeLibraryPermission(index: number) {
    this.wcmPermission.splice(index);
    this.permissionTable.renderRows();
  }

  private converToWcmPermission(perm: PrincipalPermission): WcmPermission {
    let roles: string[] = [];
    if (perm.viewer) {
      roles.push(WcmConstants.ROLE_VIEWER);
    }
    if (perm.editor) {
      roles.push(WcmConstants.ROLE_EDITOR);
    }
    if (perm.administrator) {
      roles.push(WcmConstants.ROLE_ADMIN);
    }
    let wcmPermission: WcmPermission = {
      principalId: perm.principalName,
      principalType: perm.principalType,
      wcmRoles: roles
    };
    return wcmPermission;
  }
}
