import { StoreModule } from '@ngrx/store';
import { NgModule } from '@angular/core';
import { EffectsModule } from '@ngrx/effects';

import * as wcmApp from './reducers';
import { WcmSystemEffects, ContentAreaLayoutEffects } from './effects';

@NgModule({
    imports  : [
        StoreModule.forFeature(wcmApp.wcmAppFeatureKey, wcmApp.reducers),
        EffectsModule.forFeature([WcmSystemEffects, ContentAreaLayoutEffects])
    ],
    providers: []
})
export class WcmAppStoreModule{}
