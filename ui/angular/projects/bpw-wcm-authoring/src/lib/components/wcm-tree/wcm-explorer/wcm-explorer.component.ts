import {
  Component,
  OnInit,
  OnDestroy,
  ViewEncapsulation,
  Input,
  ViewChild,
} from "@angular/core";
import { FormControl } from "@angular/forms";
import { Store } from "@ngrx/store";

import { wcmAnimations, SidebarService } from "bpw-common";
import {
  WcmService,
  WcmOperation,
  JsonForm,
  WcmItemFilter,
  WcmNode,
  OperationContext,
} from "bpw-wcm-service";
import * as fromStore from "bpw-wcm-service";
import { WcmTreeComponent } from "../wcm-tree/wcm-tree.component";

@Component({
  selector: "wcm-explorer",
  templateUrl: "./wcm-explorer.component.html",
  styleUrls: ["./wcm-explorer.component.scss"],
  encapsulation: ViewEncapsulation.None,
  animations: wcmAnimations,
})
export class WcmExplorerComponent implements OnInit, OnDestroy {
  @Input() explorerTitle: string;
  @Input() functionMap: { [key: string]: Function } = {};
  @Input() rootNode: string = "rootSiteArea";
  @Input() rootNodeType: string = "bpw:system_siteAreaType";
  @Input() nodeFilter: WcmItemFilter;
  @Input() caller: any;
  @Input() operationMap: { [key: string]: WcmOperation[] };

  searchInput: FormControl;
  @ViewChild("wcmTree", { static: true }) wcmTree: WcmTreeComponent;
  constructor(
    protected wcmService: WcmService,
    protected store: Store<fromStore.WcmAppState>,
    protected sidebarService: SidebarService
  ) {
    // super(wcmService, store, matDialog);
    // Set the defaults
    this.searchInput = new FormControl("");
  }

  ngOnInit() {}

  /**
   * On destroy
   */
  ngOnDestroy(): void {}

  onNodeSelected(wcmNode: WcmNode) {
    // this._reloadActiveNode();
  }

  onNodeOperationSelected(ctx: OperationContext) {
    //item: String, operation: WcmOperation
    this.functionMap[
      `${ctx.wcmOperation.operation}.${ctx.wcmOperation.resourceName}`
    ].call(this.caller, ctx.node);
  }
  /**
   * Toggle the sidebar
   *
   * @param name
   */
  toggleSidebar(name): void {
    this.sidebarService.getSidebar(name).toggleOpen();
  }

  reloadActiveNode(loadParent) {
    this.wcmTree.reloadActiveNode(loadParent);
  }
}
