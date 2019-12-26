import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { FlexLayoutModule } from '@angular/flex-layout';
import { CdkTableModule } from '@angular/cdk/table';
import { CdkTreeModule } from '@angular/cdk/tree';
import { MatButtonModule } from '@angular/material/button';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatRippleModule } from '@angular/material/core';
import { MatDialogModule } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatMenuModule } from '@angular/material/menu';
import { MatSelectModule } from '@angular/material/select';
import { MatToolbarModule } from '@angular/material/toolbar';

import { MatChipsModule } from '@angular/material/chips';
import { MatExpansionModule } from '@angular/material/expansion';
import { MatPaginatorModule } from '@angular/material/paginator';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { MatSortModule } from '@angular/material/sort';
import { MatTableModule } from '@angular/material/table';
import { MatTabsModule } from '@angular/material/tabs';
import { MatTreeModule } from '@angular/material/tree';
import { TranslateModule } from '@ngx-translate/core';
import { DragDropModule } from '@angular/cdk/drag-drop';
import { ScrollingModule } from '@angular/cdk/scrolling'
import { SharedUIModule, SidebarModule } from 'bpw-common';

import { RenderTemplateComponent } from './render-template/render-template.component';
import { AceEditorModule } from 'ng2-ace-editor';
import { SafePipe } from './pipes/safe.pipe';
import { RenderTemplatesComponent } from './render-templates/render-templates.component';
import { RenderLayoutDesignerComponent } from './render-layout-designer/render-layout-designer.component';
import { RenderTemplateTreeComponent } from './render-template-tree/render-template-tree.component';

@NgModule({
  declarations: [
    RenderTemplateComponent,
    SafePipe,
    RenderTemplatesComponent,
    RenderLayoutDesignerComponent,
    RenderTemplateTreeComponent
  ],
  imports: [
    RouterModule,
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    FlexLayoutModule,
    AceEditorModule,
    CdkTableModule,
    CdkTreeModule,
    MatButtonModule,
    MatCheckboxModule,
    MatDialogModule,
    MatFormFieldModule,
    MatIconModule,
    MatInputModule,
    MatMenuModule,
    MatRippleModule,
    MatSelectModule,
    MatToolbarModule,
    MatChipsModule,
    MatExpansionModule,
    MatPaginatorModule,
    MatSortModule,
    MatSnackBarModule,
    MatTableModule,
    MatTabsModule,
    MatTreeModule,
    DragDropModule,
    ScrollingModule,
    TranslateModule,
    SharedUIModule,
    SidebarModule
  ],
  exports: [
    RenderTemplateComponent,
    SafePipe,
    RenderTemplatesComponent,
    RenderLayoutDesignerComponent,
    RenderTemplateTreeComponent
  ]
})
export class RenderTemplateModule { }
