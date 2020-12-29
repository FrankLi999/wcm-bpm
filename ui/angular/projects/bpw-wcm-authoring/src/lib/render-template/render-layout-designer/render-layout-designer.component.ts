import {
  Component,
  OnInit,
  OnDestroy,
  Input,
  ViewEncapsulation,
} from "@angular/core";

import {
  CdkDragDrop,
  moveItemInArray,
  copyArrayItem,
  transferArrayItem,
  CdkDrag,
} from "@angular/cdk/drag-drop";
import { Store } from "@ngrx/store";
import { Subject } from "rxjs";
import {
  RenderTemplateLayoutRow,
  RenderTemplateLayoutColumn,
  ResourceElementRender,
} from "bpw-wcm-service";
import * as fromStore from "bpw-wcm-service";
import { _runtimeChecksFactory } from "@ngrx/store/src/runtime_checks";

@Component({
  selector: "render-layout-designer",
  templateUrl: "./render-layout-designer.component.html",
  styleUrls: ["./render-layout-designer.component.scss"],
  encapsulation: ViewEncapsulation.None,
})
export class RenderLayoutDesignerComponent implements OnInit, OnDestroy {
  @Input() contentElementsMap = new Map<String, string[]>();
  @Input() contentPropertiesMap = new Map<String, string[]>();
  @Input() queryElementsMap = new Map<String, string[]>();
  @Input() selectedContentType: string;
  @Input() query: boolean = false;
  @Input() rows: RenderTemplateLayoutRow[];

  builderTargets: string[] = [];

  // queryElementsMap = new Map<String, string[]>();
  private nextFieldGroupId: number = 0;
  private unsubscribeAll: Subject<any>;
  constructor(private store: Store<fromStore.WcmAppState>) {
    this.unsubscribeAll = new Subject();
  }

  ngOnInit() {
    this.nextFieldGroupId = 1;
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

  emptyColumn(
    column: RenderTemplateLayoutColumn,
    index: number,
    columns: RenderTemplateLayoutColumn[]
  ) {
    return column.elements.length === 0;
  }

  deleteRow(index: number, rows: RenderTemplateLayoutRow[]) {
    rows.splice(index, 1);
  }

  get contentElements(): string[] {
    return this.query
      ? this.queryElementsMap.get(this.selectedContentType)
      : this.contentElementsMap.get(this.selectedContentType);
  }

  get contentProperties(): string[] {
    return this.query
      ? []
      : this.contentPropertiesMap.get(this.selectedContentType);
  }

  addNewRow(numOfColumn: number) {
    this.rows = this.rows || [];
    this.rows.push({
      columns: [],
    });
    let row: RenderTemplateLayoutRow = this.rows[this.rows.length - 1];
    for (let i = 0; i < numOfColumn; i++) {
      let fieldGroupId = "fieldgroup" + this.nextFieldGroupId++;
      row.columns.push({
        id: fieldGroupId,
        width: 100 / numOfColumn,
        elements: [],
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

  deleteTargetField(index: number, fields: ResourceElementRender[]) {
    let fieldNames = fields.splice(index, 1);
  }

  drop(event: CdkDragDrop<string[] | ResourceElementRender[]>) {
    if (
      !this._fromPallete(event.previousContainer.id) &&
      event.previousContainer.id === event.container.id
    ) {
      moveItemInArray(
        event.container.data as ResourceElementRender[],
        event.previousIndex,
        event.currentIndex
      );
    } else if (!this._fromPallete(event.previousContainer.id)) {
      transferArrayItem(
        event.previousContainer.data as ResourceElementRender[],
        event.container.data as ResourceElementRender[],
        event.previousIndex,
        event.currentIndex
      );
    } else if (
      this._fromPallete(event.previousContainer.id) &&
      event.previousContainer.id !== event.container.id
    ) {
      copyArrayItem(
        [
          {
            name: (event.previousContainer.data as string[])[
              event.previousIndex
            ],
            source: this._elementSource(event.previousContainer.id),
          },
        ],
        event.container.data as ResourceElementRender[],
        0,
        event.currentIndex
      );
    }
  }

  private _fromPallete(prevContainerId: string): boolean {
    return (
      "propertyTargets" === prevContainerId ||
      "elementTargets" === prevContainerId
    );
  }

  private _elementSource(prevContainerId: string): string {
    return this.query
      ? "query"
      : "propertyTargets" === prevContainerId
      ? "property"
      : "element";
  }
}
