import { WcmWorkspace } from './WcmWorkspace';

export interface WcmRepository {
    name: string;
    workspacesUrl: string;
    metadata: {[key: string]: string[]};
    backupUrl: string;
    restoreUrl: string;
    activeSessionsCount: number;
    workspaces: WcmWorkspace[];
}