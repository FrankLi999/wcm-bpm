import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { FlexLayoutModule } from '@angular/flex-layout';
import { 
    FuseSharedModule, 
    FuseHighlightModule, 
    FuseWidgetModule
} from 'bpw-components';


import { MaterialModule } from './material.module';
import { EXAMPLE_LIST } from './example-components';
import { AngularMaterialElementsComponent } from './angular-material-elements.component';
import { ExampleViewerComponent } from './example-viewer/example-viewer';

const routes: Routes = [
    {
        path    : '',
        children: [
            {
                path     : ':id',
                component: AngularMaterialElementsComponent
            }
        ]
    }
];

@NgModule({
    declarations   : [
        [...EXAMPLE_LIST],
        AngularMaterialElementsComponent,
        ExampleViewerComponent
    ],
    imports        : [
        CommonModule,
        FormsModule, 
        ReactiveFormsModule,
        FlexLayoutModule,
        RouterModule.forChild(routes),

        MaterialModule,

        FuseSharedModule,
        FuseHighlightModule,
        FuseWidgetModule
    ],
    entryComponents: EXAMPLE_LIST,
})
export class AngularMaterialElementsModule
{
}

