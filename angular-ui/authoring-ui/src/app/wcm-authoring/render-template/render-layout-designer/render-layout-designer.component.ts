import { Component, OnInit, OnDestroy, Input, Inject, ViewEncapsulation } from '@angular/core';
import { MatDialog, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';

import { CdkDragDrop, moveItemInArray, copyArrayItem, transferArrayItem, CdkDrag } from '@angular/cdk/drag-drop';
import { Store } from '@ngrx/store';
import {
  RenderTemplateLayoutRow,
  RenderTemplateLayoutColumn,
  ResourceElementRender
} from '../../model';
import * as fromStore from '../../store';


@Component({
  selector: 'render-layout-designer',
  templateUrl: './render-layout-designer.component.html',
  styleUrls: ['./render-layout-designer.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class RenderLayoutDesignerComponent implements OnInit {
  
  @Input() contentElements: string[] = [];
  @Input() rows: RenderTemplateLayoutRow[];

  builderTargets: string[] = [             
  ];

  private nextFieldGroupId: number = 0;
  constructor(
    private store: Store<fromStore.WcmAppState>,
    private dialog: MatDialog) { 
  }

  ngOnInit() {
    this.nextFieldGroupId = 1;    
  }

  rowRemovable(row: RenderTemplateLayoutRow): boolean {
    return row.columns.every(this.emptyColumn);
  }

  emptyColumn(column: RenderTemplateLayoutColumn, index: number, columns: RenderTemplateLayoutColumn[]) {
    return column.elements.length === 0;
  }

  deleteRow(index: number, rows: RenderTemplateLayoutRow[]) {
    rows.splice(index, 1);
  }

  addNewRow(numOfColumn: number) {

    // let rows = this.isRows(this.resourceType.formGroups[this.resourceType.formGroups.length - 1]) ? 
    // this.resourceType.formGroups[this.resourceType.formGroups.length - 1] as FormRows:
    //     { rows: []} as FormRows;
    this.rows.push({
      columns: []
    });
    let row: RenderTemplateLayoutRow = this.rows[this.rows.length - 1];
    for (let i = 0; i < numOfColumn; i++) {
      let fieldGroupId = "fieldgroup" + this.nextFieldGroupId++; 
      row.columns.push({
        id: fieldGroupId,
        width: 100/numOfColumn,
        elements: []
      });
      this.builderTargets.push(fieldGroupId);
    }
    return false;
  }

  /** Predicate function that only allows even numbers to be dropped into a list. */
  //evenPredicate(item: CdkDrag<ResourceElementRender>) {
  evenPredicate(item: CdkDrag<ResourceElementRender>) {
    return true;
  }

  /** Predicate function that doesn't allow items to be dropped into a list. */
  noReturnPredicate() {
    return false;
  }

  deleteTargetField(index:number, fields: ResourceElementRender[]) {
    let fieldNames = fields.splice(index, 1);      	
  }

  drop(event: CdkDragDrop<string[] | ResourceElementRender[]>) {
    console.log(event)
    if ("palletFields" !== event.previousContainer.id && event.previousContainer === event.container) {
      moveItemInArray((event.container.data as ResourceElementRender[]), event.previousIndex, event.currentIndex);
    } else if ("palletFields" !== event.previousContainer.id) {
      transferArrayItem((event.previousContainer.data as ResourceElementRender[]),
        (event.container.data as ResourceElementRender[]),
        event.previousIndex,
        event.currentIndex);
    } else if ("palletFields" === event.previousContainer.id && event.previousContainer !== event.container) {
      copyArrayItem([{name: (event.previousContainer.data as string[])[event.previousIndex]}],
            (event.container.data as ResourceElementRender[]),
            0,
            event.currentIndex);
    }
  }

  
  
}