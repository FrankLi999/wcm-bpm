import { Injectable } from "@angular/core";
import { Actions, Effect, ofType } from "@ngrx/effects";
import { of, Observable } from "rxjs";
import { switchMap, map, catchError, filter } from "rxjs/operators";
import { SiteConfigService } from "../../service/site-config.service";
import { SiteConfig } from "../../model/SiteConfig";

import {
  SiteConfigActionTypes,
  SiteConfigActions,
  CreateSiteConfig,
  UpdateSiteConfig,
  LoadSiteConfig,
  LoadSiteConfigSuccessful,
  LoadSiteConfigFailed,
  DeleteSiteConfig,
  CreateSiteConfigSuccessful,
  UpdateSiteConfigSuccessful,
  SiteConfigActionFailed,
  DeleteSiteConfigSuccessful
} from "../actions/site-config.actions";
import { WcmUtils } from "../../utils/wcm-utils";

@Injectable()
export class SiteConfigEffects {
  constructor(
    private actions$: Actions<SiteConfigActions>,
    private siteConfigService: SiteConfigService
  ) {}

  @Effect()
  loadSiteConfig$: Observable<SiteConfigActions> = this.actions$.pipe(
    ofType<LoadSiteConfig>(SiteConfigActionTypes.LOAD_SITE_CONFIG),
    switchMap(action => {
      return this.siteConfigService
        .getSiteConfigs(
          action.payload.repository,
          action.payload.workspace,
          action.payload.library
        )
        .pipe(
          filter(resp => resp != null),
          map((items: SiteConfig[]) => {
            return new LoadSiteConfigSuccessful(items);
          }),
          catchError(err => {
            return of(new LoadSiteConfigFailed(err));
          })
        );
    })
  );
  @Effect()
  createSiteConfig$: Observable<SiteConfigActions> = this.actions$.pipe(
    ofType<CreateSiteConfig>(SiteConfigActionTypes.CREATE_SITE_CONFIG),
    switchMap(action => {
      return this.siteConfigService.createSiteConfig(action.payload).pipe(
        filter(resp => resp !== undefined),
        map((response: any) => {
          return new CreateSiteConfigSuccessful(action.payload);
        }),
        catchError(err => {
          return of(new SiteConfigActionFailed(err));
        })
      );
    })
  );
  @Effect()
  updateSiteConfig$: Observable<SiteConfigActions> = this.actions$.pipe(
    ofType<UpdateSiteConfig>(SiteConfigActionTypes.UPDATE_SITE_CONFIG),
    switchMap(action => {
      return this.siteConfigService.saveSiteConfig(action.payload).pipe(
        filter(resp => resp !== undefined),
        map((response: any) => {
          return new UpdateSiteConfigSuccessful(action.payload);
        }),
        catchError(err => {
          return of(new SiteConfigActionFailed(err));
        })
      );
    })
  );
  @Effect()
  deleteSiteConfig$: Observable<SiteConfigActions> = this.actions$.pipe(
    ofType<DeleteSiteConfig>(SiteConfigActionTypes.DELETE_SITE_CONFIG),
    switchMap(action => {
      return this.siteConfigService
        .purgeSiteConfig(
          action.payload.repository,
          action.payload.workspace,
          action.payload.wcmPath
        )
        .pipe(
          filter(resp => resp !== undefined),
          map((response: any) => {
            return new DeleteSiteConfigSuccessful({
              repository: action.payload.repository,
              workspace: action.payload.workspace,
              library: WcmUtils.library(action.payload.wcmPath),
              name: WcmUtils.itemName(action.payload.wcmPath)
            });
          }),
          catchError(err => {
            return of(new SiteConfigActionFailed(err));
          })
        );
    })
  );
}
