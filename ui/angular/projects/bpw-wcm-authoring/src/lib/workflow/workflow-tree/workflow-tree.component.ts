import {
  Component,
  OnInit,
  OnDestroy,
  ViewEncapsulation,
  ViewChild,
  ViewContainerRef,
} from "@angular/core";
import { Router } from "@angular/router";
import { wcmAnimations } from "bpw-common";
import { Subject } from "rxjs";
import { takeUntil, filter } from "rxjs/operators";
import { select, Store } from "@ngrx/store";
import { MatDialog } from "@angular/material/dialog";

import {
  BlockUIService,
  ConfirmationDialogService,
  SidebarService,
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
  selector: "workflow-tree",
  templateUrl: "./workflow-tree.component.html",
  styleUrls: ["./workflow-tree.component.scss"],
  encapsulation: ViewEncapsulation.None,
  animations: wcmAnimations,
})
export class WorkflowTreeComponent extends WcmNavigatorComponent
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
    this.rootNode = "workflow";
    this.rootNodeType = "bpw:workflowFolder";
    this.functionMap["Create.bpmnWorkflow"] = this.createItem;
    this.functionMap["Edit.bpmnWorkflow"] = this.editItem;
    this.functionMap["Purge.bpmnWorkflow"] = this.removeItem;
    this.nodeFilter = {
      wcmPath: "",
      nodeTypes: ["bpw:system_bpmnWorkflowType"],
    };

    this.store
      .pipe(takeUntil(this.unsubscribeAll), select(fromStore.getOperations))
      .subscribe(
        (operations: { [key: string]: WcmOperation[] }) => {
          if (operations) {
            this.operationMap = operations;
          }
        },
        (response) => {
          console.log("GET call in error", response);
          console.log(response);
        },
        () => {
          console.log("The GET observable is now completed.");
        }
      );

    this.store
      .pipe(
        takeUntil(this.unsubscribeAll),
        select(fromStore.getWorkflowStatus),
        filter((status) => !!status)
      )
      .subscribe((status) => this._handleItemStatus(status));
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

  editItem() {
    const node = this.activeNode;
    this.router.navigate(["/wcm-authoring/workflow/edit"], {
      queryParams: {
        library: WcmUtils.library(node.data.wcmPath),
        repository: node.data.repository,
        workspace: node.data.workspace,
        wcmPath: node.data.wcmPath,
        editing: "true",
      },
    });
  }

  createItem() {
    const node = this.activeNode;
    this.router.navigate(["/wcm-authoring/workflow/new"], {
      queryParams: {
        library: WcmUtils.library(node.data.wcmPath),
        repository: node.data.repository,
        workspace: node.data.workspace,
        wcmPath: node.data.wcmPath,
        editing: "false",
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
      "Are you sure you want to delete the workflow?"
    );

    confirmDialogRef.afterClosed().subscribe((result) => {
      if (result) {
        this.blockuiComponentRef = this._createBlockUIComponent(
          "Deleting workflow"
        );
        this.store.dispatch(new fromStore.DeleteWorkflow(payload));
        this.confirmationDialogService.closeConfirmation();
      }
    });
  }

  private _handleItemStatus(status: any) {
    if (status != null && this.blocking) {
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
      this.load(
        this.activeNode.data.parentId &&
          this.activeNode.data.nodeType != "bpw:workflowFolder"
          ? this.parent(this.activeNode)
          : this.activeNode
      );
    }
  }
}
