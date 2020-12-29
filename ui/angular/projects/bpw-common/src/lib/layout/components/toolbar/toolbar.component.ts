import { Component, OnDestroy, OnInit, ViewEncapsulation } from "@angular/core";
import { Router } from "@angular/router";
import { Subject } from "rxjs";
import { filter, takeUntil } from "rxjs/operators";
import { TranslateService } from "@ngx-translate/core";
import { select, Store } from "@ngrx/store";
import * as _ from "lodash";

import { Navigation } from "../../../common/types/navigation";
import { UIConfigService } from "../../../common/services/config.service";
import { SidebarService } from "../../../common/components/sidebar/sidebar.service";

import { AppConfigurationState } from "../../store/reducers/navigation.reducers";
import { getNavigation } from "../../store/selectors/navigation.selectors";
import { LogoutAction } from "../../../auth/store/actions/auth.actions";
import { AuthState } from "../../../auth/store/reducers/auth.reducers";
import { isLoggedIn } from "../../../auth/store/selectors/auth.selectors";
import { UiService } from "../../../auth/store/service/ui.service";
import { appConfig } from "bpw-common";
// declare var appConfig: any;

@Component({
  selector: "toolbar",
  templateUrl: "./toolbar.component.html",
  styleUrls: ["./toolbar.component.scss"],
  encapsulation: ViewEncapsulation.None,
})
export class ToolbarComponent implements OnInit, OnDestroy {
  uiConfig: any;
  horizontalNavbar: boolean;
  rightNavbar: boolean;
  hiddenNavbar: boolean;
  languages: any;
  navigation: Navigation[];
  selectedLanguage: any;
  userStatusOptions: any[];

  // Private
  private _unsubscribeAll: Subject<any>;

  /**
   * Constructor
   *
   * @ param {UIConfigService} _uiConfigService
   * @ param {SidebarService} _sidebarService
   * @ param {TranslateService} _translateService
   */
  constructor(
    private _authStore: Store<AuthState>,
    private _router: Router,
    private _sidebarService: SidebarService,
    private _store: Store<AppConfigurationState>,
    private _translateService: TranslateService,
    private _uiConfigService: UIConfigService,
    private _uiService: UiService
  ) {
    // Set the defaults
    this.userStatusOptions = [
      {
        title: "Online",
        icon: "icon-checkbox-marked-circle",
        color: "#4CAF50",
      },
      {
        title: "Away",
        icon: "icon-clock",
        color: "#FFC107",
      },
      {
        title: "Do not Disturb",
        icon: "icon-minus-circle",
        color: "#F44336",
      },
      {
        title: "Invisible",
        icon: "icon-checkbox-blank-circle-outline",
        color: "#BDBDBD",
      },
      {
        title: "Offline",
        icon: "icon-checkbox-blank-circle-outline",
        color: "#616161",
      },
    ];

    this.languages = [
      {
        id: "en",
        title: "English",
        flag: "us",
      },
      {
        id: "tr",
        title: "Turkish",
        flag: "tr",
      },
    ];

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
    this._store
      .pipe(select(getNavigation), takeUntil(this._unsubscribeAll))
      .subscribe((navigation: Navigation[]) => {
        if (navigation) {
          this.navigation = navigation;
        }
      });

    this._authStore
      .pipe(
        select(isLoggedIn),
        takeUntil(this._unsubscribeAll),
        filter((isLoggedIn) => !isLoggedIn)
      )
      .subscribe((loggedIn: boolean) => {
        this._router.navigateByUrl("/auth/login");
      });

    // Subscribe to the config changes
    this._uiConfigService.config
      .pipe(takeUntil(this._unsubscribeAll))
      .subscribe((config) => {
        this.horizontalNavbar = config.layout.navbar.position === "top";
        this.rightNavbar = config.layout.navbar.position === "right";
        this.hiddenNavbar = config.layout.navbar.display === false;
        this.uiConfig = config;
      });

    // Set the selected language from default languages
    this.selectedLanguage = _.find(this.languages, {
      id: this._translateService.currentLang,
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
   * @ param key
   */
  toggleSidebarOpen(key): void {
    this._sidebarService.getSidebar(key).toggleOpen();
  }

  /**
   * Search
   *
   * @ param value
   */
  search(value): void {
    // Do your search here...
    console.log(value);
  }

  userProfile() {
    this._router.navigateByUrl("/oauth2/profile");
  }
  logout() {
    this._uiService.clearUserProfile();
    this._store.dispatch(new LogoutAction());
  }
  /**
   * Set the language
   *
   * @ param lang
   */
  setLanguage(lang): void {
    // Set the selected language for the toolbar
    this.selectedLanguage = lang;

    // Use the selected language for translations
    this._translateService.use(lang.id);
  }
}
