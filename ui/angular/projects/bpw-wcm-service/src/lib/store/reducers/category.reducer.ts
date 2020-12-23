import {
  CategoryActionTypes,
  CategorywActions
} from "../actions/category.actions";
import { WCM_ACTION_SUCCESSFUL } from "../../model/wcm-response";

let categoryTimestamp: number = 0;
export interface CategoryState {
  status: any;
  timestamp: number;
}
export const CategoryInitialState: CategoryState = {
  status: null,
  timestamp: 0
};

export function CategoryReducer(
  state = CategoryInitialState,
  action: CategorywActions
): CategoryState {
  switch (action.type) {
    case CategoryActionTypes.CATEGORY_ACTION_SUCCESSFUL: {
      return {
        status: WCM_ACTION_SUCCESSFUL,
        timestamp: ++categoryTimestamp
      };
    }
    case CategoryActionTypes.CATEGORY_ACTION_FAILED: {
      return {
        status: action.payload,
        timestamp: ++categoryTimestamp
      };
    }
    case CategoryActionTypes.CATEGORY_CLEAR_ERROR: {
      categoryTimestamp = 0;
      return {
        status: null,
        timestamp: 0
      };
    }
    default:
      return state;
  }
}
