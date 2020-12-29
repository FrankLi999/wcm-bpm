import { Injectable } from "@angular/core";
import { Actions, Effect, ofType } from "@ngrx/effects";
import { of, Observable } from "rxjs";
import { switchMap, map, catchError, filter } from "rxjs/operators";
import { WcmUtils } from "../../utils/wcm-utils";
import { ContentAreaLayoutService } from "../../service/content-area-layout.service";
import {
  WcmSystemActionTypes,
  WcmSystemActions,
  CreateContentAreaLayout,
  UpdateContentAreaLayout,
  RemoveContentAreaLayout,
  ContentAreaLayoutFailed,
  ContentAreaLayoutSuccessful,
  RemoveContentAreaLayoutSuccessful
} from "../actions/wcm-system.actions";

@Injectable()
export class ContentAreaLayoutEffects {
  @Effect()
  createContentAreaLayout$: Observable<WcmSystemActions> = this.actions$.pipe(
    ofType<CreateContentAreaLayout>(
      WcmSystemActionTypes.CREATE_CONTENT_AREA_LAYOUT
    ),
    switchMap(action => {
      return this.contentAreaLayoutService
        .createContentAreaLayout(action.payload)
        .pipe(
          filter(resp => resp !== undefined),
          map((resp: any) => {
            return new ContentAreaLayoutSuccessful(action.payload);
          }),
          catchError(err => of(new ContentAreaLayoutFailed(err)))
        );
    })
  );
  @Effect()
  updateContentAreaLayout$: Observable<WcmSystemActions> = this.actions$.pipe(
    ofType<UpdateContentAreaLayout>(
      WcmSystemActionTypes.UPDATE_CONTENT_AREA_LAYOUT
    ),
    switchMap(action => {
      return this.contentAreaLayoutService
        .saveContentAreaLayout(action.payload)
        .pipe(
          filter(resp => resp !== undefined),
          map((resp: any) => {
            return new ContentAreaLayoutSuccessful(action.payload);
          }),
          catchError(err => of(new ContentAreaLayoutFailed(err)))
        );
    })
  );
  @Effect()
  deleteContentAreaLayout$: Observable<WcmSystemActions> = this.actions$.pipe(
    ofType<RemoveContentAreaLayout>(
      WcmSystemActionTypes.REMOVE_CONTENT_AREA_LAYOUT
    ),
    switchMap(action => {
      return this.contentAreaLayoutService
        .purgeContentAreaLayout(
          action.payload.repository,
          action.payload.workspace,
          action.payload.wcmPath
        )
        .pipe(
          filter(resp => resp !== undefined),
          map((resp: any) => {
            return new RemoveContentAreaLayoutSuccessful({
              repository: action.payload.repository,
              workspace: action.payload.workspace,
              library: WcmUtils.library(action.payload.wcmPath),
              name: WcmUtils.itemName(action.payload.wcmPath)
            });
          }),
          catchError(err => of(new ContentAreaLayoutFailed(err)))
        );
    })
  );
  constructor(
    private actions$: Actions<WcmSystemActions>,
    private contentAreaLayoutService: ContentAreaLayoutService
  ) {}
}
