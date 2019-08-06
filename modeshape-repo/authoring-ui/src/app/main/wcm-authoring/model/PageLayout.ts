import { HasName } from './HasName';
import { SideNav } from './SideNav';
import { LayoutRow } from './LayoutRow';
export interface PageLayout extends HasName {
  headerEnabled : boolean;
  footerEnabled : boolean;
  theme: string;
  sidenav: SideNav;
  contentWidth: number;
  rows: LayoutRow[];
}