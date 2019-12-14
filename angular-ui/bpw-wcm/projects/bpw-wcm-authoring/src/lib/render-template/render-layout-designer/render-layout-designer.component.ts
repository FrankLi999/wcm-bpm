import { Component, OnInit, OnDestroy, Input, ViewEncapsulation } from '@angular/core';

import { CdkDragDrop, moveItemInArray, copyArrayItem, transferArrayItem, CdkDrag } from '@angular/cdk/drag-drop';
import { Store } from '@ngrx/store';
import { Subject } from 'rxjs';
import {
  RenderTemplateLayoutRow,
  RenderTemplateLayoutColumn,
  ResourceElementRender
} from 'bpw-wcm-service';
import * as fromStore from 'bpw-wcm-service';


@Component({
  selector: 'render-layout-designer',
  templateUrl: './render-layout-designer.component.html',
  styleUrls: ['./render-layout-designer.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class RenderLayoutDesignerComponent implements OnInit, OnDestroy {
  @Input() contentElementsMap = new Map<String, string[]>();
  @Input() selectedContentType: string;
  @Input() rows: RenderTemplateLayoutRow[];

  builderTargets: string[] = [             
  ];

  // queryElementsMap = new Map<String, string[]>();
  private nextFieldGroupId: number = 0;
  private unsubscribeAll: Subject<any>;
  constructor(
    private store: Store<fromStore.WcmAppState>) { 
      this.unsubscribeAll = new Subject();
  }

  ngOnInit() {
    this.nextFieldGroupId = 1;
    // this.store.pipe(
    //   takeUntil(this.unsubscribeAll),
    //   select(fromStore.getAuthoringTemplates)).subscribe(
    //     (authoringTemplates: {[key: string]: AuthoringTemplate}) => {
    //       if (authoringTemplates) {
    //         Object.entries(authoringTemplates).forEach(([key, at]) => {
    //           // this.contentTypes.push(at.name);
    //           let formControls: string[] = [...Object.keys(at.formControls)];
    //           this.contentElementsMap.set(`${at.repository}/${at.workspace}/${at.library}/${at.name}`, formControls);
    //         });
    //       }
    //     }
    //   );   
  }

  /**
  * On destroy
  */
  ngOnDestroy(): void {
    this.unsubscribeAll.next();
    this.unsubscribeAll.complete();
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
  
  contentElements(): string[] {
    
    return this.selectedContentType ? this.contentElementsMap.get(this.selectedContentType) : [];
  }

  addNewRow(numOfColumn: number) {
    this.rows = this.rows || [];
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