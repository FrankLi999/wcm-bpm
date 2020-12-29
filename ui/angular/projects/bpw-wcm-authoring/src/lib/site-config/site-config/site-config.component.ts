import {
  Component,
  OnInit,
  OnDestroy,
  ViewChild,
  ViewContainerRef,
} from "@angular/core";
import { ActivatedRoute, Router } from "@angular/router";
import { select, Store } from "@ngrx/store";
import { Observable, of } from "rxjs";
import { Subject } from "rxjs";
import { takeUntil, switchMap, filter, map, tap } from "rxjs/operators";

import { BlockUIService } from "bpw-common";
import * as fromStore from "bpw-wcm-service";
import {
  SiteConfig,
  JsonForm,
  WcmConstants,
  WcmUtils,
  WCM_ACTION_SUCCESSFUL,
} from "bpw-wcm-service";
import { FormConfig } from "../../config/form-config";

@Component({
  selector: "site-config",
  templateUrl: "./site-config.component.html",
  styleUrls: ["./site-config.component.scss"],
})
export class SiteConfigComponent implements OnInit, OnDestroy {
  editing: boolean = false;
  formConfig = FormConfig;
  formData: any;
  item: SiteConfig;
  itemForm: JsonForm;
  library: string;
  repository: string;
  status$: Observable<any>;
  workspace: string;
  wcmPath: string;

  private blocking: boolean = false;
  @ViewChild("blockUIContainer", { read: ViewContainerRef })
  private blockui: ViewContainerRef;
  private componentRef: any;
  private unsubscribeAll: Subject<any>;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private store: Store<fromStore.WcmAppState>,
    private blockUIService: BlockUIService
  ) {
    this.unsubscribeAll = new Subject();
  }

  ngOnInit() {
    this.route.queryParams
      .pipe(
        takeUntil(this.unsubscribeAll),
        switchMap((param) => this._getItem(param)),
        filter((item) => item != null),
        switchMap((item) => this._getAuthoringTemplateForm(item))
      )
      .subscribe(
        (authoringTemplateForm) => (this.itemForm = authoringTemplateForm)
      );
    this.status$ = this.store.pipe(
      select(fromStore.getSiteConfigStatus),
      takeUntil(this.unsubscribeAll),
      tap((status) => this._handleWcmActionStatus(status))
    );
  }

  /**
   * On destroy
   */
  ngOnDestroy(): void {
    this.unsubscribeAll.next();
    this.unsubscribeAll.complete();
  }

  get successMessage(): string {
    return this.editing
      ? "Successfully updated site config"
      : "Successfully created site config";
  }

  backToItems() {
    this.router.navigate([WcmConstants.NAV_SITE_CONFIG_LIST]);
  }

  upsertItem(formData: any) {
    console.log(formData);
    this.item.name = formData.properties.name;
    this.item.rootSiteArea = formData.elements.rootSiteArea;
    this.item.themeColors = formData.elements.themeColors;
    this.item.customScrollbars = formData.elements.customScrollbars;
    this.item.layout = formData.elements.layout;
    this._createBlockUIComponent(
      this.editing ? "Updating site config" : "Creating site config"
    );
    if (this.editing) {
      this.store.dispatch(new fromStore.UpdateSiteConfig(this.item));
    } else {
      this.store.dispatch(new fromStore.CreateSiteConfig(this.item));
    }
  }

  private _createBlockUIComponent(message: string) {
    this.componentRef = this.blockUIService.createBlockUIComponent(
      message,
      this.blockui
    );
    this.blocking = true;
  }

  private _destroyBlockUIComponent() {
    this.blockUIService.destroyBlockUIComponent(
      this.blockui,
      this.componentRef
    );
    this.blocking = false;
  }

  private _getAuthoringTemplateForm(item: SiteConfig): Observable<JsonForm> {
    this.item = item;
    this.formData = {
      properties: {
        name: item.name,
      },
      elements: {
        rootSiteArea: item.rootSiteArea,
        themeColors: item.themeColors,
        customScrollbars: item.customScrollbars,
        layout: item.layout,
      },
    };
    return this.store.pipe(
      select(fromStore.getAuthoringTemplateForms),
      map((authoringTemplateForms) => {
        return this.editing
          ? authoringTemplateForms[WcmConstants.WCM_SITE_CONFIG_TYPE][1]
          : authoringTemplateForms[WcmConstants.WCM_SITE_CONFIG_TYPE][0];
      })
    );
  }

  private _getItem(param: any): Observable<SiteConfig> {
    this.wcmPath = param.wcmPath;
    this.workspace = param.workspace;
    this.repository = param.repository;
    this.editing = param.editing === "true";
    this.library = param.library;
    if (this.editing) {
      return this.store.pipe(
        select(fromStore.getSiteConfigByLibraryAndName, {
          library: this.library,
          name: WcmUtils.itemName(this.wcmPath),
        }),
        map((item) => {
          return { ...item };
        })
      );
    } else {
      return of({
        repository: param.repository,
        workspace: param.workspace,
        library: this.library,
        rootSiteArea: "",
        name: "",
        themeColors: {
          main: "",
        },
        customScrollbars: false,
        layout: {
          title: "horizontal-layout",
          mode: "fullwidth",
          navbar: {
            primaryBackground: "wcm-light-200",
            secondaryBackground: "wcm-light-300",
            folded: false,
            display: false,
            position: "top",
            variant: "vertical",
          },
          toolbar: {
            customBackgroundColor: false,
            background: "wcm-light-300",
            display: false,
            position: "below",
          },
          footer: {
            customBackgroundColor: true,
            background: "wcm-light-300",
            display: false,
            position: "above-static",
          },
          leftSidePanel: {
            display: false,
          },
          rightSidePanel: {
            display: false,
          },
        },
      });
    }
  }
  private _handleWcmActionStatus(status: any) {
    if (status != null && this.blocking) {
      this._destroyBlockUIComponent();
    }
    if (status === WCM_ACTION_SUCCESSFUL) {
      setTimeout(() => this.backToItems(), 500);
    }
  }
}
