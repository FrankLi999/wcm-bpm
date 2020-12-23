import {
  Component,
  OnInit,
  OnDestroy,
  ViewEncapsulation,
  ViewContainerRef,
  ViewChild,
} from "@angular/core";
import { Router } from "@angular/router";
import { filter, tap, takeUntil } from "rxjs/operators";
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
  selector: "authoring-template-tree",
  templateUrl: "./authoring-template-tree.component.html",
  styleUrls: ["./authoring-template-tree.component.scss"],
  encapsulation: ViewEncapsulation.None,
  animations: wcmAnimations,
})
export class AuthoringTemplateTreeComponent extends WcmNavigatorComponent
  implements OnInit, OnDestroy {
  unsubscribeAll: Subject<any> = new Subject();

  private blocking: boolean = false;
  @ViewChild("blockUIContainer", { read: ViewContainerRef })
  private blockui: ViewContainerRef;
  private blockuiComponentRef: any;
  private functionMap: { [key: string]: Function } = {};

  constructor(
    protected wcmService: WcmService,
    protected sidebarService: SidebarService,
    protected store: Store<fromStore.WcmAppState>,
    protected matDialog: MatDialog,
    private blockUIService: BlockUIService,
    private confirmationDialogService: ConfirmationDialogService,
    private router: Router
  ) {
    super(matDialog, sidebarService, store, wcmService);
  }

  ngOnInit() {
    this.rootNode = "authoringTemplate";
    this.rootNodeType = "bpw:authoringTemplateFolder";
    this.functionMap["Create.authoringTemplate"] = this.createItem;
    this.functionMap["Edit.authoringTemplate"] = this.editItem;
    this.functionMap["Purge.authoringTemplate"] = this.removeItem;

    this.nodeFilter = {
      wcmPath: "",
      nodeTypes: ["bpw:authoringTemplate", "bpw:folder"],
    };

    this.store
      .pipe(
        select(fromStore.getOperations),
        takeUntil(this.unsubscribeAll),
        tap((operations: { [key: string]: WcmOperation[] }) => {
          if (operations) {
            this.operationMap = operations;
          }
        })
      )
      .subscribe();

    this.store
      .pipe(
        takeUntil(this.unsubscribeAll),
        select(fromStore.getAuthoringTemplateError),
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

  createItem() {
    const node = this.activeNode;
    this.router.navigate(["/wcm-authoring/authoring-template/new"], {
      queryParams: {
        library: WcmUtils.library(node.data.wcmPath),
        repository: node.data.repository,
        workspace: node.data.workspace,
        wcmPath: node.data.wcmPath,
        editing: false,
      },
    });
  }

  doNodeOperation(item: String, operation: WcmOperation) {
    this.functionMap[`${operation.operation}.${operation.resourceName}`].call(
      this
    );
  }

  editItem() {
    const node = this.activeNode;
    this.router.navigate(["/wcm-authoring/authoring-template/edit"], {
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
      "Are you sure you want to delete the authoring template?"
    );

    confirmDialogRef.afterClosed().subscribe((result) => {
      if (result) {
        this.blockuiComponentRef = this._createBlockUIComponent(
          "Deleting  authoring template"
        );
        this.confirmationDialogService.closeConfirmation();
        // setTimeout(() => {
        this.store.dispatch(new fromStore.DeleteAuthoringTemplate(payload));

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
          this.activeNode.data.nodeType != "bpw:authoringTemplateFolder"
          ? this.parent(this.activeNode)
          : this.activeNode
      );
    }
  }
}
