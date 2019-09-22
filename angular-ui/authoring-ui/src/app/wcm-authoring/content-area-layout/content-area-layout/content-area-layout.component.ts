import { Component, OnInit, OnDestroy, Inject } from '@angular/core';
import {MatDialog, MatDialogRef, MAT_DIALOG_DATA} from '@angular/material/dialog';
import { FormBuilder, FormGroup, FormArray, Validators } from '@angular/forms';
import { select, Store } from '@ngrx/store';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';

import { 
  Theme,
  RenderTemplate,
  ContentAreaLayout,
  LayoutColumn,
  LayoutRow
} from '../../model';
import * as fromStore from '../../store';
export interface ThemeModel {
  value: String;
  viewValue: String;
}

export interface RenderTemplateModel {
  value: String;
  viewValue: String;
}

@Component({
  selector: 'content-area-layout',
  templateUrl: './content-area-layout.component.html',
  styleUrls: ['./content-area-layout.component.scss']
})
export class ContentAreaLayoutComponent implements OnInit, OnDestroy {
  pageLayoutForm: FormGroup;

  layout: ContentAreaLayout = null;
  themes: ThemeModel[] = [];
  
  renderTemplates: RenderTemplateModel[] = [];
  private unsubscribeAll: Subject<any>;
  private error: string;
  constructor(
    private dialog: MatDialog,
    private formBuilder: FormBuilder,
    private store: Store<fromStore.WcmAppState>
    ) { 
      this.unsubscribeAll = new Subject()
    }

  ngOnInit() {
      // Reactive Form
      this.pageLayoutForm = this.formBuilder.group({
          name          : ['Page Layout Name', Validators.required],
          theme         : ['tacos-2', Validators.required],
          headerEnabled : [true, Validators.required],
          footerEnabled : [true, Validators.required],
          rows: this.formBuilder.array([
            this.formBuilder.control(0, Validators.required)
          ])
      });

      this.store.pipe(
        takeUntil(this.unsubscribeAll),
        select(fromStore.getContentAreaLayoutError)
        ).subscribe(
          (error: string) => {
          if (error) {
            this.error = error;
          }
      });

      //jcrThemes: Theme[];
      this.store.pipe(
          takeUntil(this.unsubscribeAll),
          select(fromStore.getThemes)          
        ).subscribe(
        (jcrThemes: Theme[]) => {
          if (jcrThemes)
          jcrThemes.forEach(jcrTheme => {
            this.themes.push({
              value: `${jcrTheme.repositoryName}/${jcrTheme.workspace}/${jcrTheme.library}/${jcrTheme.name}`, 
              viewValue: jcrTheme.title
            })
          });
        },
        response => {
          console.log("GET THEME call in error", response);
          console.log(response);
        },
        () => {
          console.log("The GET THEME observable is now completed.");
        }
      );

      this.store.pipe(
        takeUntil(this.unsubscribeAll),
          select(fromStore.getRenderTemplates)).subscribe(
        (rts: {[key:string]:RenderTemplate}) => {
          if (rts) {
            for (let prop in rts) {
              this.renderTemplates.push({
                value: prop, 
                viewValue:rts[prop].title
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

      this.store.pipe(
          takeUntil(this.unsubscribeAll),
          select(fromStore.getContentAreaLayout)).subscribe(
        (layout: ContentAreaLayout) => {
          this.layout = layout;
        }
      );
      
  }

  /**
    * On destroy
    */
  ngOnDestroy(): void {
    this.unsubscribeAll.next();
    this.unsubscribeAll.complete();
    this.error && this.store.dispatch(new fromStore.ContentAreaLayoutClearError());
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

  removeRow(i: number) {
    this.rows.removeAt(i);
    this.layout.rows.splice(i, 1)
  }

  canRewmoveRow() : boolean {
    return this.rows.length > 1;
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
      return this.layout.sidePane.width > 0 && this.layout.sidePane.left;
  }

  showRightSidePanel() : boolean {
      return this.layout.sidePane.width > 0 && (!this.layout.sidePane.left);
  }

  public addSideViewer() {
   
    const dialogRef = this.dialog.open(RenderTemplateDialog, {
      width: '500px',
      data: {
        renderTemplates: this.renderTemplates,
        selectedRenderTemplate: ''
      }
    });

    dialogRef.afterClosed().subscribe(data => {
      if (data && data.selectedRenderTemplate) {
        this.layout.sidePane.viewers.push({
          renderTemplate: data.selectedRenderTemplate
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
    const dialogRef = this.dialog.open(RenderTemplateDialog, {
      width: '500px',
      data: {
        renderTemplates: this.renderTemplates,
        selectedRenderTemplate: ''
      }
    });

    dialogRef.afterClosed().subscribe(data => {
      if (data && data.selectedRenderTemplate) {
        this.layout.rows[rowNumber].columns[colNumber].viewers.push({
          renderTemplate: data.selectedRenderTemplate
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
    let formValue = this.pageLayoutForm.value;
    this.layout.name = formValue.name;
    this.layout.repository = 'bpwizard';
    this.layout.workspace = 'default';
    this.layout.library = 'design';

    this.store.dispatch(new fromStore.CreateContentAreaLayout(this.layout));
    
    // this.wcmService.createContentAreaLayout(this.layout).subscribe(
    //   (event: any) => {
    //     console.log(event);
    //   },
    //   response => {
    //     console.log("savePageLayout call in error", response);
    //     console.log(response);
    //   },
    //   () => {
    //     console.log("savePageLayout observable is now completed.");
    //   }
    // );
  }

  public publishPageLayout() {
    console.log(this.layout);
  }

  public cancelEditing() {
    console.log(this.layout);
  }
}

@Component({
  selector: 'render-template-dialog',
  templateUrl: 'render-template.dialog.html',
})
export class RenderTemplateDialog {

  constructor(
    public dialogRef: MatDialogRef<RenderTemplateDialog>,
    @Inject(MAT_DIALOG_DATA) public data: {
      renderTemplates: RenderTemplate[],
      selectedRenderTemplate: string}) {}

  onNoClick(): void {
    this.dialogRef.close();
  }
}