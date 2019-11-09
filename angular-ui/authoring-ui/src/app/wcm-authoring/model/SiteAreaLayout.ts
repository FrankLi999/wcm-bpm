import {SidePane} from './SidePane';
import {LayoutRow} from './LayoutRow';
export interface SiteAreaLayout {
    contentWidth: number;
    sidePane: SidePane;
	rows: LayoutRow[];
}