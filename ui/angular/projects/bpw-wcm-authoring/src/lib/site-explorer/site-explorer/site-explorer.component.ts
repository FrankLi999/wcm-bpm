import {
  Component,
  ViewEncapsulation,
  OnInit,
  OnDestroy,
  ViewChild,
  ElementRef,
  ViewContainerRef,
  AfterViewInit,
} from "@angular/core";
import { Router } from "@angular/router";
import { MatPaginator } from "@angular/material/paginator";
import { MatSort, Sort } from "@angular/material/sort";
import { MatTableDataSource } from "@angular/material/table";
import { MatDialog } from "@angular/material/dialog";
import { select, Store } from "@ngrx/store";
import { merge, Observable, Subject, Subscription, of } from "rxjs";
import {
  catchError,
  debounceTime,
  distinctUntilChanged,
  filter,
  take,
  tap,
  takeUntil,
} from "rxjs/operators";

import { WcmConfigService } from "bpw-wcm-service";
import {
  wcmAnimations,
  ConfirmationDialogService,
  BlockUIService,
} from "bpw-common";

import * as fromStore from "bpw-wcm-service";
import {
  ContentItemService,
  WcmNode,
  WcmService,
  WcmItemFilter,
  WcmConstants,
} from "bpw-wcm-service";
import { SelectAuthoringTemplateDialogComponent } from "../select-authoring-template-dialog/select-authoring-template-dialog.component";

@Component({
  selector: "site-explorer",
  templateUrl: "./site-explorer.component.html",
  styleUrls: ["./site-explorer.component.scss"],
  encapsulation: ViewEncapsulation.None,
  animations: wcmAnimations,
})
export class SiteExplorerComponent implements OnInit, OnDestroy, AfterViewInit {
  currentItem: WcmNode;
  dataSource: MatTableDataSource<WcmNode>;
  defaultSort: Sort = { active: "name", direction: "asc" };
  displayedColumns = [
    "name",
    "type",
    "owner",
    "lastModified",
    "owner",
    "status",
    "actions",
  ];
  filterSubject = new Subject<string>();
  @ViewChild("input") input: ElementRef;
  itemCount: number = 0;
  loadError: string = "";
  loading: boolean = true;
  nodeFilter: WcmItemFilter = {
    wcmPath: "",
    nodeTypes: ["bpw:(.+)_(.+)_AT", "bpw:system_fileType"],
  };
  @ViewChild(MatPaginator, { static: false }) paginator: MatPaginator;
  @ViewChild(MatSort, { static: false }) sort: MatSort;
  status$: Observable<any>;

  private blocking: boolean = false;
  @ViewChild("blockUIContainer", { read: ViewContainerRef })
  private blockui: ViewContainerRef;
  private componentRef: any;
  private filter: string = "";
  private items: WcmNode[];
  private cachedItems: WcmNode[];
  private currentWcmItem = "";
  private noData: WcmNode[] = [<WcmNode>{}];
  private subscription: Subscription = new Subscription();
  private unsubscribeAll: Subject<any>;

  get successMessage(): string {
    return "";
  }

  constructor(
    private blockUIService: BlockUIService,
    private confirmationDialogService: ConfirmationDialogService,
    private contentItemService: ContentItemService,
    private matDialog: MatDialog,
    private router: Router,
    private store: Store<fromStore.WcmAppState>,
    private wcmService: WcmService
  ) {
    this.unsubscribeAll = new Subject();
  }

  ngOnInit() {
    this.status$ = this.store.pipe(
      select(fromStore.getAuthoringTemplateError),
      takeUntil(this.unsubscribeAll),
      filter((status) => !!status),
      tap((status) => this._handleWcmActionStatus(status))
    );

    this.dataSource = new MatTableDataSource(this.noData);
  }

  ngAfterViewInit() {
    this.store
      .pipe(
        select(fromStore.getCurrentWcmItem),
        debounceTime(500),
        takeUntil(this.unsubscribeAll),
        tap((currentItem: WcmNode) => {
          this.currentItem = currentItem;
          this.paginator.pageIndex = 0;
          this._loadPagedData(true);
        })
      )
      .subscribe();
    let filter$ = this.filterSubject.pipe(
      debounceTime(150),
      distinctUntilChanged(),
      tap((value: string) => {
        this.paginator.pageIndex = 0;
        this.filter = value;
      })
    );

    let sort$ = this.sort.sortChange.pipe(
      tap(() => (this.paginator.pageIndex = 0))
    );

    this.subscription.add(
      merge(filter$, sort$, this.paginator.page)
        .pipe(tap(() => this._loadPagedData()))
        .subscribe()
    );
  }

  ngOnDestroy(): void {
    this.subscription.unsubscribe();
    this.filterSubject.complete();
    this.unsubscribeAll.next();
    this.unsubscribeAll.complete();
    this.loadError && this.store.dispatch(new fromStore.WcmSystemClearError());
  }

  public retry(): void {
    if (this.currentItem) {
      this._loadPagedData(true);
    }
  }

  newContentItem() {
    let dialogRef = this.matDialog.open(
      SelectAuthoringTemplateDialogComponent,
      {
        panelClass: "content-new-dialog",
      }
    );
    dialogRef.afterClosed().subscribe((response) => {
      if (response && response.selectedAuthoringTemplate) {
        this.router.navigate(["/wcm-authoring/site-explorer/new-content"], {
          queryParams: {
            authoringTemplate: response.selectedAuthoringTemplate,
            wcmPath: this.currentItem.wcmPath,
            repository: this.currentItem.repository,
            workspace: this.currentItem.workspace,
            editing: false,
          },
        });
      }
    });
  }

  newSiteArea() {
    this.router.navigate([WcmConstants.NAV_SA_NEW_SA], {
      queryParams: {
        wcmPath: this.currentItem.wcmPath,
        repository: this.currentItem.repository,
        workspace: this.currentItem.workspace,
        editing: false,
      },
    });
  }

  editItem(index: number) {
    if (WcmConstants.NODETYPE_CONTENT.test(this.items[index].nodeType)) {
      this._editContentItem(index);
    } else if (WcmConstants.NODETYPE_SA === this.items[index].nodeType) {
      this._editSiteArea(index);
    }
  }

  editAsDraft(index: number) {
    this.componentRef = this._createBlockUIComponent(
      "Editing content as draft"
    );
    this.confirmationDialogService.closeConfirmation();
    this.contentItemService
      .editAsDraft({
        author: this.items[index].owner,
        contentId: this.items[index].id,
        repository: this.currentItem.repository,
        wcmPath: this.items[index].wcmPath,
      })
      .pipe(
        take(1),
        tap((resp) => {
          this._destroyBlockUIComponent();
          this.router.navigate([WcmConstants.NAV_SA_EDIT_CONTENT], {
            queryParams: {
              wcmPath: this.items[index].wcmPath,
              repository: this.currentItem.repository,
              workspace: WcmConstants.WS_DRAFT,
              editing: true,
            },
          });
        }),
        catchError((err) => {
          this._destroyBlockUIComponent();
          return of(err);
        })
      )
      .subscribe();
  }
  editItemPermissions(index: number) {
    this.router.navigate([WcmConstants.NAV_SA_PERMISSION], {
      queryParams: {
        wcmPath: this.currentItem.wcmPath,
        repository: this.currentItem.repository,
        workspace: this.currentItem.workspace,
      },
    });
  }

  showItemHistory(index: number) {
    this.router.navigate([WcmConstants.NAV_SA_HISTORY], {
      queryParams: {
        wcmPath: this.currentItem.wcmPath,
        repository: this.currentItem.repository,
        workspace: this.currentItem.workspace,
      },
    });
  }

  deleteItem(index: number) {
    const confirmDialogRef = this.confirmationDialogService.confirm(
      "Are you sure you want to delete the content area layout?"
    );

    confirmDialogRef.afterClosed().subscribe((result) => {
      if (result) {
        this.componentRef = this._createBlockUIComponent(
          "Deleting content area layout"
        );
        this.confirmationDialogService.closeConfirmation();
        // setTimeout(() => {
        this.contentItemService
          .purgeContentItem(
            this.currentItem.repository,
            this.currentItem.workspace,
            this.items[index].wcmPath
          )
          .pipe(
            take(1),
            tap((resp) => {
              this._destroyBlockUIComponent();
              this._loadPagedData(true);
            }),
            catchError((err) => {
              this._destroyBlockUIComponent();
              return of(err);
            })
          )
          .subscribe();
      }
    });
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

  private _editContentItem(index: number) {
    console.log("__ _editContentItem>>> ,", this.items[index]);
    this.router.navigate([WcmConstants.NAV_SA_EDIT_CONTENT], {
      queryParams: {
        wcmPath: this.items[index].wcmPath,
        repository: this.currentItem.repository,
        workspace: this.currentItem.workspace,
        editing: true,
      },
    });
  }

  private _editSiteArea(index: number) {
    this.router.navigate([WcmConstants.NAV_SA_EDIT_SA], {
      queryParams: {
        wcmPath: this.items[index].wcmPath,
        repository: this.currentItem.repository,
        workspace: this.currentItem.workspace,
        editing: true,
      },
    });
  }

  private _handleWcmActionStatus(status: any) {
    if (status != null && this.blocking) {
      this._destroyBlockUIComponent();
    }
  }

  private _initDataTable(items: WcmNode[]) {
    this.loading = false;
    this.itemCount = items.length;
    this.cachedItems = items;
    this.currentWcmItem = this.currentItem.wcmPath;
    let startIndex = this.paginator.pageIndex * this.paginator.pageSize;
    this.items = items.slice(startIndex, startIndex + this.paginator.pageSize);
    this.dataSource.data = this.items;
  }

  private _loadPagedData(reload: boolean = false) {
    this.loading = true;
    this.nodeFilter.wcmPath = this.currentItem.wcmPath;
    this.nodeFilter.filter = this.filter.toLocaleLowerCase();
    this.nodeFilter.sortDirection = this.sort.direction;
    this.nodeFilter.pageIndex = this.paginator.pageIndex;
    this.nodeFilter.pageSize = this.paginator.pageSize;
    let itemStream =
      this.currentWcmItem === this.currentItem.wcmPath && !reload
        ? of(this.cachedItems)
        : this.wcmService.getWcmNodes(
            this.currentItem.repository,
            this.currentItem.workspace,
            this.nodeFilter
          );
    itemStream
      .pipe(
        filter((items) => !!items),
        tap((items: WcmNode[]) => this._initDataTable(items)),
        catchError((resp: any) => (this.loadError = resp.error))
      )
      .subscribe();
  }
}
