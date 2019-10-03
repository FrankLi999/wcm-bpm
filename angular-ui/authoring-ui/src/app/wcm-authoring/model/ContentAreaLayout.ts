import { HasName } from './HasName';
import { LayoutRow } from './LayoutRow';
import { SidePane} from './SidePane';
export interface ContentAreaLayout extends HasName {
  repository: string;
  workspace: string;
  library: string;
  sidePane: SidePane;
  contentWidth: number;
  rows: LayoutRow[];
}