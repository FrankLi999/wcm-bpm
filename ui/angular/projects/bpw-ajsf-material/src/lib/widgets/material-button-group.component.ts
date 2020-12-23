import { AbstractControl } from "@angular/forms";
import { Component, Input, OnInit } from "@angular/core";
import { JsonSchemaFormService, buildTitleMap } from "@bpw/ajsf-core";

@Component({
  // tslint:disable-next-line:component-selector
  selector: "material-button-group-widget",
  templateUrl: "./material-button-group.component.html",
  styleUrls: ["./material-button-group.component.scss"],
})
export class MaterialButtonGroupComponent implements OnInit {
  formControl: AbstractControl;
  controlName: string;
  controlValue: any;
  controlDisabled = false;
  boundControl = false;
  options: any;
  radiosList: any[] = [];
  vertical = false;
  @Input() layoutNode: any;
  @Input() layoutIndex: number[];
  @Input() dataIndex: number[];

  constructor(private jsf: JsonSchemaFormService) {}

  ngOnInit() {
    this.options = this.layoutNode.options || {};
    this.radiosList = buildTitleMap(
      this.options.titleMap || this.options.enumNames,
      this.options.enum,
      true
    );
    this.jsf.initializeControl(this);
  }

  updateValue(value) {
    this.options.showErrors = true;
    this.jsf.updateValue(this, value);
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
