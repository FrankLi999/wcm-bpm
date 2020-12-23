import { AbstractControl } from "@angular/forms";
import { Component, Input, OnInit } from "@angular/core";
import { JsonSchemaFormService, hasOwn } from "@bpw/ajsf-core";

@Component({
  // tslint:disable-next-line:component-selector
  selector: "material-button-widget",
  templateUrl: "./material-button.component.html",
  styleUrls: ["./material-button.component.scss"],
})
export class MaterialButtonComponent implements OnInit {
  formControl: AbstractControl;
  controlName: string;
  controlValue: any;
  controlDisabled = false;
  boundControl = false;
  options: any;
  @Input() layoutNode: any;
  @Input() layoutIndex: number[];
  @Input() dataIndex: number[];

  constructor(private jsf: JsonSchemaFormService) {}

  ngOnInit() {
    this.options = this.layoutNode.options || {};
    this.jsf.initializeControl(this);
    if (hasOwn(this.options, "disabled")) {
      this.controlDisabled = this.options.disabled;
    } else if (this.jsf.formOptions.disableInvalidSubmit) {
      this.controlDisabled = !this.jsf.isValid;
      this.jsf.isValidChanges.subscribe(
        (isValid) => (this.controlDisabled = !isValid)
      );
    }
  }

  updateValue(event) {
    if (typeof this.options.onClick === "function") {
      this.options.onClick(event);
    } else {
      this.jsf.updateValue(this, event.target.value);
    }
  }

  get readonly(): string {
    return this.options?.readonly || this.jsf.formOptions.readonly
      ? "readonly"
      : null;
  }

  get disabled(): boolean {
    return (
      this.controlDisabled ||
      this.options?.readonly ||
      this.jsf.formOptions.readonly
    );
  }
}
