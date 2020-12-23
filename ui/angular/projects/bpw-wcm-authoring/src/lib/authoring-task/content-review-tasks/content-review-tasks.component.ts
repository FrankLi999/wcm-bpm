import {
  Component,
  OnInit,
  OnDestroy,
  AfterViewInit,
  ViewChild,
  ElementRef,
  ViewContainerRef,
} from "@angular/core";
import { Router } from "@angular/router";
import { MatPaginator } from "@angular/material/paginator";
import { MatSort, Sort } from "@angular/material/sort";
import { MatTableDataSource } from "@angular/material/table";
import { select, Store } from "@ngrx/store";
import { from, merge, Observable, of, Subject, Subscription } from "rxjs";
import {
  catchError,
  debounceTime,
  distinctUntilChanged,
  filter,
  skip,
  tap,
  take,
  takeUntil,
  toArray,
  switchMap,
} from "rxjs/operators";

import { BlockUIService, ConfirmationDialogService } from "bpw-common";
import * as fromStore from "bpw-wcm-service";
import {
  ContentItemService,
  DraftItem,
  WcmConstants,
  WcmService,
} from "bpw-wcm-service";

@Component({
  selector: "content-review-tasks",
  templateUrl: "./content-review-tasks.component.html",
  styleUrls: ["./content-review-tasks.component.scss"],
})
export class ContentReviewTasksComponent
  implements OnInit, OnDestroy, AfterViewInit {
  catchedItems: DraftItem[];
  currentCategory: string = "";
  dataSource: MatTableDataSource<DraftItem>;
  defaultSort: Sort = { active: "name", direction: "asc" };
  displayedColumns = ["name", "title", "author", "actions"];

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
  private items: DraftItem[] = [];
  private subscription: Subscription = new Subscription();
  private unsubscribeAll: Subject<any> = new Subject();

  constructor(
    private blockUIService: BlockUIService,
    private confirmationDialogService: ConfirmationDialogService,
    private router: Router,
    private store: Store<fromStore.WcmAppState>,
    private wcmService: WcmService,
    private contentItemService: ContentItemService
  ) {}

  ngOnInit() {
    this.dataSource = new MatTableDataSource<DraftItem>();
  }

  ngAfterViewInit() {
    this.store
      .pipe(
        select(fromStore.getCurrentNode),
        takeUntil(this.unsubscribeAll),
        filter((nav) => !!nav.currentNode),
        switchMap((nav) => {
          return this.contentItemService.getDraftItems(
            nav.currentNode.repository,
            nav.currentNode.wcmPath
          );
        }),
        catchError((err) => (this.status$ = of(err)))
      )
      .subscribe((draftItems: DraftItem[]) => {
        this.catchedItems = draftItems;
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

  editPermissions(pageIndex: any) {
    const currentItem = this.catchedItems[this._itemIndex(pageIndex)];
    this.router.navigate([WcmConstants.NAV_SA_PERMISSION], {
      queryParams: {
        wcmPath: currentItem.wcmPath,
        repository: currentItem.repository,
        workspace: WcmConstants.WS_DRAFT,
      },
    });
  }

  showItemHistory(pageIndex: number) {
    const currentItem = this.catchedItems[this._itemIndex(pageIndex)];
    this.router.navigate([WcmConstants.NAV_SA_HISTORY], {
      queryParams: {
        wcmPath: currentItem.wcmPath,
        repository: currentItem.repository,
        workspace: WcmConstants.WS_DRAFT,
      },
    });
  }

  editDraft(pageIndex: any) {
    let currentItem = this.catchedItems[this._itemIndex(pageIndex)];
    if (currentItem.editor) {
      this._editDraft(currentItem);
      return;
    } else {
      this._createBlockUIComponent("Claiming edit task");
      this.contentItemService
        .claimEditTask({
          repository: currentItem.repository,
          wcmPath: currentItem.wcmPath,
          contentId: currentItem.id,
          processInstanceId: currentItem.processInstanceId,
        })
        .pipe(
          filter((resp) => resp !== undefined),
          take(1),
          tap((editTask: any) => {
            this._destroyBlockUIComponent();
            currentItem.editor = editTask.editor;
            this._editDraft(currentItem);
          }),
          catchError((err) => {
            this._destroyBlockUIComponent();
            //TODO: show error
            return of(err);
          })
        )
        .subscribe();
    }
  }

  reviewDraft(pageIndex: any) {
    let currentItem = this.catchedItems[this._itemIndex(pageIndex)];
    if (currentItem.reviewer) {
      this._reviewTask(currentItem);
      return;
    } else {
      this._createBlockUIComponent("Claiming review task");
      this.contentItemService
        .claimReviewTask({
          repository: currentItem.repository,
          wcmPath: currentItem.wcmPath,
          contentId: currentItem.id,
          processInstanceId: currentItem.processInstanceId,
        })
        .pipe(
          filter((resp) => resp !== undefined),
          take(1),
          tap((reviewTask: any) => {
            this._destroyBlockUIComponent();
            currentItem.reviewer = reviewTask.reviewer;
            this._reviewTask(currentItem);
          }),
          catchError((err) => {
            this._destroyBlockUIComponent();
            //TODO: show error
            return of(err);
          })
        )
        .subscribe();
    }
  }

  cancelDraft(pageIndex: number) {
    const currentItem = this.catchedItems[this._itemIndex(pageIndex)];
    const confirmDialogRef = this.confirmationDialogService.confirm(
      "Are you sure you want to delete the draft item?"
    );

    confirmDialogRef.afterClosed().subscribe((result) => {
      if (result) {
        this.componentRef = this._createBlockUIComponent(
          "Deleting the draft item"
        );
        this.confirmationDialogService.closeConfirmation();
        // setTimeout(() => {
        this.contentItemService
          .cancelDraft({
            repository: currentItem.repository,
            wcmPath: currentItem.wcmPath,
            contentId: currentItem.id,
          })
          .pipe(
            switchMap((resp) => {
              currentItem.wcmPath;
              return this.contentItemService.getDraftItems(
                currentItem.repository,
                currentItem.wcmPath
              );
            }),
            tap((draftItems: DraftItem[]) => {
              this.catchedItems = draftItems;
              this._initializeItemTable();
              this._destroyBlockUIComponent();
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

  isReviewer(pageIndex) {
    return this.catchedItems[this._itemIndex(pageIndex)].wcmAuthority.reviewer;
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

  private _itemIndex(pageIndex) {
    return this.paginator.pageIndex * this.paginator.pageSize + pageIndex;
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
        filter((item: DraftItem) => item.name.startsWith(this.filter)),
        skip(this.paginator.pageIndex * this.paginator.pageSize),
        take(this.paginator.pageSize),
        toArray()
      )
      .subscribe((items: DraftItem[]) => {
        this.items = items; //todo, is this a leak
        this.dataSource.data = this.items;
        this.loading = false;
      });
  }

  private _id(item: DraftItem): string {
    return item.wcmPath;
  }

  // private _itemPath(item: DraftItem): string {
  //   return item.wcmPath;
  // }

  private _loadItems() {
    this._initializeItemTable();
  }

  private _reviewTask(currentItem: DraftItem) {
    this.router.navigate([WcmConstants.NAV_SA_EDIT_CONTENT], {
      queryParams: {
        wcmPath: currentItem.wcmPath,
        repository: currentItem.repository,
        workspace: WcmConstants.WS_DRAFT,
        editing: true,
        reviewing: true,
      },
    });
  }

  private _editDraft(currentItem: DraftItem) {
    this.router.navigate([WcmConstants.NAV_SA_EDIT_CONTENT], {
      queryParams: {
        wcmPath: currentItem.wcmPath,
        repository: currentItem.repository,
        workspace: WcmConstants.WS_DRAFT,
        editing: true,
      },
    });
  }
}
