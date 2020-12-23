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
import {
  WcmConfigService,
  AuthoringTemplate,
  WCM_ACTION_SUCCESSFUL,
} from "bpw-wcm-service";

@Component({
  selector: "authoring-template-list",
  templateUrl: "./authoring-template-list.component.html",
  styleUrls: ["./authoring-template-list.component.scss"],
  encapsulation: ViewEncapsulation.None,
  animations: wcmAnimations,
})
export class AuthoringTemplateListComponent
  implements OnInit, OnDestroy, AfterViewInit {
  dataSource: MatTableDataSource<AuthoringTemplate>;
  defaultSort: Sort = { active: "name", direction: "asc" };
  displayedColumns = ["name", "title", "library", "actions"];
  filterSubject = new Subject<string>();
  itemCount: number = 0;
  loading: boolean = true;
  @ViewChild("input") input: ElementRef;
  @ViewChild(MatPaginator, { static: false }) paginator: MatPaginator;
  @ViewChild(MatSort, { static: false }) sort: MatSort;
  status$: Observable<any>;

  private blocking: boolean = false;
  @ViewChild("blockUIContainer", { read: ViewContainerRef })
  private blockui: ViewContainerRef;
  private blockuiComponentRef: any;
  private filter: string = "";
  private items: AuthoringTemplate[];
  private noData: AuthoringTemplate[] = [<AuthoringTemplate>{}];
  private unsubscribeAll: Subject<any> = new Subject();
  private subscription: Subscription = new Subscription();

  constructor(
    protected wcmConfigService: WcmConfigService,
    protected store: Store<fromStore.WcmAppState>,
    protected router: Router,
    private blockUIService: BlockUIService,
    private confirmationDialogService: ConfirmationDialogService
  ) {}

  ngOnInit() {
    this.dataSource = new MatTableDataSource<AuthoringTemplate>();
    this.status$ = this.store.pipe(
      select(fromStore.getAuthoringTemplateError),
      takeUntil(this.unsubscribeAll),
      filter((status) => !!status),
      tap((status) => this._handleWcmActionStatus(status))
    );
  }

  ngAfterViewInit() {
    this.store
      .pipe(
        select(fromStore.getAuthoringTemplates),
        takeUntil(this.unsubscribeAll),
        delay(0),
        tap((items: { [key: string]: AuthoringTemplate }) => {
          this._initializeTable({ ...items });
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
        .pipe(tap(() => this._loadItem()))
        .subscribe()
    );
  }

  /**
   * On destroy
   */
  ngOnDestroy(): void {
    this.unsubscribeAll.next();
    this.unsubscribeAll.complete();
    this.subscription.unsubscribe();
    this.filterSubject.complete();
  }

  get successMessage(): string {
    return "";
  }

  editItem(index: number) {
    this.router.navigate(["/wcm-authoring/authoring-template/edit"], {
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
      "Are you sure you want to delete the authoring template?"
    );

    confirmDialogRef.afterClosed().subscribe((result) => {
      if (result) {
        this._createBlockUIComponent("Deleting  authoring template");
        this.confirmationDialogService.closeConfirmation();
        this.store.dispatch(new fromStore.DeleteAuthoringTemplate(payload));
      }
    });
  }

  editPermissions(index: any) {
    this.router.navigate(
      ["/wcm-authoring/authoring-template/edit-permissions"],
      {
        queryParams: {
          repository: this.items[index].repository,
          workspace: this.items[index].workspace,
          itemName: this.items[index].name,
          library: this.items[index].library,
        },
      }
    );
  }

  showHistory(index: any) {
    this.router.navigate(["/wcm-authoring/authoring-template/show-history"], {
      queryParams: {
        repository: this.items[index].repository,
        workspace: this.items[index].workspace,
        itemName: this.items[index].name,
        library: this.items[index].library,
      },
    });
  }

  private _itemPath(item: AuthoringTemplate): string {
    return `${item.library}/authoringTemplate/${item.name}`;
  }

  private _loadItem() {
    this.store
      .pipe(select(fromStore.getAuthoringTemplates))
      .subscribe((items: { [key: string]: AuthoringTemplate }) => {
        this._initializeTable(cloneDeep(items));
      });
  }

  private _id(item: AuthoringTemplate): string {
    return `${item.library}_${item.name}`;
  }

  private _initializeTable(items: { [key: string]: AuthoringTemplate }) {
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
        toArray(),
        tap((items: AuthoringTemplate[]) => {
          this.items = items;
          this.dataSource.data = this.items.length ? this.items : this.noData;
          this.loading = false;
        })
      )
      .subscribe();
  }

  private _handleWcmActionStatus(status: any) {
    if (status != null && this.blocking) {
      this._destroyBlockUIComponent();
    }

    if (status === WCM_ACTION_SUCCESSFUL) {
      this._loadItem();
    }
  }

  private _createBlockUIComponent(message: string) {
    this.blockuiComponentRef = this.blockUIService.createBlockUIComponent(
      message,
      this.blockui
    );
    this.blocking = true;
  }

  private _destroyBlockUIComponent() {
    this.blockUIService.destroyBlockUIComponent(
      this.blockui,
      this.blockuiComponentRef
    );
    this.blocking = false;
  }
}
