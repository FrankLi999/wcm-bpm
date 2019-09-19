import { ContentAreaLayoutActions, ContentAreaLayoutActionTypes } from '../actions/content-area-layout.actions';
import { ContentAreaLayout } from '../../model';
export interface ContentAreaLayoutState {
    contentAreaLayout?: ContentAreaLayout
}

export const ContentAreaLayoutInitialState: ContentAreaLayoutState = {
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
                contentAreaLayout: {...action.payload}
            };

        case ContentAreaLayoutActionTypes.CREATE_CONTENT_AREA_LAYOUT_FAILED:
            return {
                contentAreaLayout: {...state.contentAreaLayout}
            };
        default:
            return state;
    }
}