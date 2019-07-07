import { Component, OnInit, Input, Inject, ViewEncapsulation } from '@angular/core';
import {MatDialog, MatDialogRef, MAT_DIALOG_DATA} from '@angular/material/dialog';
import {CdkDragDrop, moveItemInArray, copyArrayItem, transferArrayItem, CdkDrag} from '@angular/cdk/drag-drop';

export interface SiteFolder {
  path: string;
}
export interface Workflow {
  workflow: string;
}
export interface ResourceType {
  id: string;
  typeName: string;
  baseType: string
  description: string;
  siteFolder: SiteFolder,
  workflow: Workflow,
  publishDate : Date;
  expireDate: Date;
}
const BASE_RESOURCE_TYPE: string[] = [
  'Content', 'Page', 'Widget', 'File', 'Key/Value', 'VanityURL', 'Form', 'Persona'
];

export interface ResourceFieldColumnModel {
  id: string;
  fxFlex: number;
  // coulmnName?: string;
  fields: ResourceFieldModel[];
}

export interface ResourceFieldRowModel {
  columns: ResourceFieldColumnModel[];
  // rowName?: string;
}

export interface ResourceFieldRowsModel {
  rows: ResourceFieldRowModel[];
}

export interface ResourceFieldTabModel {
  tabRows: ResourceFieldRowModel[];
  tabName: string;
}

export interface ResourceFieldTabsModel {
  tabs: ResourceFieldTabModel[];
}

export interface ResourceFieldStepModel {
  stepRows: ResourceFieldRowModel[];
  stepName: string;
}

export interface ResourceFieldStepsModel {
  steps: ResourceFieldStepModel[];
}

export type ResourceFieldGroupModel = ResourceFieldStepsModel | ResourceFieldTabsModel | ResourceFieldRowsModel;

export interface ResourceTypeModel {
  groups: ResourceFieldGroupModel[];     
}

export interface ResourceFieldModel {
  name: string;
  icon: string;
  class: string;
  inputType: ResourceFieldData;
}

export interface ResourceFieldData {
  type: string;
  name: string;
  value?: string; // = "n/a";
  defaultValue?: string; // ="n/a";
  hint?: string; // = "n/a";
  validationEx?: string; // = "n/a"
  flagTitles?: string[]; // = [];
  flags?: boolean[]; // = [];
  dataType?: string; // = "n/a";
  dataTypeTitles?:String[];
  selectTitle?: String;
  selections?: String[];
  selected?: String;
}

@Component({
  selector: 'resource-type-layout',
  templateUrl: './resource-type-layout.component.html',
  styleUrls: ['./resource-type-layout.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class ResourceTypeLayoutComponent implements OnInit {
  
  @Input() resourceTypeName: string;
  
  dropZones: string[] = ['builder-target1'];
  resourceTypeModel: ResourceTypeModel = {
    groups: [
    //   {
    //   rows: [{
    //     columns: [{
    //       id: "fieldgroup0",
    //       fxFlex: 100,
    //       fields: []
    //     }]
    //   },{
    //     columns: [{
    //       id: "fieldgroup1",
    //       fxFlex: 50,
    //       fields: []
    //     },{
    //       id: "fieldgroup2",
    //       fxFlex: 50,
    //       fields: []
    //     }]
    //   }]
    // },{
    //   tabs:[{
    //     tabName: 'tab1',
    //     tabRows: [{
    //       columns: [{
    //         id: "fieldgroup3",
    //         fxFlex: 50,
    //         fields: []
    //       },{
    //         id: "fieldgroup4",
    //         fxFlex: 50,
    //         fields: []
    //       }]
    //     },{
    //       columns: [{
    //         id: "fieldgroup4",
    //         fxFlex: 50,
    //         fields: []
    //       },{
    //         id: "fieldgroup5",
    //         fxFlex: 50,
    //         fields: []
    //       }]
    //     }]
    //   },{
    //     tabName: 'tab2',
    //     tabRows: [{
    //       columns: [{
    //         id: "fieldgroup10",
    //         fxFlex: 50,
    //         fields: []
    //       },{
    //         id: "fieldgroup11",
    //         fxFlex: 50,
    //         fields: []
    //       }]
    //     },{
    //       columns: [{
    //         id: "fieldgroup12",
    //         fxFlex: 50,
    //         fields: []
    //       },{
    //         id: "fieldgroup13",
    //         fxFlex: 50,
    //         fields: []
    //       }]
    //     }]
    //   }]
    // },{
    //   steps:[{
    //     stepName: 'step1',
    //     stepRows: [{
    //       columns: [{
    //         id: "fieldgroup6",
    //         fxFlex: 50,
    //         fields: []
    //       },{
    //         id: "fieldgroup7",
    //         fxFlex: 50,
    //         fields: []
    //       }]
    //     },{
    //       columns: [{
    //         id: "fieldgroup8",
    //         fxFlex: 50,
    //         fields: []
    //       },{
    //         id: "fieldgroup9",
    //         fxFlex: 50,
    //         fields: []
    //       }]
    //     }]
    //   },{
    //     stepName: 'step2',
    //     stepRows: [{
    //       columns: [{
    //         id: "fieldgroup14",
    //         fxFlex: 50,
    //         fields: []
    //       },{
    //         id: "fieldgroup15",
    //         fxFlex: 50,
    //         fields: []
    //       }]
    //     },{
    //       columns: [{
    //         id: "fieldgroup16",
    //         fxFlex: 50,
    //         fields: []
    //       },{
    //         id: "fieldgroup17",
    //         fxFlex: 50,
    //         fields: []
    //       }]
    //     }]
    //   }]
    // }
    ]
  };

  controlFields: ResourceFieldModel[] = [
    { name: 'Associations',
      inputType: {
        type: 'Associations',
        name: '',
        hint: ''
      }, 
      icon: 'recent_actors', class: 'wide' 
    },
    { name: 'Binary',
      inputType: {
        type: 'Binary',
        name: '',
        hint: 'Binary type',
        flagTitles: ['Required'],
        flags: [true]
      }, 
      icon: 'theaters', 
      class: 'wide' 
    },
    { name: 'Category',
      inputType: {
        type: 'Category',
        name: '',
        hint: 'Category type',
        flagTitles: ['Required', 'User Searchable'],
        flags: [true, true],
        selectTitle: 'Select Category',
        selections: ['a', 'b', 'c'],
        selected: 'a',
      },
      icon: 'category',
      class: 'wide'
    },
    { name: 'Checkbox',
      inputType: {
        type: 'Checkbox',
        name: ''
      }, 
      icon: 'check', class: 'wide' },
    { name: 'Custom Field',
      inputType: {
        type: 'Custom Field',
        name: '',
        hint: ''
      }, 
      icon: 'reorder', class: 'wide' },
    { name: 'Date',
      inputType: {
        type: 'Date',
        name: '',
        hint: ''
      }, 
      icon: 'calenda_today', class: 'wide' },
    { name: 'Date and Time',
      inputType: {
        type: 'Date and Time',
        name: '',
        hint: ''
      }, 
      icon: 'view_day', class: 'wide' },
    { name: 'File',
      inputType: {
        type: 'File',
        name: '',
        hint: ''
      }, 
      icon: 'file_copy', class: 'wide' },
    { name: 'Hidden Field',
      inputType: {
        type: 'Hidden Field',
        name: '',
        hint: ''
      }, 
      icon: 'keyboard_hide', class: 'wide' },
    { name: 'Image',
      inputType: {
        type: 'Image',
        name: '',
        hint: ''
      }, 
      icon: 'image', class: 'wide' },
    { name: 'Key/Value',
      inputType: {
        type: 'Key/Value',
        name: '',
        hint: ''
      }, 
      icon: 'event_note', class: 'wide' },
    { name: 'Line Divider',
      inputType: {
        type: 'Line Divider',
        name: '',
        hint: ''
      }, 
      icon: 'horizontal_split', class: 'wide' },
    { name: 'Multi Select',
      inputType: {
        type: 'Multi Select',
        name: '',
        hint: ''
      }, 
      icon: 'format_list_bulleted', class: 'wide' },
    { name: 'Permissions',
      inputType: {
        type: 'Permissions',
        name: '',
        hint: ''
      }, 
      icon: 'account_circle', class: 'wide' },
    { name: 'Radio',
      inputType: {
        type: 'Radio',
        name: '',
        hint: '',
        value: '',
        flagTitles: ['Required', 'UserSearchable', 'System Indexed', 'Show In List'],
        flags: [false, false, false, false],
        dataTypeTitles: ['Text', 'True/False', 'Decimal', 'Whole Number'],
        dataType: 'Text'
      },
      icon: 'radio_button_checked',
      class: 'wide' 
    },
    { name: 'Read-only Field',
      inputType: {
        type: 'Read-only Field',
        name: '',
        hint: ''
      }, 
      icon: 'offline_pin', class: 'wide' 
    },
    { name: 'Select',
      inputType: {
        type: 'Select',
        name: '',
        hint: ''
      }, 
      icon: 'select_all', class: 'wide' },
    { name: 'Site Area',
      inputType: {
        type: 'Site Area',
        name: '',
        hint: ''
      }, 
      icon: 'archive', class: 'wide' },
    { name: 'Tag',
      inputType: {
        type: 'Tag',
        name: '',
        hint: ''
      }, 
      icon: 'flag', class: 'wide' },
    { name: 'Text',
      inputType: {
        type: 'Text',
        name: '',
        hint: ''
      }, 
      icon: 'text_fields', class: 'wide' },
    { name: 'Textarea',
      inputType: {
        type: 'Textarea',
        name: '',
        hint: ''
      }, 
      icon: 'textsms', class: 'wide' },
    { name: 'Time',
      inputType: {
        type: 'Time',
        name: '',
        hint: ''
      }, 
      icon: 'access_time', class: 'wide' },
    { name: 'Rich Text',
      inputType: {
        type: 'Rich Text',
        name: '',
        hint: ''
      }, 
      icon: 'text_format', class: 'wide' }
  ];
  builderTargets: string[] = [
    // 'fieldgroup0', 
    // 'fieldgroup1', 
    // 'fieldgroup2', 
    // 'fieldgroup3', 
    // 'fieldgroup4',
    // 'fieldgroup5',
    // 'fieldgroup6',
    // 'fieldgroup7',
    // 'fieldgroup8',
    // 'fieldgroup9',
    // 'fieldgroup10',
    // 'fieldgroup11',
    // 'fieldgroup12',
    // 'fieldgroup13',
    // 'fieldgroup14',
    // 'fieldgroup15',
    // 'fieldgroup16',
    // 'fieldgroup17'              
  ];
  baseResourceType: string = BASE_RESOURCE_TYPE[Math.round(Math.random() * (BASE_RESOURCE_TYPE.length - 1))];
  
  resourceType: ResourceType = {
    id: Math.random().toString(),
    typeName: 'A Resource Type',
    baseType: this.baseResourceType,
    description: 'Content type',
    siteFolder: {
      path: 'demo.dotcms.com'
    },
    workflow: {
      workflow: 'System'
    },
    publishDate : new Date(2019, 0, 1),
    expireDate: new Date(2019, 0, 1)
  }
  private nextFieldGroupId: number = 0;
  constructor(
      private dialog: MatDialog) { 
  }

  ngOnInit() {
    this.nextFieldGroupId = this.getNextFieldGroupId();
  }

  groupType(group: ResourceFieldGroupModel): string {
    if ((<ResourceFieldStepsModel>group) && (<ResourceFieldStepsModel>group).steps!== undefined) {
      return "stepers";
    }
    if ((<ResourceFieldTabsModel>group) && (<ResourceFieldTabsModel>group).tabs !== undefined) {
      return "tabs";
    }
    if ((<ResourceFieldRowsModel>group) && (<ResourceFieldRowsModel>group).rows !== undefined) {
      return "rows";
    }
    return "n/a";
  }

  stepRemovable(step: ResourceFieldStepModel): boolean {
    return step.stepRows.length === 0;
  }

  deleteStep(index: number, steps: ResourceFieldRowModel[]) {
    steps.splice(index, 1);
  }

  tabRemovable(tab: ResourceFieldTabModel): boolean {
    return tab.tabRows.length === 0;
  }

  deleteTab(index: number, tabs: ResourceFieldTabModel[]) {
    tabs.splice(index, 1);
  }

  rowRemovable(row: ResourceFieldRowModel): boolean {
    return row.columns.every(this.emptyColumn);
  }

  emptyColumn(column: ResourceFieldColumnModel, index: number, columns: ResourceFieldColumnModel[]) {
    return column.fields.length === 0;
  }

  deleteRow(index: number, rows: ResourceFieldRowModel[]) {
    rows.splice(index, 1);
  }

  isStepers(group: ResourceFieldGroupModel): boolean {
    return "stepers" === this.groupType(group);
  }

  isTabs(group: ResourceFieldGroupModel): boolean {
    return "tabs" === this.groupType(group);
  }

  isRows(group: ResourceFieldGroupModel): boolean {
    return "rows" === this.groupType(group);
  }

  drop(event: CdkDragDrop<ResourceFieldModel[]>) {
    if (event.previousContainer === event.container) {
      moveItemInArray(event.container.data, event.previousIndex, event.currentIndex);
    } else if ("palletFields" !== event.previousContainer.id) {
      transferArrayItem(event.previousContainer.data,
        event.container.data,
        event.previousIndex,
        event.currentIndex);
    } else {
      const dialogRef = this.dialog.open(ResourceFieldDialog, {
        width: '500px',
        data: {...event.previousContainer.data[event.previousIndex].inputType}
      });
  
      dialogRef.afterClosed().subscribe(result => {
        if (result) {
          copyArrayItem([{...event.previousContainer.data[event.previousIndex]}],
            event.container.data,
            0,
            event.currentIndex);
            event.container.data[event.currentIndex].inputType = result;
        }
      });
    }
  }

  /** Predicate function that only allows even numbers to be dropped into a list. */
  evenPredicate(item: CdkDrag<ResourceFieldModel>) {
    return true;
  }

  /** Predicate function that doesn't allow items to be dropped into a list. */
  noReturnPredicate() {
    return false;
  }

  droppableItemClass = (item: any) => `${item.class} ${item.inputType.type}`;

  deleteTargetField(index:number, fields: ResourceFieldModel[]) {
      fields.splice(index, 1);
  }

  editTargetField(index:number, fields: ResourceFieldModel[]) {
    const dialogRef = this.dialog.open(ResourceFieldDialog, {
      width: '500px',
      data: {...fields[index].inputType}
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        fields[index].inputType = result;
      }
    });
  }

  getResourceFieldHint(inputType: ResourceFieldData): string {
      let hint = '';
      if (inputType.hint) {
        hint = hint.concat(' (').concat(inputType.hint).concat(')');
      }
      return hint;
  }

  getResourceFieldFlags(inputType: ResourceFieldData): string {
      let fieldFlag = '';
      if (inputType.flags) {
        for (let i = 0; i <= inputType.flags.length; i++) {
          if (inputType.flags[i]) {
            fieldFlag = fieldFlag.concat(' . ').concat(inputType.flagTitles[i]);
          }
        }
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
      data: {...this.resourceType}
    });

    dialogRef.afterClosed().subscribe(result => {
      this.resourceType = {...result};
    });
    return false;
  }

  getNextFieldGroupId(): number {
    return 20;
  }

  addNewRow(numOfColumn: number) {

    let rows = this.isRows(this.resourceTypeModel.groups[this.resourceTypeModel.groups.length - 1]) ? 
        this.resourceTypeModel.groups[this.resourceTypeModel.groups.length - 1] :
        { rows: []};
    
    (<ResourceFieldRowsModel>rows).rows.push({
      columns: []
    });
    let row = (<ResourceFieldRowsModel>rows).rows[(<ResourceFieldRowsModel>rows).rows.length - 1];
    for (let i = 0; i < numOfColumn; i++) {
      let fieldGroupId = "fieldgroup" + this.nextFieldGroupId++; 
      row.columns.push({
        id: fieldGroupId,
        fxFlex: 100/numOfColumn,
        fields: []
      });
      this.builderTargets.push(fieldGroupId);
    }

    if (!this.isRows(this.resourceTypeModel.groups[this.resourceTypeModel.groups.length - 1])) {
      this.resourceTypeModel.groups.push(rows);
    }
    return false;
  }

  editTab(tab: ResourceFieldTabModel) {
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

  addTabRow(numOfColumn: number, tab: ResourceFieldTabModel) {
      tab.tabRows.push({
          columns: []
      });  
      let row: ResourceFieldRowModel = tab.tabRows[tab.tabRows.length - 1];
      for (let i = 0; i < numOfColumn; i++) {
          let fieldGroupId = "fieldgroup" + this.nextFieldGroupId++; 
          row.columns.push({
              id: fieldGroupId,
              fxFlex: 100/numOfColumn,
              fields: []
          });
          this.builderTargets.push(fieldGroupId);
      }
      return false;
  }

  editStep(step: ResourceFieldStepModel) {
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

  addStepRow(numOfColumn: number, step: ResourceFieldStepModel) {
      step.stepRows.push({
          columns: []
      });  
      let row: ResourceFieldRowModel = step.stepRows[step.stepRows.length - 1];
      for (let i = 0; i < numOfColumn; i++) {
          let fieldGroupId = "fieldgroup" + this.nextFieldGroupId++; 
          row.columns.push({
              id: fieldGroupId,
              fxFlex: 100/numOfColumn,
              fields: []
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
            let tabs = this.isTabs(this.resourceTypeModel.groups[this.resourceTypeModel.groups.length - 1]) ? 
                this.resourceTypeModel.groups[this.resourceTypeModel.groups.length - 1] :
                { tabs: []};
            
            (<ResourceFieldTabsModel>tabs).tabs.push({
              tabRows: [{
                columns: []
              }],
              tabName: result
            });
            let tab = (<ResourceFieldTabsModel>tabs).tabs[(<ResourceFieldTabsModel>tabs).tabs.length - 1];
            for (let i = 0; i < numOfColumn; i++) {
              let fieldGroupId = "fieldgroup" + this.nextFieldGroupId++; 
              tab.tabRows[0].columns.push({
                id: fieldGroupId,
                fxFlex: 100/numOfColumn,
                fields: []
              });
              this.builderTargets.push(fieldGroupId);
            }

            if (!this.isTabs(this.resourceTypeModel.groups[this.resourceTypeModel.groups.length - 1])) {
              this.resourceTypeModel.groups.push(tabs);
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
          let steps = this.isStepers(this.resourceTypeModel.groups[this.resourceTypeModel.groups.length - 1]) ? 
              this.resourceTypeModel.groups[this.resourceTypeModel.groups.length - 1] :
              { steps: []};
        
          (<ResourceFieldStepsModel>steps).steps.push({
              stepRows: [{
                columns: []
              }],
              stepName: result
          });
          let step = (<ResourceFieldStepsModel>steps).steps[(<ResourceFieldStepsModel>steps).steps.length - 1];
          for (let i = 0; i < numOfColumn; i++) {
              let fieldGroupId = "fieldgroup" + this.nextFieldGroupId++; 
              step.stepRows[0].columns.push({
                  id: fieldGroupId,
                  fxFlex: 100/numOfColumn,
                  fields: []
              });
              this.builderTargets.push(fieldGroupId);
          }

          if (!this.isStepers(this.resourceTypeModel.groups[this.resourceTypeModel.groups.length - 1])) {
              this.resourceTypeModel.groups.push(steps);
          }
        }
    });
    return false;
  }
}

@Component({
  selector: 'resource-type-dialog',
  templateUrl: 'resource-type-dialog.html',
})
export class ResourceTypeDialog {

  constructor(
    public dialogRef: MatDialogRef<ResourceTypeDialog>,
    @Inject(MAT_DIALOG_DATA) public data: ResourceType) {}

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
    @Inject(MAT_DIALOG_DATA) public data: ResourceFieldData) {}

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