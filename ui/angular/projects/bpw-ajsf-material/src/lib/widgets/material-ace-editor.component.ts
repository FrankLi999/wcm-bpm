import { AbstractControl } from "@angular/forms";
import {
  AfterViewInit,
  Component,
  Inject,
  Input,
  OnInit,
  Optional,
  ViewChild,
} from "@angular/core";
import { JsonSchemaFormService } from "@bpw/ajsf-core";
import { MAT_LABEL_GLOBAL_OPTIONS } from "@angular/material/core";
import { MAT_FORM_FIELD_DEFAULT_OPTIONS } from "@angular/material/form-field";

@Component({
  // tslint:disable-next-line:component-selector
  selector: "material-ace-widget",
  templateUrl: "./material-ace-editor.component.html",
  styleUrls: ["./material-ace-editor.component.scss"],
})
export class MaterialAceEditorComponent implements OnInit, AfterViewInit {
  @ViewChild("editor") editor;
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
    console.log(">>>>>>>>>>>>>> ainit ace editor options,", this.editor);
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

  ngAfterViewInit() {
    // this.editor.setTheme("eclipse");
    console.log(
      ">>>>>>>>>>>>>> after view init ace editor options,",
      this.editor
    );
    this.editor.getEditor().session.setOption("useWorker", false);
  }
  updateValue(newText: string) {
    this.jsf.updateValue(this, newText);
  }

  get readOnly(): string {
    return this.options?.readonly || this.jsf.formOptions.readonly
      ? "readonly"
      : null;
  }

  // get editorOptions(): any {
  //   return {
  //     useWorker: false,
  //   };
  // }
}
