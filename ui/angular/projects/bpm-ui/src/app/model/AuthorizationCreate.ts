export interface AuthorizationCreate {
  type: number;
  permissions: string[];
  userId: string;
  groupId: string;
  resourceType: number;
  resourceId: string;
}
