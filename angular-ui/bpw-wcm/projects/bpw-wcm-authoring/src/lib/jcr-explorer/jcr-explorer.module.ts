import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { FlexLayoutModule } from '@angular/flex-layout';
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

import { SharedUIModule, SidebarModule } from 'bpw-common';
import { JcrExplorerComponent } from './jcr-explorer/jcr-explorer.component';
import { JcrNodeComponent } from './jcr-node/jcr-node.component';
import { RepositoryComponent } from './repository/repository.component';
import { WorkspaceComponent } from './workspace/workspace.component';
import {
  JsonSchemaFormModule
  // MaterialDesignFrameworkModule
} from 'bpw-common';

@NgModule({
  declarations: [
    JcrExplorerComponent, 
    JcrNodeComponent, 
    RepositoryComponent, 
    WorkspaceComponent
  ],
  imports: [
    CommonModule,
    RouterModule,
    FormsModule, 
    ReactiveFormsModule,
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
    // HttpClientModule,
    
    // MaterialDesignFrameworkModule,
    JsonSchemaFormModule,
    // JsonSchemaFormModule.forRoot(
    //   // NoFrameworkModule,
    //   // MaterialDesignFrameworkModule,
    //   // Bootstrap3FrameworkModule,
    //   // Bootstrap4FrameworkModule,
    //   MaterialDesignFrameworkModule
    // ),
    TranslateModule,

    SharedUIModule,
    SidebarModule
  ],
  exports: [
    JcrExplorerComponent, 
    JcrNodeComponent, 
    RepositoryComponent, 
    WorkspaceComponent
  ]
})
export class JcrExplorerModule { }
