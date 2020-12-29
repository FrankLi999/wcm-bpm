import { Component, OnInit, OnDestroy, ViewEncapsulation } from "@angular/core";

import { MatDialog } from "@angular/material/dialog";
import { Store } from "@ngrx/store";

import { wcmAnimations } from "bpw-common";
import { WcmService } from "bpw-wcm-service";
import * as fromStore from "bpw-wcm-service";
import { AbstractWcmTree } from "../abstract-wcm-tree";

@Component({
  selector: "wcm-tree",
  templateUrl: "./wcm-tree.component.html",
  styleUrls: ["./wcm-tree.component.scss"],
  encapsulation: ViewEncapsulation.None,
  animations: wcmAnimations,
})
export class WcmTreeComponent extends AbstractWcmTree
  implements OnInit, OnDestroy {
  constructor(
    protected wcmService: WcmService,
    protected store: Store<fromStore.WcmAppState>,
    protected matDialog: MatDialog
  ) {
    super(wcmService, store, matDialog);
  }

  ngOnInit() {
    super.ngOnInit();
  }

  /**
   * On destroy
   */
  ngOnDestroy(): void {
    super.ngOnDestroy();
  }
}
