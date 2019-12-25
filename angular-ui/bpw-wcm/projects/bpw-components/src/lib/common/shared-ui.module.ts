import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { FlexLayoutModule } from '@angular/flex-layout';
import { MatSnackBarModule} from '@angular/material/snack-bar';
import { UIDirectivesModule } from './directives/directives';
import { UIPipesModule } from './pipes/pipes.module';

@NgModule({
    imports  : [
        CommonModule,
        FormsModule,
        ReactiveFormsModule,
        FlexLayoutModule,

        UIDirectivesModule,
        UIPipesModule,
        MatSnackBarModule
    ],
    exports  : [
        UIDirectivesModule,
        UIPipesModule
    ]
})
export class SharedUIModule {
}
