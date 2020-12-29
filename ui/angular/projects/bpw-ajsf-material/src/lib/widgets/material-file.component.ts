import {
  Component,
  Inject,
  Input,
  OnInit,
  Optional,
  ElementRef,
  ViewChild,
  ChangeDetectorRef,
} from "@angular/core";
import { AbstractControl, FormArray, FormControl } from "@angular/forms";
import { JsonSchemaFormService } from "@bpw/ajsf-core";
import { MAT_LABEL_GLOBAL_OPTIONS } from "@angular/material/core";
import { MAT_FORM_FIELD_DEFAULT_OPTIONS } from "@angular/material/form-field";

@Component({
  selector: "material-file-widget",
  templateUrl: "./material-file.component.html",
  styleUrls: ["./material-file.component.scss"],
})
export class MaterialFileComponent implements OnInit {
  formControl: AbstractControl;
  controlName: string;
  controlValue: any;
  controlDisabled = false;
  boundControl = false;
  options: any;
  separator: string = ",";
  @Input() layoutNode: any;
  @Input() layoutIndex: number[];
  @Input() dataIndex: number[];
  @ViewChild("inputValue", { static: false }) inputValue: ElementRef;
  @ViewChild("fileInput", { static: false }) fileInput: ElementRef;

  constructor(
    @Inject(MAT_FORM_FIELD_DEFAULT_OPTIONS)
    @Optional()
    public matFormFieldDefaultOptions,
    @Inject(MAT_LABEL_GLOBAL_OPTIONS) @Optional() public matLabelGlobalOptions,
    private jsf: JsonSchemaFormService,
    private changeDetector: ChangeDetectorRef
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
  get multiple(): boolean {
    return this.options?.multiple;
  }

  get accept(): string {
    return this.options?.accept;
  }

  onBlur(event) {
    if (typeof this.options.onBlur === "function") {
      this.options.onBlur(event);
    }
    this.options.showErrors = true;
  }

  onFileChange(event) {
    this.options.multiple
      ? this._onFilesChange(event)
      : this._onFileChange(event);
  }

  openFilePicker(event) {
    this.fileInput.nativeElement.click();
    if (event) {
      event.preventDefault();
      event.stopPropagation();
    }
    this._markAsTouched();
  }

  get disabled(): boolean {
    return (
      this.controlDisabled ||
      this.options?.readonly ||
      this.jsf.formOptions.readonly
    );
  }

  private _onFileChange(event) {
    let reader = new FileReader();
    if (event.target.files && event.target.files.length > 0) {
      let file = event.target.files[0];
      // reader.readAsDataURL(file);
      this._readFile(reader, event.target.files[0]);
      reader.onload = () => {
        let value = {
          filename: file.name,
          filetype: file.type,
          value: this._fileValue(reader),
        };
        this._updateValue(value.value);
        this._updateInputValue(value);
      };
    }
  }

  private _onFilesChange(event) {
    if (event.target.files && event.target.files.length > 0) {
      let values = [];
      let names = [];
      for (let i = 0; i < event.target.files.length; i++) {
        let reader = new FileReader();
        reader.onload = () => {
          values.push(this._fileValue(reader));
          names.push(event.target.files[i].name);
          // this._updateValue(JSON.stringify(values));
          this._updateValues(values);
          this._updateInputValues(names);
        };
        this._readFile(reader, event.target.files[i]);
      }
    }
  }

  private _updateInputValue(file) {
    this.inputValue.nativeElement.value = file.filename;
  }
  private _updateInputValues(names) {
    let text = names ? names.join(this.separator) : "";
    this.inputValue.nativeElement.value = text;
  }

  private _readFile(reader: FileReader, file) {
    "binary" === this.options.format
      ? reader.readAsBinaryString(file)
      : reader.readAsDataURL(file);
  }

  private _fileValue(reader) {
    return "binary" === this.options.format
      ? reader.result
      : (reader.result as string).split(",")[1];
  }

  private _updateValue(value) {
    this.jsf.updateValue(this, value);
  }

  private _updateValues(values: string[]) {
    if (values.length === 0) {
      return;
    }
    let files = this.formControl as FormArray;
    files.clear();
    for (let i = 0; i < values.length; i++) {
      files.push(new FormControl(""));
    }
    this.jsf.updateValue(this, values);
  }

  _markAsTouched() {
    this.changeDetector.markForCheck();
  }
}
