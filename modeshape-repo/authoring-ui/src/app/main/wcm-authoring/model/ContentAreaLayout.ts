import { HasName } from './HasName';
import { ResourceViewer } from './ResourceViewer';
import { LayoutRow } from './LayoutRow';
export interface ContentAreaLayout extends HasName {
  repository: string,
  workspace: string,
  library: string,
  sidePane: {
    isLeft: boolean;
    width: number;
    viewers: ResourceViewer[];
  }
  contentWidth: number;
  rows: LayoutRow[];
}