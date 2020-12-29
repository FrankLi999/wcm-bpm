import { EntityState, EntityAdapter, createEntityAdapter } from "@ngrx/entity";
import {
  ValidationRuleActionTypes,
  ValidationRuleActions
} from "../actions/validation-rule.actions";
import { ValidationRule } from "../../model/ValidationRule";
import {
  WCM_ACTION_SUCCESSFUL,
  WCM_LOAD_SUCCESSFUL
} from "../../model/wcm-response";

export interface ValidationRuleState extends EntityState<ValidationRule> {
  status?: any;
  loading: boolean;
  total: number;
}
export const validationRuleId = (item: ValidationRule) =>
  `${item.library}_${item.name}`;

export const ValidationRuleAdapter: EntityAdapter<ValidationRule> = createEntityAdapter<
  ValidationRule
>({
  selectId: rule => validationRuleId(rule)
});

export const ValidationRuleInitialState: ValidationRuleState = ValidationRuleAdapter.getInitialState(
  {
    status: null,
    loading: true,
    total: 0
  }
);

export function ValidationRuleReducer(
  state = ValidationRuleInitialState,
  action: ValidationRuleActions
): ValidationRuleState {
  switch (action.type) {
    case ValidationRuleActionTypes.LOAD_VALIDATION_RULE:
    case ValidationRuleActionTypes.CREATE_VALIDATION_RULE:
    case ValidationRuleActionTypes.UPDATE_VALIDATION_RULE:
    case ValidationRuleActionTypes.DELETE_VALIDATION_RULE: {
      return {
        ...state,
        status: null
      };
    }
    case ValidationRuleActionTypes.LOAD_VALIDATION_RULE_SUCCESSFUL: {
      return ValidationRuleAdapter.addAll(action.payload, {
        ...state,
        status: WCM_LOAD_SUCCESSFUL,
        loading: false
      });
    }
    case ValidationRuleActionTypes.CREATE_VALIDATION_RULE_SUCCESSFUL: {
      return ValidationRuleAdapter.addOne(action.payload, {
        ...state,
        status: WCM_ACTION_SUCCESSFUL,
        loading: false
      });
    }
    case ValidationRuleActionTypes.UPDATE_VALIDATION_RULE_SUCCESSFUL: {
      return ValidationRuleAdapter.upsertOne(action.payload, {
        ...state,
        status: WCM_ACTION_SUCCESSFUL,
        loading: false
      });
    }
    case ValidationRuleActionTypes.DELETE_VALIDATION_RULE_SUCCESSFUL: {
      return ValidationRuleAdapter.removeOne(
        `${action.payload.library}_${action.payload.name}`,
        {
          ...state,
          status: WCM_ACTION_SUCCESSFUL,
          loading: false
        }
      );
    }
    case ValidationRuleActionTypes.LOAD_VALIDATION_RULE_FAILED: {
      return ValidationRuleAdapter.removeAll({
        ...state,
        status: action.payload,
        loading: false
      });
    }
    case ValidationRuleActionTypes.VALIDATION_RULE_ACTION_FAILED: {
      return {
        ...state,
        status: action.payload,
        loading: false
      };
    }
    case ValidationRuleActionTypes.VALIDATION_RULE_CLEAR_ERROR:
      return {
        ...state,
        status: null
      };
    default:
      return state;
  }
}
