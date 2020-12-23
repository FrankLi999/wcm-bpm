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
  selector: "draft-item-tree",
  templateUrl: "./draft-item-tree.component.html",
  styleUrls: ["./draft-item-tree.component.scss"],
  encapsulation: ViewEncapsulation.None,
  animations: wcmAnimations,
})
export class DraftItemTreeComponent
  extends WcmNavigatorComponent
  implements OnInit, OnDestroy {
  functionMap: { [key: string]: Function } = {};
  @ViewChild("wcmExplorer", { static: true })
  wcmExplorer: WcmExplorerComponent;
  private blocking: boolean = false;
  constructor(
    protected matDialog: MatDialog,
    protected sidebarService: SidebarService,
    protected store: Store<fromStore.WcmAppState>,
    protected wcmService: WcmService
  ) {
    super(matDialog, sidebarService, store, wcmService);
  }

  ngOnInit() {
    this.workspace = WcmConstants.WS_DRAFT;
    this.rootNode = "rootSiteArea";
    this.rootNodeType = "bpw:system_siteAreaType";

    this.nodeFilter = {
      wcmPath: "",
      nodeTypes: ["bpw:system_siteAreaType"],
    };
    this.operationMap = {};
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
}
