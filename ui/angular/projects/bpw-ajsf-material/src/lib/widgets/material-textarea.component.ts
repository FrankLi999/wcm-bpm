import { AbstractControl } from "@angular/forms";
import { Component, Inject, Input, OnInit, Optional } from "@angular/core";
import { JsonSchemaFormService } from "@bpw/ajsf-core";
import { MAT_LABEL_GLOBAL_OPTIONS } from "@angular/material/core";
import { MAT_FORM_FIELD_DEFAULT_OPTIONS } from "@angular/material/form-field";

@Component({
  // tslint:disable-next-line:component-selector
  selector: "material-textarea-widget",
  templateUrl: "./material-textarea.component.html",
  styleUrls: ["./material-textarea.component.scss"],
})
export class MaterialTextareaComponent implements OnInit {
  formControl: AbstractControl;
  controlName: string;
  controlValue: any;
  controlDisabled = false;
  boundControl = false;
  options: any;
  @Input() layoutNode: any;
  @Input() layoutIndex: number[];
  @Input() dataIndex: number[];

  constructor(
    @Inject(MAT_FORM_FIELD_DEFAULT_OPTIONS)
    @Optional()
    public matFormFieldDefaultOptions,
    @Inject(MAT_LABEL_GLOBAL_OPTIONS) @Optional() public matLabelGlobalOptions,
    private jsf: JsonSchemaFormService
  ) {}

  ngOnInit() {
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

  updateValue(event) {
    this.jsf.updateValue(this, event.target.value);
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
