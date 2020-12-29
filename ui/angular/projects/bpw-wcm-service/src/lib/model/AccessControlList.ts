import { AccessControlEntry } from './AccessControlEntry';
export interface AccessControlList {
  onSaveDraftPermissions?: AccessControlEntry;
  onRejectDraftPermissions?: AccessControlEntry;
  onPublishPermissions?: AccessControlEntry;
}