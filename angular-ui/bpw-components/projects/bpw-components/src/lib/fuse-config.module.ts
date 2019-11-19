import { ModuleWithProviders, NgModule, Optional, SkipSelf } from '@angular/core';

import { FUSE_CONFIG } from './services/config.service';

@NgModule()
export class FuseConfigModule {
    constructor(@Optional() @SkipSelf() parentModule: FuseConfigModule) {
        if ( parentModule ) {
            throw new Error('FuseModule is already loaded. Import it in the AppModule only!');
        }
    }

    static forRoot(config): ModuleWithProviders<FuseConfigModule> {
        return {
            ngModule : FuseConfigModule,
            providers: [
                {
                    provide : FUSE_CONFIG,
                    useValue: config
                }
            ]
        };
    }

    static forChild(config): ModuleWithProviders {
        return {
            ngModule : FuseConfigModule,
            providers: [
                {
                    provide : FUSE_CONFIG,
                    useValue: config
                }
            ]
        };
    }
}
