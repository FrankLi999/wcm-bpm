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
import { wcmAuthoringLayoutConfig } from '../config';
import { navigation } from '../navigation/navigation';
import { locale as navigationEnglish } from '../navigation/i18n/en';
import { locale as navigationTurkish } from '../navigation/i18n/tr';

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