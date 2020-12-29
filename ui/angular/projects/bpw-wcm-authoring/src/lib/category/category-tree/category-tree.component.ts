import {
  Component,
  OnInit,
  OnDestroy,
  ViewEncapsulation,
  ViewChild,
  ViewContainerRef,
} from "@angular/core";
import { select, Store } from "@ngrx/store";
import { MatDialog } from "@angular/material/dialog";
import { takeUntil, filter, tap, map } from "rxjs/operators";

import {
  wcmAnimations,
  SidebarService,
  BlockUIService,
  ConfirmationDialogService,
} from "bpw-common";
import {
  WcmOperation,
  JsonForm,
  WcmService,
  CategoryService,
  WCM_ACTION_SUCCESSFUL,
  WcmConstants,
  WcmUtils,
  WcmNode,
} from "bpw-wcm-service";
import * as fromStore from "bpw-wcm-service";
import { WcmNavigatorComponent } from "../../components/wcm-navigator/wcm-navigator.component";
import { NewCategoryDialogComponent } from "../../dialog/new-category-dialog/new-category-dialog.component";
import { WcmExplorerComponent } from "../../components/wcm-tree/wcm-explorer/wcm-explorer.component";
import { WcmItemTreeNodeData } from "../../components/wcm-tree/model/wcm-tree.model";
@Component({
  selector: "category-tree",
  templateUrl: "./category-tree.component.html",
  styleUrls: ["./category-tree.component.scss"],
  encapsulation: ViewEncapsulation.None,
  animations: wcmAnimations,
})
export class CategoryTreeComponent extends WcmNavigatorComponent
  implements OnInit, OnDestroy {
  functionMap: { [key: string]: Function } = {};
  itemForm: JsonForm;
  @ViewChild("wcmExplorer", { static: true })
  wcmExplorer: WcmExplorerComponent;

  private blocking: boolean = false;
  @ViewChild("blockUIContainer", { read: ViewContainerRef })
  private blockui: ViewContainerRef;
  private blockuiComponentRef: any;

  constructor(
    protected matDialog: MatDialog,
    protected sidebarService: SidebarService,
    protected store: Store<fromStore.WcmAppState>,
    protected wcmService: WcmService,
    private blockUIService: BlockUIService,
    private categoryService: CategoryService,
    private confirmationDialogService: ConfirmationDialogService //private contentItemService: ContentItemService
  ) {
    super(matDialog, sidebarService, store, wcmService);
  }

  ngOnInit() {
    this.rootNode = WcmConstants.ROOTNODE_CATEGORY;
    this.rootNodeType = WcmConstants.NODETYPE_CATEGORY_FOLDER;
    this.functionMap["Create.category"] = this.createCategory;
    this.functionMap["Purge.category"] = this.removeItem;
    this.nodeFilter = {
      wcmPath: "",
      nodeTypes: [WcmConstants.NODETYPE_CATEGORY],
    };
    this.store
      .pipe(
        takeUntil(this.unsubscribeAll),
        select(fromStore.getAuthoringTemplateForms)
      )
      .subscribe((authoringTemplateForms: { [key: string]: JsonForm[] }) => {
        if (authoringTemplateForms) {
          this.itemForm =
            authoringTemplateForms[WcmConstants.WCM_CATEGORY_TYPE][0];
        }
      });
    this.store
      .pipe(takeUntil(this.unsubscribeAll), select(fromStore.getOperations))
      .subscribe((operations: { [key: string]: WcmOperation[] }) => {
        if (operations) {
          this.operationMap = operations;
        }
      });
    this.store
      .pipe(
        takeUntil(this.unsubscribeAll),
        select(fromStore.getCategoryStatus),
        filter((timsstamped) => !!timsstamped.status)
      )
      .subscribe((timsstamped) =>
        this._handleWcmActionStatus(timsstamped.status)
      );
    super.ngOnInit();
  }

  /**
   * On destroy
   */
  ngOnDestroy(): void {
    this.unsubscribeAll.next();
    this.unsubscribeAll.complete();
    this.loadError && this.store.dispatch(new fromStore.WcmSystemClearError());
  }

  get myself(): any {
    return this;
  }

  createCategory(ctx: WcmNode) {
    // const node = activeNode;
    const library = WcmUtils.library(ctx.wcmPath);
    const parent =
      ctx.nodeType === WcmConstants.NODETYPE_CATEGORY_FOLDER
        ? ""
        : WcmUtils.rootPath(
            ctx.wcmPath,
            library,
            WcmConstants.ROOTNODE_CATEGORY
          );

    let dialogRef = this.matDialog.open(NewCategoryDialogComponent, {
      panelClass: "category-new-dialog",
      data: {
        jsonFormObject: this.itemForm.formSchema,
        repositoryName: ctx.repository,
        workspaceName: ctx.workspace,
        library: library,
        properties: {
          name: "a category",
          parent: parent,
        },
      },
    });
    dialogRef.afterClosed().subscribe((formData) => {
      if (formData && formData.name) {
        this.blockuiComponentRef = this._createBlockUIComponent(
          "Creating Category"
        );
        this.categoryService
          .createCategory({
            repository: ctx.repository,
            workspace: ctx.workspace,
            library: library,
            ...formData,
          })
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

  removeItem(ctx: WcmNode) {
    const confirmDialogRef = this.confirmationDialogService.confirm(
      WcmConstants.UI_MSG_DELETE_CATEGORY
    );

    confirmDialogRef.afterClosed().subscribe((result) => {
      if (result) {
        this.blockuiComponentRef = this._createBlockUIComponent(
          WcmConstants.UI_TITLE_DELETE_CATEGORY
        );
        this.categoryService
          .purgeCategory(ctx.repository, ctx.workspace, ctx.wcmPath)
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

  private _createBlockUIComponent(message: string) {
    this.blockuiComponentRef = this.blockUIService.createBlockUIComponent(
      message,
      this.blockui
    );
    this.blocking = true;
  }

  private _handleWcmActionStatus(status: any) {
    if (status != null && this.blocking) {
      this._destroyBlockUIComponent();
    }
    if (status === WCM_ACTION_SUCCESSFUL) {
      this._reloadActiveNode();
    }
  }

  private _reloadActiveNode() {
    this.wcmExplorer.reloadActiveNode(this._loadParent);
  }

  private _loadParent(node: WcmItemTreeNodeData): boolean {
    return (
      node.parentId && node.nodeType != WcmConstants.NODETYPE_CATEGORY_FOLDER
    );
  }

  private _destroyBlockUIComponent() {
    this.blockUIService.destroyBlockUIComponent(
      this.blockui,
      this.blockuiComponentRef
    );
    this.blocking = false;
  }
}
