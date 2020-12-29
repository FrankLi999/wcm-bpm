import { Action } from "@ngrx/store";
import { WcmNode } from "../../model/WcmNode";
import { WcmItemFilter } from "../../model/WcmItemFilter";

export enum WcmNavigationActionTypes {
  SET_CURRENT_NODE = "[TreeNode] SetCurrent"
}

export class SetCurrentNode implements Action {
  readonly type = WcmNavigationActionTypes.SET_CURRENT_NODE;
  constructor(
    public payload: {
      currentNode: WcmNode;
      children: WcmNode[];
    }
  ) {}
}

export type WcmNavigationActions = SetCurrentNode;
