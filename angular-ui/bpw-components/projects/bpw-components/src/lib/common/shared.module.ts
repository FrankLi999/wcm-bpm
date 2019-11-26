import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { FlexLayoutModule } from '@angular/flex-layout';
import { MatSnackBarModule} from '@angular/material/snack-bar';
import { FuseDirectivesModule } from './directives/directives';
import { FusePipesModule } from './pipes/pipes.module';

@NgModule({
    imports  : [
        CommonModule,
        FormsModule,
        ReactiveFormsModule,
        FlexLayoutModule,

        FuseDirectivesModule,
        FusePipesModule,
        MatSnackBarModule
    ],
    exports  : [
        FuseDirectivesModule,
        FusePipesModule
    ]
})
export class FuseSharedModule {
}
