import { Component, OnInit, OnDestroy, ViewEncapsulation } from "@angular/core";

import { ActivatedRoute } from "@angular/router";
import { ContentAreaLayout, WcmSystem, SiteArea } from "bpw-wcm-service";
import { select, Store } from "@ngrx/store";
import cloneDeep from "lodash/cloneDeep";
import { Subject, Subscription, Observable, of } from "rxjs";
import { takeUntil, switchMap, filter } from "rxjs/operators";
import { WcmAppState, getWcmSystem } from "bpw-wcm-service";
import {
  SiteAreaService,
  PageConfig,
  WcmConfigService,
  SiteConfigService,
  RendererService,
} from "bpw-wcm-service";
import { UIConfig, Navigation } from "bpw-common";

@Component({
  selector: "content-area-renderer",
  templateUrl: "./content-area-renderer.component.html",
  styleUrls: ["./content-area-renderer.component.scss"],
  encapsulation: ViewEncapsulation.None,
})
export class ContentAreaRendererComponent implements OnInit, OnDestroy {
  siteAreaKey: string = "bpm~home";
  siteArea: SiteArea;
  layout?: ContentAreaLayout;
  wcmSystem: WcmSystem;
  sub: Subscription;
  repository: string;
  workspace: string;
  private unsubscribeAll: Subject<any>;

  constructor(
    private route: ActivatedRoute,
    private siteAreaService: SiteAreaService,
    private siteConfigService: SiteConfigService,
    protected wcmConfigService: WcmConfigService,
    private store: Store<WcmAppState>,
    private rendererService: RendererService
  ) {
    this.unsubscribeAll = new Subject<any>();
  }
  ngOnInit() {
    this.rendererService.clearup();
    this.sub = this.route.queryParams
      .pipe(
        switchMap((param) => this._getSiteArea(param)),
        filter((siteArea) => !!siteArea),
        switchMap((siteArea) => {
          this.siteAreaKey = siteArea.elements["url"].replace(/\//g, "~");
          return this.store.pipe(
            takeUntil(this.unsubscribeAll),
            select(getWcmSystem)
          );
        }),
        filter((wcmSystem) => !!wcmSystem),
        switchMap((wcmSystem) => this._getPageConfig(wcmSystem)),
        filter((pageConfig) => !!pageConfig)
      )
      .subscribe(
        (pageConfig: PageConfig) => {
          let uiConfug: UIConfig = {
            colorTheme: pageConfig.siteConfig.themeColors.main,
            customScrollbars: pageConfig.siteConfig.customScrollbars,
            layout: cloneDeep(pageConfig.siteConfig.layout),
          };

          let navigation: Navigation[] = [...pageConfig.navigations];
          console.log(
            "this.wcmConfigService.setupConfig",
            uiConfug,
            navigation
          );
          this.wcmConfigService.setupConfig(uiConfug, navigation);
        },
        (response) => {
          console.log("getApplicationConfig call ended in error", response);
          console.log(response);
        },
        () => {
          console.log("getApplicationConfig observable is now completed.");
        }
      );
  }

  _getPageConfig(wcmSystem: WcmSystem): Observable<PageConfig> {
    this.wcmSystem = wcmSystem;
    this.siteArea = wcmSystem.siteAreas[this.siteAreaKey];
    this.layout = cloneDeep(
      wcmSystem.contentAreaLayouts[
        this.siteArea.elements["contentAreaLayout"]
      ] || {
        name: "",
      }
    );
    if (this.siteArea.siteAreaLayout) {
      this.layout.sidePane = cloneDeep(this.siteArea.siteAreaLayout.sidePane);
      this.layout.rows = cloneDeep(this.siteArea.siteAreaLayout.rows);
    }
    if (this.siteArea.elements["siteConfig"]) {
      return this.siteConfigService.getPageConfig(
        this.wcmSystem.siteConfig.repository,
        this.wcmSystem.siteConfig.workspace,
        this.wcmSystem.siteConfig.library,
        this.siteArea.elements["siteConfig"]
      );
    } else {
      return of({
        siteConfig: this.wcmSystem.siteConfig,
        navigations: this.wcmSystem.navigations,
        langs: null,
        locales: null,
      });
    }
  }

  private _getSiteArea(param: any): Observable<SiteArea> {
    this.repository = param.repository;
    this.workspace = param.workspace;
    return this.siteAreaService.getSiteArea(
      param.repository,
      param.workspace,
      param.wcmPath
    );
  }

  /**
   * On destroy
   */
  ngOnDestroy(): void {
    this.unsubscribeAll.next();
    this.unsubscribeAll.complete();
    this.sub.unsubscribe();
  }

  leftSidePane(): boolean {
    return this.layout && this.layout.sidePane && this.layout.sidePane.left;
  }

  rightSidePane(): boolean {
    return this.layout && this.layout.sidePane && !this.layout.sidePane.left;
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
}
