import { ResourceViewer } from './ResourceViewer';
export interface SideNav {
  isLeft: boolean;
  width: number;
  viewers: ResourceViewer[];
}