import { Component, OnInit, Input } from "@angular/core";
import { MatDialog } from "@angular/material/dialog";

import {
  BaseFormGroup,
  FormColumn,
  FormControl,
  FormRow,
  FormRows,
  FormStep,
  FormSteps,
  FormTab,
  FormTabs,
  WcmConstants,
} from "bpw-wcm-service";
import { PerfectScrollconfig } from "../../config/wcm-authoring.config";
import { TabEditorDialog } from "./tab-editor-dialog.component";
import { StepEditorDialog } from "./step-editor-dialog.component";

@Component({
  selector: "form-group",
  templateUrl: "./form-group.component.html",
  styleUrls: ["./form-group.component.scss"],
})
export class FormGroupComponent implements OnInit {
  @Input() formControls: { [key: string]: FormControl };
  @Input() formGroups: BaseFormGroup[];
  @Input() builderTargets: string[];
  public perfectScrollconfig = PerfectScrollconfig;
  private nextFieldGroupId: number = 0;

  constructor(private dialog: MatDialog) {}

  ngOnInit(): void {
    this.nextFieldGroupId = this._getNextFieldGroupId();
  }

  tabRemovable(tab: FormTab): boolean {
    return tab.formGroups.length === 0;
  }

  deleteTab(index: number, tabs: FormTab[]) {
    tabs.splice(index, 1);
  }

  isRow(group: BaseFormGroup): boolean {
    return "columns" === this._groupType(group);
  }
  isStepers(group: BaseFormGroup): boolean {
    return "steps" === this._groupType(group);
  }

  isTabs(group: BaseFormGroup): boolean {
    return "tabs" === this._groupType(group);
  }

  isRows(group: BaseFormGroup): boolean {
    return "rows" === this._groupType(group);
  }

  editStep(step: FormStep) {
    const dialogRef = this.dialog.open(TabEditorDialog, {
      width: "500px",
      data: step.stepName,
    });

    dialogRef.afterClosed().subscribe((result) => {
      if (result) {
        step.stepName = result;
      }
    });
    return false;
  }

  addStepRow(numOfColumn: number, step: FormStep) {
    step.formGroups.push({ columns: [] } as FormRow);
    let row: FormRow = step.formGroups[step.formGroups.length - 1] as FormRow;
    for (let i = 0; i < numOfColumn; i++) {
      let fieldGroupId = "fieldgroup" + this.nextFieldGroupId++;
      row.columns.push({
        id: fieldGroupId,
        fxFlex: 100 / numOfColumn,
        formControls: [],
        formGroups: [],
      });
      this.builderTargets.push(fieldGroupId);
    }
    return false;
  }

  stepRemovable(step: FormStep): boolean {
    return step.formGroups.length === 0;
  }

  deleteStep(index: number, steps: FormStep[]) {
    steps.splice(index, 1);
  }

  rowRemovable(row: FormRow): boolean {
    return row.columns.every(this._emptyColumn);
  }

  deleteRow(index: number, rows: FormRow[]) {
    rows.splice(index, 1);
  }

  addNewRow(numOfColumn: number) {
    let rows = this.isRows(this.formGroups[this.formGroups.length - 1])
      ? (this.formGroups[this.formGroups.length - 1] as FormRows)
      : ({ rows: [] } as FormRows);
    rows.rows.push({
      columns: [],
    });
    let row: FormRow = rows.rows[rows.rows.length - 1];
    for (let i = 0; i < numOfColumn; i++) {
      let fieldGroupId = "fieldgroup" + this.nextFieldGroupId++;
      row.columns.push({
        id: fieldGroupId,
        fxFlex: 100 / numOfColumn,
        formControls: [],
        formGroups: [],
      });
      this.builderTargets.push(fieldGroupId);
    }

    if (!this.isRows(this.formGroups[this.formGroups.length - 1])) {
      this.formGroups.push(rows);
    }
    return false;
  }

  editTab(tab: FormTab) {
    const dialogRef = this.dialog.open(TabEditorDialog, {
      width: "500px",
      data: tab.tabName,
    });

    dialogRef.afterClosed().subscribe((result) => {
      if (result) {
        tab.tabName = result;
      }
    });
    return false;
  }

  addTabRow(numOfColumn: number, tab: FormTab) {
    tab.formGroups.push({ columns: [] } as FormRow);
    let row: FormRow = tab.formGroups[tab.formGroups.length - 1] as FormRow;
    for (let i = 0; i < numOfColumn; i++) {
      let fieldGroupId = "fieldgroup" + this.nextFieldGroupId++;
      row.columns.push({
        id: fieldGroupId,
        fxFlex: 100 / numOfColumn,
        formControls: [],
        formGroups: [],
      });
      this.builderTargets.push(fieldGroupId);
    }
    return false;
  }

  addNewTab(numOfColumn: number) {
    const dialogRef = this.dialog.open(TabEditorDialog, {
      width: "500px",
      data: "tab_name",
    });

    dialogRef.afterClosed().subscribe((result) => {
      if (result) {
        let tabs = this.isTabs(this.formGroups[this.formGroups.length - 1])
          ? (this.formGroups[this.formGroups.length - 1] as FormTabs)
          : ({ tabs: [] } as FormTabs);
        tabs.tabs.push({
          formGroups: [
            {
              columns: [],
            } as FormRow,
          ],
          tabName: result,
        });
        let tab = tabs.tabs[tabs.tabs.length - 1];
        for (let i = 0; i < numOfColumn; i++) {
          let fieldGroupId = "fieldgroup" + this.nextFieldGroupId++;
          (tab.formGroups[0] as FormRow).columns.push({
            id: fieldGroupId,
            fxFlex: 100 / numOfColumn,
            formControls: [],
            formGroups: [],
          });
          this.builderTargets.push(fieldGroupId);
        }

        if (!this.isTabs(this.formGroups[this.formGroups.length - 1])) {
          this.formGroups.push(tabs);
        }
      }
    });
    return false;
  }

  addNewStep(numOfColumn: number) {
    const dialogRef = this.dialog.open(StepEditorDialog, {
      width: "500px",
      data: "step_name",
    });

    dialogRef.afterClosed().subscribe((result) => {
      if (result) {
        let steps = this.isStepers(this.formGroups[this.formGroups.length - 1])
          ? (this.formGroups[this.formGroups.length - 1] as FormSteps)
          : ({ steps: [] } as FormSteps);

        (<FormSteps>steps).steps.push({
          formGroups: [
            {
              columns: [],
            } as FormRow,
          ],
          stepName: result,
        });
        let step = (<FormSteps>steps).steps[
          (<FormSteps>steps).steps.length - 1
        ];
        for (let i = 0; i < numOfColumn; i++) {
          let fieldGroupId = "fieldgroup" + this.nextFieldGroupId++;
          (step.formGroups[0] as FormRow).columns.push({
            id: fieldGroupId,
            fxFlex: 100 / numOfColumn,
            formControls: [],
            formGroups: [],
          });
          this.builderTargets.push(fieldGroupId);
        }

        if (!this.isStepers(this.formGroups[this.formGroups.length - 1])) {
          this.formGroups.push(steps);
        }
      }
    });
    return false;
  }

  private _emptyColumn(
    column: FormColumn,
    index: number,
    columns: FormColumn[]
  ) {
    return column.formControls.length === 0;
  }

  private _groupType(group: BaseFormGroup): string {
    if (!group) {
      return WcmConstants.LAYOUT_GROUP_NA;
    }

    if ((group as FormSteps).steps !== undefined) {
      return WcmConstants.LAYOUT_GROUP_STEPS;
    }
    if ((group as FormTabs).tabs !== undefined) {
      return WcmConstants.LAYOUT_GROUP_TABS;
    }
    if ((group as FormRows).rows !== undefined) {
      return WcmConstants.LAYOUT_GROUP_ROWS;
    }
    if ((group as FormRow).columns !== undefined) {
      return WcmConstants.LAYOUT_GROUP_COLUMNS;
    }
    return WcmConstants.LAYOUT_GROUP_NA;
  }

  private _getNextFieldGroupId(): number {
    return 20;
  }
}
