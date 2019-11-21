import { Injectable } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { Store } from '@ngrx/store';
import {
  Locale,
  FuseConfig,
  FuseNavigation,
  FuseConfigService,
  FuseNavigationService,
  FuseTranslationLoaderService
} from 'bpw-components';

import { 
AppConfigurationState,
SetNavigation
} from 'bpw-layout';

@Injectable({
  providedIn: 'root'
})
export class WcmConfigService {
  constructor(
    private _fuseConfigService: FuseConfigService,
    private _fuseNavigationService: FuseNavigationService,
    private _fuseTranslationLoaderService: FuseTranslationLoaderService,
    private _translateService: TranslateService,
    private _store: Store<AppConfigurationState>
  ) {  }

  setupConfig(fuseConfig: FuseConfig, navigation: FuseNavigation[], langs?: string[], locales?: Locale[]) {
    this._fuseConfigService.config = {
      ...fuseConfig
    };

    const currentNavigation: FuseNavigation[] = (this._fuseNavigationService.getCurrentNavigation() as FuseNavigation[]);
    this._fuseNavigationService.unregister("main");

    const wcmNavigation = currentNavigation.length > 0 ? [currentNavigation[0], ...navigation] : [...navigation];
    
    this._fuseNavigationService.unregister("main");    
    this._store.dispatch(new SetNavigation(wcmNavigation));
    // Register the navigation to the service
    this._fuseNavigationService.register('main', wcmNavigation);
  
    // Set the main navigation as our current navigation
    this._fuseNavigationService.setCurrentNavigation('main');    
    
    if (langs) {
      // Add languages
      this._translateService.addLangs(langs);

      // Set the default language
      this._translateService.setDefaultLang(langs[0]);
      // Use a language
      this._translateService.use(langs[0]);
    }
    if (locales) {
      // Set the navigation translations
      this._fuseTranslationLoaderService.loadTranslations(...locales);
    }
  }
}