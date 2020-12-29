import {
  Component,
  OnInit,
  OnDestroy,
  ViewEncapsulation,
  ViewChild,
  ViewContainerRef,
} from "@angular/core";
import { Router } from "@angular/router";
import { takeUntil } from "rxjs/operators";
import { select, Store } from "@ngrx/store";
import { MatDialog } from "@angular/material/dialog";
import { Subject } from "rxjs";
import { filter } from "rxjs/operators";

import {
  wcmAnimations,
  SidebarService,
  ConfirmationDialogService,
  BlockUIService,
} from "bpw-common";
import {
  WcmOperation,
  WcmService,
  WCM_ACTION_SUCCESSFUL,
  WcmUtils,
} from "bpw-wcm-service";
import * as fromStore from "bpw-wcm-service";
import { WcmNavigatorComponent } from "../../components/wcm-navigator/wcm-navigator.component";

@Component({
  selector: "content-area-layout-tree",
  templateUrl: "./content-area-layout-tree.component.html",
  styleUrls: ["./content-area-layout-tree.component.scss"],
  encapsulation: ViewEncapsulation.None,
  animations: wcmAnimations,
})
export class ContentAreaLayoutTreeComponent extends WcmNavigatorComponent
  implements OnInit, OnDestroy {
  private blocking: boolean = false;
  @ViewChild("blockUIContainer", { read: ViewContainerRef })
  private blockui: ViewContainerRef;
  private blockuiComponentRef: any;

  functionMap: { [key: string]: Function } = {};
  unsubscribeAll: Subject<any> = new Subject();

  constructor(
    protected matDialog: MatDialog,
    protected sidebarService: SidebarService,
    protected store: Store<fromStore.WcmAppState>,
    protected wcmService: WcmService,
    private blockUIService: BlockUIService,
    private confirmationDialogService: ConfirmationDialogService,
    private router: Router
  ) {
    super(matDialog, sidebarService, store, wcmService);
  }

  ngOnInit() {
    this.rootNode = "contentAreaLayout";
    this.rootNodeType = "bpw:contentAreaLayoutFolder";
    this.functionMap["Create.contentAreaLayout"] = this.createItem;
    this.functionMap["Edit.contentAreaLayout"] = this.editItem;
    this.functionMap["Purge.contentAreaLayout"] = this.removeItem;

    this.nodeFilter = {
      wcmPath: "",
      nodeTypes: ["bpw:contentAreaLayout", "bpw:folder"],
      // nodeTypes: ["bpw:folder"]
    };

    this.store
      .pipe(select(fromStore.getOperations), takeUntil(this.unsubscribeAll))
      .subscribe(
        (operations: { [key: string]: WcmOperation[] }) => {
          if (operations) {
            this.operationMap = operations;
          }
        },
        (response) => {
          console.log("GET call in error", response);
        },
        () => {
          console.log("The GET observable is now completed.");
        }
      );
    this.store
      .pipe(
        select(fromStore.getContentAreaLayoutsError),
        takeUntil(this.unsubscribeAll),
        filter((status) => {
          return !!status;
        })
      )
      .subscribe((status) => this._handleItemStatus(status));
    super.ngOnInit();
  }

  /**
   * On destroy
   */
  ngOnDestroy(): void {
    this.unsubscribeAll.complete();
    this.loadError && this.store.dispatch(new fromStore.WcmSystemClearError());
  }

  doNodeOperation(item: String, operation: WcmOperation) {
    this.functionMap[`${operation.operation}.${operation.resourceName}`].call(
      this
    );
  }

  createItem() {
    const node = this.activeNode;
    const library = WcmUtils.library(node.data.wcmPath);
    this.router.navigate(["/wcm-authoring/content-area-layout/new"], {
      queryParams: {
        library: library,
        repository: node.data.repository,
        workspace: node.data.workspace,
        layoutName: "Page Layout Name",
        editing: false,
      },
    });
  }

  removeItem() {
    const node = this.activeNode;

    const payload = {
      repository: node.data.repository,
      workspace: node.data.workspace,
      wcmPath: node.data.wcmPath,
    };

    const confirmDialogRef = this.confirmationDialogService.confirm(
      "Are you sure you want to delete the content area layout?"
    );

    confirmDialogRef.afterClosed().subscribe((result) => {
      if (result) {
        this.blockuiComponentRef = this._createBlockUIComponent(
          "Deleting content area layout"
        );
        this.confirmationDialogService.closeConfirmation();
        this.store.dispatch(new fromStore.RemoveContentAreaLayout(payload));
      }
    });
  }

  editItem() {
    const node = this.activeNode;
    const library = WcmUtils.library(node.data.wcmPath);
    this.router.navigate(["/wcm-authoring/content-area-layout/edit"], {
      queryParams: {
        library: library,
        repository: node.data.repository,
        workspace: node.data.workspace,
        layoutName: node.data.name,
        editing: true,
      },
    });
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

  private _handleItemStatus(status: any) {
    if (status && this.blocking) {
      this._destroyBlockUIComponent();
    }
    if (status === WCM_ACTION_SUCCESSFUL) {
      this._reloadActiveNode();
    }
  }

  private _reloadActiveNode() {
    if (this.activeNode) {
      this.load(
        this.activeNode.data.parentId &&
          this.activeNode.data.nodeType != "bpw:contentAreaLayoutFolder"
          ? this.parent(this.activeNode)
          : this.activeNode
      );
    }
  }
}
