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
import cloneDeep from "lodash/cloneDeep";

import {
  BlockUIService,
  wcmAnimations,
  ConfirmationDialogService,
} from "bpw-common";
import * as fromStore from "bpw-wcm-service";
import { WcmConfigService, Form, WCM_ACTION_SUCCESSFUL } from "bpw-wcm-service";

@Component({
  selector: "form-list",
  templateUrl: "./form-list.component.html",
  styleUrls: ["./form-list.component.scss"],
  encapsulation: ViewEncapsulation.None,
  animations: wcmAnimations,
})
export class FormListComponent implements OnInit, OnDestroy, AfterViewInit {
  dataSource: MatTableDataSource<Form>;
  defaultSort: Sort = { active: "name", direction: "asc" };
  displayedColumns = ["name", "title", "library", "actions"];
  filterSubject = new Subject<string>();
  @ViewChild("input") input: ElementRef;
  itemCount: number = 0;
  loading: boolean = true;
  status$: Observable<any>;
  @ViewChild(MatSort, { static: false }) sort: MatSort;
  @ViewChild(MatPaginator, { static: false }) paginator: MatPaginator;

  private blocking: boolean = false;
  @ViewChild("blockUIContainer", { read: ViewContainerRef })
  private blockui: ViewContainerRef;
  private componentRef: any;
  private filter: string = "";
  private items: Form[];
  private noData: Form[] = [<Form>{}];
  private unsubscribeAll: Subject<any> = new Subject();
  private subscription: Subscription = new Subscription();

  constructor(
    protected wcmConfigService: WcmConfigService,
    protected store: Store<fromStore.WcmAppState>,
    protected router: Router,
    private blockUIService: BlockUIService,
    private confirmationDialogService: ConfirmationDialogService
  ) {
    this.unsubscribeAll = new Subject();
  }

  ngOnInit() {
    this.dataSource = new MatTableDataSource<Form>();
    this.status$ = this.store.pipe(
      select(fromStore.getFormError),
      takeUntil(this.unsubscribeAll),
      filter((status) => !!status),
      tap((status) => this._handleWcmActionStatus(status))
    );
  }

  ngAfterViewInit() {
    this.store
      .pipe(
        select(fromStore.getFormTemplates),
        takeUntil(this.unsubscribeAll),
        delay(0),
        filter((items) => !!items)
      )
      .subscribe((items: { [key: string]: Form }) => {
        this._initializeItemTable(cloneDeep(items));
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
    this.subscription.unsubscribe();
    this.filterSubject.complete();
    this.unsubscribeAll.complete();
  }

  get successMessage(): string {
    return "";
  }

  editItem(index: number) {
    this.router.navigate(["/wcm-authoring/form-designer/edit"], {
      queryParams: {
        repository: this.items[index].repository,
        workspace: this.items[index].workspace,
        wcmPath: this._itemPath(this.items[index]),
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
      "Are you sure you want to delete the form?"
    );

    confirmDialogRef.afterClosed().subscribe((result) => {
      if (result) {
        this.componentRef = this._createBlockUIComponent("Deleting Form");
        this.confirmationDialogService.closeConfirmation();
        this.store.dispatch(new fromStore.DeleteForm(payload));
      }
    });
  }

  editPermissions(index: any) {
    this.router.navigate(["/wcm-authoring/form-designer/edit-permissions"], {
      queryParams: {
        repository: this.items[index].repository,
        workspace: this.items[index].workspace,
        itemName: this.items[index].name,
        library: this.items[index].library,
      },
    });
  }

  showHistory(index: any) {
    this.router.navigate(["/wcm-authoring/form-designer/show-history"], {
      queryParams: {
        repository: this.items[index].repository,
        workspace: this.items[index].workspace,
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
  private _id(item: Form): string {
    return `${item.library}_${item.name}`;
  }

  private _initializeItemTable(items: { [key: string]: Form }) {
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
      Object.values(items).sort((o1, o2) =>
        this._id(o1) > this._id(o2) ? 1 : this._id(o1) === this._id(o2) ? 0 : -1
      )
    )
      .pipe(
        skip(this.paginator.pageIndex * this.paginator.pageSize),
        take(this.paginator.pageSize),
        toArray()
      )
      .subscribe((items: Form[]) => {
        this.items = items; //todo, is this a leak
        this.dataSource.data = this.itemCount ? this.items : this.noData;
        this.loading = false;
      });
  }

  private _itemPath(item: Form): string {
    return `${item.library}/form/${item.name}`;
  }

  private _loadItems() {
    this.store
      .pipe(
        select(fromStore.getFormTemplates),
        filter((items) => !!items)
      )
      .subscribe((items: { [key: string]: Form }) => {
        this._initializeItemTable(cloneDeep(items));
      });
  }
}
