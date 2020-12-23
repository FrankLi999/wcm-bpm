import { Component, OnDestroy, OnInit } from "@angular/core";
import { Router, NavigationEnd } from "@angular/router";
import { TranslateService } from "@ngx-translate/core";
import { Store } from "@ngrx/store";
import { filter } from "rxjs/operators";
import {
  Navigation,
  UIConfigService,
  NavigationService,
  SidebarService,
  SplashScreenService,
  TranslationLoaderService,
  AppConfigurationState,
  SetNavigation,
} from "bpw-common";
import { AuthConfigService } from "../../../service/auth-config.service";
import { authLayoutConfig } from "../../../config/auth-layout.config";
import { navigation } from "../../../navigation/navigation";
import { locale as navigationEnglish } from "../../../navigation/i18n/en";
import { locale as navigationTurkish } from "../../../navigation/i18n/tr";
import { appConfig } from "bpw-common";
// declare var appConfig: any;
@Component({
  selector: "wcm-auth",
  templateUrl: "./auth.component.html",
  styleUrls: ["./auth.component.scss"],
})
export class AuthComponent implements OnInit, OnDestroy {
  constructor(
    private _router: Router,
    private _uiConfigService: UIConfigService,
    private _navigationService: NavigationService,
    private _sidebarService: SidebarService,
    private _splashScreenService: SplashScreenService,
    private _translationLoaderService: TranslationLoaderService,
    private _translateService: TranslateService,
    private _store: Store<AppConfigurationState>,
    protected authConfigService: AuthConfigService
  ) {
    this._uiConfigService.config = {
      ...authLayoutConfig,
    };
    this._navigationService.unregister("main");
    const authNavigation = [...navigation];
    this._store.dispatch(new SetNavigation(authNavigation));
    // Register the navigation to the service
    this._navigationService.register("main", authNavigation);
    // Set the main navigation as our current navigation
    this._navigationService.setCurrentNavigation("main");
    // Add languages
    this._translateService.addLangs(["en", "tr"]);
    // Set the default language
    this._translateService.setDefaultLang("en");
    // Set the navigation translations
    this._translationLoaderService.loadTranslations(
      navigationEnglish,
      navigationTurkish
    );
    // Use a language
    // this._translateService.use("en");
    authConfigService.setupConfig(
      authLayoutConfig,
      navigation,
      ["en", "tr"],
      [navigationEnglish, navigationTurkish]
    );
    appConfig.routeSubscriptions.forEach((subscription) => {
      subscription.unsubscribe();
    });
    appConfig.routeSubscriptions.splice(0, appConfig.routeSubscriptions.length);
    appConfig.routeSubscriptions.push(
      this._router.events
        .pipe(filter((e) => e instanceof NavigationEnd))
        .subscribe((event) => {
          authConfigService.setupConfig(
            authLayoutConfig,
            navigation,
            ["en", "tr"],
            [navigationEnglish, navigationTurkish]
          );
        })
    );
  }

  ngOnInit(): void {
    if (this._router.url === "/auth") {
      this._router.navigateByUrl(appConfig.defaultUrl);
    }
  }

  ngOnDestroy(): void {}
}
