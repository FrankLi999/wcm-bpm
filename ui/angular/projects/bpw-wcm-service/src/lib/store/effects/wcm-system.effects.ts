import { Injectable } from "@angular/core";
import { Actions, Effect, ofType } from "@ngrx/effects";
import { of, Observable } from "rxjs";
import { switchMap, map, filter, catchError } from "rxjs/operators";
import { UiService } from "bpw-common";
import {
  WcmSystemActionTypes,
  WcmSystemActions,
} from "../actions/wcm-system.actions";
import { WcmService } from "../../service/wcm.service";
import { RtService } from "../../service/rt.service";
import { AtService } from "../../service/at.service";
import { FormService } from "../../service/form.service";
import { WcmSystem } from "../../model/WcmSystem";
import { WcmUtils } from "../../utils/wcm-utils";

import {
  GetWcmSystem,
  GetWcmSystemFailed,
  GetWcmSystemSuccess,
  CreateRenderTemplate,
  UpdateRenderTemplate,
  DeleteRenderTemplate,
  DeleteRenderTemplateSuccessful,
  RenderTemplateActionFailed,
  RenderTemplateActionSuccessful,
  CreateAuthoringTemplate,
  UpdateAuthoringTemplate,
  DeleteAuthoringTemplate,
  AuthoringTemplateFailed,
  AuthoringTemplateSuccessful,
  DeleteAuthoringTemplateSuccessful,
  CreateForm,
  UpdateForm,
  DeleteForm,
  FormSuccessful,
  FormFailed,
  DeleteFormSuccessful,
} from "../actions/wcm-system.actions";

@Injectable()
export class WcmSystemEffects {
  constructor(
    private actions$: Actions<WcmSystemActions>,
    private atService: AtService,
    private formService: FormService,
    private rtService: RtService,
    private uiService: UiService,
    private wcmService: WcmService
  ) {}

  @Effect()
  getWcmSystemForAuthoring$: Observable<WcmSystemActions> = this.actions$.pipe(
    ofType<GetWcmSystem>(WcmSystemActionTypes.GET_WCMSYSTEM_FOR_AUTHORING),
    switchMap((action) => {
      return this.wcmService
        .getWcmSystemForAuthoring(
          action.payload.repository,
          action.payload.workspace,
          action.payload.library,
          action.payload.siteConfig
        )
        .pipe(
          filter((wcmSystem) => wcmSystem != null),
          map((wcmSystem: WcmSystem) => {
            return new GetWcmSystemSuccess(wcmSystem);
          }),
          catchError((err) => {
            this.uiService.gotoApplicationErrorPage(
              "Failed to load site configuration." +
                this.uiService.getErrorMessage(err)
            );
            return of(new GetWcmSystemFailed(err));
          })
        );
    })
  );

  @Effect()
  getWcmSystem$: Observable<WcmSystemActions> = this.actions$.pipe(
    ofType<GetWcmSystem>(WcmSystemActionTypes.GET_WCMSYSTEM),
    switchMap((action) => {
      return this.wcmService
        .getWcmSystem(
          action.payload.repository,
          action.payload.workspace,
          action.payload.library,
          action.payload.siteConfig
        )
        .pipe(
          filter((wcmSystem) => wcmSystem != null),
          map((wcmSystem: WcmSystem) => {
            return new GetWcmSystemSuccess(wcmSystem);
          }),
          catchError((err) => {
            this.uiService.gotoApplicationErrorPage(
              "Failed to load site configuration." +
                this.uiService.getErrorMessage(err)
            );
            return of(new GetWcmSystemFailed(err));
          })
        );
    })
  );

  @Effect()
  createRenderTemplate$: Observable<WcmSystemActions> = this.actions$.pipe(
    ofType<CreateRenderTemplate>(WcmSystemActionTypes.CREATE_RENDER_TEMPLATE),
    switchMap((action) => {
      return this.rtService.createRenderTemplate(action.payload).pipe(
        filter((resp) => resp !== undefined),
        map((resp: any) => {
          return new RenderTemplateActionSuccessful(action.payload);
        }),
        catchError((err) => of(new RenderTemplateActionFailed(err)))
      );
    })
  );
  @Effect()
  updateRenderTemplate$: Observable<WcmSystemActions> = this.actions$.pipe(
    ofType<UpdateRenderTemplate>(WcmSystemActionTypes.UPDATE_RENDER_TEMPLATE),
    switchMap((action) => {
      return this.rtService.saveRenderTemplate(action.payload).pipe(
        filter((resp) => resp !== undefined),
        map((resp: any) => {
          return new RenderTemplateActionSuccessful(action.payload);
        }),
        catchError((err) => of(new RenderTemplateActionFailed(err)))
      );
    })
  );
  @Effect()
  deleteRenderTemplate$: Observable<WcmSystemActions> = this.actions$.pipe(
    ofType<DeleteRenderTemplate>(WcmSystemActionTypes.DELETE_RENDER_TEMPLATE),
    switchMap((action) => {
      return this.rtService
        .purgeRenderTemplate(
          action.payload.repository,
          action.payload.workspace,
          action.payload.wcmPath
        )
        .pipe(
          filter((resp) => resp !== undefined),
          map((resp: any) => {
            return new DeleteRenderTemplateSuccessful({
              repository: action.payload.repository,
              workspace: action.payload.workspace,
              library: WcmUtils.library(action.payload.wcmPath),
              name: WcmUtils.itemName(action.payload.wcmPath),
            });
          }),
          catchError((err) => of(new RenderTemplateActionFailed(err)))
        );
    })
  );
  @Effect()
  createAuthoringTemplate$: Observable<WcmSystemActions> = this.actions$.pipe(
    ofType<CreateAuthoringTemplate>(
      WcmSystemActionTypes.CREATE_AUTHORING_TEMPLATE
    ),
    switchMap((action) => {
      return this.atService.createAuthoringTemplate(action.payload).pipe(
        filter((resp) => resp !== undefined),
        map((response: any) => {
          return new AuthoringTemplateSuccessful(action.payload);
        }),
        catchError((err) => {
          return of(new AuthoringTemplateFailed(err));
        })
      );
    })
  );
  @Effect()
  updateAuthoringTemplate$: Observable<WcmSystemActions> = this.actions$.pipe(
    ofType<UpdateAuthoringTemplate>(
      WcmSystemActionTypes.UPDATE_AUTHORING_TEMPLATE
    ),
    switchMap((action) => {
      return this.atService.saveAuthoringTemplate(action.payload).pipe(
        filter((resp) => resp !== undefined),
        map((response: any) => new AuthoringTemplateSuccessful(action.payload)),
        catchError((err) => of(new AuthoringTemplateFailed(err)))
      );
    })
  );

  @Effect()
  deleteAuthoringTemplate$: Observable<WcmSystemActions> = this.actions$.pipe(
    ofType<DeleteAuthoringTemplate>(
      WcmSystemActionTypes.DELETE_AUTHORING_TEMPLATE
    ),
    switchMap((action) => {
      return this.atService
        .purgeAuthoringTemplate(
          action.payload.repository,
          action.payload.workspace,
          action.payload.wcmPath
        )
        .pipe(
          filter((resp) => resp !== undefined),
          map((response: any) => {
            return new DeleteAuthoringTemplateSuccessful({
              repository: action.payload.repository,
              workspace: action.payload.workspace,
              library: WcmUtils.library(action.payload.wcmPath),
              name: WcmUtils.itemName(action.payload.wcmPath),
            });
          }),
          catchError((err) => of(new AuthoringTemplateFailed(err)))
        );
    })
  );
  @Effect()
  createForm$: Observable<WcmSystemActions> = this.actions$.pipe(
    ofType<CreateForm>(WcmSystemActionTypes.CREATE_FORM),
    switchMap((action) => {
      return this.formService.createForm(action.payload).pipe(
        filter((resp) => resp !== undefined),
        map((response: any) => {
          return new FormSuccessful(action.payload);
        }),
        catchError((err) => {
          return of(new FormFailed(err));
        })
      );
    })
  );
  @Effect()
  updateForm$: Observable<WcmSystemActions> = this.actions$.pipe(
    ofType<UpdateForm>(WcmSystemActionTypes.UPDATE_FORM),
    switchMap((action) => {
      return this.formService.saveForm(action.payload).pipe(
        filter((resp) => resp !== undefined),
        map((response: any) => new FormSuccessful(action.payload)),
        catchError((err) => of(new FormFailed(err)))
      );
    })
  );
  @Effect()
  deleteForm$: Observable<WcmSystemActions> = this.actions$.pipe(
    ofType<DeleteForm>(WcmSystemActionTypes.DELETE_FORM),
    switchMap((action) => {
      return this.formService
        .purgeForm(
          action.payload.repository,
          action.payload.workspace,
          action.payload.wcmPath
        )
        .pipe(
          filter((resp) => resp !== undefined),
          map((response: any) => {
            return new DeleteFormSuccessful({
              repository: action.payload.repository,
              workspace: action.payload.workspace,
              library: WcmUtils.library(action.payload.wcmPath),
              name: WcmUtils.itemName(action.payload.wcmPath),
            });
          }),
          catchError((err) => of(new FormFailed(err)))
        );
    })
  );
}
