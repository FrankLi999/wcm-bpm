import { Injectable } from '@angular/core';
import { Actions, Effect, ofType } from '@ngrx/effects';
import { of, Observable } from 'rxjs';
import { switchMap, map, catchError } from 'rxjs/operators';
import { WcmSystemActionTypes, WcmSystemActions } from '../actions/wcm-system.actions';
import { WcmService } from '../../service/wcm.service'; 
import { WcmSystem, RenderTemplate, AuthoringTemplate } from '../../model';
import { 
  GetWcmSystem, 
  GetWcmSystemFailed, 
  GetWcmSystemSuccess, 
  CreateRenderTemplate,  
  CreateRenderTemplateFailed, 
  CreateRenderTemplateSuccess,
  CreateAuthoringTemplate,  
  CreateAuthoringTemplateFailed
} from '../actions/wcm-system.actions';

@Injectable()
export class WcmSystemEffects {

  @Effect()
  getWcmSystem$: Observable<WcmSystemActions> = this.actions$.pipe(
    ofType<GetWcmSystem>(WcmSystemActionTypes.GET_WCMSYSTEM),
    switchMap((action) => {
      return this.wcmService.getWcmSystem(
          action.payload.repository,
          action.payload.workspace,
          action.payload.library,
          action.payload.siteConfig)
        .pipe(
            map((wcmSystem: WcmSystem) => {
              return new GetWcmSystemSuccess(wcmSystem);
            }),
            catchError(err => of(new GetWcmSystemFailed(err)))
        );
      })
  );

  @Effect()
  createRenderTemplate$: Observable<WcmSystemActions> = this.actions$.pipe(
    ofType<CreateRenderTemplate>(WcmSystemActionTypes.CREATE_RENDER_TEMPLATE),
    switchMap((action) => {
      return this.wcmService.createRenderTemplate(action.payload)
        .pipe(
            map((rt: RenderTemplate) => {
                return new CreateRenderTemplateSuccess(rt);
            }),
            catchError(err => of(new CreateRenderTemplateFailed(err)))
        );
      })
  );

  @Effect()
  createAuthoringTemplate$: Observable<WcmSystemActions> = this.actions$.pipe(
    ofType<CreateAuthoringTemplate>(WcmSystemActionTypes.CREATE_AUTHORING_TEMPLATE),
    switchMap((action) => {
      return this.wcmService.createAuthoringTemplate(action.payload)
        .pipe(
            map((at: AuthoringTemplate) => {
                return new GetWcmSystem({
                  repository: 'bpwizard',
                  workspace: 'default',
                  library: 'camunda',
                  siteConfig: 'bpm'
              });
            }),
            catchError(err => of(new CreateAuthoringTemplateFailed(err)))
        );
      })
  );

  constructor(
    private actions$: Actions<WcmSystemActions>,
    private wcmService: WcmService) {}
}
