import { StoreModule } from '@ngrx/store';
import { NgModule } from '@angular/core';
import { EffectsModule } from '@ngrx/effects';
import { authFeatureKey, authReducers} from './reducers/auth.reducers';
import { AuthEffects} from './effects/auth.effects';
@NgModule({
    imports: [
        StoreModule.forFeature(authFeatureKey, authReducers),
        EffectsModule.forFeature([AuthEffects])
    ]
})
export class AuthStoreModule{}