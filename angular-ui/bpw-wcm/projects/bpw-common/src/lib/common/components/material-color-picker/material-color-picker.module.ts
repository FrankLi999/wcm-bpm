import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FlexLayoutModule } from '@angular/flex-layout';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatMenuModule } from '@angular/material/menu';
import { MatTooltipModule } from '@angular/material/tooltip';

import { UIPipesModule } from '../../pipes/pipes.module';

import { MaterialColorPickerComponent } from './material-color-picker.component';

@NgModule({
    declarations: [
        MaterialColorPickerComponent
    ],
    imports: [
        CommonModule,

        FlexLayoutModule,

        MatButtonModule,
        MatIconModule,
        MatMenuModule,
        MatTooltipModule,

        UIPipesModule
    ],
    exports: [
        MaterialColorPickerComponent
    ],
})
export class MaterialColorPickerModule
{
}
