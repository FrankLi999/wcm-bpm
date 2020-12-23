import { Action } from '@ngrx/store';
import { Navigation } from '../../../common/types/navigation';

export enum NavigationActionTypes {
  SET_WCM_NAVIGATION = '[Navigation] Set'
}

export class SetNavigation implements Action {
  readonly type = NavigationActionTypes.SET_WCM_NAVIGATION;
  constructor(public payload: Navigation[]) {
  }
}

export type NavigationActions = SetNavigation;