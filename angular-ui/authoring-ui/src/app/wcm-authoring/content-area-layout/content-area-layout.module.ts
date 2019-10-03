import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { FlexLayoutModule } from '@angular/flex-layout';
import { RouterModule, Routes } from '@angular/router';
import { CdkTableModule } from '@angular/cdk/table';
import { CdkTreeModule } from '@angular/cdk/tree';
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

import { FuseSharedModule, FuseSidebarModule } from 'bpw-components';

import { ContentAreaDesignerComponent } from './content-area-designer/content-area-designer.component';
import { ContentAreaLayoutComponent } from './content-area-layout/content-area-layout.component';
import { SelectRenderTemplateDialog } from './content-area-layout/select-render-template.dialog';
import { ContentAreaLayoutsComponent } from './content-area-layouts/content-area-layouts.component';
import { ResourceViewerComponent } from './resource-viewer/resource-viewer.component';
import { AuthGuard } from 'bpw-store';
import * as fromGuards from '../store/guards';
import { SelectContentItemDialog } from './/select-content-item-dialog/select-content-item.dialog';
import { ContentSelectorComponent } from './content-selector/content-selector.component';
const routes: Routes = [
    {
        path       : 'content-area-layout/edit',
        component  : ContentAreaDesignerComponent,
        canActivate: [AuthGuard, fromGuards.ResolveGuard] 
    },
    {
        path       : 'content-area-layout/list',
        component  : ContentAreaLayoutsComponent,
        canActivate: [AuthGuard, fromGuards.ResolveGuard] 
    }
];

@NgModule({
    declarations   : [
        ContentAreaDesignerComponent,
        ContentAreaLayoutComponent,
        ContentAreaLayoutsComponent,
        ResourceViewerComponent,
        SelectRenderTemplateDialog,
        SelectContentItemDialog,
        ContentSelectorComponent
    ],
    imports        : [
        RouterModule.forChild(routes),
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
        TranslateModule,
        FuseSharedModule,
        FuseSidebarModule
    ],
    providers      : [
    ],
    entryComponents: [
        SelectRenderTemplateDialog,
        SelectContentItemDialog
    ]
})
export class ContentAreaLayoutModule { }
