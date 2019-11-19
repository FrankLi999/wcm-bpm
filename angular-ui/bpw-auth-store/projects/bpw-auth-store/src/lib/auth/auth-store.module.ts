import { StoreModule } from '@ngrx/store';
import { NgModule } from '@angular/core';
import { EffectsModule } from '@ngrx/effects';
import { MatDialogModule} from '@angular/material/dialog';
import { authFeatureKey, authReducers} from './store/reducers/auth.reducers';
import { AuthEffects} from './store/effects/auth.effects';
import { SimpleDialogComponent } from './service/ui.service';
@NgModule({
    imports: [
        MatDialogModule,
        StoreModule.forFeature(authFeatureKey, authReducers),
        EffectsModule.forFeature([AuthEffects])
    ],
    declarations: [
        SimpleDialogComponent
    ],
    exports  : [
        SimpleDialogComponent
    ],
    entryComponents: [
        SimpleDialogComponent
    ]
})
export class AuthStoreModule{}