import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
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

import { TranslateModule } from '@ngx-translate/core';

import { FuseSharedModule, FuseSidebarModule } from 'bpw-components';
import { WorkflowComponent } from './workflow/workflow.component';
import { WorkflowsComponent } from './workflows/workflows.component';
import { WorkflowTreeComponent } from './workflow-tree/workflow-tree.component';

@NgModule({
  declarations: [WorkflowComponent, WorkflowsComponent, WorkflowTreeComponent],
  imports: [
    CommonModule,
    RouterModule,
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
    TranslateModule,
    FuseSharedModule,
    FuseSidebarModule
  ],
  exports: [WorkflowComponent, WorkflowsComponent, WorkflowTreeComponent]
})
export class WorkflowModule { }
