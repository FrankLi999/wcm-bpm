import {
  WcmSystemActions,
  WcmSystemActionTypes,
} from "../actions/wcm-system.actions";
import { ContentAreaLayout } from "../../model/ContentAreaLayout";
import { WcmConstants } from "../../utils/wcm-constants";
export interface ContentAreaLayoutState {
  contentAreaLayout?: ContentAreaLayout;
  error?: string;
}

export const ContentAreaLayoutInitialState: ContentAreaLayoutState = {
  error: null,
  contentAreaLayout: {
    name: "",
    repository: WcmConstants.REPO_BPWIZARD,
    workspace: WcmConstants.WS_DEFAULT,
    library: "design",
    contentWidth: 100,
    sidePane: {
      left: true,
      width: 0,
      viewers: [],
    },
    rows: [
      {
        columns: [
          {
            width: 100,
            viewers: [],
          },
        ],
      },
    ],
  },
};

export function ContentAreaLayoutReducer(
  state = ContentAreaLayoutInitialState,
  action: WcmSystemActions
): ContentAreaLayoutState {
  switch (action.type) {
    case WcmSystemActionTypes.CREATE_NEW_CONTENT_AREA_LAYOUT: {
      return {
        contentAreaLayout: {
          ...ContentAreaLayoutInitialState.contentAreaLayout,
          repository: action.repository,
          workspace: action.workspace,
          library: action.library,
        },
        error: null,
      };
    }
    case WcmSystemActionTypes.EDIT_CONTENT_AREA_LAYOUT:
      return {
        contentAreaLayout: { ...action.payload },
        error: null,
      };
    case WcmSystemActionTypes.CONTENT_AREA_LAYOUT_SUCCESSFUL:
      return {
        contentAreaLayout: { ...action.payload },
        error: null,
      };
    case WcmSystemActionTypes.CONTENT_AREA_LAYOUT_FAILED:
      return {
        ...state,
        error: action.payload,
      };
    case WcmSystemActionTypes.CONTENT_AREA_LAYOUT_CLEAR_ERROR:
      return {
        ...state,
        error: null,
      };
    case WcmSystemActionTypes.REMOVE_CONTENT_AREA_LAYOUT_SUCCESSFUL:
      return ContentAreaLayoutInitialState;
    default:
      return state;
  }
}
