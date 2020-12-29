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
  WcmNode,
} from "bpw-wcm-service";
import * as fromStore from "bpw-wcm-service";
import { WcmNavigatorComponent } from "../../components/wcm-navigator/wcm-navigator.component";

@Component({
  selector: "site-config-tree",
  templateUrl: "./site-config-tree.component.html",
  styleUrls: ["./site-config-tree.component.scss"],
  encapsulation: ViewEncapsulation.None,
  animations: wcmAnimations,
})
export class SiteConfigTreeComponent extends WcmNavigatorComponent
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
    this.rootNode = "siteConfig";
    this.rootNodeType = "bpw:siteConfigFolder";
    this.functionMap["Create.siteConfig"] = this.createItem;
    this.functionMap["Edit.siteConfig"] = this.editItem;
    this.functionMap["Purge.siteConfig"] = this.removeItem;
    this.nodeFilter = {
      wcmPath: "",
      nodeTypes: ["bpw:system_siteConfigType"],
    };

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
        select(fromStore.getSiteConfigStatus),
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

  get myself(): any {
    return this;
  }

  createItem(ctx: WcmNode) {
    this.router.navigate(["/wcm-authoring/site-config/new"], {
      queryParams: {
        library: WcmUtils.library(ctx.wcmPath),
        repository: ctx.repository,
        workspace: ctx.workspace,
        wcmPath: ctx.wcmPath,
        editing: "false",
      },
    });
  }

  editItem(ctx: WcmNode) {
    this.router.navigate(["/wcm-authoring/site-config/edit"], {
      queryParams: {
        library: WcmUtils.library(ctx.wcmPath),
        repository: ctx.repository,
        workspace: ctx.workspace,
        wcmPath: ctx.wcmPath,
        editing: "true",
      },
    });
  }

  removeItem(ctx: WcmNode) {
    const payload = {
      repository: ctx.repository,
      workspace: ctx.workspace,
      wcmPath: ctx.wcmPath,
    };

    const confirmDialogRef = this.confirmationDialogService.confirm(
      "Are you sure you want to delete the site config?"
    );

    confirmDialogRef.afterClosed().subscribe((result) => {
      if (result) {
        this.blockuiComponentRef = this._createBlockUIComponent(
          "Deleting site config"
        );
        this.store.dispatch(new fromStore.DeleteSiteConfig(payload));
        this.confirmationDialogService.closeConfirmation();
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

  private _handleItemStatus(status: any) {
    if (status != null && this.blocking) {
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
          this.activeNode.data.nodeType != "bpw:siteConfigFolder"
          ? this.parent(this.activeNode)
          : this.activeNode
      );
    }
  }
}
