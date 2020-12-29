import {
  Component,
  OnInit,
  OnDestroy,
  AfterViewInit,
  ViewChild,
  ElementRef,
  ViewContainerRef,
  ViewEncapsulation,
} from "@angular/core";
import { Router } from "@angular/router";
import { MatPaginator } from "@angular/material/paginator";
import { MatSort, Sort } from "@angular/material/sort";
import { MatTableDataSource } from "@angular/material/table";
import { select, Store } from "@ngrx/store";
import { Observable, Subject, Subscription, from, merge, pipe } from "rxjs";
import {
  debounceTime,
  distinctUntilChanged,
  filter,
  map,
  skip,
  tap,
  take,
  takeUntil,
  toArray,
} from "rxjs/operators";

import {
  BlockUIService,
  wcmAnimations,
  ConfirmationDialogService,
} from "bpw-common";
import * as fromStore from "bpw-wcm-service";
import {
  CategoryService,
  WcmNode,
  WCM_ACTION_SUCCESSFUL,
  WcmConstants,
  WcmUtils,
} from "bpw-wcm-service";

@Component({
  selector: "category",
  templateUrl: "./category.component.html",
  styleUrls: ["./category.component.scss"],
  encapsulation: ViewEncapsulation.None,
  animations: wcmAnimations,
})
export class CategoryComponent implements OnInit, OnDestroy, AfterViewInit {
  catchedItems: WcmNode[];
  currentCategory: string = "";
  defaultSort: Sort = { active: "name", direction: "asc" };
  dataSource: MatTableDataSource<WcmNode>;
  displayedColumns = ["name", "actions"];
  filterSubject = new Subject<string>();
  @ViewChild("input") input: ElementRef;
  itemCount: number = 0;
  loading: boolean = true;
  @ViewChild(MatPaginator, { static: false }) paginator: MatPaginator;
  @ViewChild(MatSort, { static: false }) sort: MatSort;
  status$: Observable<any>;

  private blocking: boolean = false;
  @ViewChild("blockUIContainer", { read: ViewContainerRef })
  private blockui: ViewContainerRef;
  private componentRef: any;
  private filter: string = "";
  private items: WcmNode[] = [<WcmNode>{}];
  private subscription: Subscription = new Subscription();
  private unsubscribeAll: Subject<any> = new Subject();

  constructor(
    private blockUIService: BlockUIService,
    private confirmationDialogService: ConfirmationDialogService,
    private router: Router,
    private store: Store<fromStore.WcmAppState>,
    private categoryService: CategoryService
  ) {}

  ngOnInit() {
    this.dataSource = new MatTableDataSource<WcmNode>();
    this.status$ = this.store.pipe(
      select(fromStore.getCategoryStatus),
      takeUntil(this.unsubscribeAll),
      filter((categoryStatus) => !!categoryStatus.status),
      map((categoryStatus) => categoryStatus.status),
      tap((categoryStatus) => this._handleWcmActionStatus(status))
    );
  }

  ngAfterViewInit() {
    this.store
      .pipe(
        select(fromStore.getCurrentNode),
        takeUntil(this.unsubscribeAll),
        filter((nav) => !!nav.currentNode)
      )
      .subscribe((currentNavigation) => {
        this.currentCategory =
          currentNavigation.currentNode.nodeType ===
          WcmConstants.NODETYPE_CATEGORY
            ? currentNavigation.currentNode.name
            : "";
        this.catchedItems = [...currentNavigation.children];
        this._initializeItemTable();
      });

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
        .pipe(tap(() => this._loadItems()))
        .subscribe()
    );
  }

  /**
   * On destroy
   */
  ngOnDestroy(): void {
    this.unsubscribeAll.complete();
    this.subscription.unsubscribe();
    this.filterSubject.complete();
  }

  get successMessage(): string {
    return "";
  }

  deleteItem(index: number) {
    const confirmDialogRef = this.confirmationDialogService.confirm(
      WcmConstants.UI_MSG_DELETE_CATEGORY
    );

    confirmDialogRef.afterClosed().subscribe((result) => {
      if (result) {
        this.componentRef = this._createBlockUIComponent(
          WcmConstants.UI_TITLE_DELETE_CATEGORY
        );
        this.categoryService
          .purgeCategory(
            this.items[index].repository,
            this.items[index].workspace,
            this._itemPath(this.items[index])
          )
          .pipe(filter((resp) => resp !== undefined))
          .subscribe(
            (resp: any) => {
              this.store.dispatch(new fromStore.CategoryActionSuccessful());
            },
            (response) => {
              this.store.dispatch(new fromStore.CategoryActionFailed(response));
            },
            () => {
              this.confirmationDialogService.closeConfirmation();
            }
          );
      }
    });
  }

  editPermissions(index: any) {
    this.router.navigate([WcmConstants.NAV_CATEGORY_PERMISSION], {
      queryParams: {
        repository: this.items[index].repository,
        workspace: this.items[index].workspace,
        itemName: this.items[index].name,
        library: WcmUtils.library(this.items[index].wcmPath),
      },
    });
  }

  private _initializeItemTable() {
    this.loading = true;
    this.items = [];
    let itemCount = 0;
    from(this.catchedItems)
      .pipe(
        tap((item) => {
          if (item.name.startsWith(this.filter)) {
            itemCount++;
          }
        })
      )
      .subscribe();
    this.itemCount = itemCount;
    if (
      this.paginator.pageIndex > 0 &&
      this.paginator.pageIndex * this.paginator.pageSize >= this.itemCount
    ) {
      this.paginator.pageIndex--;
    }
    from(
      this.catchedItems.sort((o1, o2) =>
        this._id(o1) > this._id(o2) ? 1 : this._id(o1) === this._id(o2) ? 0 : -1
      )
    )
      .pipe(
        filter((item: WcmNode) => item.name.startsWith(this.filter)),
        skip(this.paginator.pageIndex * this.paginator.pageSize),
        take(this.paginator.pageSize),
        toArray()
      )
      .subscribe((items: WcmNode[]) => {
        this.items = items; //todo, is this a leak
        this.dataSource.data = this.items;
        this.loading = false;
      });
  }

  private _id(item: WcmNode): string {
    return item.wcmPath;
  }

  private _itemPath(item: WcmNode): string {
    return item.wcmPath;
  }

  private _loadItems() {
    this._initializeItemTable();
  }

  private _handleWcmActionStatus(status: any) {
    if (status != null && this.blocking) {
      this._destroyBlockUIComponent();
    }

    if (status === WCM_ACTION_SUCCESSFUL) {
      this._loadItems();
    }
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
