import { StoreDevtoolsModule } from '@ngrx/store-devtools';
import { EffectsModule } from '@ngrx/effects';
import { MetaReducer, StoreModule } from '@ngrx/store';
import { NgModule } from '@angular/core';
import { storeFreeze } from 'ngrx-store-freeze';
import { StoreRouterConnectingModule } from '@ngrx/router-store';

import { environment } from '../../environments/environment';
import { RouterEffects, RouteSnapshotSerializer } from 'bpw-common';
import { appReducers} from './app.reducer';
export const metaReducers: MetaReducer<any>[] = !environment.production
    ? [storeFreeze]
    : [];

@NgModule({
    imports  : [
        StoreModule.forRoot(appReducers, {metaReducers}),
        EffectsModule.forRoot([RouterEffects]),
        !environment.production ? StoreDevtoolsModule.instrument() : [],
        StoreRouterConnectingModule.forRoot({
            serializer: RouteSnapshotSerializer
        })
    ]
})

export class AppStoreModule {
}