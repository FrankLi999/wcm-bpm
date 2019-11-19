import { Injectable } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { Store } from '@ngrx/store';
import {
  FuseNavigation,
  FuseConfigService,
  FuseNavigationService,
  FuseSidebarService,
  FuseSplashScreenService,
  FuseTranslationLoaderService
} from 'bpw-components';

import { 
AppConfigurationState,
SetNavigation
} from 'bpw-layout';
import { wcmAuthoringLayoutConfig } from './wcm-authoring.config';

@Injectable({
  providedIn: 'root'
})
export class WcmConfigService {
  constructor(
    private _fuseConfigService: FuseConfigService,
    private _fuseNavigationService: FuseNavigationService,
    private _fuseSidebarService: FuseSidebarService,
    private _fuseSplashScreenService: FuseSplashScreenService,
    private _fuseTranslationLoaderService: FuseTranslationLoaderService,
    private _translateService: TranslateService,
    private _store: Store<AppConfigurationState>
  ) {  }

  setupConfig() {
    this._fuseConfigService.config = {
      ...wcmAuthoringLayoutConfig
    };
  }
}