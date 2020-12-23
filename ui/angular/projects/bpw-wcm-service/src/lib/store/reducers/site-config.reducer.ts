import { EntityState, EntityAdapter, createEntityAdapter } from "@ngrx/entity";
import {
  SiteConfigActionTypes,
  SiteConfigActions
} from "../actions/site-config.actions";
import { SiteConfig } from "../../model/SiteConfig";
import {
  WCM_ACTION_SUCCESSFUL,
  WCM_LOAD_SUCCESSFUL
} from "../../model/wcm-response";

export interface SiteConfigState extends EntityState<SiteConfig> {
  status?: any;
  loading: boolean;
  total: number;
}
export const siteConfigId = (item: SiteConfig) =>
  `${item.library}_${item.name}`;

export const SiteConfigAdapter: EntityAdapter<SiteConfig> = createEntityAdapter<
  SiteConfig
>({
  selectId: siteConfig => siteConfigId(siteConfig)
});

export const SiteConfigInitialState: SiteConfigState = SiteConfigAdapter.getInitialState(
  {
    status: null,
    loading: true,
    total: 0
  }
);

export function SiteConfigReducer(
  state = SiteConfigInitialState,
  action: SiteConfigActions
): SiteConfigState {
  switch (action.type) {
    case SiteConfigActionTypes.LOAD_SITE_CONFIG:
    case SiteConfigActionTypes.CREATE_SITE_CONFIG:
    case SiteConfigActionTypes.UPDATE_SITE_CONFIG:
    case SiteConfigActionTypes.DELETE_SITE_CONFIG: {
      return {
        ...state,
        status: null
      };
    }
    case SiteConfigActionTypes.LOAD_SITE_CONFIG_SUCCESSFUL: {
      return SiteConfigAdapter.addAll(action.payload, {
        ...state,
        status: WCM_LOAD_SUCCESSFUL,
        loading: false
      });
    }
    case SiteConfigActionTypes.CREATE_SITE_CONFIG_SUCCESSFUL: {
      return SiteConfigAdapter.addOne(action.payload, {
        ...state,
        status: WCM_ACTION_SUCCESSFUL,
        loading: false
      });
    }
    case SiteConfigActionTypes.UPDATE_SITE_CONFIG_SUCCESSFUL: {
      return SiteConfigAdapter.upsertOne(action.payload, {
        ...state,
        status: WCM_ACTION_SUCCESSFUL,
        loading: false
      });
    }
    case SiteConfigActionTypes.DELETE_SITE_CONFIG_SUCCESSFUL: {
      return SiteConfigAdapter.removeOne(
        `${action.payload.library}_${action.payload.name}`,
        {
          ...state,
          status: WCM_ACTION_SUCCESSFUL,
          loading: false
        }
      );
    }
    case SiteConfigActionTypes.LOAD_SITE_CONFIG_FAILED: {
      return SiteConfigAdapter.removeAll({
        ...state,
        status: action.payload,
        loading: false
      });
    }
    case SiteConfigActionTypes.SITE_CONFIG_ACTION_FAILED: {
      return {
        ...state,
        status: action.payload,
        loading: false
      };
    }
    case SiteConfigActionTypes.SITE_CONFIG_CLEAR_ERROR:
      return {
        ...state,
        status: null
      };
    default:
      return state;
  }
}
