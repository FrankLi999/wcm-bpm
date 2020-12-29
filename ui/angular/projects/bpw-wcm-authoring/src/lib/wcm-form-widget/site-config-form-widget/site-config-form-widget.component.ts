import {
  Component,
  OnInit,
  OnDestroy,
  ViewEncapsulation,
  ViewChild,
  Inject,
  Optional,
  Input,
} from "@angular/core";
import { AbstractControl } from "@angular/forms";
import { Subject } from "rxjs";
import { Store } from "@ngrx/store";
import { MAT_LABEL_GLOBAL_OPTIONS } from "@angular/material/core";
import { MAT_FORM_FIELD_DEFAULT_OPTIONS } from "@angular/material/form-field";
import { JsonSchemaFormService } from "@bpw/ajsf-core";
import { wcmAnimations } from "bpw-common";
import { WcmOperation, WcmItemFilter, WcmNode } from "bpw-wcm-service";
import * as fromStore from "bpw-wcm-service";
import { WcmTreeComponent } from "../../components/wcm-tree/wcm-tree/wcm-tree.component";

@Component({
  selector: "site-config-form-widget",
  templateUrl: "./site-config-form-widget.component.html",
  styleUrls: ["./site-config-form-widget.component.scss"],
  encapsulation: ViewEncapsulation.None,
  animations: wcmAnimations,
})
export class SiteConfigFormWidgetComponent implements OnInit, OnDestroy {
  @Input() rootNode: string = "rootSiteArea";
  @Input() rootNodeType: string = "bpw:system_siteAreaType";

  @Input() nodeFilter: WcmItemFilter;
  @Input() operationMap: { [key: string]: WcmOperation[] } = {};
  @ViewChild("wcmTree", { static: true }) wcmTree: WcmTreeComponent;
  //unsubscribeAll: Subject<any> = new Subject();

  formControl: AbstractControl;
  controlName: string;
  controlValue: string;
  controlDisabled = false;
  boundControl = false;
  options: any;
  autoCompleteList: string[] = [];
  @Input() layoutNode: any;
  @Input() layoutIndex: number[];
  @Input() dataIndex: number[];

  dropDown: boolean = false;
  constructor(
    @Inject(MAT_FORM_FIELD_DEFAULT_OPTIONS)
    @Optional()
    public matFormFieldDefaultOptions,
    @Inject(MAT_LABEL_GLOBAL_OPTIONS) @Optional() public matLabelGlobalOptions,
    private jsf: JsonSchemaFormService,
    protected store: Store<fromStore.WcmAppState>
  ) {}

  ngOnInit() {
    this.rootNode = "siteConfig";
    this.rootNodeType = "bpw:siteConfigFolder";
    this.nodeFilter = {
      wcmPath: "",
      nodeTypes: ["bpw:system_siteConfigType"],
    };

    this.options = this.layoutNode.options || {};
    this.jsf.initializeControl(this);
    if (
      !this.options.notitle &&
      !this.options.description &&
      this.options.placeholder
    ) {
      this.options.description = this.options.placeholder;
    }
  }

  /**
   * On destroy
   */
  ngOnDestroy(): void {
    // this.unsubscribeAll.next();
    // this.unsubscribeAll.complete();
    this.store.dispatch(new fromStore.WcmSystemClearError());
  }

  updateValue(event) {
    this.jsf.updateValue(this, event.target.value);
  }

  hideDropDown(event) {
    event.stopPropagation();
    this.dropDown = false;
    return false;
  }

  togglewDropDown(event) {
    event.stopPropagation();
    this.dropDown = !this.dropDown;
    return false;
  }

  onNodeSelected(node: WcmNode) {
    if (node.nodeType === "bpw:system_siteConfigType") {
      // this.formControl.setValue(node.wcmPath);
      this.jsf.updateValue(this, node.wcmPath);
      this.dropDown = false;
    }
    return false;
  }
}
