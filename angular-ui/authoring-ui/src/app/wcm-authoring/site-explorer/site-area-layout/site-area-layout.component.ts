import { Component, OnInit, OnDestroy, Input } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { MatDialog } from '@angular/material/dialog';
import { Subscription, Observable, Subject} from 'rxjs';
import { switchMap, takeUntil, filter } from 'rxjs/operators';
import { select, Store } from '@ngrx/store';
import cloneDeep from 'lodash-es/cloneDeep';

import { 
  SiteArea,
  SiteAreaLayout,
  LayoutRow,
  LayoutColumn,
  RenderTemplate,
  RenderTemplateModel
} from '../../model';
import * as fromStore from '../../store';
import { WcmService } from 'app/wcm-authoring/service/wcm.service';
import { SelectRenderTemplateDialog } from '../../components/select-render-template/select-render-template.dialog';

@Component({
  selector: 'app-site-area-layout',
  templateUrl: './site-area-layout.component.html',
  styleUrls: ['./site-area-layout.component.scss']
})
export class SiteAreaLayoutComponent implements OnInit, OnDestroy {
  @Input() repository: string;
  @Input() workspace: string;
  @Input() nodePath: string;
  @Input() editing: boolean = false;
  @Input() siteArea: SiteArea;
  renderTemplates: RenderTemplateModel[] = [];
  sub: Subscription;
  layout: SiteAreaLayout;

  private unsubscribeAll: Subject<any>;
  constructor(
    private wcmService: WcmService,
    private route: ActivatedRoute,
    private store: Store<fromStore.WcmAppState>,
    private dialog: MatDialog

  ) { 
    this.unsubscribeAll = new Subject<any>();
  }

  ngOnInit() {    

    this.sub = this.route.queryParams.pipe(
      switchMap(param => this.getSiteArea(param)),
      filter(siteArea => siteArea != null),
      switchMap(siteArea => {
        this.siteArea = siteArea;
        this.editing = this.siteArea.siteAreaLayout != undefined;
        return this.store.pipe(
          takeUntil(this.unsubscribeAll),
          select(fromStore.getWcmSystem))
      }),
      filter(wcmSystem => wcmSystem != null),
    ).subscribe(wcmSystem => {   
      let contentAreaLayout = wcmSystem.contentAreaLayouts[this.siteArea.contentAreaLayout];
      this.layout = this.editing ? cloneDeep(this.siteArea.siteAreaLayout) :
        {
          contentWidth: contentAreaLayout ? contentAreaLayout.contentWidth : 100,
          sidePane: contentAreaLayout ? cloneDeep(contentAreaLayout.sidePane) : {},
          rows: contentAreaLayout ? cloneDeep(contentAreaLayout.rows) : []
        }
    });

    this.store.pipe(
      takeUntil(this.unsubscribeAll),
        select(fromStore.getRenderTemplates)).subscribe(
      (rts: {[key:string]:RenderTemplate}) => {
        if (rts) {
          for (let prop in rts) {
            this.renderTemplates.push({
              value: prop, 
              title:rts[prop].title,
              isQuery: rts[prop].isQuery,
              resourceName: rts[prop].resourceName
            })
          }
        }
      },
      response => {
        console.log("GET RT call in error", response);
        console.log(response);
      },
      () => {
        console.log("The GET RT observable is now completed.");
      }
    );
  }

  ngOnDestroy(): void {
    this.sub.unsubscribe();
  }

  getSiteArea(param: any): Observable<SiteArea> {
    this.nodePath = param.nodePath;
    this.workspace = param.workspace;
    this.repository = param.repository;
    return this.wcmService.getSiteArea(this.repository, this.workspace, this.nodePath);
  }

  addRow(): void {
    // this.rows.push(this.formBuilder.control(0, Validators.required));
    this.layout.rows.push({
      columns : [ {
        width: 100,
        viewers : []
      }]});
  }

  updateRow(rowNumber: number, rowType: number) {
    let currentRow = this.layout.rows[rowNumber];
    let columns : LayoutColumn[];
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
    return this.renderTemplates.find(rt => rt.value === renderTemplate);
  }

  removeRow(i: number) {
    // this.rows.removeAt(i);
    this.layout.rows.splice(i, 1)
  }

  canRewmoveRow() : boolean {
    return this.layout.rows.length > 1;
    // return this.rows.length > 1;
  }

  removeViewer(rowNumber: number, columnNumber: number, viewerIndex: number): void {
      this.layout.rows[rowNumber].columns[columnNumber].viewers.splice(viewerIndex, 1);
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

  updateSidePaneWidth(sideWidth: number, contentWide: number, leftSide: boolean) {
      this.layout.sidePane.width = sideWidth;
      this.layout.contentWidth = contentWide;
      this.layout.sidePane.left = leftSide;
  }

  updatedCurrentRowColumns(currentRow: LayoutRow, columnWidths: number[]) : LayoutColumn[] {
      let columns : LayoutColumn[] = [];
      let availableColumns = (currentRow.columns.length > columnWidths.length) ? columnWidths.length : currentRow.columns.length;
      columns = currentRow.columns.slice(0, availableColumns);
      if (availableColumns < columnWidths.length) {
          for (let i = 0; i < columnWidths.length - availableColumns; i++) {
              columns.push({
                  viewers: [],
                  width: 100
              });
          }
      }
      for (let i = 0; i < columnWidths.length; i++) {
          columns[i].width = columnWidths[i];
      }
      return columns;
  }

  showLeftSidePanel() : boolean {
      return this.layout.sidePane && this.layout.sidePane.width > 0 && this.layout.sidePane.left;
  }

  showRightSidePanel() : boolean {
      return this.layout.sidePane && this.layout.sidePane.width > 0 && (!this.layout.sidePane.left);
  }

  public addSideViewer() {
  
    const dialogRef = this.dialog.open(SelectRenderTemplateDialog, {
      width: '500px',
      data: {
        renderTemplates: this.renderTemplates,
        selectedRenderTemplate: ''
      }
    });

    dialogRef.afterClosed().subscribe(data => {
      if (data && data.selectedRenderTemplate) {
        this.layout.sidePane.viewers.push({
          renderTemplate: data.selectedRenderTemplate.value,
          title: data.selectedRenderTemplate.title
        });
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
      width: '500px',
      data: {
        renderTemplates: this.renderTemplates,
        selectedRenderTemplate: ''
      }
    });

    dialogRef.afterClosed().subscribe(data => {
      if (data && data.selectedRenderTemplate) {
        this.layout.rows[rowNumber].columns[colNumber].viewers.push({
          renderTemplate: data.selectedRenderTemplate.value,
          title: data.selectedRenderTemplate.title
        });
      }
    });
    return false;
  }

  public removeResourceViewer(rowNumber: number, colNumber: number, viewerIndex: number) {
      this.layout.rows[rowNumber].columns[colNumber].viewers.splice(viewerIndex, 1);
      return false;
  }

  public savePageLayout() {
    console.log('save page layout', this.layout);
    // let formValue = this.pageLayoutForm.value;
    // this.layout.name = formValue.name;
    // this.layout.repository = this.repository;
    // this.layout.workspace = this.workspace;
    // this.layout.library = this.library;
    // this.store.dispatch(new fromStore.CreateContentAreaLayout(this.layout));
    
  }

  public publishPageLayout() {
    console.log(this.layout);
  }

  public cancelEditing() {
    console.log(this.layout);
  }
}
