import { Action } from "@ngrx/store";
import { ValidationRule } from "../../model/ValidationRule";
import { LoadParameters } from "../../model/load-parameters";

export enum ValidationRuleActionTypes {
  LOAD_VALIDATION_RULE = "[ValidationRule] LOAD",
  LOAD_VALIDATION_RULE_SUCCESSFUL = "[ValidationRule] LOAD Successful",
  LOAD_VALIDATION_RULE_FAILED = "[ValidationRule] LOAD FAILED",
  CREATE_VALIDATION_RULE = "[ValidationRule] Create",
  UPDATE_VALIDATION_RULE = "[ValidationRule] Update",
  CREATE_VALIDATION_RULE_SUCCESSFUL = "[ValidationRule] Create Successful",
  UPDATE_VALIDATION_RULE_SUCCESSFUL = "[ValidationRule] Update Successful",
  VALIDATION_RULE_ACTION_FAILED = "[ValidationRule] Action Failed",
  VALIDATION_RULE_CLEAR_ERROR = "[ValidationRule] Clear Error",
  DELETE_VALIDATION_RULE = "[ValidationRule] Remove",
  DELETE_VALIDATION_RULE_SUCCESSFUL = "[ValidationRule] Remove Successful"
}

export class LoadValidationRule implements Action {
  readonly type = ValidationRuleActionTypes.LOAD_VALIDATION_RULE;
  constructor(public payload: LoadParameters) {}
}

export class LoadValidationRuleSuccessful implements Action {
  readonly type = ValidationRuleActionTypes.LOAD_VALIDATION_RULE_SUCCESSFUL;
  constructor(public payload: ValidationRule[]) {}
}

export class LoadValidationRuleFailed implements Action {
  readonly type = ValidationRuleActionTypes.LOAD_VALIDATION_RULE_FAILED;
  constructor(public payload: string) {}
}

export class CreateValidationRule implements Action {
  readonly type = ValidationRuleActionTypes.CREATE_VALIDATION_RULE;
  constructor(public payload: ValidationRule) {}
}

export class UpdateValidationRule implements Action {
  readonly type = ValidationRuleActionTypes.UPDATE_VALIDATION_RULE;
  constructor(public payload: ValidationRule) {}
}

export class CreateValidationRuleSuccessful implements Action {
  readonly type = ValidationRuleActionTypes.CREATE_VALIDATION_RULE_SUCCESSFUL;
  constructor(public payload: ValidationRule) {}
}

export class UpdateValidationRuleSuccessful implements Action {
  readonly type = ValidationRuleActionTypes.UPDATE_VALIDATION_RULE_SUCCESSFUL;
  constructor(public payload: ValidationRule) {}
}

export class ValidationRuleActionFailed implements Action {
  readonly type = ValidationRuleActionTypes.VALIDATION_RULE_ACTION_FAILED;
  constructor(public payload: string) {}
}

export class DeleteValidationRule implements Action {
  readonly type = ValidationRuleActionTypes.DELETE_VALIDATION_RULE;
  constructor(
    public payload: {
      repository: string;
      workspace: string;
      wcmPath: string;
    }
  ) {}
}

export class DeleteValidationRuleSuccessful implements Action {
  readonly type = ValidationRuleActionTypes.DELETE_VALIDATION_RULE_SUCCESSFUL;
  constructor(
    public payload: {
      repository: string;
      workspace: string;
      library: string;
      name: string;
    }
  ) {}
}

export class ValidationRuleClearError implements Action {
  readonly type = ValidationRuleActionTypes.VALIDATION_RULE_CLEAR_ERROR;
  constructor() {}
}

export type ValidationRuleActions =
  | LoadValidationRule
  | LoadValidationRuleSuccessful
  | LoadValidationRuleFailed
  | CreateValidationRule
  | UpdateValidationRule
  | DeleteValidationRule
  | CreateValidationRuleSuccessful
  | UpdateValidationRuleSuccessful
  | ValidationRuleActionFailed
  | DeleteValidationRuleSuccessful
  | ValidationRuleClearError;
