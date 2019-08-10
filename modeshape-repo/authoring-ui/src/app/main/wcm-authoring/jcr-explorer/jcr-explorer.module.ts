import { NgModule } from '@angular/core';
import { FlexLayoutModule } from '@angular/flex-layout';
import { HttpClientModule } from '@angular/common/http';
import { RouterModule,  Routes } from '@angular/router';


import { CdkTableModule } from '@angular/cdk/table';
import { CdkTreeModule } from '@angular/cdk/tree';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
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

import { FuseSharedModule } from '@fuse/shared.module';
import { FuseSidebarModule } from '@fuse/components';
import { JcrExplorerComponent } from './jcr-explorer/jcr-explorer.component';
import { JcrNodeComponent } from './jcr-node/jcr-node.component';
import { RepositoryComponent } from './repository/repository.component';
import { WorkspaceComponent } from './workspace/workspace.component';
import { DynamicUiComponent } from './dynamic-ui/dynamic-ui.component';
import {
  JsonSchemaFormModule, 
  MaterialDesignFrameworkModule
} from '../../dynamic-ui';
import { AceEditorDirective } from './dynamic-ui/ace-editor.directive';
const routes: Routes = [
  {
    path     : 'jcr-explorer',
    component: JcrExplorerComponent      
  },
  {
    path     : 'dynamic-ui',
    component: DynamicUiComponent
  }
];

@NgModule({
  declarations: [
    AceEditorDirective, 
    JcrExplorerComponent, 
    JcrNodeComponent, 
    RepositoryComponent, 
    WorkspaceComponent, 
    DynamicUiComponent
  ],
  imports: [
    RouterModule.forChild(routes),
    CdkTableModule,
    CdkTreeModule,
    MatButtonModule,
    MatCardModule,
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
   
    FlexLayoutModule,
    HttpClientModule,
    
    MaterialDesignFrameworkModule,
    JsonSchemaFormModule,
    // JsonSchemaFormModule.forRoot(
    //   // NoFrameworkModule,
    //   // MaterialDesignFrameworkModule,
    //   // Bootstrap3FrameworkModule,
    //   // Bootstrap4FrameworkModule,
    //   MaterialDesignFrameworkModule
    // ),
    TranslateModule,

    FuseSharedModule,
    FuseSidebarModule
  ]
})
export class JcrExplorerModule { }
