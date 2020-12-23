import { Component, Input, OnInit } from "@angular/core";
import { AbstractControl } from "@angular/forms";
import { JsonSchemaFormService } from "@bpw/ajsf-core";

@Component({
  // tslint:disable-next-line:component-selector
  selector: "material-checkbox-widget",
  templateUrl: "./material-checkbox.component.html",
  styleUrls: ["./material-checkbox.component.scss"],
})
export class MaterialCheckboxComponent implements OnInit {
  formControl: AbstractControl;
  controlName: string;
  controlValue: any;
  controlDisabled = false;
  boundControl = false;
  options: any;
  trueValue: any = true;
  falseValue: any = false;
  showSlideToggle = false;
  @Input() layoutNode: any;
  @Input() layoutIndex: number[];
  @Input() dataIndex: number[];

  constructor(private jsf: JsonSchemaFormService) {}

  ngOnInit() {
    this.options = this.layoutNode.options || {};
    this.jsf.initializeControl(this, !this.options.readonly);
    if (this.controlValue === null || this.controlValue === undefined) {
      this.controlValue = false;
      this.jsf.updateValue(this, this.falseValue);
    }
    if (
      this.layoutNode.type === "slide-toggle" ||
      this.layoutNode.format === "slide-toggle"
    ) {
      this.showSlideToggle = true;
    }
  }

  updateValue(event) {
    this.options.showErrors = true;
    this.jsf.updateValue(
      this,
      event.checked ? this.trueValue : this.falseValue
    );
  }

  get isChecked() {
    return this.jsf.getFormControlValue(this) === this.trueValue;
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
