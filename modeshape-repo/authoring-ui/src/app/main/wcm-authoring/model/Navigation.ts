import { NavigationBadge } from './NavigationBadge';

export interface NavigationItem {
    id: string;
    title: string;
    type: 'item' | 'group' | 'collapsable';
    translate?: string;
    icon?: string;
    hidden?: boolean;
    url?: string;
    classes?: string;
    exactMatch?: boolean;
    externalUrl?: boolean;
    openInNewTab?: boolean;
    function?: any;
    badge?: NavigationBadge;
    children?: NavigationItem[];
}

export interface Navigation extends NavigationItem {
    children?: NavigationItem[];
}
