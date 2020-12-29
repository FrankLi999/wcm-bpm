import { AbstractControl } from "@angular/forms";
import {
  Component,
  Inject,
  Input,
  OnInit,
  Optional,
  ViewChild,
  ElementRef,
} from "@angular/core";
import { JsonSchemaFormService } from "@bpw/ajsf-core";
import { MAT_LABEL_GLOBAL_OPTIONS } from "@angular/material/core";
import { MAT_FORM_FIELD_DEFAULT_OPTIONS } from "@angular/material/form-field";
import { MatChipInputEvent } from "@angular/material/chips";
import {
  MatAutocompleteSelectedEvent,
  MatAutocomplete,
} from "@angular/material/autocomplete";
import { COMMA, ENTER } from "@angular/cdk/keycodes";
// TODO: Add this control

@Component({
  // tslint:disable-next-line:component-selector
  selector: "material-chip-list-widget",
  templateUrl: "./material-chip-list.component.html",
  styleUrls: ["./material-chip-list.component.scss"],
})
export class MaterialChipListComponent implements OnInit {
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

  selectable = true;
  removable = true;
  dropDown = false;
  separatorKeysCodes: number[] = [ENTER, COMMA];
  chips: string[] = [];
  @ViewChild("chipInput", { static: true }) chipInput: ElementRef<
    HTMLInputElement
  >;
  @ViewChild("auto", { static: true }) matAutocomplete: MatAutocomplete;
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

  add(event: MatChipInputEvent): void {
    const input = event.input;
    const value = event.value;

    // Add our fruit
    if ((value || "").trim()) {
      this.chips.push(value.trim());
    }

    // Reset the input value
    if (input) {
      input.value = "";
    }

    this.formControl.setValue(null);
    this.jsf.updateValue(this, this.chips);
  }

  remove(fruit: string): void {
    const index = this.chips.indexOf(fruit);

    if (index >= 0) {
      this.chips.splice(index, 1);
      this.jsf.updateValue(this, this.chips);
    }
  }

  selected(event: MatAutocompleteSelectedEvent): void {
    this.chips.push(event.option.viewValue);
    this.chipInput.nativeElement.value = "";
    this.formControl.setValue(null);
    this.jsf.updateValue(this, this.chips);
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
