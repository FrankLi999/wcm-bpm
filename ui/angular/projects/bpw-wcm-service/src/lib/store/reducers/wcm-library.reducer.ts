import { EntityState, EntityAdapter, createEntityAdapter } from "@ngrx/entity";
import {
  WcmLibraryActionTypes,
  WcmLibraryActions,
} from "../actions/wcm-library.actions";
import { Library } from "../../model/Library";
import { WCM_ACTION_SUCCESSFUL } from "../../model/wcm-response";

export interface WcmLibraryState extends EntityState<Library> {
  status?: any;
  loading: boolean;
}

export const WcmLibraryAdapter: EntityAdapter<Library> = createEntityAdapter<
  Library
>({
  selectId: (library) => library.name,
});

export const WcmLibraryInitialState: WcmLibraryState = WcmLibraryAdapter.getInitialState(
  {
    status: null,
    loading: true,
    total: 0,
  }
);

export function WcmLibraryReducer(
  state = WcmLibraryInitialState,
  action: WcmLibraryActions
): WcmLibraryState {
  switch (action.type) {
    case WcmLibraryActionTypes.LOAD_LIBRARY:
    case WcmLibraryActionTypes.CREATE_LIBRARY:
    case WcmLibraryActionTypes.UPDATE_LIBRARY:
    case WcmLibraryActionTypes.DELETE_LIBRARY: {
      return {
        ...state,
        status: null,
      };
    }
    case WcmLibraryActionTypes.LIBRARY_ACTION_SUCCESS: {
      let newState: WcmLibraryState = null;
      if (action.payload.action === WcmLibraryActionTypes.LOAD_LIBRARY) {
        newState = WcmLibraryAdapter.addAll(action.payload.libraries, {
          ...state,
          status: WCM_ACTION_SUCCESSFUL,
          loading: false,
        });
      } else if (
        action.payload.action === WcmLibraryActionTypes.CREATE_LIBRARY
      ) {
        newState = WcmLibraryAdapter.addOne(action.payload.libraries[0], {
          ...state,
          status: WCM_ACTION_SUCCESSFUL,
          loading: false,
        });
      } else if (
        action.payload.action === WcmLibraryActionTypes.UPDATE_LIBRARY
      ) {
        newState = WcmLibraryAdapter.upsertOne(action.payload.libraries[0], {
          ...state,
          status: WCM_ACTION_SUCCESSFUL,
          loading: false,
        });
      } else if (
        action.payload.action === WcmLibraryActionTypes.DELETE_LIBRARY
      ) {
        newState = WcmLibraryAdapter.removeOne(
          action.payload.libraries[0].name,
          {
            ...state,
            status: WCM_ACTION_SUCCESSFUL,
            loading: false,
          }
        );
      }
      return newState;
    }
    case WcmLibraryActionTypes.LIBRARY_ACTION_FAILED:
      let newstate = null;
      if (action.payload.action === WcmLibraryActionTypes.LOAD_LIBRARY) {
        newstate = WcmLibraryAdapter.removeAll({
          ...state,
          status: action.payload.status,
          loading: false,
        });
      } else {
        newstate = {
          ...state,
          status: action.payload.status,
          loading: false,
        };
      }
      return newstate;
    case WcmLibraryActionTypes.LIBRARY_ACTION_CLEAR:
      return {
        ...state,
        status: null,
      };
    default:
      return state;
  }
}
