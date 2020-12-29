import { StoreModule } from '@ngrx/store';
import { NgModule } from '@angular/core';

import { appFeatureKey, reducers } from './reducers/navigation.reducers';

@NgModule({
    imports  : [
        StoreModule.forFeature(appFeatureKey, reducers)
    ],
    providers: []
})
export class AppConfigStoreModule{}