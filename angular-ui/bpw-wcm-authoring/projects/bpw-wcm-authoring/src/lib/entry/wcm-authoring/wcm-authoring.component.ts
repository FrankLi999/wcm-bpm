import { Component, OnDestroy, OnInit } from '@angular/core';
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
      private _fuseConfigService: FuseConfigService,
      private _fuseNavigationService: FuseNavigationService,
      private _fuseSidebarService: FuseSidebarService,
      private _fuseSplashScreenService: FuseSplashScreenService,
      private _fuseTranslationLoaderService: FuseTranslationLoaderService,
      private _translateService: TranslateService,
      private _store: Store<AppConfigurationState>) {
    
    this._fuseConfigService.config = {
      ...wcmAuthoringLayoutConfig
    };
    
    // Get default navigation
    const currentNavigation: FuseNavigation[] = (this._fuseNavigationService.getCurrentNavigation() as FuseNavigation[]);
    this._fuseNavigationService.unregister("main");
    const wcmNavigation = [currentNavigation[0], ...navigation];
    this._store.dispatch(new SetNavigation(wcmNavigation));
    // Register the navigation to the service
    this._fuseNavigationService.register('main', wcmNavigation);
    
    // Set the main navigation as our current navigation
    this._fuseNavigationService.setCurrentNavigation('main');    
    
    // Add languages
    this._translateService.addLangs(['en', 'tr']);

    // Set the default language
    this._translateService.setDefaultLang('en');

    // Set the navigation translations
    this._fuseTranslationLoaderService.loadTranslations(navigationEnglish, navigationTurkish);

    // Use a language
    this._translateService.use('en');  
  }
  
  ngOnInit(): void {    
  }

  ngOnDestroy(): void {
  }
}