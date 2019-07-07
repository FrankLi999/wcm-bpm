import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, FormArray, Validators } from '@angular/forms';
export interface Theme {
  value: string;
  viewValue: string;
}
export interface SideBar {
  value: number;
  viewValue: string;
}

export interface LayoutRow {
  value: number;
  viewValue: string;
}

export interface LayoutColumn {
  value: string;
  viewValue: string;
}

export interface Viewer {
  renderTemplates: string[];
}

export interface Column {
  viewers: Viewer[];
  width: number;
}

export interface Row {
  columns: Column[];
}

export interface Layout {
  sideWidth: number;
  contentWidth: number;
  leftSide: boolean,
  sideViewers: Viewer[],
  rows : Row[];
}

@Component({
  selector: 'page-layout',
  templateUrl: './page-layout.component.html',
  styleUrls: ['./page-layout.component.scss']
})
export class PageLayoutComponent implements OnInit {
  pageLayoutForm: FormGroup;
  layout : Layout = {
    sideWidth: 0,
    contentWidth: 100,
    leftSide: true,
    sideViewers: [],
    rows : [{
      columns : [ {
        width: 100,
        viewers : []
      }]
    }]
  };
  themes: Theme[] = [
    {value: 'steak-0', viewValue: 'Steak'},
    {value: 'pizza-1', viewValue: 'Pizza'},
    {value: 'tacos-2', viewValue: 'Tacos'}
  ];
  
  constructor(private formBuilder: FormBuilder) { }

  ngOnInit() {
      // Reactive Form
      this.pageLayoutForm = this.formBuilder.group({
          pageLayoutName  : ['Page Layout Name', Validators.required],
          themeName     : ['tacos-2', Validators.required],
          sidebar       : [0, Validators.required],
          headerEnabled : [true, Validators.required],
          footerEnabled : [true, Validators.required],
          rows: this.formBuilder.array([
            this.formBuilder.control(0, Validators.required)
          ])
      });
  }

  get rows(): FormArray {
      return this.pageLayoutForm.get('rows') as FormArray;
  };

  addRow(): void {
      this.rows.push(this.formBuilder.control(0, Validators.required));
      this.layout.rows.push({
        columns : [ {
          width: 100,
          viewers : []
        }]});
  }

  updateRow(rowNumber: number, rowType: number) {
    let currentRow = this.layout.rows[rowNumber];
    let columns : Column[];
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

  removeRow(i: number) {
    this.rows.removeAt(i);
    this.layout.rows.splice(i, 1)
  }

  canRewmoveRow() : boolean {
    return this.rows.length > 1;
  }


  addViewer(rowNumber: number, columnNumber: number, viewer: Viewer): void {
      this.layout.rows[rowNumber].columns[columnNumber].viewers.push(viewer);
  }

  removeViewer(rowNumber: number, columnNumber: number, viewerIndex: number): void {
      this.layout.rows[rowNumber].columns[columnNumber].viewers.splice(viewerIndex, 1);
  }

  updateSidebar(sidebarType: number) {
      switch (sidebarType) {
          case 0:
              this.updateSidenavWidth(0, 100, true);
              break;
          case 1:
              this.updateSidenavWidth(20, 80, true);
              break;
          case 2:
              this.updateSidenavWidth(30, 70, true);
              break;
          case 3:
              this.updateSidenavWidth(40, 60, true);
              break;
          case 4:
              this.updateSidenavWidth(20, 80, false);
              break;
          case 5:
              this.updateSidenavWidth(30, 70, false);
              break;  
          case 6:
          default:
              this.updateSidenavWidth(40, 60, false);
              break; 
      }
  }

  updateSidenavWidth(sideWidth: number, contentWide: number, leftSide: boolean) {
      this.layout.sideWidth = sideWidth;
      this.layout.contentWidth = contentWide;
      this.layout.leftSide = leftSide;
  }



  updatedCurrentRowColumns(currentRow: Row, columnWidths: number[]) :Column[] {
      let columns : Column[] = [];
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
  
  showLeftSideNav() : boolean {
      return this.layout.sideWidth > 0 && this.layout.leftSide;
  }

  showRightSideNav() : boolean {
      return this.layout.sideWidth > 0 && (!this.layout.leftSide);
  }

  public addSideViewer() {
      this.layout.sideViewers.push({
        renderTemplates: []
      });
      return false;
  }

  public removeSideViewer(viewerIndex: number) {
      this.layout.sideViewers.splice(viewerIndex, 1);
      return false;
  }

  public addResourceViewer(rowNumber: number, colNumber: number) {
    this.layout.rows[rowNumber].columns[colNumber].viewers.push({
      renderTemplates: []
    });
    return false;
  }

  public removeResourceViewer(rowNumber: number, colNumber: number, viewerIndex: number) {
      this.layout.rows[rowNumber].columns[colNumber].viewers.splice(viewerIndex, 1);
      return false;
  }

  public addRenderTemplate(rowNumber: number, colNumber: number, viewIndex: number, renderTemplate:string) {
    this.layout.rows[rowNumber].columns[colNumber].viewers[viewIndex].renderTemplates.push(renderTemplate);
    return false;
  }

  public removeRenderTemplate(rowNumber: number, colNumber: number, viewIndex: number, renderTemplateIndex:number) {
    this.layout.rows[rowNumber].columns[colNumber].viewers[viewIndex].renderTemplates.splice(renderTemplateIndex, 1);
    return false;
  }
  
  public savePageLayout() {
    console.log(this.layout);
  }

  public publishPageLayout() {
    console.log(this.layout);
  }

  public cancelEditing() {
    console.log(this.layout);
  }
}
