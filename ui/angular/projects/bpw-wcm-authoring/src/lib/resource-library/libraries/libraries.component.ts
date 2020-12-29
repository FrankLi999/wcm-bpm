import {
  Component,
  ElementRef,
  ViewChild,
  OnInit,
  OnDestroy,
  AfterViewInit,
  ViewEncapsulation,
  ViewContainerRef,
} from "@angular/core";
import { Router } from "@angular/router";
import { MatPaginator } from "@angular/material/paginator";
import { MatSort, Sort } from "@angular/material/sort";
import { MatTableDataSource } from "@angular/material/table";
import { select, Store } from "@ngrx/store";

import {
  debounceTime,
  distinctUntilChanged,
  tap,
  takeUntil,
} from "rxjs/operators";
import { Observable, merge, Subscription, Subject } from "rxjs";

import {
  wcmAnimations,
  ConfirmationDialogService,
  BlockUIService,
} from "bpw-common";
import * as fromStore from "bpw-wcm-service";
import {
  WcmConfigService,
  WcmService,
  Library,
  LoadParameters,
  WcmConstants,
} from "bpw-wcm-service";

@Component({
  selector: "resource-libraries",
  templateUrl: "./libraries.component.html",
  styleUrls: ["./libraries.component.scss"],
  encapsulation: ViewEncapsulation.None,
  animations: wcmAnimations,
})
export class LibrariesComponent implements OnInit, OnDestroy, AfterViewInit {
  dataSource: MatTableDataSource<Library>;
  defaultSort: Sort = { active: "name", direction: "asc" };

  displayedColumns = ["name", "title", "language", "description", "actions"];
  filterSubject = new Subject<string>();
  @ViewChild("input") input: ElementRef;
  itemCount: number;
  loading: boolean;
  @ViewChild(MatPaginator, { static: false }) paginator: MatPaginator;
  status$: Observable<any>;
  @ViewChild(MatSort, { static: false }) sort: MatSort;
  private blocking: boolean = false;
  @ViewChild("blockUIContainer", { read: ViewContainerRef })
  private blockui: ViewContainerRef;
  private componentRef: any;
  private filter: string = "";
  private items: Library[];
  private noData: Library[] = [<Library>{}];
  private subscription: Subscription = new Subscription();
  private unsubscribeAll: Subject<any> = new Subject();

  constructor(
    protected wcmConfigService: WcmConfigService,
    protected wcmService: WcmService,
    protected store: Store<fromStore.WcmAppState>,
    protected router: Router,
    private confirmationDialogService: ConfirmationDialogService,
    private blockUIService: BlockUIService
  ) {}

  public ngOnInit(): void {
    this.store
      .pipe(select(fromStore.getWcmLibraries), takeUntil(this.unsubscribeAll))
      .subscribe((items) => this._initializeTable(items));

    this.subscription.add(
      this.store
        .pipe(
          select(fromStore.getWcmLibraryLoading),
          takeUntil(this.unsubscribeAll)
        )
        .subscribe((loading) => {
          if (loading) {
            this.dataSource = new MatTableDataSource(this.noData);
          }
          this.loading = loading;
        })
    );
    this.status$ = this.store.pipe(
      select(fromStore.getWcmLibraryStatus),
      takeUntil(this.unsubscribeAll),
      tap((status) => this._handleWcmActionStatus(status))
    );
  }

  ngAfterViewInit() {
    this._loadItems();
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
  public ngOnDestroy(): void {
    this.unsubscribeAll.next();
    this.unsubscribeAll.complete();
    this.subscription.unsubscribe();
    this.filterSubject.complete();
    this.store.dispatch(new fromStore.LibraryActionClear());
  }

  get successMessage(): string {
    return "";
  }

  deleteItem(index: any) {
    const confirmDialogRef = this.confirmationDialogService.confirm(
      "Are you sure you want to delete the library?"
    );

    confirmDialogRef.afterClosed().subscribe((result) => {
      if (result) {
        this.componentRef = this._createBlockUIComponent("Deleting library");
        this.confirmationDialogService.closeConfirmation();
        // setTimeout(() => {
        this.store.dispatch(new fromStore.DeleteLibrary(this.items[index]));
        // }, 3500);
      }
    });
  }

  newItem() {
    this.router.navigate(["/wcm-authoring/resource-library/new"], {
      queryParams: {
        repository: WcmConstants.REPO_BPWIZARD,
        workspace: WcmConstants.WS_DEFAULT,
        editing: "false",
      },
    });
  }

  editItem(index: any) {
    this.router.navigate(["/wcm-authoring/resource-library/edit"], {
      queryParams: {
        repository: WcmConstants.REPO_BPWIZARD,
        workspace: WcmConstants.WS_DEFAULT,
        editing: "true",
        libraryIndex: index,
      },
    });
  }

  editItemPermissions(index: any) {
    this.router.navigate(["/wcm-authoring/resource-library/edit-permissions"], {
      queryParams: {
        repository: WcmConstants.REPO_BPWIZARD,
        workspace: WcmConstants.WS_DEFAULT,
        libraryIndex: index,
      },
    });
  }

  retry(): void {
    this._loadItems();
  }

  showItemHistory(index: any) {
    this.router.navigate(["/wcm-authoring/resource-library/show-history"], {
      queryParams: {
        repository: WcmConstants.REPO_BPWIZARD,
        workspace: WcmConstants.WS_DEFAULT,
        libraryIndex: index,
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

  private _loadItems() {
    this.store.dispatch(
      new fromStore.LoadLibrary(<LoadParameters>{
        repository: WcmConstants.REPO_BPWIZARD,
        workspace: WcmConstants.WS_DEFAULT,
        filter: this.filter.toLocaleLowerCase(),
        sortDirection: this.sort.direction,
        pageIndex: this.paginator.pageIndex,
        pageSize: this.paginator.pageSize,
      })
    );
  }

  private _handleWcmActionStatus(status: any) {
    if (status != null && this.blocking) {
      this._destroyBlockUIComponent();
    }
  }

  private _initializeTable(items: Library[]): void {
    this.items = items;
    this.itemCount = items.length;
    this.dataSource = new MatTableDataSource(
      items.length ? items : this.noData
    );
  }
}
