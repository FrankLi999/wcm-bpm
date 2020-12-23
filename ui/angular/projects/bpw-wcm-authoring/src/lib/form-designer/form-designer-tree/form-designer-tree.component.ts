import {
  Component,
  OnInit,
  OnDestroy,
  ViewEncapsulation,
  ViewContainerRef,
  ViewChild,
} from "@angular/core";
import { Router } from "@angular/router";
import { takeUntil, filter } from "rxjs/operators";
import { select, Store } from "@ngrx/store";
import { MatDialog } from "@angular/material/dialog";
import { Subject } from "rxjs";

import { WcmOperation } from "bpw-wcm-service";
import * as fromStore from "bpw-wcm-service";
import { WcmService, WCM_ACTION_SUCCESSFUL, WcmUtils } from "bpw-wcm-service";
import { WcmNavigatorComponent } from "../../components/wcm-navigator/wcm-navigator.component";

import {
  wcmAnimations,
  ConfirmationDialogService,
  BlockUIService,
  SidebarService,
} from "bpw-common";

@Component({
  selector: "form-designer-tree",
  templateUrl: "./form-designer-tree.component.html",
  styleUrls: ["./form-designer-tree.component.scss"],
  encapsulation: ViewEncapsulation.None,
  animations: wcmAnimations,
})
export class FormDesignerTreeComponent extends WcmNavigatorComponent
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
    this.rootNode = "form";
    this.rootNodeType = "bpw:formFolder";
    this.functionMap["Create.form"] = this.createItem;
    this.functionMap["Edit.form"] = this.editItem;
    this.functionMap["Purge.form"] = this.removeItem;

    this.nodeFilter = {
      wcmPath: "",
      nodeTypes: ["bpw:formType"],
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
        select(fromStore.getFormError),
        takeUntil(this.unsubscribeAll),
        filter((error) => error === WCM_ACTION_SUCCESSFUL)
      )
      .subscribe((resp) => {
        this._handleWcmStatus();
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
    this.router.navigate(["/wcm-authoring/form-designer/new"], {
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
    this.router.navigate(["/wcm-authoring/form-designer/edit"], {
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
      "Are you sure you want to delete the form?"
    );

    confirmDialogRef.afterClosed().subscribe((result) => {
      if (result) {
        this.blockuiComponentRef = this._createBlockUIComponent(
          "Deleting form"
        );
        this.confirmationDialogService.closeConfirmation();
        // setTimeout(() => {
        this.store.dispatch(new fromStore.DeleteForm(payload));

        // }, 3500);
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

  private _destroyBlockUIComponent() {
    this.blockUIService.destroyBlockUIComponent(
      this.blockui,
      this.blockuiComponentRef
    );
    this.blocking = false;
  }

  private _handleWcmStatus() {
    if (this.blocking) {
      this._destroyBlockUIComponent();
    }
    this._reloadActiveNode();
  }

  private _reloadActiveNode() {
    if (this.activeNode) {
      this.load(
        this.activeNode.data.parentId &&
          this.activeNode.data.nodeType != "bpw:formFolder"
          ? this.parent(this.activeNode)
          : this.activeNode
      );
    }
  }
}
