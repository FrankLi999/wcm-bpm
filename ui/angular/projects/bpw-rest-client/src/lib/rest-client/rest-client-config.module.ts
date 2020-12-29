import { NgModule, Optional, SkipSelf, ModuleWithProviders } from '@angular/core';
import { API_CONFIG } from './services/api-config.service';
@NgModule()
export class RestClientConfigModule { 
  constructor(@Optional() @SkipSelf() parentModule: RestClientConfigModule) {
    if ( parentModule ) {
      throw new Error('RestClientModule is already loaded. Import it in the AppModule only!');
    }
  }

  static forRoot(config): ModuleWithProviders<RestClientConfigModule> {
    return {
      ngModule : RestClientConfigModule,
      providers: [{
        provide : API_CONFIG,
        useValue: config
      }]
    };
  }

  static forChild(config): ModuleWithProviders<RestClientConfigModule> {
    return {
      ngModule : RestClientConfigModule,
      providers: [{
        provide : API_CONFIG,
        useValue: config
      }]
    };
  }
}