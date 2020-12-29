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
import { Observable, Subject, Subscription, from, merge } from "rxjs";
import {
  debounceTime,
  delay,
  distinctUntilChanged,
  filter,
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
  WcmConfigService,
  ContentAreaLayout,
  WcmConstants,
  WcmNode,
  WCM_ACTION_SUCCESSFUL,
} from "bpw-wcm-service";

@Component({
  selector: "app-page-layouts",
  templateUrl: "./content-area-layouts.component.html",
  styleUrls: ["./content-area-layouts.component.scss"],
  encapsulation: ViewEncapsulation.None,
  animations: wcmAnimations,
})
export class ContentAreaLayoutsComponent
  implements OnInit, OnDestroy, AfterViewInit {
  dataSource: MatTableDataSource<ContentAreaLayout>;
  defaultSort: Sort = { active: "name", direction: "asc" };
  displayedColumns = ["name", "title", "library", "actions"];
  filterSubject = new Subject<string>();
  @ViewChild("input") input: ElementRef;
  itemCount: number = 0;
  loading: boolean = true;
  @ViewChild(MatPaginator, { static: false }) paginator: MatPaginator;
  status$: Observable<any>;
  @ViewChild(MatSort, { static: false }) sort: MatSort;

  private blocking: boolean = false;
  @ViewChild("blockUIContainer", { read: ViewContainerRef })
  private blockui: ViewContainerRef;
  private componentRef: any;
  private currentItem: WcmNode;
  private filter: string = "";
  private noData: ContentAreaLayout[] = [<ContentAreaLayout>{}];
  private items: ContentAreaLayout[];
  private subscription: Subscription = new Subscription();
  private unsubscribeAll: Subject<any> = new Subject();

  constructor(
    protected wcmConfigService: WcmConfigService,
    protected store: Store<fromStore.WcmAppState>,
    protected router: Router,
    private blockUIService: BlockUIService,
    private confirmationDialogService: ConfirmationDialogService
  ) {}

  ngOnInit() {
    this.dataSource = new MatTableDataSource<ContentAreaLayout>();
    this.store
      .pipe(select(fromStore.getCurrentWcmItem), takeUntil(this.unsubscribeAll))
      .subscribe((currentItem: WcmNode) => {
        this.currentItem = currentItem;
      });
    this.status$ = this.store.pipe(
      select(fromStore.getContentAreaLayoutsError),
      filter((status) => !!status),
      tap((status) => this._handleWcmActionStatus(status))
    );
  }

  ngAfterViewInit() {
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
    this._loadItems();
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
    this.subscription.unsubscribe();
    this.filterSubject.complete();
    this.unsubscribeAll.complete();
  }

  get successMessage(): string {
    return "";
  }

  editItem(index: number) {
    this.router.navigate(["/wcm-authoring/content-area-layout/edit"], {
      queryParams: {
        repository: this.items[index].repository,
        workspace: this.items[index].workspace,
        layoutName: this.items[index].name,
        library: this.items[index].library,
        editing: true,
      },
    });
  }

  deleteItem(index: number) {
    const payload = {
      repository: this.items[index].repository,
      workspace: this.items[index].workspace,
      wcmPath: this._itemPath(this.items[index]),
    };

    const confirmDialogRef = this.confirmationDialogService.confirm(
      "Are you sure you want to delete the content area layout?"
    );

    confirmDialogRef.afterClosed().subscribe((result) => {
      if (result) {
        this.componentRef = this._createBlockUIComponent(
          "Deleting content area layout"
        );
        this.confirmationDialogService.closeConfirmation();
        this.store.dispatch(new fromStore.RemoveContentAreaLayout(payload));
      }
    });
  }

  editPermissions(index: number) {
    this.router.navigate(
      ["/wcm-authoring/content-area-layout/edit-permissions"],
      {
        queryParams: {
          repository: WcmConstants.REPO_BPWIZARD,
          workspace: WcmConstants.WS_DEFAULT,
          itemName: this.items[index].name,
          library: this.items[index].library,
        },
      }
    );
  }

  showHistory(index: number) {
    this.router.navigate(["/wcm-authoring/content-area-layout/show-history"], {
      queryParams: {
        repository: this.currentItem.repository,
        workspace: this.currentItem.workspace,
        itemName: this.items[index].name,
        library: this.items[index].library,
      },
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

  private _handleWcmActionStatus(status: any) {
    if (status != null && this.blocking) {
      this._destroyBlockUIComponent();
    }

    if (status === WCM_ACTION_SUCCESSFUL) {
      this._loadItems();
    }
  }

  private _initializeDataTable(items: { [key: string]: ContentAreaLayout }) {
    this.loading = true;
    let itemCount = 0;
    Object.keys(items).forEach((key) => {
      const item = items[key];
      if (item.library === "system" || !item.name.startsWith(this.filter)) {
        delete items[key];
      } else {
        itemCount++;
      }
    });
    this.itemCount = itemCount;
    if (
      this.paginator.pageIndex > 0 &&
      this.paginator.pageIndex * this.paginator.pageSize >= this.itemCount
    ) {
      this.paginator.pageIndex--;
    }

    from(
      Object.values(items).sort((layout1, layout2) =>
        layout1.name > layout2.name ? 1 : layout1.name === layout2.name ? 0 : -1
      )
    )
      .pipe(
        skip(this.paginator.pageIndex * this.paginator.pageSize),
        take(this.paginator.pageSize),
        toArray()
      )
      .subscribe((items: ContentAreaLayout[]) => {
        this.items = items; //todo, is this a leak
        this.dataSource.data = this.itemCount ? this.items : this.noData;
        this.loading = false;
      });
  }

  private _itemPath(layout: ContentAreaLayout): string {
    return `${layout.library}/contentAreaLayout/${layout.name}`;
  }

  private _loadItems() {
    this.store
      .pipe(
        select(fromStore.getContentAreaLayouts),
        delay(0),
        filter((items) => !!items)
      )
      .subscribe((items: { [key: string]: ContentAreaLayout }) => {
        this._initializeDataTable(items);
      });
  }
}
