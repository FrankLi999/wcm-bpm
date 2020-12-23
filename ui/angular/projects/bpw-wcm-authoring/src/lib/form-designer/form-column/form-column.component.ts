import { Component, OnInit, Input } from "@angular/core";
import { MatDialog } from "@angular/material/dialog";
import {
  CdkDragDrop,
  moveItemInArray,
  copyArrayItem,
  transferArrayItem,
  CdkDrag,
} from "@angular/cdk/drag-drop";
import { filter } from "rxjs/operators";
import { Store, select } from "@ngrx/store";
import {
  AuthoringTemplate,
  ControlField,
  FormColumn,
  FormControl,
  WcmConstants,
  JsonForm,
} from "bpw-wcm-service";
import * as fromStore from "bpw-wcm-service";
import {
  form_input_controls,
  binary_field_controls,
  object_controls,
  formated_input_controls,
  textarea_controls,
} from "../../config/form-config";
import { ResourceFieldDialog } from "./resource-field-dialog.component";

@Component({
  selector: "form-column",
  templateUrl: "./form-column.component.html",
  styleUrls: ["./form-column.component.scss"],
})
export class FormColumnComponent implements OnInit {
  // @Input() resourceType: AuthoringTemplate;
  @Input() formControls: { [key: string]: FormControl };
  @Input() columns: FormColumn[];
  @Input() builderTargets: string[];

  private formInputForms: JsonForm[];
  private objectForms: JsonForm[];
  private customFieldForms: JsonForm[];
  private binaryInputForms: JsonForm[];
  private formattedInputForms: JsonForm[];
  private textareaForms: JsonForm[];

  constructor(
    private dialog: MatDialog,
    private store: Store<fromStore.WcmAppState>
  ) {}

  ngOnInit(): void {
    this.store
      .pipe(
        select(fromStore.getForms),
        filter((jsonForms) => !!jsonForms)
      )
      .subscribe((jsonForms) => this._getControlForms(jsonForms));
  }

  /** Predicate function that only allows even numbers to be dropped into a list. */
  evenPredicate(item: CdkDrag<FormControl>) {
    return true;
  }

  drop(event: CdkDragDrop<ControlField[] | String[]>) {
    if (event.previousContainer === event.container) {
      moveItemInArray(
        event.container.data as String[],
        event.previousIndex,
        event.currentIndex
      );
    } else if ("palletFields" !== event.previousContainer.id) {
      transferArrayItem(
        event.previousContainer.data as String[],
        event.container.data as String[],
        event.previousIndex,
        event.currentIndex
      );
    } else {
      const controlField = (event.previousContainer.data as ControlField[])[
        event.previousIndex
      ];
      const dialogRef = this.dialog.open(ResourceFieldDialog, {
        panelClass: "resource-type-dialog",
        width: "800px",
        height: "800px",
        data: {
          controlFieldForm: this._getControlFieldForm(controlField.name, false),
          controlField: {
            name: "",
            controlType: controlField.name,
            multiple: false,
            mandatory: false,
            userSearchable: false,
            systemIndexed: false,
            showInList: false,
            unique: false,
            editable: false,
            expandable: false,
            richText: false,
          },
        },
      });

      dialogRef.afterClosed().subscribe((result) => {
        if (result) {
          copyArrayItem(
            [result.name],
            event.container.data as String[],
            0,
            event.currentIndex
          );
          this.formControls[result.name] = result;
        }
      });
    }
  }

  editTargetField(index: number, column: FormColumn) {
    //let fields: string[] = column.formControls;
    const name = column.formControls[index];
    const field: FormControl = this.formControls[name];
    const dialogRef = this.dialog.open(ResourceFieldDialog, {
      panelClass: "resource-type-dialog",
      width: "800px",
      height: "800px",
      data: {
        controlFieldForm: this._getControlFieldForm(field.controlType, true),
        controlField: {
          ...field,
        },
      },
    });
    dialogRef.afterClosed().subscribe((result) => {
      if (result) {
        this.formControls[result.name] = result;
        if (name != result.name) {
          column.formControls[index] = result.name;
          delete this.formControls[name];
        }
      }
    });
  }

  deleteTargetField(index: number, column: FormColumn) {
    // let fields: string[] = column.formControls;
    let fieldNames = column.formControls.splice(index, 1);
    delete this.formControls[fieldNames[0]];
  }

  getResourceFieldHint(formControlName: string): string {
    const formControl = this.formControls[formControlName];
    let hint = "";
    if (formControl.formControlLayout?.hint) {
      hint = hint
        .concat(" (")
        .concat(formControl.formControlLayout.hint)
        .concat(")");
    }
    return hint;
  }

  getResourceFieldFlags(formControlName: string): string {
    const formControl = this.formControls[formControlName];
    let fieldFlag = "";
    if (formControl.mandatory) {
      fieldFlag = fieldFlag.concat(" . ").concat("Required");
    }
    if (formControl.userSearchable) {
      fieldFlag = fieldFlag.concat(" . ").concat("User Searchable");
    }

    if (formControl.systemIndexed) {
      fieldFlag = fieldFlag.concat(" . ").concat("System Indexed");
    }

    if (formControl.showInList) {
      fieldFlag = fieldFlag.concat(" . ").concat("Show In List");
    }

    return fieldFlag;
  }

  getControlTypeName(formControl: string): string {
    return this.formControls[formControl].controlType;
  }

  getFormGroups(column: FormColumn) {
    // column.formGroups = column.formGroups || [];
    return column.formGroups;
  }

  private _getControlFieldForm(
    controlType: string,
    editMode: boolean
  ): JsonForm {
    let jsonForms: JsonForm[] = form_input_controls.includes(controlType)
      ? this.formInputForms
      : formated_input_controls.includes(controlType)
      ? this.formattedInputForms
      : textarea_controls.includes(controlType)
      ? this.textareaForms
      : binary_field_controls.includes(controlType)
      ? this.binaryInputForms
      : object_controls.includes(controlType)
      ? this.objectForms
      : this.customFieldForms;
    return editMode ? jsonForms[1] : jsonForms[0];
  }

  private _getControlForms(forms: { [key: string]: JsonForm[] }) {
    this.formInputForms = forms[WcmConstants.WCM_FORM_INPUT_TYPE];
    this.objectForms = forms[WcmConstants.WCM_FORM_REF_TYPE];
    this.customFieldForms = forms[WcmConstants.WCM_FORM_CUST_FIELD_TYPE];
    this.binaryInputForms = forms[WcmConstants.WCM_FORM_BINARY_TYPE];
    this.formattedInputForms = forms[WcmConstants.WCM_FORM_FORMATED_TYPE];
    this.textareaForms = forms[WcmConstants.WCM_FORM_TEXT_AREA_TYPE];
  }
}
