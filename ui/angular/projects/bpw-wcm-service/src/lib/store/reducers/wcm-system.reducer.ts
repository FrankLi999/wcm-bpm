import {
  WcmSystemActions,
  WcmSystemActionTypes,
} from "../actions/wcm-system.actions";
import { WcmSystem } from "../../model/WcmSystem";
import { WCM_ACTION_SUCCESSFUL } from "../../model/wcm-response";
import { WcmConstants } from "../../utils/wcm-constants";
import { WcmUtils } from "../../utils/wcm-utils";

export interface WcmSystemState {
  wcmSystem?: WcmSystem;
  loading: boolean;
  loaded: boolean;
  loadError?: string;
  atError?: string;
  rtError?: string;
  renderError?: string;
  queryError?: string;
  formError?: string;
}

export const WcmSystemInitialState: WcmSystemState = {
  wcmSystem: {
    wcmRepositories: [],
    jcrThemes: [],
    operations: {},
    rendertemplates: {},
    authoringTemplateForms: {},
    formTemplates: {},
    forms: {},
    queryStatements: [],
    renderTemplates: {},
    contentAreaLayouts: {},
    siteConfig: null,
    navigations: [],
    siteAreas: {},
    authoringTemplates: {},
    controlFiels: [],
  },
  loading: false,
  loaded: false,
  loadError: null,
  atError: null,
  rtError: null,
  renderError: null,
  queryError: null,
};

export function WcmSystemReducer(
  state = WcmSystemInitialState,
  action: WcmSystemActions
): WcmSystemState {
  switch (action.type) {
    case WcmSystemActionTypes.GET_WCMSYSTEM:
      return {
        ...state,
        loading: true,
        loaded: false,
        loadError: null,
      };
    case WcmSystemActionTypes.GET_WCMSYSTEM_SUCCESS:
      return {
        wcmSystem: { ...action.payload },
        loading: false,
        loaded: true,
        loadError: null,
      };
    case WcmSystemActionTypes.WCMSYSTEM_CLEAR_ERROR:
      return {
        ...state,
        loadError: null,
      };
    case WcmSystemActionTypes.GET_WCMSYSTEM_FAILED:
      return {
        ...state,
        loading: false,
        loaded: false,
        loadError: action.payload,
      };
    case WcmSystemActionTypes.UPDATE_SITE_AREA_LAYOUT: {
      let siteAreas = {
        ...state.wcmSystem.siteAreas,
      };
      siteAreas[action.payload.properties["url"].replace(/\//g, "~")] =
        action.payload;
      return {
        ...state,
        wcmSystem: {
          ...state.wcmSystem,
          siteAreas: siteAreas,
        },
        loading: false,
        loaded: false,
      };
    }
    case WcmSystemActionTypes.CREATE_RENDER_TEMPLATE:
    case WcmSystemActionTypes.UPDATE_RENDER_TEMPLATE:
    case WcmSystemActionTypes.DELETE_RENDER_TEMPLATE: {
      return {
        ...state,
        rtError: null,
      };
    }
    case WcmSystemActionTypes.RENDER_TEMPLATE_ACTION_SUCCESSFUL: {
      let renderTemplates = {};
      renderTemplates[
        WcmUtils.itemPath(
          action.payload.library,
          WcmConstants.ROOTNODE_RT,
          action.payload.name
        )
      ] = action.payload;
      return {
        wcmSystem: {
          ...state.wcmSystem,
          renderTemplates: {
            ...state.wcmSystem.authoringTemplates,
            ...renderTemplates,
          },
        },
        loading: false,
        loaded: true,
        rtError: WCM_ACTION_SUCCESSFUL,
      };
    }
    case WcmSystemActionTypes.RENDER_TEMPLATE_ACTION_FAILED: {
      return {
        ...state,
        rtError: action.payload,
      };
    }
    case WcmSystemActionTypes.RENDER_TEMPLATE_CLEAR_ERROR:
      return {
        ...state,
        rtError: null,
      };
    case WcmSystemActionTypes.DELETE_RENDER_TEMPLATE_SUCCESSFUL: {
      let renderTemplates = {
        ...state.wcmSystem.renderTemplates,
      };
      delete renderTemplates[
        WcmUtils.itemPath(
          action.payload.library,
          WcmConstants.ROOTNODE_RT,
          action.payload.name
        )
      ];
      let wcmrt = {
        renderTemplates: {
          ...renderTemplates,
        },
      };
      return {
        wcmSystem: {
          ...state.wcmSystem,
          ...wcmrt,
        },
        loading: false,
        loaded: true,
        rtError: WCM_ACTION_SUCCESSFUL,
      };
    }
    case WcmSystemActionTypes.AUTHORING_TEMPLATE_FAILED: {
      return {
        ...state,
        atError: action.payload,
      };
    }
    case WcmSystemActionTypes.CREATE_AUTHORING_TEMPLATE:
    case WcmSystemActionTypes.UPDATE_AUTHORING_TEMPLATE:
    case WcmSystemActionTypes.DELETE_AUTHORING_TEMPLATE: {
      return {
        ...state,
        atError: null,
      };
    }
    case WcmSystemActionTypes.AUTHORING_TEMPLATE_SUCCESSFUL: {
      let authoringTemplates = {};
      authoringTemplates[
        WcmUtils.itemPath(
          action.payload.library,
          WcmConstants.ROOTNODE_AT,
          action.payload.name
        )
      ] = action.payload;
      return {
        wcmSystem: {
          ...state.wcmSystem,
          authoringTemplates: {
            ...state.wcmSystem.authoringTemplates,
            ...authoringTemplates,
          },
        },
        loading: false,
        loaded: true,
        atError: WCM_ACTION_SUCCESSFUL,
      };
    }
    case WcmSystemActionTypes.DELETE_AUTHORING_TEMPLATE_SUCCESSFUL: {
      let authoringTemplates = {
        ...state.wcmSystem.authoringTemplates,
      };
      delete authoringTemplates[
        WcmUtils.itemPath(
          action.payload.library,
          WcmConstants.ROOTNODE_AT,
          action.payload.name
        )
      ];
      let wcmat = {
        authoringTemplates: {
          ...authoringTemplates,
        },
      };
      return {
        wcmSystem: {
          ...state.wcmSystem,
          ...wcmat,
        },
        loading: false,
        loaded: true,
        atError: WCM_ACTION_SUCCESSFUL,
      };
    }
    case WcmSystemActionTypes.AUTHORING_TEMPLATE_CLEAR_ERROR: {
      return {
        ...state,
        atError: null,
      };
    }
    case WcmSystemActionTypes.FORM_FAILED: {
      return {
        ...state,
        formError: action.payload,
      };
    }
    case WcmSystemActionTypes.CREATE_FORM:
    case WcmSystemActionTypes.UPDATE_FORM:
    case WcmSystemActionTypes.DELETE_FORM: {
      return {
        ...state,
        formError: null,
      };
    }
    case WcmSystemActionTypes.FORM_SUCCESSFUL: {
      let formTemplates = {};
      formTemplates[
        WcmUtils.itemPath(
          action.payload.library,
          WcmConstants.ROOTNODE_FORM,
          action.payload.name
        )
      ] = action.payload;
      return {
        wcmSystem: {
          ...state.wcmSystem,
          formTemplates: {
            ...state.wcmSystem.formTemplates,
            ...formTemplates,
          },
        },
        loading: false,
        loaded: true,
        formError: WCM_ACTION_SUCCESSFUL,
      };
    }
    case WcmSystemActionTypes.DELETE_FORM_SUCCESSFUL: {
      let formTemplates = {
        ...state.wcmSystem.formTemplates,
      };
      delete formTemplates[
        WcmUtils.itemPath(
          action.payload.library,
          WcmConstants.ROOTNODE_FORM,
          action.payload.name
        )
      ];
      let wcmFormTemplates = {
        formTemplates: {
          ...formTemplates,
        },
      };
      return {
        wcmSystem: {
          ...state.wcmSystem,
          ...wcmFormTemplates,
        },
        loading: false,
        loaded: true,
        formError: WCM_ACTION_SUCCESSFUL,
      };
    }
    case WcmSystemActionTypes.FORM_CLEAR_ERROR: {
      return {
        ...state,
        formError: null,
      };
    }
    case WcmSystemActionTypes.CREATE_CONTENT_AREA_LAYOUT:
    case WcmSystemActionTypes.UPDATE_CONTENT_AREA_LAYOUT:
    case WcmSystemActionTypes.REMOVE_CONTENT_AREA_LAYOUT: {
      return {
        ...state,
        renderError: null,
      };
    }
    case WcmSystemActionTypes.CONTENT_AREA_LAYOUT_FAILED: {
      return {
        ...state,
        renderError: action.payload,
      };
    }
    case WcmSystemActionTypes.CONTENT_AREA_LAYOUT_SUCCESSFUL: {
      let contentAreaLayouts = {};
      contentAreaLayouts[
        WcmUtils.itemPath(
          action.payload.library,
          WcmConstants.ROOTNODE_LAYOUT,
          action.payload.name
        )
      ] = action.payload;
      return {
        wcmSystem: {
          ...state.wcmSystem,
          contentAreaLayouts: {
            ...state.wcmSystem.contentAreaLayouts,
            ...contentAreaLayouts,
          },
        },
        loading: false,
        loaded: true,
        renderError: WCM_ACTION_SUCCESSFUL,
      };
    }
    case WcmSystemActionTypes.REMOVE_CONTENT_AREA_LAYOUT_SUCCESSFUL: {
      let contentAreaLayouts = {
        ...state.wcmSystem.contentAreaLayouts,
      };
      delete contentAreaLayouts[
        WcmUtils.itemPath(
          action.payload.library,
          WcmConstants.ROOTNODE_LAYOUT,
          action.payload.name
        )
      ];
      let wcmcontentAreaLayouts = {
        contentAreaLayouts: {
          ...contentAreaLayouts,
        },
      };
      return {
        wcmSystem: {
          ...state.wcmSystem,
          ...wcmcontentAreaLayouts,
        },
        loading: false,
        loaded: true,
        renderError: WCM_ACTION_SUCCESSFUL,
      };
    }
    case WcmSystemActionTypes.CONTENT_AREA_LAYOUT_CLEAR_ERROR:
      return {
        ...state,
        renderError: null,
      };
    default:
      return state;
  }
}
