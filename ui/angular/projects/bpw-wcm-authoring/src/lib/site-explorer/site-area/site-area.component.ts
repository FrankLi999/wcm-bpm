import { Component, OnInit, OnDestroy, Input } from "@angular/core";
import { ActivatedRoute, Router } from "@angular/router";
import { Subscription, Observable, of, Subject } from "rxjs";
import { switchMap, map, filter, takeUntil } from "rxjs/operators";
import { select, Store } from "@ngrx/store";
import cloneDeep from "lodash/cloneDeep";

import { ConfirmationDialogService } from "bpw-common";
import * as fromStore from "bpw-wcm-service";
import {
  JsonForm,
  SiteArea,
  SiteAreaLayout,
  WcmConfigService,
  SiteAreaService,
  WcmConstants,
  ContentAreaLayout,
} from "bpw-wcm-service";
import { FormConfig } from "../../config/form-config";

@Component({
  selector: "site-area",
  templateUrl: "./site-area.component.html",
  styleUrls: ["./site-area.component.scss"],
})
export class SiteAreaComponent implements OnInit, OnDestroy {
  @Input() repository: string;
  @Input() workspace: string;
  @Input() wcmPath: string;
  @Input() editing: boolean = false;
  layout: SiteAreaLayout;
  contentAreaLayouts: { [key: string]: ContentAreaLayout };
  private unsubscribeAll: Subject<any>;
  private currentContentAreaLayout: string;
  sub: Subscription;
  itemForm: JsonForm;
  formConfig = FormConfig;
  selectedFramework = "material-design";
  selectedLanguage = "en";
  formData: any;
  siteArea: SiteArea;
  constructor(
    protected wcmConfigService: WcmConfigService,
    private siteAreaService: SiteAreaService,
    private confirmationDialogService: ConfirmationDialogService,
    private route: ActivatedRoute,
    private store: Store<fromStore.WcmAppState>,
    protected router: Router
  ) {
    this.unsubscribeAll = new Subject<any>();
  }

  ngOnInit() {
    this.sub = this.route.queryParams
      .pipe(
        switchMap((param) => this._getSiteArea(param)),
        // filter((siteArea) => siteArea != null),
        switchMap((siteArea) => this._getAuthoringTemplateForms(siteArea))
      )
      .subscribe((authoringTemplateForms) => {
        this.itemForm = this.editing
          ? authoringTemplateForms[1]
          : authoringTemplateForms[0];
      });
  }
  backToItems() {
    this.router.navigate([WcmConstants.NAV_SA_NAVIGATOR]);
  }

  /**
   * On destroy
   */
  ngOnDestroy(): void {
    this.sub.unsubscribe();
    this.unsubscribeAll.next();
    this.unsubscribeAll.complete();
  }

  submitSiteArea(formData: any) {
    const sa: SiteArea = {
      ...formData,
      repository: this.repository,
      wcmPath: this.wcmPath,
      workspace: this.workspace,
      elements: {
        ...formData.elements,
      },
      properties: {
        ...formData.properties,
      },
      // badge: formData.properties.badge,
      // searchData: formData.properties.searchData,
      // metadata: formData.properties.metadata,
    };
    // delete sa.properties.badge;
    // delete sa.properties.searchData;
    // delete sa.properties.metadata;

    if (this.editing) {
      this.siteAreaService.saveSiteArea(sa).subscribe((event: any) => {
        console.log(event);
      });
    } else {
      this.siteAreaService.createSiteArea(sa).subscribe((event: any) => {
        console.log(event);
      });
    }
  }

  onFormDataChanged(formData: any) {
    if (
      this.siteArea.siteAreaLayout &&
      formData.elements?.contentAreaLayout &&
      formData.elements?.contentAreaLayout !==
        this.siteArea.elements.contentAreaLayout
    ) {
      const confirmDialogRef = this.confirmationDialogService.confirm(
        "Are you sure you want to reset the content area layout?"
      );

      confirmDialogRef.afterClosed().subscribe((result) => {
        if (result) {
          this.siteArea.siteAreaLayout = null;
          this.currentContentAreaLayout = formData.elements.contentAreaLayout;
          this.layout = this._resolveLayoutFromPageLayout();
        } else {
          console.log("leave it as is");
        }
      });
    }
  }

  commitLayout(layout: SiteAreaLayout) {
    this.siteArea = {
      ...this.siteArea,
      siteAreaLayout: layout,
    };
  }

  private _resolveLayoutFromPageLayout(): SiteAreaLayout {
    let contentAreaLayout = this.currentContentAreaLayout
      ? this.contentAreaLayouts[this.currentContentAreaLayout]
      : null;
    return {
      contentWidth: contentAreaLayout ? contentAreaLayout.contentWidth : 100,
      sidePane: contentAreaLayout ? cloneDeep(contentAreaLayout.sidePane) : {},
      rows: contentAreaLayout ? cloneDeep(contentAreaLayout.rows) : [],
    };
  }

  private _getSiteArea(param: any): Observable<SiteArea> {
    this.wcmPath = param.wcmPath;
    this.workspace = param.workspace;
    this.repository = param.repository;
    this.editing = param.editing === "true";
    if (this.editing) {
      return this.siteAreaService.getSiteArea(
        this.repository,
        this.workspace,
        this.wcmPath
      );
    } else {
      return of({
        repository: param.repository,
        workspace: param.workspace,
        name: "site area name",
      });
    }
  }

  private _getAuthoringTemplateForms(
    siteArea: SiteArea
  ): Observable<JsonForm[]> {
    this.siteArea = siteArea;
    if (this.editing) {
      this.currentContentAreaLayout = this.siteArea.elements.contentAreaLayout;
    }
    this.store
      .pipe(
        takeUntil(this.unsubscribeAll),
        select(fromStore.getWcmSystem),
        filter((wcmSystem) => wcmSystem != null)
      )
      .subscribe((wcmSystem) => {
        this.contentAreaLayouts = wcmSystem.contentAreaLayouts;
        this.layout = this._resolveLayoutFromPageLayout();
      });
    this.formData = {
      elements: {
        ...siteArea.elements,
      },
      properties: {
        ...siteArea.properties,
        // metadata: siteArea.metadata,
        // searchData: siteArea.searchData,
        // badge: siteArea.badge,
      },
    };
    return this.store.pipe(
      select(fromStore.getAuthoringTemplateForms),
      map(
        (authoringTemplateForms) =>
          authoringTemplateForms[WcmConstants.WCM_SA_TYPE]
      )
    );
  }
}
