import { HasName } from './HasName';
import { Workspace } from './Workspace';
export interface Repository extends HasName {
    workspacesUrl: string;
    metadata: {[key: string]: string[]};
    backupUrl: string;
    restoreUrl: string;
    activeSessionsCount: number;
    workspaces?: Workspace[];
}