import { Action } from '@ngrx/store';
import { FuseNavigation } from '../../../common/types/fuse-navigation';

export enum NavigationActionTypes {
  SET_FUSE_NAVIGATION = '[Navigation] Set'
}

export class SetNavigation implements Action {
  readonly type = NavigationActionTypes.SET_FUSE_NAVIGATION;
  constructor(public payload: FuseNavigation[]) {
  }
}

export type NavigationActions = SetNavigation;