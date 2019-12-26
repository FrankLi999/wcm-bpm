import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { FlexLayoutModule } from '@angular/flex-layout';
import { RouterModule } from '@angular/router';
import { CdkTableModule } from '@angular/cdk/table';
import { CdkTreeModule } from '@angular/cdk/tree';
import { ScrollingModule } from '@angular/cdk/scrolling';
import { MatButtonModule } from '@angular/material/button';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatGridListModule } from '@angular/material/grid-list';
import { MatRippleModule } from '@angular/material/core';
import { MatDialogModule } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatListModule } from '@angular/material/list';
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

import { SharedUIModule, SidebarModule } from 'bpw-common';

import { ContentAreaDesignerComponent } from './content-area-designer/content-area-designer.component';
import { ContentAreaLayoutComponent } from './content-area-layout/content-area-layout.component';
import { ContentAreaLayoutsComponent } from './content-area-layouts/content-area-layouts.component';
import { ContentAreaLayoutTreeComponent } from './content-area-layout-tree/content-area-layout-tree.component';
import { ComponentModule } from '../components/component.module';

@NgModule({
    declarations   : [
        ContentAreaDesignerComponent,
        ContentAreaLayoutComponent,
        ContentAreaLayoutsComponent,

        ContentAreaLayoutTreeComponent
    ],
    imports        : [
        RouterModule,
        CommonModule,
        FormsModule, 
        ReactiveFormsModule,
        FlexLayoutModule,
        MatButtonModule,
        MatCheckboxModule,
        MatChipsModule,
        MatDialogModule,        
        MatExpansionModule,
        MatFormFieldModule,
        MatGridListModule,
        MatIconModule,
        MatInputModule,
        MatListModule,
        MatMenuModule,
        MatRippleModule,
        MatPaginatorModule,
        MatSelectModule,
        MatSortModule,
        MatSnackBarModule,
        MatTabsModule,
        MatTableModule,
        MatToolbarModule,
        MatTreeModule,
        CdkTableModule,        
        CdkTreeModule,
        ScrollingModule,
        TranslateModule,
        SharedUIModule,
        SidebarModule,
        ComponentModule
    ],
    exports : [
        ContentAreaDesignerComponent,
        ContentAreaLayoutComponent,
        ContentAreaLayoutsComponent,
        ContentAreaLayoutTreeComponent
    ]
})
export class ContentAreaLayoutModule { }
