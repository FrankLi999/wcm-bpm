import { ModuleWithProviders, NgModule, Optional, SkipSelf } from '@angular/core';

import { WCM_UI_CONFIG } from './services/config.service';

@NgModule()
export class UIConfigModule {
    constructor(@Optional() @SkipSelf() parentModule: UIConfigModule) {
        if ( parentModule ) {
            throw new Error('UIConfigModule is already loaded. Import it in the AppModule only!');
        }
    }

    static forRoot(config): ModuleWithProviders<UIConfigModule> {
        console.log(">>>>>>>>>>>>>>>>>... ui config:", config);
        return {
            ngModule : UIConfigModule,
            providers: [{
                provide : WCM_UI_CONFIG,
                useValue: config
            }]
        };
    }

    static forChild(config): ModuleWithProviders {
        return {
            ngModule : UIConfigModule,
            providers: [{
                provide : WCM_UI_CONFIG,
                useValue: config
            }]
        };
    }
}
