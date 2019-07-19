import { RestItem } from './RestItem'; 
import { RestProperty } from './RestProperty'; 
export interface RestNode extends RestItem {
    jcrProperties: RestProperty[];
    children: RestNode[];
    customProperties: {[key: string]: string};
    id: string;
    nodePath?: string;
    repositoryName?: string;
    workspaceName?: string;
}