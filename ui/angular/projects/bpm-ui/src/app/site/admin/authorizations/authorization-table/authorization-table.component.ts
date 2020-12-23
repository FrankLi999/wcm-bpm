import {
  Component,
  OnInit,
  OnDestroy,
  ViewEncapsulation,
  ViewChild,
  ViewContainerRef,
} from "@angular/core";
import { MatTableDataSource } from "@angular/material/table";
import { MatDialog } from "@angular/material/dialog";
import { MatPaginator } from "@angular/material/paginator";
import { filter, tap, takeUntil } from "rxjs/operators";
import { forkJoin, Subject } from "rxjs";
import {
  wcmAnimations,
  ConfirmationDialogService,
  BlockUIService,
} from "bpw-common";
import { Resource } from "../../../../model/Resource";
import { Authorization } from "../../../../model/Authorization";
import { AuthorizationService } from "../../../../services/authorization.service";
import { NewAuthorizationDialogComponent } from "../new-authorization-dialog/new-authorization-dialog.component";
const GrantType = {
  0: "GLOBAL",
  1: "ALLOW",
  2: "DENY",
};

@Component({
  selector: "authorization-table",
  templateUrl: "./authorization-table.component.html",
  styleUrls: ["./authorization-table.component.scss"],
  encapsulation: ViewEncapsulation.None,
  animations: wcmAnimations,
})
export class AuthorizationTableComponent implements OnInit, OnDestroy {
  resource: Resource = {
    resource: "",
    resourceId: -1,
    permissions: [],
    title: "",
  };
  itemCount: number = 0;
  loading: boolean = true;
  inUpdates = {};
  dataSource: MatTableDataSource<Authorization>;
  displayedColumns = [
    "type",
    "principal",
    "permissions",
    "resource",
    "actions",
  ];
  @ViewChild(MatPaginator, { static: false }) paginator: MatPaginator;
  private blocking: boolean = false;
  @ViewChild("blockUIContainer", { read: ViewContainerRef })
  private blockui: ViewContainerRef;
  private componentRef: any;
  // private items: Authorization[];
  private noData: Authorization[] = [<Authorization>{}];
  private _unsubscribeAll: Subject<any>;

  constructor(
    private blockUIService: BlockUIService,
    private confirmationDialogService: ConfirmationDialogService,
    private dialog: MatDialog,
    private authorizationService: AuthorizationService
  ) {
    this._unsubscribeAll = new Subject();
  }

  ngOnInit(): void {
    this.dataSource = new MatTableDataSource(this.noData);
  }

  ngOnDestroy(): void {
    this._unsubscribeAll.next();
    this._unsubscribeAll.complete();
  }

  ngAfterViewInit() {
    this.authorizationService.onResourceChanged
      .pipe(
        takeUntil(this._unsubscribeAll),
        filter((resource) => resource != undefined),
        tap((resource) => {
          this.resource = resource;
          this._loadPagedData(true);
        })
      )
      .subscribe();
  }

  handlePageEvent(pageEvent) {
    this._loadPagedData(true);
  }

  newAuthorization() {
    const dialogRef = this.dialog.open(NewAuthorizationDialogComponent, {
      width: "500px",
      data: {
        authorization: {
          type: 1,
          permissions: [],
          userId: "",
          groupId: null,
          resourceType: this.resource.resourceId,
          resourceId: "",
        },
        resource: this.resource,
      },
    });

    dialogRef.afterClosed().subscribe((data) => {
      if (data) {
        this.componentRef = this._createBlockUIComponent(
          "Creating authorization"
        );
        this.authorizationService
          .createAuthorization(data.authorization)
          .subscribe(() => {
            this._destroyBlockUIComponent();
            this._loadPagedData(true);
          });
      }
    });
  }

  deleteAuthorization(index: number) {
    const confirmDialogRef = this.confirmationDialogService.confirm(
      "Are you sure you want to delete the authorization?"
    );

    confirmDialogRef.afterClosed().subscribe((result) => {
      if (result) {
        this.componentRef = this._createBlockUIComponent(
          "Deleting authorization"
        );
        this.confirmationDialogService.closeConfirmation();
        this.authorizationService
          .deleteAuthorization(this.dataSource.data[index].id)
          .subscribe(() => {
            this._destroyBlockUIComponent();
            this._loadPagedData(true);
          });
      }
    });
  }

  editAuthorization(index: number) {
    this.inUpdates[index] = { ...this.dataSource.data[index] };
  }

  updateAuthorization(index: number) {
    this.componentRef = this._createBlockUIComponent("Deleting authorization");
    this.confirmationDialogService.closeConfirmation();
    this.authorizationService
      .updateAuthorization(this.inUpdates[index].id, this.inUpdates[index])
      .subscribe(() => {
        this._destroyBlockUIComponent();
      });

    let tableData = this.dataSource.data.slice();
    tableData[index] = this.inUpdates[index];
    delete this.inUpdates[index];
    this.dataSource.data = tableData;
  }

  cancelUpdate(index: number) {
    delete this.inUpdates[index];
  }

  retry() {
    this._loadPagedData(true);
  }

  switchToGroup(index) {
    this.inUpdates[index].groupId = this.inUpdates[index].userId;
    this.inUpdates[index].userId = null;
  }

  switchToUser(index) {
    this.inUpdates[index].userId = this.inUpdates[index].groupId;
    this.inUpdates[index].groupId = null;
  }

  getGrantType(type: number) {
    return GrantType[type];
  }

  getPrincipal(item: Authorization) {
    return item.userId ? item.userId : item.groupId;
  }

  isGroup(item: Authorization) {
    return item.groupId;
  }

  isUser(item: Authorization) {
    return item.userId;
  }

  isInUpdate(index: number): boolean {
    return Object.keys(this.inUpdates).length > 0
      ? this.inUpdates[index]
      : false;
  }

  getPermissions(item) {
    return item.permissions
      ? item.permissions.includes("ALL")
        ? "ALL"
        : item.permissions.join(", ")
      : "";
  }

  toggleAllSelection(index) {
    if (this.inUpdates[index].permissions.includes("ALL")) {
      this.inUpdates[index].permissions = [...this.resource.permissions];
      this.inUpdates[index].permissions.unshift("ALL");
    } else {
      this.inUpdates[index].permissions = [];
    }
  }

  togglePerOne(index) {
    if (this.inUpdates[index].permissions.includes("ALL")) {
      this.inUpdates[index].permissions.shift();
      this.inUpdates[index].permissions = [
        ...this.inUpdates[index].permissions,
      ];
    }
    if (
      this.inUpdates[index].permissions.length ===
      this.resource.permissions.length
    ) {
      this.inUpdates[index].permissions = [
        "ALL",
        ...this.inUpdates[index].permissions,
      ];
    }
  }

  private _loadPagedData(reload: boolean = false) {
    if (this.resource.resourceId < 0) {
      return; //TODO
    }
    this.loading = true;
    forkJoin({
      items: this.authorizationService.getAuthorizations(
        this.paginator.pageIndex * this.paginator.pageSize,
        this.paginator.pageSize,
        this.resource.resourceId
      ),
      count: this.authorizationService.getAuthorizationCount(
        this.resource.resourceId
      ),
    })
      .pipe(
        tap((data) => {
          this.itemCount = data.count.count;
          this._initDataTable(data.items);
        })
      )
      .subscribe();
  }

  private _initDataTable(items: Authorization[]) {
    this.loading = false;
    this.dataSource.data = items;
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
}
