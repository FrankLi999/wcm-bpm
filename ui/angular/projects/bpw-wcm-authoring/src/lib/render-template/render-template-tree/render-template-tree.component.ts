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
  BlockUIService,
  ConfirmationDialogService,
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
  selector: "render-template-tree",
  templateUrl: "./render-template-tree.component.html",
  styleUrls: ["./render-template-tree.component.scss"],
  encapsulation: ViewEncapsulation.None,
  animations: wcmAnimations,
})
export class RenderTemplateTreeComponent extends WcmNavigatorComponent
  implements OnInit, OnDestroy {
  functionMap: { [key: string]: Function } = {};
  unsubscribeAll: Subject<any> = new Subject();

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
    private confirmationDialogService: ConfirmationDialogService,
    private router: Router
  ) {
    super(matDialog, sidebarService, store, wcmService);
  }

  ngOnInit() {
    this.rootNode = "renderTemplate";
    this.rootNodeType = "bpw:renderTemplateFolder";
    this.functionMap["Create.renderTemplate"] = this.createItem;
    this.functionMap["Edit.renderTemplate"] = this.editItem;
    this.functionMap["Purge.renderTemplate"] = this.removeItem;

    this.nodeFilter = {
      wcmPath: "",
      nodeTypes: ["bpw:renderTemplate", "bpw:folder"],
    };

    this.store
      .pipe(select(fromStore.getOperations), takeUntil(this.unsubscribeAll))
      .subscribe((operations: { [key: string]: WcmOperation[] }) => {
        if (operations) {
          this.operationMap = operations;
        }
      });

    this.store
      .pipe(
        select(fromStore.getRenderTemplateError),
        takeUntil(this.unsubscribeAll),
        filter((status) => !!status)
      )
      .subscribe((status) => {
        this._handleItemStatus(status);
      });
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

  doNodeOperation(item: String, operation: WcmOperation) {
    this.functionMap[`${operation.operation}.${operation.resourceName}`].call(
      this
    );
  }

  createItem() {
    const node = this.activeNode;
    this.router.navigate(["/wcm-authoring/render-template/new"], {
      queryParams: {
        library: WcmUtils.library(node.data.wcmPath),
        repository: node.data.repository,
        workspace: node.data.workspace,
        wcmPath: node.data.wcmPath,
        editing: false,
      },
    });
  }

  editItem() {
    const node = this.activeNode;
    this.router.navigate(["/wcm-authoring/render-template/edit"], {
      queryParams: {
        library: WcmUtils.library(node.data.wcmPath),
        repository: node.data.repository,
        workspace: node.data.workspace,
        wcmPath: node.data.wcmPath,
        editing: true,
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
      "Are you sure you want to delete the render template?"
    );

    confirmDialogRef.afterClosed().subscribe((result) => {
      if (result) {
        this.blockuiComponentRef = this._createBlockUIComponent(
          "Deleting render template"
        );
        this.confirmationDialogService.closeConfirmation();
        console.log("delete ", payload);
        this.store.dispatch(new fromStore.DeleteRenderTemplate(payload));
      }
    });
  }

  private _handleItemStatus(status: any) {
    if (status && this.blocking) {
      this._destroyBlockUIComponent();
    }
    if (status === WCM_ACTION_SUCCESSFUL) {
      this._reloadActiveNode();
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

  private _reloadActiveNode() {
    if (this.activeNode) {
      console.log("reload this.activeNode", this.activeNode);
      this.load(
        this.activeNode.data.parentId &&
          this.activeNode.data.nodeType != "bpw:renderTemplateFolder"
          ? this.parent(this.activeNode)
          : this.activeNode
      );
    }
  }
}
