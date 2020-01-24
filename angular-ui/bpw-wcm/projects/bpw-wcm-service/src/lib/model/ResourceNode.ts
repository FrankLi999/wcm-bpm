import { AccessControlEntry } from './AccessControlEntry';
export interface ResourceNode {
    title?: string;
	description?: string;
    lockOwner?: string;
    acl?: AccessControlEntry;
}