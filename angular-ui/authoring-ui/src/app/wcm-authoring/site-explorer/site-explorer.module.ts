import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Routes } from '@angular/router';
import { CdkTableModule } from '@angular/cdk/table';
import { CdkTreeModule } from '@angular/cdk/tree';
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
import { MatTreeModule } from '@angular/material/tree';
import { TranslateModule } from '@ngx-translate/core';
import { FuseSharedModule, FuseSidebarModule } from 'bpw-components';
import {
  JsonSchemaFormModule, 
  MaterialDesignFrameworkModule
} from 'bpw-form';
import { SiteExplorerComponent } from './site-explorer/site-explorer.component';
import { FolderOverviewComponent } from './folder-overview/folder-overview.component';
import * as fromGuards from '../store/guards';
import { AuthGuard } from 'bpw-store';
import { SelectAuthoringTemplateDialogComponent } from './select-authoring-template-dialog/select-authoring-template-dialog.component';
import { AuthoringTemplateSelectorComponent } from './authoring-template-selector/authoring-template-selector.component';
import { NewContentItemComponent } from './new-content-item/new-content-item.component';
import { EditContentItemComponent } from './edit-content-item/edit-content-item.component';
const routes: Routes = [
    {
        path       : 'site-explorer/navigator',
        component  : SiteExplorerComponent,
        canActivate: [AuthGuard, fromGuards.ResolveGuard]
    },
    {
      path       : 'site-explorer/new-content',
      component  : NewContentItemComponent,
      canActivate: [AuthGuard, fromGuards.ResolveGuard]
    },
    {
      path       : 'site-explorer/edit-content',
      component  : EditContentItemComponent,
      canActivate: [AuthGuard, fromGuards.ResolveGuard]
    }
    
  //   {
  //     path       : 'site-explorer/**',
  //     redirectTo: 'site-explorer/navigator'
  // },
];
@NgModule({
  declarations: [
    SiteExplorerComponent,
    FolderOverviewComponent,
    SelectAuthoringTemplateDialogComponent,
    AuthoringTemplateSelectorComponent,
    NewContentItemComponent,
    EditContentItemComponent
  ],
  imports: [
    CommonModule,
    RouterModule.forChild(routes),
    CdkTableModule,
    CdkTreeModule,
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
    MatTableModule,
    MatToolbarModule,
    MatTreeModule,
    
    TranslateModule,

    FuseSharedModule,
    FuseSidebarModule,
    JsonSchemaFormModule, 
    MaterialDesignFrameworkModule
  ],
  entryComponents: [
    SelectAuthoringTemplateDialogComponent
  ]
})
export class SiteExplorerModule { }
