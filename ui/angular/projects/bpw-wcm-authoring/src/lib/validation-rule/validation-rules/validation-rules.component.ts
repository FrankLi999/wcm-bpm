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
  ValidationRule,
  LoadParameters,
  WCM_ACTION_SUCCESSFUL,
} from "bpw-wcm-service";

@Component({
  selector: "validation-rules",
  templateUrl: "./validation-rules.component.html",
  styleUrls: ["./validation-rules.component.scss"],
  encapsulation: ViewEncapsulation.None,
  animations: wcmAnimations,
})
export class ValidationRulesComponent
  implements OnInit, OnDestroy, AfterViewInit {
  dataSource: MatTableDataSource<ValidationRule>;
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
  private items: ValidationRule[];
  private noData: ValidationRule[] = [<ValidationRule>{}];
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
    this.dataSource = new MatTableDataSource<ValidationRule>();
    this.status$ = this.store.pipe(
      takeUntil(this.unsubscribeAll),
      select(fromStore.getValidationRuleStatus),
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
        select(fromStore.getValidationRules),
        delay(0),
        filter((items) => !!items)
      )
      .subscribe((items: ValidationRule[]) => {
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

  editItem(index: number) {
    this.router.navigate(["/wcm-authoring/validation-rule/edit"], {
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
      "Are you sure you want to delete the validation rule?"
    );

    confirmDialogRef.afterClosed().subscribe((result) => {
      if (result) {
        this.componentRef = this._createBlockUIComponent(
          "Deleting Validation Rule"
        );
        this.confirmationDialogService.closeConfirmation();
        this.store.dispatch(new fromStore.DeleteValidationRule(payload));
      }
    });
  }

  editPermissions(index: any) {
    this.router.navigate(["/wcm-authoring/validation-rule/edit-permissions"], {
      queryParams: {
        repository: this.items[index].repository,
        workspace: this.items[index].workspace,
        itemName: this.items[index].name,
        library: this.items[index].library,
      },
    });
  }

  showHistory(index: any) {
    this.router.navigate(["/wcm-authoring/validation-rule/show-history"], {
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

  private _id(item: ValidationRule): string {
    return `${item.library}_${item.name}`;
  }

  private _initializeItemTable(items: ValidationRule[]) {
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
          (item: ValidationRule) =>
            item.library !== "system" && item.name.startsWith(this.filter)
        ),
        skip(this.paginator.pageIndex * this.paginator.pageSize),
        take(this.paginator.pageSize),
        toArray()
      )
      .subscribe((items: ValidationRule[]) => {
        this.items = items; //todo, is this a leak
        this.dataSource.data = this.itemCount ? this.items : this.noData;
        this.loading = false;
      });
  }

  private _itemPath(item: ValidationRule): string {
    return `${item.library}/validationRule/${item.name}`;
  }

  private _loadItems() {
    this.store
      .pipe(
        select(fromStore.getCurrentNode),
        filter((nav) => !!nav.currentNode)
      )
      .subscribe((currentNavigation) => {
        this.store.dispatch(
          new fromStore.LoadValidationRule(<LoadParameters>{
            repository: currentNavigation.currentNode.repository,
            workspace: currentNavigation.currentNode.workspace,
            filter: this.filter.toLocaleLowerCase(),
            sortDirection: this.sort.direction,
            pageIndex: this.paginator.pageIndex,
            pageSize: this.paginator.pageSize,
          })
        );
      });
  }
}
