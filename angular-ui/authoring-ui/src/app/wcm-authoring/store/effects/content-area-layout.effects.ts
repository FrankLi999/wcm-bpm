import { Injectable } from '@angular/core';
import { Actions, Effect, ofType } from '@ngrx/effects';
import { of, Observable } from 'rxjs';
import { switchMap, map, catchError } from 'rxjs/operators';
import { ContentAreaLayoutActionTypes, ContentAreaLayoutActions } from '../actions/content-area-layout.actions';
import { WcmService } from '../../service/wcm.service'; 
import { ContentAreaLayout } from '../../model';
import { CreateContentAreaLayout, CreateContentAreaLayoutFailed, CreateContentAreaLayoutSuccess } from '../actions';
@Injectable()
export class ContentAreaLayoutEffects {

  @Effect()
  getContentAreaLayout$: Observable<ContentAreaLayoutActions> = this.actions$.pipe(
    ofType<CreateContentAreaLayout>(ContentAreaLayoutActionTypes.CREATE_CONTENT_AREA_LAYOUT),
    switchMap((action) => {
      return this.wcmService.createContentAreaLayout(
          action.payload)
        .pipe(
            map((response: any) => {
                return new CreateContentAreaLayoutSuccess(action.payload);
            }),
            catchError(err => of(new CreateContentAreaLayoutFailed(err)))
        );
      })
  );

  constructor(
    private actions$: Actions<ContentAreaLayoutActions>,
    private wcmService: WcmService) {}
}
