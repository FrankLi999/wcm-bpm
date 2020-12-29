import { Component, OnInit, OnDestroy, Input } from "@angular/core";
import { MatDialog } from "@angular/material/dialog";
import { Store } from "@ngrx/store";
import { WcmNavigatorComponent } from "../wcm-navigator/wcm-navigator.component";
import { WcmAppState } from "bpw-wcm-service";
import { WcmService } from "bpw-wcm-service";
import { WcmOperation } from "bpw-wcm-service";
import { SidebarService } from "bpw-common";

@Component({
  selector: "content-selector",
  templateUrl: "./content-selector.component.html",
  styleUrls: ["./content-selector.component.scss"],
})
export class ContentSelectorComponent extends WcmNavigatorComponent
  implements OnInit, OnDestroy {
  functionMap: { [key: string]: Function };
  @Input() selectedContentItems: string[];
  @Input() authoringTemplate: string;
  @Input() nodeType: string;
  constructor(
    protected matDialog: MatDialog,
    protected sidebarService: SidebarService,
    protected store: Store<WcmAppState>,
    protected wcmService: WcmService
  ) {
    super(matDialog, sidebarService, store, wcmService);
  }

  ngOnInit() {
    this.rootNode = "rootSiteArea";
    this.rootNodeType = "bpw:system_siteAreaType";
    this.nodeFilter = {
      wcmPath: "",
      nodeTypes: ["bpw:system_siteAreaType", "bpw:(.+)_(.+)_AT"],
      conditions: {},
    };
    this.nodeFilter.conditions[this.nodeType] = {
      "bpw:authoringTemplate": this.authoringTemplate,
    };
    this.functionMap = {
      "Select.content": this.selectContent,
    };

    this.operationMap = {
      "bpw:(.+)_(.+)_AT": [
        {
          jcrType: "bpw:(.+)_(.+)_AT",
          resourceName: "content",
          operation: "Select",
          defaultTitle: "Select Content Item",
          defaultIcon: "create",
        },
      ],
    };
    super.ngOnInit();
  }

  doNodeOperation(item: string, operation: WcmOperation) {
    this.functionMap[`${operation.operation}.${operation.resourceName}`].call(
      this
    );
  }

  disableOperation(item: string, operation: WcmOperation) {
    if (this._isContentType(operation.jcrType)) {
      return this.selectedContentItems.includes(this.activeNode.data.wcmPath);
    } else {
      return false;
    }
  }

  private _isContentType(jcrType: string): boolean {
    return /bpw:(.+)_(.+)_AT/.test(jcrType);
  }

  selectContent() {
    return this.selectedContentItems.push(this.activeNode.data.wcmPath);
  }

  removeSelection(index: number) {
    return this.selectedContentItems.splice(index, 1);
  }
}
