import { Injectable } from "@angular/core";
import { Actions, Effect, ofType } from "@ngrx/effects";
import { of, Observable } from "rxjs";
import { switchMap, map, catchError, filter } from "rxjs/operators";
import { ValidationRuleService } from "../../service/validation-rule.service";
import { ValidationRule } from "../../model/ValidationRule";

import {
  ValidationRuleActionTypes,
  ValidationRuleActions,
  CreateValidationRule,
  UpdateValidationRule,
  LoadValidationRule,
  LoadValidationRuleSuccessful,
  LoadValidationRuleFailed,
  DeleteValidationRule,
  CreateValidationRuleSuccessful,
  UpdateValidationRuleSuccessful,
  ValidationRuleActionFailed,
  DeleteValidationRuleSuccessful
} from "../actions/validation-rule.actions";
import { WcmUtils } from "../../utils/wcm-utils";

@Injectable()
export class ValidationRuleEffects {
  constructor(
    private actions$: Actions<ValidationRuleActions>,
    private validationRuleService: ValidationRuleService
  ) {}

  @Effect()
  loadValidationRule$: Observable<ValidationRuleActions> = this.actions$.pipe(
    ofType<LoadValidationRule>(ValidationRuleActionTypes.LOAD_VALIDATION_RULE),
    switchMap(action => {
      return this.validationRuleService
        .loadValidationRules(
          action.payload.repository,
          action.payload.workspace,
          action.payload.filter,
          action.payload.sortDirection,
          action.payload.pageIndex,
          action.payload.pageSize
        )
        .pipe(
          filter(resp => resp != null),
          map((items: ValidationRule[]) => {
            return new LoadValidationRuleSuccessful(items);
          }),
          catchError(err => {
            return of(new LoadValidationRuleFailed(err));
          })
        );
    })
  );
  @Effect()
  createValidationRule$: Observable<ValidationRuleActions> = this.actions$.pipe(
    ofType<CreateValidationRule>(
      ValidationRuleActionTypes.CREATE_VALIDATION_RULE
    ),
    switchMap(action => {
      return this.validationRuleService
        .createValidationRule(action.payload)
        .pipe(
          filter(resp => resp !== undefined),
          map((response: any) => {
            return new CreateValidationRuleSuccessful(action.payload);
          }),
          catchError(err => {
            return of(new ValidationRuleActionFailed(err));
          })
        );
    })
  );
  @Effect()
  updateValidationRule$: Observable<ValidationRuleActions> = this.actions$.pipe(
    ofType<UpdateValidationRule>(
      ValidationRuleActionTypes.UPDATE_VALIDATION_RULE
    ),
    switchMap(action => {
      return this.validationRuleService.saveValidationRule(action.payload).pipe(
        filter(resp => resp !== undefined),
        map((response: any) => {
          return new UpdateValidationRuleSuccessful(action.payload);
        }),
        catchError(err => {
          return of(new ValidationRuleActionFailed(err));
        })
      );
    })
  );
  @Effect()
  deleteValidationRule$: Observable<ValidationRuleActions> = this.actions$.pipe(
    ofType<DeleteValidationRule>(
      ValidationRuleActionTypes.DELETE_VALIDATION_RULE
    ),
    switchMap(action => {
      return this.validationRuleService
        .purgeValidationRule(
          action.payload.repository,
          action.payload.workspace,
          action.payload.wcmPath
        )
        .pipe(
          filter(resp => resp !== undefined),
          map((response: any) => {
            return new DeleteValidationRuleSuccessful({
              repository: action.payload.repository,
              workspace: action.payload.workspace,
              library: WcmUtils.library(action.payload.wcmPath),
              name: WcmUtils.itemName(action.payload.wcmPath)
            });
          }),
          catchError(err => {
            return of(new ValidationRuleActionFailed(err));
          })
        );
    })
  );
}
