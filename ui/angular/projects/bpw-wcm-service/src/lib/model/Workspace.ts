import { HasName } from './HasName';
import { RestNode } from './RestNode';
export interface Workspace extends HasName {
    repositoryUrl: string;
    queryUrl: string;
    itemsUrl: string;
    binaryUrl: string;
    nodeTypesUrl: string;
    restNodes?: RestNode[];
    // repositoryName?: string;
}