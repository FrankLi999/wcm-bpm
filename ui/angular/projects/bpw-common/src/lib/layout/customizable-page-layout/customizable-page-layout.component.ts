import { Component, Inject, Input, OnDestroy, OnInit } from "@angular/core";
import { DOCUMENT } from "@angular/common";
import { Platform } from "@angular/cdk/platform";
import { TranslateService } from "@ngx-translate/core";
import { Subject } from "rxjs";
import { takeUntil } from "rxjs/operators";
import { Store } from "@ngrx/store";
// import "brace";
// import "brace/mode/html";
// import "brace/theme/github";

import { UIConfigService } from "../../common/services/config.service";
import { NavigationService } from "../../common/components/navigation/navigation.service";
import { SidebarService } from "../../common/components/sidebar/sidebar.service";
import { AppConfigurationState } from "../store/reducers/navigation.reducers";
import { SetNavigation } from "../store/actions/navigation.action";
import { SplashScreenService } from "../../common/services/splash-screen.service";
import {
  TranslationLoaderService,
  Locale,
} from "../../common/services/translation-loader.service";
@Component({
  selector: "customizable-page-layout",
  templateUrl: "./customizable-page-layout.component.html",
  styleUrls: ["./customizable-page-layout.component.scss"],
})
export class CustomizablePageLayoutComponent implements OnInit, OnDestroy {
  uiConfig: any;
  @Input() navigation: any;
  @Input() translations: Locale[];
  @Input() langs: string[];
  // Private
  private _unsubscribeAll: Subject<any>;

  /**
   * Constructor
   *
   * @param DOCUMENT document
   * @param UIConfigService _uiConfigService
   * @param NavigationService _navigationService
   * @param SidebarService _sidebarService
   * @param SplashScreenService _splashScreenService
   * @param TranslationLoaderService _translationLoaderService
   * @param Platform _platform
   * @param TranslateService _translateService
   */
  constructor(
    @Inject(DOCUMENT) private document: any,
    private _uiConfigService: UIConfigService,
    private _navigationService: NavigationService,
    private _sidebarService: SidebarService,
    private _splashScreenService: SplashScreenService,
    private _translationLoaderService: TranslationLoaderService,
    private _translateService: TranslateService,
    private _platform: Platform,
    private _store: Store<AppConfigurationState>
  ) {
    // Set the private defaults
    this._unsubscribeAll = new Subject();
  }

  // -----------------------------------------------------------------------------------------------------
  // @ Lifecycle hooks
  // -----------------------------------------------------------------------------------------------------

  /**
   * On init
   */
  ngOnInit(): void {
    // Get default navigation
    // this.navigation = navigation;
    this._store.dispatch(new SetNavigation(this.navigation));
    // Register the navigation to the service
    this._navigationService.register("main", this.navigation);

    // Set the main navigation as our current navigation
    this._navigationService.setCurrentNavigation("main");

    // Add languages
    this._translateService.addLangs(this.langs);

    // Set the default language
    this._translateService.setDefaultLang("en");

    // Set the navigation translations
    this._translationLoaderService.loadTranslations(...this.translations);

    // Use a language
    this._translateService.use("en");

    /**
     * ----------------------------------------------------------------------------------------------------
     * ngxTranslate Fix Start
     * ----------------------------------------------------------------------------------------------------
     */

    /**
     * If you are using a language other than the default one, i.e. Turkish in this case,
     * you may encounter an issue where some of the components are not actually being
     * translated when your app first initialized.
     *
     * This is related to ngxTranslate module and below there is a temporary fix while we
     * are moving the multi language implementation over to the Angular's core language
     * service.
     **/

    // Set the default language to 'en' and then back to 'tr'.
    // '.use' cannot be used here as ngxTranslate won't switch to a language that's already
    // been selected and there is no way to force it, so we overcome the issue by switching
    // the default language back and forth.
    /**
         setTimeout(() => {
            this._translateService.setDefaultLang('en');
            this._translateService.setDefaultLang('tr');
         });
         */

    /**
     * ----------------------------------------------------------------------------------------------------
     * ngxTranslate Fix End
     * ----------------------------------------------------------------------------------------------------
     */

    // Add is-mobile class to the body if the platform is mobile
    if (this._platform.ANDROID || this._platform.IOS) {
      this.document.body.classList.add("is-mobile");
    }

    // Subscribe to config changes
    this._uiConfigService.config
      .pipe(takeUntil(this._unsubscribeAll))
      .subscribe((config) => {
        this.uiConfig = config;

        // Boxed
        if (this.uiConfig.layout.width === "boxed") {
          this.document.body.classList.add("boxed");
        } else {
          this.document.body.classList.remove("boxed");
        }

        // Color theme - Use normal for loop for IE11 compatibility
        for (let i = 0; i < this.document.body.classList.length; i++) {
          const className = this.document.body.classList[i];

          if (className.startsWith("theme-")) {
            this.document.body.classList.remove(className);
          }
        }

        this.document.body.classList.add(this.uiConfig.colorTheme);
      });
  }

  /**
   * On destroy
   */
  ngOnDestroy(): void {
    // Unsubscribe from all subscriptions
    this._unsubscribeAll.next();
    this._unsubscribeAll.complete();
  }

  // -----------------------------------------------------------------------------------------------------
  // @ Public methods
  // -----------------------------------------------------------------------------------------------------

  /**
   * Toggle sidebar open
   *
   * @param key
   */
  toggleSidebarOpen(key): void {
    this._sidebarService.getSidebar(key).toggleOpen();
  }
}
