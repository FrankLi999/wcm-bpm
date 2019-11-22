
import { WcmSystemActions, WcmSystemActionTypes } from '../actions/wcm-system.actions';
import { WcmSystem } from '../../model/WcmSystem';
export interface WcmSystemState {
    wcmSystem?: WcmSystem;
    loading: boolean;
    loaded: boolean;
    loadError?:string;
    atError?:string;
    rtError?:string;
}

export const WcmSystemInitialState: WcmSystemState = {
    wcmSystem:  {
      wcmRepositories: [],
      jcrThemes: [],
      operations: {},
      rendertemplates: {},
      jsonForms: {},
	  renderTemplates: {},
      contentAreaLayouts: {},
    
      siteConfig: null,
	  navigations: [],
	  //Navigation id to SiteArea map
      siteAreas: {},
      authoringTemplates: {},
      controlFiels: []
    },
    loading : false,
    loaded  : false,
    loadError: null,
    atError: null,
    rtError: null
};

export function WcmSystemReducer(state = WcmSystemInitialState, action: WcmSystemActions): WcmSystemState {
    switch (action.type) {
        case WcmSystemActionTypes.GET_WCMSYSTEM:
            return {
                ...state,
                loading: true,
                loaded : false,
                loadError: null
            };
        case WcmSystemActionTypes.GET_WCMSYSTEM_SUCCESS:
            return {
                wcmSystem: { ... action.payload},
                loading: false,
                loaded : true,
                loadError: null
            };
        case WcmSystemActionTypes.WCMSYSTEM_CLEAR_ERROR:
            return {
                ...state,
                loadError: null
            };
        case WcmSystemActionTypes.GET_WCMSYSTEM_FAILED:
            return {
                ...state,
                loading: false,
                loaded : false,
                loadError: action.payload
            };
        case WcmSystemActionTypes.UPDATE_SITE_AREA_LAYOUT: {
            let siteAreas = {
                ...state.wcmSystem.siteAreas
            }
            siteAreas[action.payload.url.replace(/\//g, '~')] = action.payload;
            return {
                ...state,
                wcmSystem: {
                    ...state.wcmSystem,
                    siteAreas: siteAreas
                },
                loading: false,
                loaded : false
            };
        }    
        case WcmSystemActionTypes.CREATE_RENDER_TEMPLATE_SUCCESS: {            
            let wcmSystem: WcmSystem = { ... state.wcmSystem };
            wcmSystem.renderTemplates[`${action.payload.repository}/${action.payload.workspace}/${action.payload.library}/${action.payload.name}`] = action.payload;
            return {
                wcmSystem: wcmSystem,
                loading: false,
                loaded : true,
                rtError: null
            };
          }
        case WcmSystemActionTypes.RENDER_TEMPLATE_CLEAR_ERROR:
            return {
                ...state,
                rtError: null
            };    
        case WcmSystemActionTypes.CREATE_AUTHORING_TEMPLATE_FAILED: {
            return {
                ...state,
                atError: action.payload
            }; 
        }
        case WcmSystemActionTypes.AUTHORING_TEMPLATE_CLEAR_ERROR:
            return {
                ...state,
                atError: null
            }; 
        default:
            return state;
    }
}