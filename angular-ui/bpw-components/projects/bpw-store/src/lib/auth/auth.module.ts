import { StoreModule } from '@ngrx/store';
import { NgModule } from '@angular/core';
import { EffectsModule } from '@ngrx/effects';
import { authFeatureKey, authReducers} from './store/reducers/auth.reducers';
import { AuthEffects} from './store/effects/auth.effects';
@NgModule({
    imports: [
        StoreModule.forFeature(authFeatureKey, authReducers),
        EffectsModule.forFeature([AuthEffects])
    ]
})
export class AuthModule{}