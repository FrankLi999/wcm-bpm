export interface Authorization {
  id: string;
  type: number;
  permissions: string[];
  userId?: string;
  groupId?: string;
  resourceType: number;
  resourceId: string;
  removalTime?: string;
  rootProcessInstanceId?: string;
}
