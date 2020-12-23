import { Component, OnDestroy, OnInit, ViewEncapsulation } from "@angular/core";
import { Location } from "@angular/common";
import { Router, NavigationEnd } from "@angular/router";
import { TranslateService } from "@ngx-translate/core";
import cloneDeep from "lodash/cloneDeep";
import { filter } from "rxjs/operators";
import {
  Navigation,
  UIConfigService,
  NavigationService,
  SidebarService,
  SplashScreenService,
  TranslationLoaderService,
} from "bpw-common";

import {
  WcmSystem,
  SiteArea,
  WcmService,
  RendererService,
  ContentAreaLayout,
} from "bpw-wcm-service";

import { appConfig } from "bpw-common";
// declare var appConfig: any;

@Component({
  selector: "wcm-page",
  templateUrl: "./wcm-page.component.html",
  styleUrls: ["./wcm-page.component.scss"],
  encapsulation: ViewEncapsulation.None,
})
export class WcmPageComponent implements OnInit, OnDestroy {
  wcmSystem: WcmSystem;
  siteArea: SiteArea;
  layout?: ContentAreaLayout;
  constructor(
    private router: Router,
    private location: Location,
    private _rendererService: RendererService,
    private _wcmService: WcmService,
    private _uiConfigService: UIConfigService,
    private _navigationService: NavigationService,
    private _sidebarService: SidebarService,
    private _splashScreenService: SplashScreenService,
    private _translationLoaderService: TranslationLoaderService,
    private _translateService: TranslateService
  ) {}

  get repository(): string {
    return appConfig.repository;
  }

  get workspace(): string {
    return appConfig.workspace;
  }

  ngOnInit(): void {
    // if (this.wcmSystem) {
    this.router.events
      .pipe(filter((e) => e instanceof NavigationEnd))
      .subscribe((event) => {
        this._prepareRenderingContext(this.wcmSystem);
      });

    this._setupWcmSite();
  }

  ngOnDestroy(): void {}

  private _setupWcmSite() {
    this._wcmService
      .getWcmSystem(
        appConfig.repository,
        appConfig.workspace,
        appConfig.library,
        appConfig.siteConfig
      )
      .pipe(filter((wcmSystem) => wcmSystem != null))
      .subscribe(
        (wcmSystem) => {
          this.wcmSystem = wcmSystem;
          this._prepareRenderingContext(this.wcmSystem);
        },
        (response) => {
          console.log("getWcmSystem call ended in error", response);
          console.log(response);
        },
        () => {
          console.log("getWcmSystem observable is now completed.");
        }
      );
  }
  private _prepareRenderingContext(wcmSystem: WcmSystem) {
    this._uiConfigService.config = {
      colorTheme: wcmSystem.siteConfig.themeColors.main,
      customScrollbars: wcmSystem.siteConfig.customScrollbars,
      layout: cloneDeep(wcmSystem.siteConfig.layout),
    };

    // Get default navigation
    const currentNavigation: Navigation[] = this._navigationService.getCurrentNavigation() as Navigation[];
    this._navigationService.unregister("main");
    const wcmNavigation =
      currentNavigation.length > 0
        ? [currentNavigation[0], ...wcmSystem.navigations]
        : [...wcmSystem.navigations];
    // this._store.dispatch(new SetNavigation(wcmNavigation));
    // Register the navigation to the service
    this._navigationService.register("main", wcmNavigation);
    // Set the main navigation as our current navigation
    this._navigationService.setCurrentNavigation("main");
    // Add languages
    if (wcmSystem.langs && wcmSystem.langs.length > 0) {
      this._translateService.addLangs(wcmSystem.langs);

      // Set the default language
      this._translateService.setDefaultLang(wcmSystem.langs[0]);

      // Set the navigation translations
      if (wcmSystem.locales) {
        this._translationLoaderService.loadTranslations(...wcmSystem.locales);
      }

      // Use a language
      this._translateService.use(wcmSystem.langs[0]);
    }
    this.siteArea =
      wcmSystem.siteAreas[
        this.router.url.split("?")[0].trim().replace(/\//g, "~")
      ];
    this._rendererService.clearup();
    this.layout = cloneDeep(
      wcmSystem.contentAreaLayouts[
        this.siteArea.elements["contentAreaLayout"]
      ] || { name: "" }
    );
    if (this.siteArea.siteAreaLayout) {
      this.layout.contentWidth = this.siteArea.siteAreaLayout.contentWidth;
      this.layout.sidePane = cloneDeep(this.siteArea.siteAreaLayout.sidePane);
      this.layout.rows = cloneDeep(this.siteArea.siteAreaLayout.rows);
    }
  }

  leftSidePane(): boolean {
    return (
      this.layout &&
      this.layout.sidePane &&
      this.layout.sidePane.left &&
      this.layout.sidePane.width > 0
    );
  }

  rightSidePane(): boolean {
    return (
      this.layout &&
      this.layout.sidePane &&
      !this.layout.sidePane.left &&
      this.layout.sidePane.width > 0
    );
  }

  sideRenderId(viewerIndex) {
    return `s_${viewerIndex}`;
  }

  contentRenderId(rowIndex, columnIndex, viewerIndex) {
    return `c_${rowIndex}_${columnIndex}_${viewerIndex}`;
  }

  getSiteAreaKey(siteArea: SiteArea): string {
    return siteArea.elements["url"].replace(/\//g, "~");
  }

  preloop(rt: string): string {
    return this.wcmSystem.renderTemplates[rt].preloop;
  }

  postloop(rt: string): string {
    return this.wcmSystem.renderTemplates[rt].postloop;
  }

  hasPreloop(rt: string): boolean {
    return !!this.wcmSystem.renderTemplates[rt].preloop;
  }

  hasPostloop(rt: string): boolean {
    return !!this.wcmSystem.renderTemplates[rt].postloop;
  }

  contentPathParam(viewer): string[] {
    let paramName = viewer.contentParameter || "contentPath";
    return this.location.getState()[paramName]
      ? this.location.getState()[paramName].split(",")
      : [];
  }
}
