import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatTableModule } from '@angular/material/table';
import { MatTabsModule } from '@angular/material/tabs';

import { SharedUIModule } from 'bpw-common';

import { SearchModernComponent } from './search-modern.component';
import { SearchModernService } from './search-modern.service';


const routes = [
    {
        path     : 'search/modern',
        component: SearchModernComponent,
        resolve  : {
            search: SearchModernService
        }
    }
];

@NgModule({
    declarations: [
        SearchModernComponent,
    ],
    imports     : [
        RouterModule.forChild(routes),

        MatButtonModule,
        MatFormFieldModule,
        MatIconModule,
        MatInputModule,
        MatTableModule,
        MatTabsModule,

        SharedUIModule
    ],
    providers   : [
        SearchModernService
    ]
})
export class SearchModernModule
{
}
