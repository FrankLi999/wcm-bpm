import { Component, Inject, Input, OnInit, Optional } from "@angular/core";
import { AbstractControl } from "@angular/forms";
import {
  JsonSchemaFormService,
  dateToString,
  stringToDate,
} from "@bpw/ajsf-core";
import { MatDatepickerInputEvent } from "@angular/material/datepicker";
import { MAT_LABEL_GLOBAL_OPTIONS } from "@angular/material/core";
import { MAT_FORM_FIELD_DEFAULT_OPTIONS } from "@angular/material/form-field";

@Component({
  // tslint:disable-next-line:component-selector
  selector: "material-datepicker-widget",
  templateUrl: "./material-datepicker.component.html",
  styleUrls: ["./material-datepicker.component.scss"],
})
export class MaterialDatepickerComponent implements OnInit {
  formControl: AbstractControl;
  controlName: string;
  controlValue: any;
  dateValue: any;
  controlDisabled = false;
  boundControl = false;
  options: any;
  autoCompleteList: string[] = [];
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
    this.jsf.initializeControl(this, !this.options.readonly);
    if (this.controlValue) {
      this.setDate(dateToString(new Date(this.controlValue)));
    }
    if (
      !this.options.notitle &&
      !this.options.description &&
      this.options.placeholder
    ) {
      this.options.description = this.options.placeholder;
    }
  }

  updateValue(event: MatDatepickerInputEvent<Date>) {
    this.options.showErrors = true;
    if (event.value) {
      this.setDate(dateToString(event.value));
    }
  }

  setDate(date: string) {
    this.formControl.setValue(date, this.options);
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
