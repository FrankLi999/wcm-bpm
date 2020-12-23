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
import { WcmResourceFieldDialog } from "./resource-field-dialog.component";

@Component({
  selector: "wcm-authoring-form-column",
  templateUrl: "./form-column.component.html",
  styleUrls: ["./form-column.component.scss"],
})
export class WcmFormColumnComponent implements OnInit {
  // @Input() resourceType: AuthoringTemplate;
  @Input() elements: { [key: string]: FormControl };
  @Input() properties: { [key: string]: FormControl };
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
      const dialogRef = this.dialog.open(WcmResourceFieldDialog, {
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
            [WcmConstants.CONTENT_ELEMENT_PREFIX + result.name],
            event.container.data as String[],
            0,
            event.currentIndex
          );
          this.elements[result.name] = result;
        }
      });
    }
  }

  editTargetField(index: number, fields: string[]) {
    const [prefix, name] = fields[index].split(".", 2);
    let field: FormControl =
      prefix === WcmConstants.CONTENT_ELEMENT
        ? this.elements[name]
        : this.properties[name];
    const dialogRef = this.dialog.open(WcmResourceFieldDialog, {
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
        this.elements[result.name] = result;
        if (name != result.name) {
          fields[index] = prefix + "." + result.name;
          delete this.elements[name];
        }
      }
    });
  }

  deleteTargetField(index: number, fields: string[]) {
    let fieldNames = fields.splice(index, 1);
    delete this.elements[fieldNames[0]];
  }

  getResourceFieldHint(formControlName: string): string {
    const [prefix, name] = formControlName.split(".", 2);
    let formControl =
      prefix === WcmConstants.CONTENT_ELEMENT
        ? this.elements[name]
        : this.properties[name];
    let hint = "";
    if (formControl.formControlLayout.hint) {
      hint = hint
        .concat(" (")
        .concat(formControl.formControlLayout.hint)
        .concat(")");
    }
    return hint;
  }

  getResourceFieldFlags(formControlName: string): string {
    const [prefix, name] = formControlName.split(".", 2);
    let formControl =
      prefix === WcmConstants.CONTENT_ELEMENT
        ? this.elements[name]
        : this.properties[name];
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

  getControlFieldName(formControl: string): string {
    const [prefix, name] = formControl.split(".", 2);
    return prefix === WcmConstants.CONTENT_ELEMENT
      ? this.elements[name].controlType
      : this.properties[name].controlType;
  }

  getFormGroups(column: FormColumn) {
    column.formGroups = column.formGroups || [];
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
