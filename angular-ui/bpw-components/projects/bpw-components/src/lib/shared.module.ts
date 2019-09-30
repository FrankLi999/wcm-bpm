import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { FlexLayoutModule } from '@angular/flex-layout';
import { MatSnackBarModule} from '@angular/material/snack-bar';
import { MatDialogModule} from '@angular/material/dialog';
import { FuseDirectivesModule } from './directives/directives';
import { FusePipesModule } from './pipes/pipes.module';
import { SimpleDialogComponent, UiService } from './services/ui.service';

@NgModule({
    imports  : [
        CommonModule,
        FormsModule,
        ReactiveFormsModule,
        FlexLayoutModule,

        FuseDirectivesModule,
        FusePipesModule,
        MatSnackBarModule,
        MatDialogModule,
    ],
    declarations: [
        SimpleDialogComponent
    ],
    exports  : [
        FuseDirectivesModule,
        FusePipesModule,
        SimpleDialogComponent
    ]
})
export class FuseSharedModule {
}
