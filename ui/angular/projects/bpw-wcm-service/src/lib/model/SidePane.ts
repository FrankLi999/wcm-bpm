import { ResourceViewer } from './ResourceViewer';
export interface SidePane {
  left?: boolean;
  width?: number;
  viewers?: ResourceViewer[];
}
