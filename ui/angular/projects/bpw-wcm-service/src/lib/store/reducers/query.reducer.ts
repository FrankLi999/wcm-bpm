import { EntityState, EntityAdapter, createEntityAdapter } from "@ngrx/entity";
import { QueryActionTypes, QueryActions } from "../actions/query.actions";
import { QueryStatement } from "../../model/QueryStatement";
import {
  WCM_ACTION_SUCCESSFUL,
  WCM_LOAD_SUCCESSFUL,
} from "../../model/wcm-response";

export interface QueryState extends EntityState<QueryStatement> {
  status?: any;
  loading: boolean;
  total: number;
}
export const queryId = (item: QueryStatement) => `${item.library}_${item.name}`;

export const QueryAdapter: EntityAdapter<QueryStatement> = createEntityAdapter<
  QueryStatement
>({
  selectId: (item) => `${item.library}_${item.name}`,
});

export const QueryInitialState: QueryState = QueryAdapter.getInitialState({
  status: null,
  loading: true,
  total: 0,
});

export function QueryReducer(
  state = QueryInitialState,
  action: QueryActions
): QueryState {
  switch (action.type) {
    case QueryActionTypes.LOAD_QUERY:
    case QueryActionTypes.CREATE_QUERY:
    case QueryActionTypes.UPDATE_QUERY:
    case QueryActionTypes.DELETE_QUERY: {
      return {
        ...state,
        status: null,
      };
    }
    case QueryActionTypes.LOAD_QUERY_SUCCESSFUL: {
      return QueryAdapter.addAll(action.payload, {
        ...state,
        status: WCM_LOAD_SUCCESSFUL,
        loading: false,
      });
    }
    case QueryActionTypes.CREATE_QUERY_SUCCESSFUL: {
      return QueryAdapter.addOne(action.payload, {
        ...state,
        status: WCM_ACTION_SUCCESSFUL,
        loading: false,
      });
    }
    case QueryActionTypes.UPDATE_QUERY_SUCCESSFUL: {
      return QueryAdapter.upsertOne(action.payload, {
        ...state,
        status: WCM_ACTION_SUCCESSFUL,
        loading: false,
      });
    }
    case QueryActionTypes.DELETE_QUERY_SUCCESSFUL: {
      return QueryAdapter.removeOne(
        `${action.payload.library}_${action.payload.name}`,
        {
          ...state,
          status: WCM_ACTION_SUCCESSFUL,
          loading: false,
        }
      );
    }
    case QueryActionTypes.LOAD_QUERY_FAILED: {
      return QueryAdapter.removeAll({
        ...state,
        status: action.payload,
        loading: false,
      });
    }
    case QueryActionTypes.QUERY_ACTION_FAILED: {
      return {
        ...state,
        status: action.payload,
        loading: false,
      };
    }
    case QueryActionTypes.QUERY_CLEAR_ERROR:
      return {
        ...state,
        status: null,
      };
    default:
      return state;
  }
}
