import { HasName } from './HasName';
import { LayoutRow } from './LayoutRow';
import { SidePane} from './SidePane';
import { ResourceNode } from './ResourceNode';
export interface ContentAreaLayout extends ResourceNode, HasName {
  repository: string;
  workspace: string;
  library: string;
  sidePane: SidePane;
  contentWidth: number;
  rows: LayoutRow[];
}