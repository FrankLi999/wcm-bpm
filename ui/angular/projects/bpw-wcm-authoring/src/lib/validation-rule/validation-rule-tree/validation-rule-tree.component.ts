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
import { takeUntil } from "rxjs/operators";
import { select, Store } from "@ngrx/store";
import { MatDialog } from "@angular/material/dialog";
import { Subject } from "rxjs";
import { filter } from "rxjs/operators";
import {
  SidebarService,
  BlockUIService,
  ConfirmationDialogService,
} from "bpw-common";
import {
  WcmOperation,
  WCM_ACTION_SUCCESSFUL,
  WcmService,
  WcmUtils,
} from "bpw-wcm-service";
import * as fromStore from "bpw-wcm-service";
import { WcmNavigatorComponent } from "../../components/wcm-navigator/wcm-navigator.component";

@Component({
  selector: "validation-rule-tree",
  templateUrl: "./validation-rule-tree.component.html",
  styleUrls: ["./validation-rule-tree.component.scss"],
  encapsulation: ViewEncapsulation.None,
  animations: wcmAnimations,
})
export class ValidationRuleTreeComponent extends WcmNavigatorComponent
  implements OnInit, OnDestroy {
  private blocking: boolean = false;
  @ViewChild("blockUIContainer", { read: ViewContainerRef })
  private blockui: ViewContainerRef;
  private blockuiComponentRef: any;

  functionMap: { [key: string]: Function } = {};
  unsubscribeAll: Subject<any> = new Subject();

  constructor(
    protected matDialog: MatDialog,
    protected wcmService: WcmService,
    protected sidebarService: SidebarService,
    protected store: Store<fromStore.WcmAppState>,
    private blockUIService: BlockUIService,
    private confirmationDialogService: ConfirmationDialogService,
    private router: Router
  ) {
    super(matDialog, sidebarService, store, wcmService);
  }

  ngOnInit() {
    this.rootNode = "validationRule";
    this.rootNodeType = "bpw:validationRuleFolder";
    this.functionMap["Create.validationRule"] = this.createItem;
    this.functionMap["Edit.validationRule"] = this.editItem;
    this.functionMap["Purge.validationRule"] = this.removeItem;
    this.nodeFilter = {
      wcmPath: "",
      nodeTypes: ["bpw:system_validationRuleType"],
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
          console.log(response);
        },
        () => {
          console.log("The GET observable is now completed.");
        }
      );
    this.store
      .pipe(
        select(fromStore.getValidationRuleStatus),
        takeUntil(this.unsubscribeAll),
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
    this.loadError &&
      this.store.dispatch(new fromStore.ValidationRuleClearError());
  }

  doNodeOperation(item: String, operation: WcmOperation) {
    this.functionMap[`${operation.operation}.${operation.resourceName}`].call(
      this
    );
  }

  editItem() {
    const node = this.activeNode;
    const library = node.data.wcmPath.split("/")[0];
    this.router.navigate(["/wcm-authoring/validation-rule/edit"], {
      queryParams: {
        library: library,
        repository: node.data.repository,
        workspace: node.data.workspace,
        wcmPath: node.data.wcmPath,
        editing: "true",
      },
    });
  }

  createItem() {
    const node = this.activeNode;
    const library = WcmUtils.library(node.data.wcmPath);
    this.router.navigate(["/wcm-authoring/validation-rule/new"], {
      queryParams: {
        library: library,
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
      "Are you sure you want to delete the validation rule?"
    );

    confirmDialogRef.afterClosed().subscribe((result) => {
      if (result) {
        this.blockuiComponentRef = this._createBlockUIComponent(
          "Deleting validation rule"
        );
        this.store.dispatch(new fromStore.DeleteValidationRule(payload));
        this.confirmationDialogService.closeConfirmation();
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
      this.load(
        this.activeNode.data.parentId &&
          this.activeNode.data.nodeType != "bpw:validationRuleFolder"
          ? this.parent(this.activeNode)
          : this.activeNode
      );
    }
  }
}
