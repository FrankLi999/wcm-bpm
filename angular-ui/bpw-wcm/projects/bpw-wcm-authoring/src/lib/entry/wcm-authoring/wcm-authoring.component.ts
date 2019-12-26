import { Component, OnDestroy, OnInit } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { Store } from '@ngrx/store';
import {
  Navigation,
  UIConfigService,
  NavigationService,
  SidebarService,
  SplashScreenService,
  TranslationLoaderService,
  AppConfigurationState,
  SetNavigation
} from 'bpw-common';
import { wcmAuthoringLayoutConfig } from '../../config/wcm-authoring.config';
import { navigation } from '../../navigation/navigation';
import { locale as navigationEnglish } from '../../navigation/i18n/en';
import { locale as navigationTurkish } from '../../navigation/i18n/tr';
@Component({
  selector: 'bpw-wcm-authoring',
  templateUrl: './wcm-authoring.component.html',
  styleUrls: ['./wcm-authoring.component.css']
})
export class WcmAuthoringComponent implements OnInit, OnDestroy {
  constructor(
      private _uiConfigService: UIConfigService,
      private _navigationService: NavigationService,
      private _sidebarService: SidebarService,
      private _splashScreenService: SplashScreenService,
      private _translationLoaderService: TranslationLoaderService,
      private _translateService: TranslateService,
      private _store: Store<AppConfigurationState>) {
    
    this._uiConfigService.config = {
      ...wcmAuthoringLayoutConfig
    };
    
    // Get default navigation
    const currentNavigation: Navigation[] = (this._navigationService.getCurrentNavigation() as Navigation[]);
    this._navigationService.unregister("main");
    const wcmNavigation = [currentNavigation[0], ...navigation];
    this._store.dispatch(new SetNavigation(wcmNavigation));
    // Register the navigation to the service
    this._navigationService.register('main', wcmNavigation);
    
    // Set the main navigation as our current navigation
    this._navigationService.setCurrentNavigation('main');    
    
    // Add languages
    this._translateService.addLangs(['en', 'tr']);

    // Set the default language
    this._translateService.setDefaultLang('en');

    // Set the navigation translations
    this._translationLoaderService.loadTranslations(navigationEnglish, navigationTurkish);

    // Use a language
    this._translateService.use('en');  
  }
  
  ngOnInit(): void {    
  }

  ngOnDestroy(): void {
  }
}