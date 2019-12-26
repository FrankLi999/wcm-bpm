import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { RouterModule, Routes } from '@angular/router';
import { MatButtonModule } from '@angular/material/button';
import { MatRippleModule } from '@angular/material/core';
import { MatIconModule } from '@angular/material/icon';
import { MatSlideToggleModule } from '@angular/material/slide-toggle';
import { MatTableModule } from '@angular/material/table';

import { SharedUIModule, SidebarModule } from 'bpw-common';

import { FileManagerService } from './file-manager.service';
import { FileManagerComponent } from './file-manager.component';
import { FileManagerDetailsSidebarComponent } from './sidebars/details/details.component';
import { FileManagerFileListComponent } from './file-list/file-list.component';
import { FileManagerMainSidebarComponent } from './sidebars/main/main.component';

const routes: Routes = [
    {
        path     : '**',
        component: FileManagerComponent,
        children : [],
        resolve  : {
            files: FileManagerService
        }
    }
];

@NgModule({
    declarations: [
        FileManagerComponent,
        FileManagerFileListComponent,
        FileManagerMainSidebarComponent,
        FileManagerDetailsSidebarComponent
    ],
    imports     : [
        CommonModule,
        FormsModule, 
        ReactiveFormsModule,
        RouterModule.forChild(routes),

        MatButtonModule,
        MatIconModule,
        MatRippleModule,
        MatSlideToggleModule,
        MatTableModule,

        SharedUIModule,
        SidebarModule
    ],
    providers   : [
        FileManagerService
    ]
})
export class FileManagerModule {
}
