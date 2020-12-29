import {
  Component,
  OnInit,
  OnDestroy,
  ViewEncapsulation,
  ViewChild,
  Input,
  Inject,
  Optional,
} from "@angular/core";
import { AbstractControl, FormControl, FormArray } from "@angular/forms";
import { MAT_LABEL_GLOBAL_OPTIONS } from "@angular/material/core";
import { MAT_FORM_FIELD_DEFAULT_OPTIONS } from "@angular/material/form-field";
import { COMMA, ENTER } from "@angular/cdk/keycodes";
import { Subject } from "rxjs";
import { Store } from "@ngrx/store";

import { JsonSchemaFormService } from "@bpw/ajsf-core";
import { wcmAnimations } from "bpw-common";
import {
  WcmOperation,
  WcmConstants,
  WcmNode,
  WcmItemFilter,
} from "bpw-wcm-service";
import * as fromStore from "bpw-wcm-service";
import { WcmTreeComponent } from "../../components/wcm-tree/wcm-tree/wcm-tree.component";

@Component({
  selector: "category-form-widget",
  templateUrl: "./category-form-widget.component.html",
  styleUrls: ["./category-form-widget.component.scss"],
  encapsulation: ViewEncapsulation.None,
  animations: wcmAnimations,
})
export class CategoryFormWidgetComponent implements OnInit, OnDestroy {
  @Input() rootNode: string = "rootSiteArea";
  @Input() rootNodeType: string = "bpw:system_siteAreaType";

  @Input() nodeFilter: WcmItemFilter;
  @Input() operationMap: { [key: string]: WcmOperation[] } = {};
  @ViewChild("wcmTree", { static: true }) wcmTree: WcmTreeComponent;
  // protected unsubscribeAll: Subject<any>;
  chipControl = new FormControl();
  formControl: AbstractControl;
  controlName: string;
  controlValue: string[];
  controlDisabled = false;
  boundControl = false;
  options: any;
  autoCompleteList: string[] = [];
  @Input() layoutNode: any;
  @Input() layoutIndex: number[];
  @Input() dataIndex: number[];

  selectable = true;
  removable = true;
  dropDown = false;
  separatorKeysCodes: number[] = [ENTER, COMMA];
  chips: string[] = [];

  constructor(
    @Inject(MAT_FORM_FIELD_DEFAULT_OPTIONS)
    @Optional()
    public matFormFieldDefaultOptions,
    @Inject(MAT_LABEL_GLOBAL_OPTIONS) @Optional() public matLabelGlobalOptions,
    private jsf: JsonSchemaFormService,
    protected store: Store<fromStore.WcmAppState>
  ) {}

  ngOnInit() {
    this.rootNode = WcmConstants.ROOTNODE_CATEGORY;
    this.rootNodeType = WcmConstants.NODETYPE_CATEGORY_FOLDER;
    this.nodeFilter = {
      wcmPath: "",
      nodeTypes: [WcmConstants.NODETYPE_CATEGORY],
    };

    this.options = this.layoutNode.options || {};
    this.jsf.initializeControl(this);
    let categories = this.formControl as FormArray;
    if (this.controlValue && !this.controlValue[0]) {
      categories.clear();
      this.controlValue = [];
    }
    this.chips =
      this.controlValue && this.controlValue[0] ? this.controlValue : [];

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

  remove(category: string): void {
    const index = this.chips.indexOf(category);

    if (index >= 0) {
      this.chips.splice(index, 1);
      (this.formControl as FormArray).removeAt(index);
      this.jsf.updateValue(this, this.chips);
    }
  }

  onNodeSelected(node: WcmNode) {
    if (
      node.nodeType === WcmConstants.NODETYPE_CATEGORY &&
      !this.chips.includes(node.wcmPath)
    ) {
      (this.formControl as FormArray).push(new FormControl(""));
      this.chips.push(node.wcmPath);
      this.jsf.updateValue(this, this.chips);
    }
    return false;
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
}
