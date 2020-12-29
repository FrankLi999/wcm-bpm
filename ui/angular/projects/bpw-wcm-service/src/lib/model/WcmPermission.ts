export interface WcmPermission {
  principalId: string;
  principalType: string;
  //Viewer, Reviewer, Editor, Administrator
  wcmRoles: string[];
}
