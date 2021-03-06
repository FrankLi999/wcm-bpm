import {
  Component,
  OnInit,
  OnDestroy,
  Output,
  EventEmitter,
} from "@angular/core";
import { MatDialog } from "@angular/material/dialog";
import { Store } from "@ngrx/store";

import * as fromStore from "bpw-wcm-service";
import { WcmService, WcmOperation } from "bpw-wcm-service";
import { SidebarService } from "bpw-common";
import { WcmNavigatorComponent } from "../../components/wcm-navigator/wcm-navigator.component";
import { WcmItemFlatTreeNode } from "../../components/wcm-tree/model/wcm-tree.model";

@Component({
  selector: "authoring-template-selector",
  templateUrl: "./authoring-template-selector.component.html",
  styleUrls: ["./authoring-template-selector.component.scss"],
})
export class AuthoringTemplateSelectorComponent extends WcmNavigatorComponent
  implements OnInit, OnDestroy {
  functionMap: { [key: string]: Function };

  @Output() authoringTemplateSelected: EventEmitter<any> = new EventEmitter();
  constructor(
    protected matDialog: MatDialog,
    protected sidebarService: SidebarService,
    protected store: Store<fromStore.WcmAppState>,
    protected wcmService: WcmService
  ) {
    super(matDialog, sidebarService, store, wcmService);
  }

  ngOnInit() {
    this.rootNode = "authoringTemplate";
    this.rootNodeType = "bpw:authoringTemplateFolder";
    this.nodeFilter = {
      wcmPath: "",
      nodeTypes: ["bpw:authoringTemplate"],
    };

    this.operationMap = {};
    super.ngOnInit();
  }

  doNodeOperation(item: string, operation: WcmOperation) {
    this.functionMap[`${operation.operation}.${operation.resourceName}`].call(
      this
    );
  }

  loadChildren(node: WcmItemFlatTreeNode, onlyFirstTime = true) {
    super.loadChildren(node, onlyFirstTime);
    if (node.data.nodeType === "bpw:authoringTemplate") {
      this.authoringTemplateSelected.emit(node);
    }
  }
}
