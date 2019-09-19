import { StoreDevtoolsModule } from '@ngrx/store-devtools';
import { EffectsModule } from '@ngrx/effects';
import { MetaReducer, StoreModule } from '@ngrx/store';
import { NgModule } from '@angular/core';
import { storeFreeze } from 'ngrx-store-freeze';
import { StoreRouterConnectingModule } from '@ngrx/router-store';

import { environment } from 'environments/environment';
import { reducers, effects, RouteSnapshotSerializer } from 'app/store';

export const metaReducers: MetaReducer<any>[] = !environment.production
    ? [storeFreeze]
    : [];

@NgModule({
    imports  : [
        StoreModule.forRoot(reducers, {metaReducers}),
        EffectsModule.forRoot(effects),
        !environment.production ? StoreDevtoolsModule.instrument() : [],
        StoreRouterConnectingModule.forRoot({
            serializer: RouteSnapshotSerializer
            //     navigationActionTiming: NavigationActionTiming.PostActivation,
            //     routerState: RouterState.Full
            
        })
    ] // ,
    // providers: [
    //     {
    //         provide : RouterStateSerializer,
    //         useClass: RouteSnapshotSerializer
    //     }
    // ]
})

export class AppStoreModule
{
}
