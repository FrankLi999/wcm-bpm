import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
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

import { FuseSharedModule } from '@fuse/shared.module';
import { FuseSidebarModule } from '@fuse/components';

import { PageDesignerComponent } from './page-designer/page-designer.component';
import { PageLayoutComponent } from './page-layout/page-layout.component';
import { PageLayoutsComponent } from './page-layouts/page-layouts.component';
import { ResourceViewerComponent } from './resource-viewer/resource-viewer.component';

const routes: Routes = [
    {
        path       : 'edit',
        component  : PageDesignerComponent
    },
    {
        path       : 'list',
        component  : PageLayoutsComponent
    },
    {
        path       : '**',
        redirectTo: 'list'
    }
];

@NgModule({
    declarations   : [
        PageDesignerComponent,
        PageLayoutComponent,
        PageLayoutsComponent,
        ResourceViewerComponent
    ],
    imports        : [
        RouterModule.forChild(routes),
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
    providers      : [
    ]
})
export class PageLayoutModule { }
