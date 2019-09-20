import { ContentAreaLayoutActions, ContentAreaLayoutActionTypes } from '../actions/content-area-layout.actions';
import { ContentAreaLayout } from '../../model';
export interface ContentAreaLayoutState {
    contentAreaLayout?: ContentAreaLayout,
    error?: string;
}

export const ContentAreaLayoutInitialState: ContentAreaLayoutState = {
    error: null,
    contentAreaLayout: {
        name: '',
        repository: 'bpwizard',
        workspace: 'default',
        library: 'design',
        contentWidth: 100,
        sidePane: {
          left: true,
          width: 0,
          viewers: []
        },
        rows : [{
          columns : [ {
            width: 100,
            viewers : []
          }]
        }]
      }
};

export function ContentAreaLayoutReducer(state = ContentAreaLayoutInitialState, action: ContentAreaLayoutActions): ContentAreaLayoutState {
    switch (action.type) {
        case ContentAreaLayoutActionTypes.CREATE_CONTENT_AREA_LAYOUT_SUCCESS:

            return {
                contentAreaLayout: {...action.payload},
                error: null
            };
        case ContentAreaLayoutActionTypes.CREATE_CONTENT_AREA_LAYOUT_FAILED:
            return {
               ...state,
               error: action.payload
            };
        case ContentAreaLayoutActionTypes.CONTENT_AREA_LAYOUT_CLEAR_ERROR:
            return {
                ...state,
                error: null
            };
        default:
            return state;
    }
}