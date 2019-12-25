import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { FlexLayoutModule } from '@angular/flex-layout';
import { CdkTableModule } from '@angular/cdk/table';
import { CdkTreeModule } from '@angular/cdk/tree';
import { ScrollingModule } from '@angular/cdk/scrolling';
import { MatButtonModule } from '@angular/material/button';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatRippleModule } from '@angular/material/core';
import { MatDialogModule } from '@angular/material/dialog';
import { MatExpansionModule } from '@angular/material/expansion';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatGridListModule } from '@angular/material/grid-list';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatMenuModule } from '@angular/material/menu';
import { MatPaginatorModule } from '@angular/material/paginator';
import { MatSelectModule } from '@angular/material/select';
import { MatSortModule } from '@angular/material/sort';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatTableModule } from '@angular/material/table';
import { MatTabsModule } from '@angular/material/tabs';
import { MatTreeModule } from '@angular/material/tree';
import { TranslateModule } from '@ngx-translate/core';
import { SharedUIModule, SidebarModule } from 'bpw-components';
import {
  JsonSchemaFormModule 
  // MaterialDesignFrameworkModule
} from 'bpw-components';
import { SiteExplorerComponent } from './site-explorer/site-explorer.component';
import { FolderOverviewComponent } from './folder-overview/folder-overview.component';
import { SelectAuthoringTemplateDialogComponent } from './select-authoring-template-dialog/select-authoring-template-dialog.component';
import { AuthoringTemplateSelectorComponent } from './authoring-template-selector/authoring-template-selector.component';
import { ContentItemComponent } from './content-item/content-item.component';
import { SiteAreaComponent } from './site-area/site-area.component';
import { SiteAreaLayoutComponent } from './site-area-layout/site-area-layout.component';
import { SiteTreeComponent } from './site-tree/site-tree.component';
import { ComponentModule } from '../components/component.module';

@NgModule({
  declarations: [
    SiteExplorerComponent,
    FolderOverviewComponent,
    SelectAuthoringTemplateDialogComponent,
    AuthoringTemplateSelectorComponent,
    ContentItemComponent,
    SiteAreaComponent,
    SiteAreaLayoutComponent,
    SiteTreeComponent
  ],
  imports: [
    CommonModule,
    RouterModule,
    FlexLayoutModule,
    CdkTableModule,
    CdkTreeModule,
    ScrollingModule,
    MatButtonModule,
    MatCheckboxModule,
    MatDialogModule,
    MatExpansionModule,
    MatFormFieldModule,
    MatGridListModule,
    MatIconModule,
    MatInputModule,
    MatMenuModule,
    MatPaginatorModule,
    MatRippleModule,
    MatSelectModule,
    MatSortModule,
    MatTabsModule,
    MatTableModule,
    MatToolbarModule,
    MatTreeModule,
    
    TranslateModule,

    SharedUIModule,
    SidebarModule,
    JsonSchemaFormModule, 
    // MaterialDesignFrameworkModule,
    ComponentModule
  ],
  exports: [
    SiteExplorerComponent,
    FolderOverviewComponent,
    SelectAuthoringTemplateDialogComponent,
    AuthoringTemplateSelectorComponent,
    ContentItemComponent,
    SiteAreaComponent,
    SiteAreaLayoutComponent,
    SiteTreeComponent
  ],
  entryComponents: [
    SelectAuthoringTemplateDialogComponent
  ]
})
export class SiteExplorerModule { }
