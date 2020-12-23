import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatTableModule } from '@angular/material/table';
import { MatTabsModule } from '@angular/material/tabs';

import { SharedUIModule } from 'bpw-common';

import { SearchClassicComponent } from './search-classic.component';
import { SearchClassicService } from './search-classic.service';


const routes = [
    {
        path     : 'search/classic',
        component: SearchClassicComponent,
        resolve  : {
            search: SearchClassicService
        }
    }
];

@NgModule({
    declarations: [
        SearchClassicComponent,
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
        SearchClassicService
    ]
})
export class SearchClassicModule
{
}
