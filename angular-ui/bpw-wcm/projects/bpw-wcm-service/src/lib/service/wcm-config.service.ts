import { Injectable } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { Store } from '@ngrx/store';
import {
  Locale,
  UIConfig,
  Navigation,
  UIConfigService,
  NavigationService,
  TranslationLoaderService,
  AppConfigurationState,
  SetNavigation
} from 'bpw-common';

@Injectable({
  providedIn: 'root'
})
export class WcmConfigService {
  constructor(
    private _uiConfigService: UIConfigService,
    private _navigationService: NavigationService,
    private _translationLoaderService: TranslationLoaderService,
    private _translateService: TranslateService,
    private _store: Store<AppConfigurationState>
  ) {  }

  setupConfig(uiConfig: UIConfig, navigation: Navigation[], langs?: string[], locales?: Locale[]) {
    this._uiConfigService.config = {
      ...uiConfig
    };

    const currentNavigation: Navigation[] = (this._navigationService.getCurrentNavigation() as Navigation[]);
    this._navigationService.unregister("main");

    const wcmNavigation = currentNavigation.length > 0 ? [currentNavigation[0], ...navigation] : [...navigation];
    
    this._navigationService.unregister("main");    
    this._store.dispatch(new SetNavigation(wcmNavigation));
    // Register the navigation to the service
    this._navigationService.register('main', wcmNavigation);
  
    // Set the main navigation as our current navigation
    this._navigationService.setCurrentNavigation('main');    
    
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
      this._translationLoaderService.loadTranslations(...locales);
    }
  }
}