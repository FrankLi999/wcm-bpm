import { AbstractControl } from "@angular/forms";
import { buildTitleMap } from "@bpw/ajsf-core";
import { Component, Input, OnInit } from "@angular/core";
import { JsonSchemaFormService, TitleMapItem } from "@bpw/ajsf-core";

// TODO: Change this to use a Selection List instead?
// https://material.angular.io/components/list/overview

@Component({
  // tslint:disable-next-line:component-selector
  selector: "material-checkboxes-widget",
  templateUrl: "./material-checkboxes.component.html",
  styleUrls: ["./material-checkboxes.component.scss"],
})
export class MaterialCheckboxesComponent implements OnInit {
  formControl: AbstractControl;
  controlName: string;
  controlValue: any;
  controlDisabled = false;
  boundControl = false;
  options: any;
  horizontalList = false;
  formArray: AbstractControl;
  checkboxList: TitleMapItem[] = [];
  @Input() layoutNode: any;
  @Input() layoutIndex: number[];
  @Input() dataIndex: number[];

  constructor(private jsf: JsonSchemaFormService) {}

  ngOnInit() {
    this.options = this.layoutNode.options || {};
    this.horizontalList =
      this.layoutNode.type === "checkboxes-inline" ||
      this.layoutNode.type === "checkboxbuttons";
    this.jsf.initializeControl(this);
    this.checkboxList = buildTitleMap(
      this.options.titleMap || this.options.enumNames,
      this.options.enum,
      true
    );
    if (this.boundControl) {
      const formArray = this.jsf.getFormControl(this);
      for (const checkboxItem of this.checkboxList) {
        checkboxItem.checked = formArray.value.includes(checkboxItem.value);
      }
    }
  }

  get allChecked(): boolean {
    return (
      this.checkboxList.filter((t) => t.checked).length ===
      this.checkboxList.length
    );
  }

  get someChecked(): boolean {
    const checkedItems = this.checkboxList.filter((t) => t.checked).length;
    return checkedItems > 0 && checkedItems < this.checkboxList.length;
  }

  updateValue() {
    this.options.showErrors = true;
    if (this.boundControl) {
      this.jsf.updateArrayCheckboxList(this, this.checkboxList);
    }
  }

  updateAllValues(event: any) {
    this.options.showErrors = true;
    this.checkboxList.forEach((t) => (t.checked = event.checked));
    this.updateValue();
  }

  onBlur(event) {
    if (typeof this.options.onBlur === "function") {
      this.options.onBlur(event);
    }
    this.options.showErrors = true;
  }

  get disabled(): boolean {
    return (
      this.controlDisabled ||
      this.options?.readonly ||
      this.jsf.formOptions.readonly
    );
  }
}
