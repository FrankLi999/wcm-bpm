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
  SiteConfig,
  WcmUtils,
  WCM_ACTION_SUCCESSFUL,
} from "bpw-wcm-service";

@Component({
  selector: "site-configs",
  templateUrl: "./site-configs.component.html",
  styleUrls: ["./site-configs.component.scss"],
  encapsulation: ViewEncapsulation.None,
  animations: wcmAnimations,
})
export class SiteConfigsComponent implements OnInit, OnDestroy, AfterViewInit {
  dataSource: MatTableDataSource<SiteConfig>;
  defaultSort: Sort = { active: "name", direction: "asc" };
  displayedColumns = ["name", "title", "library", "actions"];
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
  private items: SiteConfig[];
  private noData: SiteConfig[] = [<SiteConfig>{}];
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
    this.dataSource = new MatTableDataSource<SiteConfig>();
    this.status$ = this.store.pipe(
      select(fromStore.getSiteConfigStatus),
      takeUntil(this.unsubscribeAll),
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
    this.subscription.add(
      merge(filter$, sort$, this.paginator.page)
        .pipe(tap(() => this._loadItems()))
        .subscribe()
    );
    this._loadItems();
    this.store
      .pipe(
        select(fromStore.getSiteConfigs),
        takeUntil(this.unsubscribeAll),
        delay(0),
        filter((items) => !!items)
      )
      .subscribe((items: SiteConfig[]) => {
        this._initializeItemTable(items);
      });
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

  deleteItem(index: number) {
    const payload = {
      repository: this.items[index].repository,
      workspace: this.items[index].workspace,
      wcmPath: this._itemPath(this.items[index]),
    };

    const confirmDialogRef = this.confirmationDialogService.confirm(
      "Are you sure you want to delete the site config?"
    );

    confirmDialogRef.afterClosed().subscribe((result) => {
      if (result) {
        this.componentRef = this._createBlockUIComponent(
          "Deleting Site Config"
        );
        this.confirmationDialogService.closeConfirmation();
        this.store.dispatch(new fromStore.DeleteSiteConfig(payload));
      }
    });
  }

  editItem(index: number) {
    this.router.navigate(["/wcm-authoring/site-config/edit"], {
      queryParams: {
        repository: this.items[index].repository,
        workspace: this.items[index].workspace,
        wcmPath: this._itemPath(this.items[index]),
        library: this.items[index].library,
        editing: true,
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

  private _id(item: SiteConfig): string {
    return `${item.library}_${item.name}`;
  }

  private _itemPath(item: SiteConfig): string {
    return `/${item.library}/siteConfig/${item.name}`;
  }

  private _loadItems() {
    this.store
      .pipe(
        select(fromStore.getCurrentNode),
        takeUntil(this.unsubscribeAll),
        filter((nav) => !!nav.currentNode)
      )
      .subscribe((currentNavigation) => {
        this.store.dispatch(
          new fromStore.LoadSiteConfig({
            repository: currentNavigation.currentNode.repository,
            workspace: currentNavigation.currentNode.workspace,
            library: WcmUtils.library(currentNavigation.currentNode.wcmPath),
          })
        );
      });
  }

  private _initializeItemTable(items: SiteConfig[]) {
    this.loading = true;
    let itemCount = 0;
    from(items)
      .pipe(
        tap((item) => {
          if (item.library !== "system" && item.name.startsWith(this.filter)) {
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
      items.sort((o1, o2) =>
        this._id(o1) > this._id(o2) ? 1 : this._id(o1) === this._id(o2) ? 0 : -1
      )
    )
      .pipe(
        filter(
          (item: SiteConfig) =>
            item.library !== "system" && item.name.startsWith(this.filter)
        ),
        skip(this.paginator.pageIndex * this.paginator.pageSize),
        take(this.paginator.pageSize),
        toArray()
      )
      .subscribe((items: SiteConfig[]) => {
        this.items = items; //todo, is this a leak
        this.dataSource.data = this.itemCount ? this.items : this.noData;
        this.loading = false;
      });
  }

  private _handleWcmActionStatus(status: any) {
    if (status != null && this.blocking) {
      this._destroyBlockUIComponent();
    }

    if (status === WCM_ACTION_SUCCESSFUL) {
      this._loadItems();
    }
  }
}
