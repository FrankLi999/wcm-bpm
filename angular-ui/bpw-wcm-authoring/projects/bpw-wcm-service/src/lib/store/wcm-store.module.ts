import { StoreModule } from '@ngrx/store';
import { NgModule } from '@angular/core';
import { EffectsModule } from '@ngrx/effects';

import {wcmAppFeatureKey, reducers} from './reducers/wcm-authoring.reducer';
import { ContentAreaLayoutEffects } from './effects/content-area-layout.effects';
import { WcmSystemEffects } from './effects/wcm-system.effects';
@NgModule({
    imports  : [
        StoreModule.forFeature(wcmAppFeatureKey, reducers),
        EffectsModule.forFeature([WcmSystemEffects, ContentAreaLayoutEffects])
    ]
})
export class WcmAppStoreModule {}
