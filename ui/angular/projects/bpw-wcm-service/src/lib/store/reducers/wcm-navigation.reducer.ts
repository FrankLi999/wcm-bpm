import { WcmNode } from "../../model/WcmNode";
import {
  WcmNavigationActions,
  WcmNavigationActionTypes
} from "../actions/wcm-navigation.actions";

export interface WcmNavigationState {
  currentNode: WcmNode;
  children: WcmNode[];
}
export const WcmNavigationInitialState: WcmNavigationState = {
  currentNode: null,
  children: []
};
export function WcmNavigationReducer(
  state = WcmNavigationInitialState,
  action: WcmNavigationActions
): WcmNavigationState {
  switch (action.type) {
    case WcmNavigationActionTypes.SET_CURRENT_NODE:
      return {
        ...action.payload
      };
    default:
      return state;
  }
}
