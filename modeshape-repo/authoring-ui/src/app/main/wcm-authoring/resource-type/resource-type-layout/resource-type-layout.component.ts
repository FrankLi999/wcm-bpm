import { Component, OnInit, Input, Inject, ViewEncapsulation } from '@angular/core';
import {MatDialog, MatDialogRef, MAT_DIALOG_DATA} from '@angular/material/dialog';
import {CdkDragDrop, moveItemInArray, copyArrayItem, transferArrayItem, CdkDrag} from '@angular/cdk/drag-drop';
import {
  ControlField,
  AuthoringTemplate,
  BaseFormGroup,
  FormSteps,
  FormTabs,
  FormTab,
  FormStep,
  FormRow,
  FormRows,
  FormColumn,
  TemplateField
} from '../../model';
import { WcmService } from '../../service/wcm.service';
const BASE_RESOURCE_TYPE: string[] = [
  'Content', 'Page', 'Widget', 'File', 'Key/Value', 'VanityURL', 'Form', 'Persona'
];

@Component({
  selector: 'resource-type-layout',
  templateUrl: './resource-type-layout.component.html',
  styleUrls: ['./resource-type-layout.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class ResourceTypeLayoutComponent implements OnInit {
  
  @Input() resourceTypeName: string;
  
  dropZones: string[] = ['builder-target1'];
  controlFields: ControlField[] = [];
  
  builderTargets: string[] = [             
  ];
  baseResourceType: string = BASE_RESOURCE_TYPE[Math.round(Math.random() * (BASE_RESOURCE_TYPE.length - 1))];
  resourceType: AuthoringTemplate = {
    repository: 'bpwizard',
    workspace: 'default',
    library: 'design',
    name: 'Resource Type name',
    title: 'Title',
    description: 'Content type',
    baseResourceType: 'Content',
    workflow: ['System'],
    categories: [],
    publishDate: new Date(2019, 0, 1),
    formGroups: [],
    formControls: {}
  }

  private nextFieldGroupId: number = 0;
  
  constructor(
      private wcmService: WcmService,
      private dialog: MatDialog) { 
  }

  ngOnInit() {
    this.nextFieldGroupId = this.getNextFieldGroupId();
    this.wcmService.getControlField('bpwizard', 'default').subscribe(
      (controlFiels: ControlField[]) => {
        if (controlFiels) {
          this.controlFields = controlFiels;
        }
      }
    )
  }

  groupType(group: BaseFormGroup): string {
    if (!group) {
      return 'n/a';
    }

    if ((group as FormSteps).steps!== undefined) {
      return "steps";
    }
    if ((group as FormTabs).tabs !== undefined) {
      return "tabs";
    }
    if ((group as FormRows).rows !== undefined) {
      return "rows";
    }
    if ((group as FormRow).columns !== undefined) {
      return "columns";
    }
    return "n/a";
  }

  stepRemovable(step: FormStep): boolean {
    return step.formGroups.length === 0;
  }

  deleteStep(index: number, steps: FormStep[]) {
    steps.splice(index, 1);
  }

  tabRemovable(tab: FormTab): boolean {
    return tab.formGroups.length === 0;
  }

  deleteTab(index: number, tabs: FormTab[]) {
    tabs.splice(index, 1);
  }

  rowRemovable(row: FormRow): boolean {
    return row.columns.every(this.emptyColumn);
  }

  emptyColumn(column: FormColumn, index: number, columns: FormColumn[]) {
    return column.formControls.length === 0;
  }

  deleteRow(index: number, rows: FormRow[]) {
    rows.splice(index, 1);
  }

  isStepers(group: BaseFormGroup): boolean {
    return "steps" === this.groupType(group);
  }

  isTabs(group: BaseFormGroup): boolean {
    return "tabs" === this.groupType(group);
  }

  isRows(group: BaseFormGroup): boolean {
    return "rows" === this.groupType(group);
  }

  isRow(group: BaseFormGroup): boolean {
    return "columns" === this.groupType(group);
  }

  drop(event: CdkDragDrop<ControlField[]|String[]>) {
    if (event.previousContainer === event.container) {
      moveItemInArray((event.container.data as String[]), event.previousIndex, event.currentIndex);
    } else if ("palletFields" !== event.previousContainer.id) {
      transferArrayItem((event.previousContainer.data as String[]),
        (event.container.data as String[]),
        event.previousIndex,
        event.currentIndex);
    } else {
      const controlField = (event.previousContainer.data as ControlField[])[event.previousIndex];
      const dialogRef = this.dialog.open(ResourceFieldDialog, {
        width: '500px',
        data: {
          currentFieldName: '',
          controlField: controlField,
          templateField: {
            name: ''
          }
        }
      });
  
      dialogRef.afterClosed().subscribe(result => {
        if (result) {
          result.templateField.controlName = result.controlField.name;
          copyArrayItem([result.templateField.name],
            (event.container.data as String[]),
            0,
            event.currentIndex);
            this.resourceType.formControls[result.templateField.name] = result.templateField;
        }
      });
    }
  }

  /** Predicate function that only allows even numbers to be dropped into a list. */
  evenPredicate(item: CdkDrag<TemplateField>) {
    return true;
  }

  /** Predicate function that doesn't allow items to be dropped into a list. */
  noReturnPredicate() {
    return false;
  }

  droppableItemClass = (item: any) => `wide ${item.inputType.controlName}`;

  deleteTargetField(index:number, fields: string[]) {
      let fieldNames = fields.splice(index, 1);      	
      delete this.resourceType.formControls[fieldNames[0]];
  }

  getControlFieldName(formControl:string): string {
     return this.resourceType.formControls[formControl].controlName;
  }
  editTargetField(index:number, fields: string[]) {
    let field:TemplateField  = this.resourceType.formControls[fields[index]];
    const dialogRef = this.dialog.open(ResourceFieldDialog, {
      width: '500px',
      data: {
        currentFieldName: fields[index],
        controlField: this.getControlField(field.controlName),
        templateField: field
      }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        fields[index] = result.templateField.name;
        if (result.currentFieldName != result.templateField.name) {
          delete this.resourceType.formControls[result.currentFieldName];
        }
        this.resourceType.formControls[result.templateField.name] = result.templateField;
      }
    });
  }

  getControlField(controlName: string): ControlField {
    return this.controlFields.find(controlField => controlField.name == controlName);
  }

  getResourceFieldHint(templateFieldName: string): string {
    let templateField = this.resourceType.formControls[templateFieldName];
    let hint = '';
    if (templateField.hint) {
      hint = hint.concat(' (').concat(templateField.hint).concat(')');
    }
    return hint;
  }

  getResourceFieldFlags(templateFieldName: string): string {
    let templateField = this.resourceType.formControls[templateFieldName];
    let fieldFlag = '';
    if (templateField.mandatory) {
      fieldFlag = fieldFlag.concat(' . ').concat('Required');
    }
    if (templateField.userSearchable) {
      fieldFlag = fieldFlag.concat(' . ').concat('User Searchable');
    }
    
    if (templateField.systemIndexed) {
      fieldFlag = fieldFlag.concat(' . ').concat('System Indexed');
    }

    if (templateField.showInList) {
      fieldFlag = fieldFlag.concat(' . ').concat('Show In List');
    }

    if (templateField.unique) {
      fieldFlag = fieldFlag.concat(' . ').concat('Unique');
    }
    return fieldFlag;
  }

  getIcon(baseResourceType: string) {
    let resourceTypeIndex = BASE_RESOURCE_TYPE.indexOf(baseResourceType);
    let resourceTypeIcon = '';
    switch (resourceTypeIndex) { //more_vert
      case 0:
          resourceTypeIcon = 'archive';
          break;
      case 1:
          resourceTypeIcon = 'pages';
          break;
      case 2:
          resourceTypeIcon = 'build';
          break;
      case 3:
          resourceTypeIcon = 'file_copy';
          break;
      case 4:
          resourceTypeIcon = 'domain';
          break;
      case 5:
          resourceTypeIcon = 'http';
          break;                                    
      case 6:
          resourceTypeIcon = 'apps';
          break;
      case 7:
      default:  
          resourceTypeIcon = 'person_outline';
          break;
    }
    return resourceTypeIcon;
  }

  editResourceType() {
    const dialogRef = this.dialog.open(ResourceTypeDialog, {
      width: '500px',
      data: this.resourceType
    });

    dialogRef.afterClosed().subscribe(result => {
      this.resourceType = result;
    });
    return false;
  }

  getNextFieldGroupId(): number {
    return 20;
  }

  addNewRow(numOfColumn: number) {

    let rows = this.isRows(this.resourceType.formGroups[this.resourceType.formGroups.length - 1]) ? 
    this.resourceType.formGroups[this.resourceType.formGroups.length - 1] as FormRows:
        { rows: []} as FormRows;
    rows.rows.push({
      columns: []
    });
    let row: FormRow = rows.rows[rows.rows.length - 1];
    for (let i = 0; i < numOfColumn; i++) {
      let fieldGroupId = "fieldgroup" + this.nextFieldGroupId++; 
      row.columns.push({
        id: fieldGroupId,
        fxFlex: 100/numOfColumn,
        formControls: []
      });
      this.builderTargets.push(fieldGroupId);
    }

    if (!this.isRows(this.resourceType.formGroups[this.resourceType.formGroups.length - 1])) {
      this.resourceType.formGroups.push(rows);
    }
    return false;
  }

  editTab(tab: FormTab) {
      const dialogRef = this.dialog.open(TabEditorDialog, {
          width: '500px',
          data: tab.tabName
      });

      dialogRef.afterClosed().subscribe(result => {
          if (result) {
              tab.tabName = result;
          }
      });
      return false;
  }

  addTabRow(numOfColumn: number, tab: FormTab) {
      tab.formGroups.push({columns: []} as FormRow);  
      let row: FormRow = tab.formGroups[tab.formGroups.length - 1] as FormRow;
      for (let i = 0; i < numOfColumn; i++) {
          let fieldGroupId = "fieldgroup" + this.nextFieldGroupId++; 
          row.columns.push({
              id: fieldGroupId,
              fxFlex: 100/numOfColumn,
              formControls: []
          });
          this.builderTargets.push(fieldGroupId);
      }
      return false;
  }

  editStep(step: FormStep) {
      const dialogRef = this.dialog.open(TabEditorDialog, {
          width: '500px',
          data: step.stepName
      });

      dialogRef.afterClosed().subscribe(result => {
          if (result) {
              step.stepName = result;
          }
      });
      return false;
  }

  addStepRow(numOfColumn: number, step: FormStep) {
      step.formGroups.push({columns: []} as FormRow);  
      let row: FormRow = step.formGroups[step.formGroups.length - 1] as FormRow;
      for (let i = 0; i < numOfColumn; i++) {
          let fieldGroupId = "fieldgroup" + this.nextFieldGroupId++; 
          row.columns.push({
              id: fieldGroupId,
              fxFlex: 100/numOfColumn,
              formControls: []
          });
          this.builderTargets.push(fieldGroupId);
      }
      return false;
  }

  addNewTab(numOfColumn: number) {
    const dialogRef = this.dialog.open(TabEditorDialog, {
      width: '500px',
      data: 'tab_name'
    });

    dialogRef.afterClosed().subscribe(result => {
        if (result) {
            let tabs = this.isTabs(this.resourceType.formGroups[this.resourceType.formGroups.length - 1]) ? 
            this.resourceType.formGroups[this.resourceType.formGroups.length - 1] as FormTabs:
                { tabs: []} as FormTabs;
           tabs.tabs.push({
              formGroups: [{
                columns: []
              } as FormRow],
              tabName: result
            });
            let tab = tabs.tabs[tabs.tabs.length - 1];
            for (let i = 0; i < numOfColumn; i++) {
              let fieldGroupId = "fieldgroup" + this.nextFieldGroupId++; 
              (tab.formGroups[0] as FormRow).columns.push({
                id: fieldGroupId,
                fxFlex: 100/numOfColumn,
                formControls: []
              });
              this.builderTargets.push(fieldGroupId);
            }

            if (!this.isTabs(this.resourceType.formGroups[this.resourceType.formGroups.length - 1])) {
              this.resourceType.formGroups.push(tabs);
            }
        }
    });
    return false;
  }

  addNewStep(numOfColumn: number) {
    const dialogRef = this.dialog.open(StepEditorDialog, {
      width: '500px',
      data: 'step_name'
    });

    dialogRef.afterClosed().subscribe(result => {
        if (result) {
          let steps = this.isStepers(this.resourceType.formGroups[this.resourceType.formGroups.length - 1]) ? 
          this.resourceType.formGroups[this.resourceType.formGroups.length - 1] as FormSteps:
              { steps: []} as FormSteps;
        
          (<FormSteps>steps).steps.push({
            formGroups: [{
                columns: []
              } as FormRow],
              stepName: result
          });
          let step = (<FormSteps>steps).steps[(<FormSteps>steps).steps.length - 1];
          for (let i = 0; i < numOfColumn; i++) {
              let fieldGroupId = "fieldgroup" + this.nextFieldGroupId++; 
              (step.formGroups[0] as FormRow).columns.push({
                  id: fieldGroupId,
                  fxFlex: 100/numOfColumn,
                  formControls: []
              });
              this.builderTargets.push(fieldGroupId);
          }

          if (!this.isStepers(this.resourceType.formGroups[this.resourceType.formGroups.length - 1])) {
            this.resourceType.formGroups.push(steps);
          }
        }
    });
    return false;
  }

  saveResourceType() {
    console.log(this.resourceType);
    this.wcmService.createAuthoringTemplate(this.resourceType)
      .subscribe(
        (event: any) => {
          console.log(event);
        },
        response => {
          console.log("GET call in error", response);
          console.log(response);
        },
        () => {
          console.log("The GET observable is now completed.");
        }
      );
  }
}

@Component({
  selector: 'resource-type-dialog',
  templateUrl: 'resource-type-dialog.html',
})
export class ResourceTypeDialog {

  constructor(
    public dialogRef: MatDialogRef<ResourceTypeDialog>,
    @Inject(MAT_DIALOG_DATA) public data: AuthoringTemplate) {}

  onNoClick(): void {
    this.dialogRef.close();
  }
}

@Component({
  selector: 'resource-field-dialog',
  templateUrl: 'resource-field-dialog.html',
})
export class ResourceFieldDialog {

  constructor(
    public dialogRef: MatDialogRef<ResourceFieldDialog>,
    @Inject(MAT_DIALOG_DATA) public data: {
      templateField:TemplateField,
      readonly controlField: ControlField}) {}

  onNoClick(): void {
    this.dialogRef.close();
  }
}

@Component({
  selector: 'tab-editor-dialog',
  templateUrl: 'tab-editor-dialog.html',
})
export class TabEditorDialog {

  constructor(
    public dialogRef: MatDialogRef<TabEditorDialog>,
    @Inject(MAT_DIALOG_DATA) public data: string) {}

  onNoClick(): void {
    this.dialogRef.close();
  }
}

@Component({
  selector: 'step-editor-dialog',
  templateUrl: 'step-editor-dialog.html',
})
export class StepEditorDialog {

  constructor(
    public dialogRef: MatDialogRef<StepEditorDialog>,
    @Inject(MAT_DIALOG_DATA) public data: string) {}

  onNoClick(): void {
    this.dialogRef.close();
  }
}