
import { WcmSystemActions, WcmSystemActionTypes } from '../actions/wcm-system.actions';
import { WcmSystem } from '../../model';
export interface WcmSystemState {
    wcmSystem?: WcmSystem;
    loading: boolean;
    loaded: boolean;
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
    loaded  : false
};

export function WcmSystemReducer(state = WcmSystemInitialState, action: WcmSystemActions): WcmSystemState {
    switch (action.type) {
        case WcmSystemActionTypes.GET_WCMSYSTEM:
            return {
                ...state,
                loading: true,
                loaded : false
            };
        case WcmSystemActionTypes.GET_WCMSYSTEM_SUCCESS:
            return {
                wcmSystem: { ... action.payload},
                loading: false,
                loaded : true
            };

        case WcmSystemActionTypes.GET_WCMSYSTEM_FAILED:
            return {
                ...state,
                loading: false,
                loaded : false
            };
        case WcmSystemActionTypes.CREATE_RENDER_TEMPLATE_SUCCESS: {            
            let wcmSystem: WcmSystem = { ... state.wcmSystem };
            wcmSystem.renderTemplates[`${action.payload.repository}/${action.payload.workspace}/${action.payload.library}/${action.payload.name}`] = action.payload;
            return {
                wcmSystem: wcmSystem,
                loading: false,
                loaded : true
            };
          }    
        case WcmSystemActionTypes.CREATE_AUTHORING_TEMPLATE_SUCCESS: {
            let wcmSystem: WcmSystem = { ... state.wcmSystem };
            wcmSystem.authoringTemplates[`${action.payload.repository}/${action.payload.workspace}/${action.payload.library}/${action.payload.name}`] = action.payload;
            return {
                wcmSystem: wcmSystem,
                loading: false,
                loaded : true
            }; 
          }
        default:
            return state;
    }
}