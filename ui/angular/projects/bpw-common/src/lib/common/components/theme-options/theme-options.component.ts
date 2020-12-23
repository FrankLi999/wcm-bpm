import {
  Component,
  HostBinding,
  Inject,
  OnDestroy,
  OnInit,
  Renderer2,
  ViewEncapsulation,
} from "@angular/core";
import { FormBuilder, FormControl, FormGroup } from "@angular/forms";
import { DOCUMENT } from "@angular/common";
import { Subject } from "rxjs";
import { takeUntil } from "rxjs/operators";

import { wcmAnimations } from "../../animations/wcm-animations";
import { UIConfigService } from "../../services/config.service";
import { NavigationService } from "../navigation/navigation.service";
import { SidebarService } from "../sidebar/sidebar.service";

@Component({
  selector: "theme-options",
  templateUrl: "./theme-options.component.html",
  styleUrls: ["./theme-options.component.scss"],
  encapsulation: ViewEncapsulation.None,
  animations: wcmAnimations,
})
export class ThemeOptionsComponent implements OnInit, OnDestroy {
  uiConfig: any;
  form: FormGroup;

  @HostBinding("class.bar-closed")
  barClosed: boolean;

  // Private
  private _unsubscribeAll: Subject<any>;

  /**
   * Constructor
   *
   * @ param {DOCUMENT} document
   * @ param {FormBuilder} _formBuilder
   * @ param {UIConfigService} _uiConfigService
   * @ param {NavigationService} _navigationService
   * @ param {SidebarService} _sidebarService
   * @ param {Renderer2} _renderer
   */
  constructor(
    @Inject(DOCUMENT) private document: any,
    private _formBuilder: FormBuilder,
    private _uiConfigService: UIConfigService,
    private _navigationService: NavigationService,
    private _sidebarService: SidebarService,
    private _renderer: Renderer2
  ) {
    // Set the defaults
    this.barClosed = true;

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
    // Build the config form
    // noinspection TypeScriptValidateTypes
    this.form = this._formBuilder.group({
      colorTheme: new FormControl(),
      customScrollbars: new FormControl(),
      layout: this._formBuilder.group({
        title: new FormControl(),
        mode: new FormControl(),
        navbar: this._formBuilder.group({
          primaryBackground: new FormControl(),
          secondaryBackground: new FormControl(),
          folded: new FormControl(),
          display: new FormControl(true),
          position: new FormControl(),
          variant: new FormControl(),
        }),
        toolbar: this._formBuilder.group({
          background: new FormControl(),
          customBackgroundColor: new FormControl(),
          display: new FormControl(true),
          position: new FormControl(),
        }),
        footer: this._formBuilder.group({
          background: new FormControl(),
          customBackgroundColor: new FormControl(),
          display: new FormControl(true),
          position: new FormControl(),
        }),
        leftSidePanel: this._formBuilder.group({
          display: new FormControl(false),
        }),
        rightSidePanel: this._formBuilder.group({
          display: new FormControl(true),
        }),
      }),
    });
    console.log(">>>>>>>>>>>> 1");
    // Subscribe to the config changes
    this._uiConfigService.config
      .pipe(takeUntil(this._unsubscribeAll))
      .subscribe((config) => {
        // Update the stored config
        this.uiConfig = config;
        console.log(">>>>>>>>>>>> 2,", this.uiConfig);
        // Set the config form values without emitting an event
        // so that we don't end up with an infinite loop
        this.form.setValue(config, { emitEvent: false });
      });

    // Subscribe to the specific form value changes (layout.title)
    this.form
      .get("layout.title")
      .valueChanges.pipe(takeUntil(this._unsubscribeAll))
      .subscribe((value) => {
        // Reset the form values based on the
        // selected layout style
        this._resetFormValues(value);
      });

    // Subscribe to the form value changes
    this.form.valueChanges
      .pipe(takeUntil(this._unsubscribeAll))
      .subscribe((config) => {
        // Update the config
        this._uiConfigService.config = config;
      });

    //TODO: utilize ngrx mechanism to add new navigation item
    // // Add customize nav item that opens the bar programmatically
    // const customFunctionNavItem = {
    //     id      : 'custom-function',
    //     title   : 'Custom Function',
    //     type    : 'group',
    //     icon    : 'settings',
    //     children: [
    //         {
    //             id      : 'customize',
    //             title   : 'Customize',
    //             type    : 'item',
    //             icon    : 'settings',
    //             function: () => {
    //                 this.toggleSidebarOpen('themeOptionsPanel');
    //             }
    //         }
    //     ]
    // };

    // this._navigationService.addNavigationItem(customFunctionNavItem, 'end');
  }

  /**
   * On destroy
   */
  ngOnDestroy(): void {
    // Unsubscribe from all subscriptions
    this._unsubscribeAll.next();
    this._unsubscribeAll.complete();

    // Remove the custom function menu
    this._navigationService.removeNavigationItem("custom-function");
  }

  // -----------------------------------------------------------------------------------------------------
  // @ Private methods
  // -----------------------------------------------------------------------------------------------------

  /**
   * Reset the form values based on the
   * selected layout style
   *
   * @ param value
   * @ private
   */
  private _resetFormValues(value): void {
    switch (value) {
      // Vertical Layout #1
      case "vertical-layout-1": {
        this.form.patchValue({
          layout: {
            mode: "fullwidth",
            navbar: {
              primaryBackground: "wcm-light-200",
              secondaryBackground: "wcm-light-300",
              folded: false,
              display: true,
              position: "left",
              variant: "vertical",
            },
            toolbar: {
              background: "wcm-light-300",
              customBackgroundColor: false,
              display: true,
              position: "below-static",
            },
            footer: {
              background: "wcm-light-300",
              customBackgroundColor: true,
              display: true,
              position: "below-static",
            },
            leftSidePanel: {
              display: false,
            },
            rightSidePanel: {
              display: true,
            },
          },
        });

        break;
      }

      // Vertical Layout #2
      case "vertical-layout-2": {
        this.form.patchValue({
          layout: {
            mode: "fullwidth",
            navbar: {
              primaryBackground: "wcm-light-200",
              secondaryBackground: "wcm-light-300",
              folded: false,
              display: true,
              position: "left",
              variant: "vertical",
            },
            toolbar: {
              background: "wcm-light-300",
              customBackgroundColor: false,
              display: true,
              position: "below",
            },
            footer: {
              background: "wcm-light-300",
              customBackgroundColor: true,
              display: true,
              position: "below",
            },
            leftSidePanel: {
              display: false,
            },
            rightSidePanel: {
              display: true,
            },
          },
        });

        break;
      }

      // Vertical Layout #3
      case "vertical-layout-3": {
        this.form.patchValue({
          layout: {
            mode: "fullwidth",
            navbar: {
              primaryBackground: "wcm-light-200",
              secondaryBackground: "wcm-light-300",
              folded: false,
              display: true,
              position: "left",
              layout: "vertical",
            },
            toolbar: {
              background: "wcm-light-300",
              customBackgroundColor: false,
              display: true,
              position: "above-static",
            },
            footer: {
              background: "wcm-light-300",
              customBackgroundColor: true,
              display: true,
              position: "above-static",
            },
            leftSidePanel: {
              display: false,
            },
            rightSidePanel: {
              display: true,
            },
          },
        });

        break;
      }

      // Horizontal Layout #1
      case "horizontal-layout": {
        this.form.patchValue({
          layout: {
            mode: "fullwidth",
            navbar: {
              primaryBackground: "wcm-light-200",
              secondaryBackground: "wcm-light-300",
              folded: false,
              display: true,
              position: "top",
              variant: "vertical",
            },
            toolbar: {
              background: "wcm-light-300",
              customBackgroundColor: false,
              display: true,
              position: "above",
            },
            footer: {
              background: "wcm-light-300",
              customBackgroundColor: true,
              display: true,
              position: "above-fixed",
            },
            leftSidePanel: {
              display: false,
            },
            rightSidePanel: {
              display: true,
            },
          },
        });

        break;
      }
    }
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
}
