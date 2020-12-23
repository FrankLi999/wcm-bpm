import { AbstractControl } from "@angular/forms";
import { Component, Input, OnInit } from "@angular/core";
import { JsonSchemaFormService, buildTitleMap } from "@bpw/ajsf-core";

@Component({
  // tslint:disable-next-line:component-selector
  selector: "material-radios-widget",
  templateUrl: "./material-radios.component.html",
  styleUrls: ["./material-radios.component.scss"],
})
export class MaterialRadiosComponent implements OnInit {
  formControl: AbstractControl;
  controlName: string;
  controlValue: any;
  controlDisabled = false;
  boundControl = false;
  options: any;
  flexDirection = "column";
  radiosList: any[] = [];
  @Input() layoutNode: any;
  @Input() layoutIndex: number[];
  @Input() dataIndex: number[];

  constructor(private jsf: JsonSchemaFormService) {}

  ngOnInit() {
    this.options = this.layoutNode.options || {};
    if (this.layoutNode.type === "radios-inline") {
      this.flexDirection = "row";
    }
    this.radiosList = buildTitleMap(
      this.options.titleMap || this.options.enumNames,
      this.options.enum,
      true
    );
    this.jsf.initializeControl(this, !this.options.readonly);
  }

  updateValue(value) {
    this.options.showErrors = true;
    this.jsf.updateValue(this, value);
  }

  onBlur(event) {
    if (typeof this.options.onBlur === "function") {
      this.options.onBlur(event);
    }
    this.options.showErrors = true;
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
