import { Component, OnDestroy, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { TranslateService } from '@ngx-translate/core';
import { Store } from '@ngrx/store';
import cloneDeep from 'lodash-es/cloneDeep';
import { filter } from 'rxjs/operators';
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
import {
  WcmSystem,
  SiteArea,
  WcmService,
  RendererService,
  ContentAreaLayout
} from 'bpw-wcm-service';

declare var appConfig: any;

@Component({
  selector: 'bpw-wcm-renderer',
  templateUrl: './wcm-renderer.component.html',
  styleUrls: ['./wcm-renderer.component.scss']
})
export class WcmRendererComponent implements OnInit, OnDestroy {
  wcmSystem: WcmSystem;
  siteArea: SiteArea;
  layout?: ContentAreaLayout;
  constructor(
    private router: Router,
    private _rendererService: RendererService,
    private _wcmService: WcmService,
    private _fuseConfigService: FuseConfigService,
    private _fuseNavigationService: FuseNavigationService,
    private _fuseSidebarService: FuseSidebarService,
    private _fuseSplashScreenService: FuseSplashScreenService,
    private _fuseTranslationLoaderService: FuseTranslationLoaderService,
    private _translateService: TranslateService,
    private _store: Store<AppConfigurationState>) {
  }

  ngOnInit(): void {    
    
    // if (this.wcmSystem) {
      this._wcmService.getWcmSystem(
        appConfig.repository,
        appConfig.workspace,
        appConfig.library,
        appConfig.siteConfig
        ).pipe(
          filter(wcmSystem => wcmSystem != null)
        ).subscribe(
          wcmSystem => {
            this.prepareRenderingContext(wcmSystem)
            this.wcmSystem = wcmSystem;
          },
          response => {
            console.log("getWcmSystem call ended in error", response);
            console.log(response);
          },
          () => {
            console.log("getWcmSystem observable is now completed.");
          }
        );
    // } else {
    //   this.prepareRenderingContext(this.wcmSystem);
    // }
  }

  ngOnDestroy(): void {
  }

  private prepareRenderingContext(wcmSystem: WcmSystem) {
    
    this._fuseConfigService.config = {
      colorTheme: wcmSystem.siteConfig.colorTheme,
      customScrollbars: wcmSystem.siteConfig.customScrollbars,
      layout: cloneDeep(wcmSystem.siteConfig.layout)
    };
    
    // Get default navigation
    const currentNavigation: FuseNavigation[] = (this._fuseNavigationService.getCurrentNavigation() as FuseNavigation[]);
    this._fuseNavigationService.unregister("main");
    const wcmNavigation = currentNavigation.length > 0 ? 
      [currentNavigation[0], ...wcmSystem.navigations] : [...wcmSystem.navigations];
    this._store.dispatch(new SetNavigation(wcmNavigation));
    // Register the navigation to the service
    this._fuseNavigationService.register('main', wcmNavigation);
    
    // Set the main navigation as our current navigation
    this._fuseNavigationService.setCurrentNavigation('main');    
    
    // Add languages
    if (wcmSystem.langs && wcmSystem.langs.length > 0) {
      this._translateService.addLangs(wcmSystem.langs);

      // Set the default language
      this._translateService.setDefaultLang(wcmSystem.langs[0]);
      
      // Set the navigation translations
      if (wcmSystem.locales) {
        this._fuseTranslationLoaderService.loadTranslations(...wcmSystem.locales);
      }

      // Use a language
      this._translateService.use(wcmSystem.langs[0]);  
    }

    this.siteArea = wcmSystem.siteAreas[this.router.url.replace(/\//g, '~')];
    this._rendererService.clearup();
    this.layout = cloneDeep(wcmSystem.contentAreaLayouts[this.siteArea.contentAreaLayout]||{});
    if (this.siteArea.siteAreaLayout) {
      this.layout.sidePane = cloneDeep(this.siteArea.siteAreaLayout.sidePane);
      this.layout.rows = cloneDeep(this.siteArea.siteAreaLayout.rows);
    }
  }

  leftSidePane(): boolean {
    return this.layout && this.layout.sidePane && this.layout.sidePane.left; 
  }

  rightSidePane(): boolean {
    return this.layout && this.layout.sidePane && (!this.layout.sidePane.left); 
  }
  
  sideRenderId(viewerIndex) {
    return `s_${viewerIndex}`;
  }

  contentRenderId(rowIndex, columnIndex, viewerIndex) {
    return `c_${rowIndex}_${columnIndex}_${viewerIndex}`;
  }

  getSiteAreaKey(siteArea: SiteArea): string {
    return siteArea.url.replace(/\//g, '~');
  }
}