import {
  Component,
  OnInit,
  OnDestroy,
  Input,
  ViewContainerRef,
  ViewChild,
} from "@angular/core";
import { Router } from "@angular/router";
import { MatDialog } from "@angular/material/dialog";
import { FormBuilder, FormGroup, Validators } from "@angular/forms";
import { select, Store } from "@ngrx/store";
import { Subject } from "rxjs";
import { filter, takeUntil } from "rxjs/operators";
import cloneDeep from "lodash/cloneDeep";
import { BlockUIService, UiService } from "bpw-common";
import * as fromStore from "bpw-wcm-service";
import {
  ContentAreaLayout,
  ContentAreaLayoutClearError,
  CreateContentAreaLayout,
  getContentAreaLayoutError,
  getRenderTemplates,
  getContentAreaLayout,
  LayoutColumn,
  LayoutRow,
  RenderTemplate,
  RenderTemplateModel,
  UpdateContentAreaLayout,
  WcmAppState,
  WcmConstants,
  WCM_ACTION_SUCCESSFUL,
} from "bpw-wcm-service";

import { SelectRenderTemplateDialog } from "../../components/select-render-template/select-render-template.dialog";

@Component({
  selector: "content-area-layout",
  templateUrl: "./content-area-layout.component.html",
  styleUrls: ["./content-area-layout.component.scss"],
})
export class ContentAreaLayoutComponent implements OnInit, OnDestroy {
  @Input() editing: boolean;
  error: string;
  @Input() layoutName: string;
  @Input() library: string;
  pageLayoutForm: FormGroup;
  layout: ContentAreaLayout = null;
  renderTemplates: RenderTemplateModel[] = [];
  @Input() repository: string;
  @Input() workspace: string;

  private blocking: boolean = false;
  @ViewChild("blockUIContainer", { read: ViewContainerRef })
  private blockui: ViewContainerRef;
  private blockuiComponentRef: any;
  private unsubscribeAll: Subject<any>;

  constructor(
    private blockUIService: BlockUIService,
    private dialog: MatDialog,
    private formBuilder: FormBuilder,
    private router: Router,
    private store: Store<WcmAppState>,
    private uiService: UiService
  ) {
    this.unsubscribeAll = new Subject();
  }

  ngOnInit() {
    this.layoutName = this.layoutName || "Page Layout Name";
    this.pageLayoutForm = this.formBuilder.group({
      name: [this.layoutName, Validators.required],
    });
    this.store
      .pipe(select(getContentAreaLayoutError), takeUntil(this.unsubscribeAll))
      .subscribe((error: string) => {
        if (error) {
          this.error = this.uiService.getErrorMessage(error);
        }
      });

    this.store
      .pipe(select(getRenderTemplates), takeUntil(this.unsubscribeAll))
      .subscribe(
        (rts: { [key: string]: RenderTemplate }) => {
          if (rts) {
            for (let prop in rts) {
              this.renderTemplates.push({
                value: prop,
                title: rts[prop].title,
                query: rts[prop].query,
                resourceName: rts[prop].resourceName,
              });
            }
          }
        },
        (response) => {
          console.log("GET RT call in error", response);
          console.log(response);
        },
        () => {
          console.log("The GET RT observable is now completed.");
        }
      );

    this.store
      .pipe(select(getContentAreaLayout), takeUntil(this.unsubscribeAll))
      .subscribe((layout: ContentAreaLayout) => {
        this.layout = cloneDeep(layout);
        this.layout.repository = this.repository;
        this.layout.workspace = this.workspace;
        this.layout.library = this.library;
      });
  }

  /**
   * On destroy
   */
  ngOnDestroy(): void {
    this.unsubscribeAll.complete();
    this.error && this.store.dispatch(new ContentAreaLayoutClearError());
  }

  addRow(): void {
    // this.rows.push(this.formBuilder.control(0, Validators.required));
    this.layout.rows.push({
      columns: [
        {
          width: 100,
          viewers: [],
        },
      ],
    });
  }

  updateRow(rowNumber: number, rowType: number) {
    let currentRow = this.layout.rows[rowNumber];
    let columns: LayoutColumn[];
    switch (rowType) {
      case 0:
        columns = this.updatedCurrentRowColumns(currentRow, [100]);
        break;
      case 1:
        columns = this.updatedCurrentRowColumns(currentRow, [50, 50]);
        break;
      case 2:
        columns = this.updatedCurrentRowColumns(currentRow, [33, 66]);
        break;
      case 3:
        columns = this.updatedCurrentRowColumns(currentRow, [66, 33]);
        break;
      case 4:
        columns = this.updatedCurrentRowColumns(currentRow, [25, 75]);
        break;
      case 5:
        columns = this.updatedCurrentRowColumns(currentRow, [75, 25]);
        break;
      case 6:
        columns = this.updatedCurrentRowColumns(currentRow, [33, 33, 33]);
        break;
      case 7:
      default:
        columns = this.updatedCurrentRowColumns(currentRow, [25, 25, 25, 25]);
        break;
    }
    currentRow.columns = columns;
  }

  renderTemplate(renderTemplate: String): RenderTemplateModel {
    return this.renderTemplates.find((rt) => rt.value === renderTemplate);
  }

  removeRow(i: number) {
    // this.rows.removeAt(i);
    this.layout.rows.splice(i, 1);
  }

  canRewmoveRow(): boolean {
    return this.layout.rows.length > 1;
    // return this.rows.length > 1;
  }

  removeViewer(
    rowNumber: number,
    columnNumber: number,
    viewerIndex: number
  ): void {
    this.layout.rows[rowNumber].columns[columnNumber].viewers.splice(
      viewerIndex,
      1
    );
  }

  updateSidePane(sidePaneType: number) {
    switch (sidePaneType) {
      case 0:
        this.updateSidePaneWidth(0, 100, true);
        break;
      case 1:
        this.updateSidePaneWidth(20, 80, true);
        break;
      case 2:
        this.updateSidePaneWidth(30, 70, true);
        break;
      case 3:
        this.updateSidePaneWidth(40, 60, true);
        break;
      case 4:
        this.updateSidePaneWidth(20, 80, false);
        break;
      case 5:
        this.updateSidePaneWidth(30, 70, false);
        break;
      case 6:
      default:
        this.updateSidePaneWidth(40, 60, false);
        break;
    }
  }

  updateSidePaneWidth(
    sideWidth: number,
    contentWide: number,
    leftSide: boolean
  ) {
    this.layout.sidePane.width = sideWidth;
    this.layout.contentWidth = contentWide;
    this.layout.sidePane.left = leftSide;
  }

  updatedCurrentRowColumns(
    currentRow: LayoutRow,
    columnWidths: number[]
  ): LayoutColumn[] {
    let columns: LayoutColumn[] = [];
    let availableColumns =
      currentRow.columns.length > columnWidths.length
        ? columnWidths.length
        : currentRow.columns.length;
    columns = currentRow.columns.slice(0, availableColumns);
    if (availableColumns < columnWidths.length) {
      for (let i = 0; i < columnWidths.length - availableColumns; i++) {
        columns.push({
          viewers: [],
          width: 100,
        });
      }
    }
    for (let i = 0; i < columnWidths.length; i++) {
      columns[i].width = columnWidths[i];
    }
    return columns;
  }

  get showLeftSidePanel(): boolean {
    return (
      this.layout.sidePane &&
      this.layout.sidePane.width > 0 &&
      this.layout.sidePane.left
    );
  }

  get showRightSidePanel(): boolean {
    return (
      this.layout.sidePane &&
      this.layout.sidePane.width > 0 &&
      !this.layout.sidePane.left
    );
  }

  public addSideViewer() {
    const sideWidth = this.layout.sidePane.width; //TODO
    const left = this.layout.sidePane.left;
    const dialogRef = this.dialog.open(SelectRenderTemplateDialog, {
      width: "500px",
      data: {
        renderTemplates: this.renderTemplates,
        selectedRenderTemplate: "",
      },
    });

    dialogRef.afterClosed().subscribe((data) => {
      if (data && data.selectedRenderTemplate) {
        this.layout.sidePane.viewers.push({
          renderTemplate: data.selectedRenderTemplate.value,
          title: data.selectedRenderTemplate.title,
        });
        this.layout.sidePane.width = sideWidth;
        this.layout.sidePane.left = left;
      }
    });
    return false;
  }

  public removeSideViewer(viewerIndex: number) {
    this.layout.sidePane.viewers.splice(viewerIndex, 1);
    return false;
  }

  public addResourceViewer(rowNumber: number, colNumber: number) {
    const dialogRef = this.dialog.open(SelectRenderTemplateDialog, {
      width: "500px",
      data: {
        renderTemplates: this.renderTemplates,
        selectedRenderTemplate: "",
      },
    });
    dialogRef.afterClosed().subscribe((data) => {
      if (data && data.selectedRenderTemplate) {
        this.layout.rows[rowNumber].columns[colNumber].viewers.push({
          renderTemplate: data.selectedRenderTemplate.value,
          title: data.selectedRenderTemplate.title,
        });
      }
    });
    return false;
  }

  public removeResourceViewer(
    rowNumber: number,
    colNumber: number,
    viewerIndex: number
  ) {
    this.layout.rows[rowNumber].columns[colNumber].viewers.splice(
      viewerIndex,
      1
    );
    return false;
  }

  public savePageLayout() {
    this._createBlockUIComponent(
      this.editing
        ? "Updating content area layout"
        : "Creating content area layout"
    );
    let formValue = this.pageLayoutForm.value;
    this.layout.name = formValue.name;
    let unsubscribe: Subject<any> = new Subject();
    this.store
      .pipe(
        takeUntil(unsubscribe),
        filter((error) => !!error),
        select(fromStore.getFormError)
      )
      .subscribe((resp) => {
        unsubscribe.complete();
        if (resp === WCM_ACTION_SUCCESSFUL) {
          this._handleWcmActionStatus(resp);
          setTimeout(() => this._backToItems(), 500);
        }
      });
    if (this.editing) {
      this.store.dispatch(new UpdateContentAreaLayout(this.layout));
    } else {
      this.store.dispatch(new CreateContentAreaLayout(this.layout));
    }
  }

  public publishPageLayout() {
    console.log(this.layout);
  }

  public cancelEditing() {
    console.log(this.layout);
  }

  private _backToItems() {
    this.router.navigate([WcmConstants.NAV_LAYOUT_LIST]);
  }

  private _createBlockUIComponent(message: string) {
    this.blockuiComponentRef = this.blockUIService.createBlockUIComponent(
      message,
      this.blockui
    );
    this.blocking = true;
  }

  private _destroyBlockUIComponent() {
    this.blockUIService.destroyBlockUIComponent(
      this.blockui,
      this.blockuiComponentRef
    );
    this.blocking = false;
  }

  private _handleWcmActionStatus(status: any) {
    if (status != null && this.blocking) {
      this._destroyBlockUIComponent();
    }
  }
}
