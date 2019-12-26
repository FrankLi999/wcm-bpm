import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { FlexLayoutModule } from '@angular/flex-layout';
import { RouterModule, Routes } from '@angular/router';
import { MatButtonModule } from '@angular/material/button';
import { MatDividerModule } from '@angular/material/divider';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatMenuModule } from '@angular/material/menu';
import { MatSelectModule } from '@angular/material/select';
import { MatTableModule } from '@angular/material/table';
import { MatTabsModule } from '@angular/material/tabs';
import { NgxChartsModule } from '@swimlane/ngx-charts';

import { SharedUIModule, SidebarModule, WidgetModule } from 'bpw-common';

import { ProjectDashboardComponent } from './project.component';
import { ProjectDashboardService } from './project.service';

const routes: Routes = [
    {
        path     : '**',
        component: ProjectDashboardComponent,
        resolve  : {
            data: ProjectDashboardService
        }
    }
];

@NgModule({
    declarations: [
        ProjectDashboardComponent
    ],
    imports     : [
        CommonModule,
        FormsModule,
        ReactiveFormsModule,
        FlexLayoutModule,
        RouterModule.forChild(routes),

        MatButtonModule,
        MatDividerModule,
        MatFormFieldModule,
        MatIconModule,
        MatMenuModule,
        MatSelectModule,
        MatTableModule,
        MatTabsModule,

        NgxChartsModule,

        SharedUIModule,
        SidebarModule,
        WidgetModule
    ],
    providers   : [
        ProjectDashboardService
    ]
})
export class ProjectDashboardModule{
}

